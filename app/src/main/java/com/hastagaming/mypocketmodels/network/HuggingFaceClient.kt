package com.hastagaming.mypocketmodels.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

// Format permintaan ke Hugging Face
data class HFRequest(
    val inputs: String,
    val parameters: Map<String, Any> = mapOf("max_new_tokens" to 250)
)

// Format jawaban (biasanya berupa list)
data class HFResponse(
    val generated_text: String
)

interface HuggingFaceApi {
    @POST("models/{modelId}")
    suspend fun generateResponse(
        @Path("modelId") modelId: String,
        @Header("Authorization") token: String,
        @Body request: HFRequest
    ): List<HFResponse>
}

object HFRetrofitClient {
    private const val BASE_URL = "https://api-inference.huggingface.co/"

    val instance: HuggingFaceApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(HuggingFaceApi::class.java)
    }
}
