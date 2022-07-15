package com.techyourchance.dagger2course.common.dependencyinjection

import com.techyourchance.dagger2course.questions.FetchQuestionDetailUseCase
import com.techyourchance.dagger2course.questions.FetchQuestionUseCase
import com.techyourchance.dagger2course.screens.common.ScreensNavigator
import com.techyourchance.dagger2course.screens.common.dialogs.DialogsNavigator
import com.techyourchance.dagger2course.screens.common.viewmvc.ViewMvcFactory
import java.lang.reflect.Field

class Injector(private val compositionRoot: PresentationCompositionRoot) {
    fun inject(client: Any) {
        getAllFields(client).forEach {
            if (isAnnotatedForInjection(it))
                injectField(client, it)
        }
    }

    private fun injectField(client: Any, field: Field) {
        val isAccessibleInitially = field.isAccessible
        field.isAccessible = true
        field.set(client, getServiceForClass(field.type))
        field.isAccessible = isAccessibleInitially
    }

    private fun getServiceForClass(type: Class<*>): Any {
        return when (type) {
            DialogsNavigator::class.java -> { compositionRoot.dialogsNavigator }
            ScreensNavigator::class.java -> { compositionRoot.screensNavigator }
            FetchQuestionUseCase::class.java -> { compositionRoot.fetchQuestionsUseCase }
            FetchQuestionDetailUseCase::class.java -> { compositionRoot.fetchQuestionDetailUseCase }
            ViewMvcFactory::class.java -> { compositionRoot.viewMvcFactory }
            else ->{
                throw Exception("unsupported service type $type")
            }
        }
    }

    private fun isAnnotatedForInjection(field: Field): Boolean {
        val fieldAnnotation = field.annotations
        fieldAnnotation.forEach {
            if (it is Service) {
                return true
            }
        }
        return false
    }

    private fun getAllFields(client: Any): Array<out Field> {
        val clientClass = client::class.java
        return clientClass.declaredFields
    }


}