// domain/model/Question.kt
package com.example.quiztestapp.domain.model

data class Question(
    val id: String,
    val category: String,
    val difficulty: String,
    val language: String,
    val text: String,
    val options: List<String>,
    val correctAnswerIndex: Int,
    val correctAnswer: String
)