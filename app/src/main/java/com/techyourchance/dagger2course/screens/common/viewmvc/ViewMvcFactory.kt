
package com.techyourchance.dagger2course.screens.common.viewmvc

import android.view.LayoutInflater
import android.view.ViewGroup
import com.techyourchance.dagger2course.screens.questiondetails.QuestionDetailsViewMvc
import com.techyourchance.dagger2course.screens.questionslist.QuestionListViewMvc

class ViewMvcFactory(private val layoutInflater: LayoutInflater) {

    fun newQuestionsDetailsViewMvc(parent: ViewGroup?): QuestionDetailsViewMvc{
      return QuestionDetailsViewMvc(layoutInflater, parent)
    }

    fun newQuestionListViewMvc(parent: ViewGroup?): QuestionListViewMvc{
        return QuestionListViewMvc(layoutInflater, parent)
    }
}
