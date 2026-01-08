// data/repository/Mappers.kt
package com.example.quiztestapp.data.repository

import com.example.quiztestapp.data.api.QuestionDto
import com.example.quiztestapp.data.db.QuizEntity
import com.example.quiztestapp.domain.model.Question
import kotlinx.serialization.json.Json
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer

// ✅ DTO → Domain
fun QuestionDto.toDomain(): Question {
    return Question(
        id = id,
        category = category,
        difficulty = difficulty,
        language = language,
        text = text,
        options = options,
        correctAnswerIndex = correctAnswerIndex,
        correctAnswer = correctAnswer
    )
}

// ✅ Domain → Entity (Room DB için)
fun Question.toEntity(): QuizEntity {
    val jsonOptions: String = Json.encodeToString(
        ListSerializer(String.serializer()),
        options
    )
    return QuizEntity(
        id = id,
        category = category,
        difficulty = difficulty,
        language = language,
        text = text,
        optionsJson = jsonOptions,
        correctAnswerIndex = correctAnswerIndex,
        correctAnswer = correctAnswer
    )
}

// ✅ Entity → Domain
fun QuizEntity.toDomain(): Question {
    val list: List<String> = Json.decodeFromString(
        ListSerializer(String.serializer()),
        optionsJson
    )
    return Question(
        id = id,
        category = category,
        difficulty = difficulty,
        language = language,
        text = text,
        options = list,
        correctAnswerIndex = correctAnswerIndex,
        correctAnswer = correctAnswer
    )
}