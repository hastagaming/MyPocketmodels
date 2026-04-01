package com.hastagaming.mypocketmodels.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hastagaming.mypocketmodels.data.ChatDao
import com.hastagaming.mypocketmodels.data.MessageEntity
import com.hastagaming.mypocketmodels.network.OllamaRequest
import com.hastagaming.mypocketmodels.network.RetrofitClient
import kotlinx.coroutines.launch

class ChatViewModel(private val chatDao: ChatDao) : ViewModel() {

    val allMessages = chatDao.getAllMessages()

    fun sendMessage(userText: String) {
        viewModelScope.launch {
            // 1. Simpan pesan user ke memori
            chatDao.insertMessage(MessageEntity(sender = "user", text = userText))

            try {
                // 2. Tanya ke AI (Ollama)
                val response = RetrofitClient.instance.generateResponse(
                    OllamaRequest(prompt = userText)
                )

                // 3. Simpan jawaban AI ke memori
                chatDao.insertMessage(MessageEntity(sender = "ai", text = response.response))
            } catch (e: Exception) {
                chatDao.insertMessage(MessageEntity(sender = "ai", text = "Gagal terhubung ke AI: ${e.message}"))
            }
        }
    }
}