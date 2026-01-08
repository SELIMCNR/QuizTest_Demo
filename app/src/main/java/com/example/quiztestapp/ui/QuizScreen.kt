package com.example.quiztestapp.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.quiztestapp.domain.model.Question
import com.example.quiztestapp.ui.components.ResultCard
import com.example.quiztestapp.viewmodel.QuizUiState
import com.example.quiztestapp.viewmodel.QuizViewModel

@Composable
fun QuizScreen(
    viewModel: QuizViewModel,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(Unit) { viewModel.load() }

    Crossfade(
        targetState = viewModel.uiState,
        modifier = modifier.fillMaxSize()
    ) { state ->
        when (state) {
            is QuizUiState.Loading -> LoadingState()
            is QuizUiState.Error -> ErrorState(
                message = state.message,
                onRetry = { viewModel.retry() }
            )
            is QuizUiState.Ready -> ReadyState(
                questions = state.questions,
                currentIndex = state.currentIndex,
                selectedOption = state.selectedOption,
                score = state.score,
                showResult = state.showResult,
                finished = state.finished,
                onSelect = { viewModel.selectOption(it) },
                onNext = { viewModel.next() }
            )
        }
    }
}

@Composable
private fun LoadingState() {
    Box(Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorState(message: String, onRetry: () -> Unit) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Hata: $message", color = Color.Red)
        Spacer(Modifier.height(16.dp))
        Button(onClick = onRetry) { Text("Tekrar dene") }
    }
}

@Composable
private fun ReadyState(
    questions: List<Question>,
    currentIndex: Int,
    selectedOption: Int?,
    score: Int,
    showResult: Boolean,
    finished: Boolean,
    onSelect: (Int) -> Unit,
    onNext: () -> Unit
) {
    if (finished) {
        ResultCard(score = score, total = questions.size)
        return
    }

    val q = questions[currentIndex]

    Column(Modifier.padding(16.dp)) {
        Text(
            text = "Soru ${currentIndex + 1}/${questions.size}",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(Modifier.height(8.dp))
        Text(text = q.text, style = MaterialTheme.typography.titleLarge)

        Spacer(Modifier.height(16.dp))

        q.options.forEachIndexed { index, option ->
            Button(
                onClick = { onSelect(index) },
                enabled = selectedOption == null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Text(option)
            }
        }

        AnimatedVisibility(
            visible = showResult,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            val correct = selectedOption == q.correctAnswerIndex
            Column {
                Spacer(Modifier.height(16.dp))
                Text(
                    text = if (correct) "✅ Doğru!" else "❌ Yanlış!",
                    color = if (correct) Color(0xFF2E7D32) else Color(0xFFC62828),
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(Modifier.height(8.dp))
                Button(onClick = onNext) {
                    Text(
                        text = if (currentIndex < questions.lastIndex) "Sonraki Soru" else "Bitir"
                    )
                }
                Spacer(Modifier.height(8.dp))
                AssistChip(
                    onClick = {},
                    label = { Text("Skor: $score") }
                )
            }
        }
    }
}