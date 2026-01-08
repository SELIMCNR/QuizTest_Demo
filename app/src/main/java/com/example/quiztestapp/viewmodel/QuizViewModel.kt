package com.example.quiztestapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quiztestapp.domain.repository.QuizRepository
import kotlinx.coroutines.launch
// QuizViewModel.kt
class QuizViewModel(
    private val repository: QuizRepository
) : ViewModel() {

    var uiState: QuizUiState by mutableStateOf(QuizUiState.Loading)
        private set

    // Kullanıcı seçimleri
    var selectedCategory: String? = null
    var selectedLanguage: String = "en" // Varsayılan İngilizce
    var selectedAmount: Int = 10
    var selectedDifficulty: String? = null

    fun load(forceRefresh: Boolean = false) {
        uiState = QuizUiState.Loading
        viewModelScope.launch {
            val result = repository.loadQuestions(
                forceRefresh = forceRefresh,
                category = selectedCategory,
                language = selectedLanguage,
                difficulty = selectedDifficulty,
                amount = selectedAmount,
                mode = if (selectedCategory == null) "mixed" else "category",
                offset = 0,
                seed = null
            )
            uiState = result.fold(
                onSuccess = { qs ->
                    QuizUiState.Ready(
                        questions = qs,
                        currentIndex = 0,
                        selectedOption = null,
                        score = 0,
                        showResult = false,
                        finished = qs.isEmpty()
                    )
                },
                onFailure = { e -> QuizUiState.Error(e.message ?: "Bir hata oluştu") }
            )
        }
    }

    fun selectOption(index: Int) {
        val state = uiState as? QuizUiState.Ready ?: return
        val question = state.questions[state.currentIndex]
        val correct = question.correctAnswerIndex

        // ✅ Zorluk bazlı puanlama
        val difficultyWeight = when (question.difficulty.lowercase()) {
            "easy" -> 1
            "medium" -> 2
            "hard" -> 3
            else -> 1
        }

        val newScore = if (index == correct) state.score + difficultyWeight else state.score

        uiState = state.copy(
            selectedOption = index,
            showResult = true,
            score = newScore
        )
    }

    fun next() {
        val state = uiState as? QuizUiState.Ready ?: return
        val nextIndex = state.currentIndex + 1
        if (nextIndex < state.questions.size) {
            uiState = state.copy(
                currentIndex = nextIndex,
                selectedOption = null,
                showResult = false
            )
        } else {
            uiState = state.copy(finished = true)
        }
    }

    fun retry() = load(forceRefresh = true)
}