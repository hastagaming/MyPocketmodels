package com.hastagaming.mypocketmodels.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory // Pastikan pakai titik (.) di sini
import retrofit2.http.Body
import retrofit2.http.Post

// Format permintaan ke Ollama
data class OllamaRequest(
    val model: String = "llama3", 
    val prompt: String,
    val stream: Boolean = false
)

// Format jawaban dari Ollama
data class OllamaResponse(
    val response: String
)

interface OllamaApi {
    @Post("api/generate")
    suspend fun generateResponse(@Body request: OllamaRequest): OllamaResponse
}

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:11434/" 

    val instance: OllamaApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OllamaApi::class.java)
    }
}
