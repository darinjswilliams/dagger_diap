package com.techyourchance.dagger2course.screens.questionslist

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.techyourchance.dagger2course.MyApplication
import com.techyourchance.dagger2course.questions.FetchQuestionUseCase
import com.techyourchance.dagger2course.questions.Question
import com.techyourchance.dagger2course.screens.common.ScreensNavigator
import com.techyourchance.dagger2course.screens.common.dialogs.DialogsNavigator
import com.techyourchance.dagger2course.screens.common.dialogs.ServerErrorDialogFragment
import com.techyourchance.dagger2course.screens.questiondetails.QuestionDetailsActivity
import kotlinx.coroutines.*

class QuestionsListActivity : AppCompatActivity(),
    QuestionListViewMvc.Listener {

    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    private lateinit var viewMvc: QuestionListViewMvc

    private lateinit var fetchQuestionUseCase: FetchQuestionUseCase

    private var isDataLoaded = false

    private lateinit var dialogsNavigator: DialogsNavigator

    private lateinit var screensNavigator: ScreensNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewMvc = QuestionListViewMvc(LayoutInflater.from(this), null)

        setContentView(viewMvc.rootView)

        //Note how we are getting once instance of retrofit from application class
        //since the application class is consider a global object, it will only be instantiate once

        fetchQuestionUseCase = FetchQuestionUseCase((application as MyApplication).stackoverflowApi)

        dialogsNavigator = DialogsNavigator(supportFragmentManager)

        screensNavigator = ScreensNavigator(this)

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