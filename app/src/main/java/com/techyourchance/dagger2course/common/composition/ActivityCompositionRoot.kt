package com.techyourchance.dagger2course.common.composition

import androidx.appcompat.app.AppCompatActivity
import com.techyourchance.dagger2course.questions.FetchQuestionDetailUseCase
import com.techyourchance.dagger2course.questions.FetchQuestionUseCase
import com.techyourchance.dagger2course.screens.common.ScreensNavigator
import com.techyourchance.dagger2course.screens.common.dialogs.DialogsNavigator

class ActivityCompositionRoot(
    private val activity: AppCompatActivity,
    private val appCompositionRoot: AppCompositionRoot
) {

    //if you want to keep state use lazy which will tie to the activity scope
    val screensNavigator by lazy {
        ScreensNavigator(activity)
    }

    private val fragmentManager get() = activity.supportFragmentManager

    val dialogsNavigator get() = DialogsNavigator(fragmentManager)


    private val stackoverflowApi get() = appCompositionRoot.stackoverflowApi

    //get a new instance of fetchQuestion useCase when it is query, to use getter with get()
    //each time it is call a new instance is create, because we overridden get
    val fetchQuestionsUseCase get() = FetchQuestionUseCase(stackoverflowApi)

    val fetchQuestionDetailUseCase get() = FetchQuestionDetailUseCase(stackoverflowApi)


}