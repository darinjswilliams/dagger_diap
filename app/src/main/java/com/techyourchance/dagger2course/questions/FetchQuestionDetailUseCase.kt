package com.techyourchance.dagger2course.questions

import com.techyourchance.dagger2course.Constants
import com.techyourchance.dagger2course.networking.StackoverflowApi
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

open class FetchQuestionDetailUseCase(
    private val stackoverflowApi: StackoverflowApi

) {

    sealed class Result {
        class Success(val question: QuestionWithBody) : Result()
        object Failure : Result()
    }


    suspend fun fetchQuestionDetail(questionId: String): Result {
        return withContext(Dispatchers.IO) {
            val response = stackoverflowApi.questionDetails(questionId)

            try {

                if (response.isSuccessful && response.body() != null) {
                    return@withContext Result.Success(response.body()!!.question)

                } else {
                    return@withContext Result.Failure
                }
            } catch (t: Throwable) {
                if (t !is CancellationException) {
                    return@withContext Result.Failure

                } else {
                    throw t
                }
            }
        }
    }
}