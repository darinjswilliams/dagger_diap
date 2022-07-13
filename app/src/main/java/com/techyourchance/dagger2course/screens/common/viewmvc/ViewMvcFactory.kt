package com.techyourchance.dagger2course.screens.common.viewmvc

import android.view.LayoutInflater
import android.view.ViewGroup
import com.techyourchance.dagger2course.screens.questiondetails.QuestionDetailsViewMvc

class ViewMvcFactory(private val layoutInflater: LayoutInflater) {

    fun newQuestionsDetailsViewMvc(parent: ViewGroup?): QuestionDetailsViewMvc{
      return QuestionDetailsViewMvc(layoutInflater, parent)
    }
}