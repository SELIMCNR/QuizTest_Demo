package com.example.quiztestapp.data.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuestionDto(
    @SerialName("id") val id: String,
    @SerialName("category") val category: String,
    @SerialName("difficulty") val difficulty: String,
    @SerialName("language") val language: String,
    @SerialName("text") val text: String,
    @SerialName("options") val options: List<String>,
    @SerialName("correctAnswerIndex") val correctAnswerIndex: Int,
    @SerialName("correctAnswer") val correctAnswer: String
)