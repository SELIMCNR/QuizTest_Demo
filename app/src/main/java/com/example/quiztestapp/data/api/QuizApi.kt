package com.example.quiztestapp.data.api

import retrofit2.http.GET
import retrofit2.http.Query

interface QuizApi {
    @GET("quiz/questions")
    suspend fun getQuestions(
        @Query("category") category: String? = null,
        @Query("language") language: String = "tr",
        @Query("difficulty") difficulty: String? = null,
        @Query("amount") amount: Int = 10,
        @Query("mode") mode: String = "mixed",
        @Query("offset") offset: Int = 0,
        @Query("seed") seed: Int? = null
    ): List<QuestionDto>
}