package com.techyourchance.dagger2course.screens.questionslist

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.techyourchance.dagger2course.MyApplication
import com.techyourchance.dagger2course.questions.FetchQuestionUseCase
import com.techyourchance.dagger2course.questions.Question
import com.techyourchance.dagger2course.screens.common.ScreensNavigator
import com.techyourchance.dagger2course.screens.common.activities.BaseActivity
import com.techyourchance.dagger2course.screens.common.dialogs.DialogsNavigator
import com.techyourchance.dagger2course.screens.common.dialogs.ServerErrorDialogFragment
import com.techyourchance.dagger2course.screens.questiondetails.QuestionDetailsActivity
import kotlinx.coroutines.*

class QuestionsListActivity : BaseActivity(),
    QuestionListViewMvc.Listener {

    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    private lateinit var viewMvc: QuestionListViewMvc

    private lateinit var fetchQuestionUseCase: FetchQuestionUseCase

    private var isDataLoaded = false

    private lateinit var dialogsNavigator: DialogsNavigator

    private lateinit var screensNavigator: ScreensNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewMvc = compositionRoot.viewMvcFactory.newQuestionListViewMvc(null)

        setContentView(viewMvc.rootView)

        fetchQuestionUseCase = compositionRoot.fetchQuestionsUseCase

        dialogsNavigator = compositionRoot.dialogsNavigator

        screensNavigator = compositionRoot.screensNavigator

    }

    override fun onStart() {
        super.onStart()

        //register listener
        viewMvc.registerListener(this)

        if (!isDataLoaded) {
            fetchQuestions()
        }
    }

    override fun onStop() {
        super.onStop()
        coroutineScope.coroutineContext.cancelChildren()

        //unregister listener
        viewMvc.unregisterListener(this)
    }

    override fun onRefreshClicked() {
        fetchQuestions()
    }

    //when ever question is clicked start new activity
    override fun onQuestionClicked(clickedQuestion: Question) {
        screensNavigator.toQuestionDetails(clickedQuestion.id)
    }

    private fun fetchQuestions() {
        coroutineScope.launch {
            viewMvc.showProgressIndication()
            val result = fetchQuestionUseCase.fetchLatestQuestions()

            try {
                when (result) {
                    is FetchQuestionUseCase.Result.Success -> {
                        viewMvc.bindQuestions(result.questions)
                        isDataLoaded = true
                    }

                    else -> {
                        onFetchFailed()
                    }
                }
            } finally {
                viewMvc.hideProgressIndication()
            }

        }
    }

    private fun onFetchFailed() {
        dialogsNavigator.showServerErrorDialog()
    }


}