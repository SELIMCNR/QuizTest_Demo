package com.example.quiztestapp.data.repository

import com.example.quiztestapp.data.api.QuizApi
import com.example.quiztestapp.data.db.QuizDao
import com.example.quiztestapp.domain.model.Question
import com.example.quiztestapp.domain.repository.QuizRepository

class QuizRepositoryImpl(
    private val api: QuizApi,
    private val dao: QuizDao
) : QuizRepository {

    override suspend fun loadQuestions(
        forceRefresh: Boolean,
        category: String?,
        language: String,
        difficulty: String?,
        amount: Int,
        mode: String,
        offset: Int,
        seed: Int?
    ): Result<List<Question>> {
        return try {
            val cached = dao.getAll().map { it.toDomain() }
            if (!forceRefresh && cached.isNotEmpty()) {
                Result.success(cached)
            } else {
                val remote = api.getQuestions(
                    category = category,
                    language = language,
                    difficulty = difficulty,
                    amount = amount,
                    mode = mode,
                    offset = offset,
                    seed = seed
                ).map { it.toDomain() }

                dao.clear()
                dao.upsertAll(remote.map { it.toEntity() })
                Result.success(remote)
            }
        } catch (e: Exception) {
            val cached = dao.getAll().map { it.toDomain() }
            if (cached.isNotEmpty()) Result.success(cached) else Result.failure(e)
        }
    }
}