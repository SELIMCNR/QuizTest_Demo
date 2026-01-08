package com.example.quiztestapp.viewmodel


import com.example.quiztestapp.domain.model.Question
sealed interface QuizUiState {
    data object Loading : QuizUiState
    data class Error(val message: String) : QuizUiState
    data class Ready(
        val questions: List<Question>,
        val currentIndex: Int,
        val selectedOption: Int?,
        val score: Int,
        val showResult: Boolean,
        val finished: Boolean
    ) : QuizUiState
}