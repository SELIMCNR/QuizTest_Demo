package com.example.quiztestapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.room.Room
import com.example.quiztestapp.data.api.QuizApi
import com.example.quiztestapp.data.db.QuizDatabase
import com.example.quiztestapp.data.repository.QuizRepositoryImpl
import com.example.quiztestapp.ui.QuizScreen
import com.example.quiztestapp.ui.theme.QuiztestTheme
import com.example.quiztestapp.viewmodel.QuizViewModel
import com.example.quiztestapp.viewmodel.QuizViewModelFactory
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {

    // ✅ ViewModel property olarak tanımlandı
    private val vm: QuizViewModel by viewModels {
        QuizViewModelFactory(repo)
    }

    // ✅ Repository property olarak tanımlandı
    private val repo: QuizRepositoryImpl by lazy {
        // Network
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(60000, TimeUnit.SECONDS)
            .readTimeout(60000, TimeUnit.SECONDS)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://quiz-api-bcq5.onrender.com/")
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .build()
        val api = retrofit.create(QuizApi::class.java)

        // DB
        val db = Room.databaseBuilder(
            applicationContext,
            QuizDatabase::class.java,
            "quiz.db"
        ).build()

        QuizRepositoryImpl(api, db.quizDao())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            QuiztestTheme {
                androidx.compose.material3.Scaffold { innerPadding ->
                    QuizScreen(
                        viewModel = vm,
                        modifier = androidx.compose.ui.Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}