package com.techyourchance.dagger2course.screens.questiondetails

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.techyourchance.dagger2course.questions.FetchQuestionDetailUseCase
import com.techyourchance.dagger2course.questions.FetchQuestionUseCase
import com.techyourchance.dagger2course.screens.common.dialogs.DialogsNavigator
import com.techyourchance.dagger2course.screens.common.dialogs.ServerErrorDialogFragment
import kotlinx.coroutines.*

class QuestionDetailsActivity : AppCompatActivity(), QuestionDetailsViewMvc.Listener {

    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    private lateinit var questionId: String

    private lateinit var viewMvc: QuestionDetailsViewMvc

    private lateinit var fetchQuestionDetailUseCase: FetchQuestionDetailUseCase

    private lateinit var dialogsNavigator: DialogsNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewMvc = QuestionDetailsViewMvc(LayoutInflater.from(this), null)
        setContentView(viewMvc.rootView)

        fetchQuestionDetailUseCase = FetchQuestionDetailUseCase()

        questionId = intent.extras!!.getString(EXTRA_QUESTION_ID)!!

        dialogsNavigator = DialogsNavigator(supportFragmentManager)
    }

    override fun onStart() {
        super.onStart()
        viewMvc.registerListener(this)
        fetchQuestionDetails()
    }

    override fun onStop() {
        super.onStop()
        coroutineScope.coroutineContext.cancelChildren()
        viewMvc.unregisterListener(this)
    }

    private fun fetchQuestionDetails() {
        coroutineScope.launch {
            viewMvc.showProgressIndication()
            val result = fetchQuestionDetailUseCase.fetchQuestionDetail(questionId)

            try {

                when(result){
                    is FetchQuestionDetailUseCase.Result.Success ->{
                       viewMvc.bindQuestionBody(result.question.body)
                    } else -> {
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

    override fun onBackClicked() {
        onBackPressed()
    }

    companion object {
        const val EXTRA_QUESTION_ID = "EXTRA_QUESTION_ID"
        fun start(context: Context, questionId: String) {
            val intent = Intent(context, QuestionDetailsActivity::class.java)
            intent.putExtra(EXTRA_QUESTION_ID, questionId)
            context.startActivity(intent)
        }
    }

}