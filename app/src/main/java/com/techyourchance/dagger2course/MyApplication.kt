package com.techyourchance.dagger2course

import android.app.Application
import com.techyourchance.dagger2course.common.composition.AppCompositionRoot
import com.techyourchance.dagger2course.networking.StackoverflowApi
import com.techyourchance.dagger2course.questions.FetchQuestionDetailUseCase
import com.techyourchance.dagger2course.questions.FetchQuestionUseCase
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class MyApplication: Application() {

    lateinit var appCompositionRoot: AppCompositionRoot
    override fun onCreate() {
        appCompositionRoot = AppCompositionRoot()
        super.onCreate()
    }

}