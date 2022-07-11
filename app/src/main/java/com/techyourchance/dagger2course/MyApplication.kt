package com.techyourchance.dagger2course

import android.app.Application
import com.techyourchance.dagger2course.networking.StackoverflowApi
import com.techyourchance.dagger2course.questions.FetchQuestionDetailUseCase
import com.techyourchance.dagger2course.questions.FetchQuestionUseCase
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class MyApplication: Application() {

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val stackoverflowApi: StackoverflowApi = retrofit.create(StackoverflowApi::class.java)

    //get a new instance of fetchQuestion useCase when it is query, to use getter with get()
    val fetchQuestionsUseCase get() = FetchQuestionUseCase(stackoverflowApi)

    val fetchQuestionDetailUseCase get() = FetchQuestionDetailUseCase(stackoverflowApi)

    override fun onCreate() {
        super.onCreate()
    }

}