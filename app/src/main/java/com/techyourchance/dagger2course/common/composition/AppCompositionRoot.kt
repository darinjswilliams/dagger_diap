package com.techyourchance.dagger2course.common.composition

import com.techyourchance.dagger2course.Constants
import com.techyourchance.dagger2course.networking.StackoverflowApi
import com.techyourchance.dagger2course.questions.FetchQuestionDetailUseCase
import com.techyourchance.dagger2course.questions.FetchQuestionUseCase
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AppCompositionRoot {

    //instantiate it once, as a global object
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    //instantiate it once, as a global object
    val stackoverflowApi: StackoverflowApi = retrofit.create(StackoverflowApi::class.java)

    //get a new instance of fetchQuestion useCase when it is query, to use getter with get()
    //each time it is call a new instance is create, because we overridden get
    val fetchQuestionsUseCase get() = FetchQuestionUseCase(stackoverflowApi)

    val fetchQuestionDetailUseCase get() = FetchQuestionDetailUseCase(stackoverflowApi)

}