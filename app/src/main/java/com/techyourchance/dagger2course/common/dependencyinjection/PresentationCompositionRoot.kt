package com.techyourchance.dagger2course.common.dependencyinjection

import com.techyourchance.dagger2course.questions.FetchQuestionDetailUseCase
import com.techyourchance.dagger2course.questions.FetchQuestionUseCase
import com.techyourchance.dagger2course.screens.common.dialogs.DialogsNavigator
import com.techyourchance.dagger2course.screens.common.viewmvc.ViewMvcFactory

class PresentationCompositionRoot(private val activityCompositionRoot: ActivityCompositionRoot) {

    private val layoutInflater get() = activityCompositionRoot.layout
    private val fragmentManager get() = activityCompositionRoot.fragmentManager
    private val stackoverflowApi get() = activityCompositionRoot.stackoverflowApi

    val viewMvcFactory get() = ViewMvcFactory(layoutInflater)

    val dialogsNavigator get() = DialogsNavigator(fragmentManager)

    val screensNavigator get() = activityCompositionRoot.screensNavigator

    //get a new instance of fetchQuestion useCase when it is query, to use getter with get()
    //each time it is call a new instance is create, because we overridden get
    val fetchQuestionsUseCase get() = FetchQuestionUseCase(stackoverflowApi)

    val fetchQuestionDetailUseCase get() = FetchQuestionDetailUseCase(stackoverflowApi)

}