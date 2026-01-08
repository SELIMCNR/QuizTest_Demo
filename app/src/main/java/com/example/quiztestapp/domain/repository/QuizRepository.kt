// domain/repository/QuizRepository.kt
package com.example.quiztestapp.domain.repository

import com.example.quiztestapp.domain.model.Question

interface QuizRepository {
    suspend fun loadQuestions(
        forceRefresh: Boolean = false,
        category: String? = null,
        language: String = "tr",
        difficulty: String? = null,
        amount: Int = 10,
        mode: String = "mixed",
        offset: Int = 0,
        seed: Int? = null
    ): Result<List<Question>>
}