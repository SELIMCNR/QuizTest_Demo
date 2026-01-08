package com.example.quiztestapp.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "questions")
data class QuizEntity(
    @PrimaryKey val id: String,
    val category: String,
    val difficulty: String,
    val language: String,
    val text: String,
    val optionsJson: String,
    val correctAnswerIndex: Int,
    val correctAnswer: String
)