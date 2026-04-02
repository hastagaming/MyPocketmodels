package com.hastagaming.mypocketmodels

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// Pastikan package data ini sesuai dengan lokasi MessageEntity kamu
import com.hastagaming.mypocketmodels.data.MessageEntity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Log sederhana untuk memastikan token terminjeksi dengan benar (Cek di Logcat)
        val tokenStatus = if (BuildConfig.HF_TOKEN.isNotEmpty()) "Terdeteksi" else "Kosong"
        Log.d("Mymodels_Debug", "HF_TOKEN Status: $tokenStatus")

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainNavigation()
                }
            }
        }
    }
}

@Composable
fun MainNavigation() {
    var showAboutPage by remember { mutableStateOf(false) }

    if (showAboutPage) {
        AboutScreen(onBack = { showAboutPage = false })
    } else {
        ChatScreen(onInfoClick = { showAboutPage = true })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(onInfoClick: () -> Unit) {
    var textFieldValue by remember { mutableStateOf("") }
    val messages = remember { mutableStateListOf<MessageEntity>() }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("MyPocketModels", fontWeight = FontWeight.Bold) },
            actions = {
                IconButton(onClick = onInfoClick) {
                    Icon(Icons.Default.Info, contentDescription = "Tentang Aplikasi")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFF101820),
                titleContentColor = Color.White,
                actionIconContentColor = Color.White
            )
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(messages) { message ->
                ChatBubble(message)
            }
        }

        Surface(shadowElevation = 8.dp, color = MaterialTheme.colorScheme.surface) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = textFieldValue,
                    onValueChange = { textFieldValue = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Tanya MyPocketModels...") },
                    shape = RoundedCornerShape(24.dp),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )

                Spacer(modifier = Modifier.width(8.dp))

                FloatingActionButton(
                    onClick = {
                        if (textFieldValue.isNotBlank()) {
                            val userText = textFieldValue
                            messages.add(MessageEntity(sender = "user", text = userText))
                            textFieldValue = ""

                            // --- LOGIKA PENGGUNAAN TOKEN ---
                            // Di sini nanti kamu panggil OllamaClient.kt
                            // Contoh penggunaan token secara pasif:
                            val responseText = if (BuildConfig.HF_TOKEN.isNotEmpty()) {
                                "Sedang memproses via Hugging Face..."
                            } else {
                                "Token belum terpasang. Gunakan mode lokal saja."
                            }
                            
                            messages.add(MessageEntity(sender = "ai", text = responseText))
                        }
                    },
                    containerColor = Color(0xFF007AFF),
                    contentColor = Color.White,
                    shape = RoundedCornerShape(50.dp),
                    modifier = Modifier.size(52.dp)
                ) {
                    Icon(Icons.Default.Send, contentDescription = "Kirim")
                }
            }
        }
    }
}

@Composable
fun ChatBubble(message: MessageEntity) {
    val isUser = message.sender == "user"
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
    ) {
        Surface(
            color = if (isUser) Color(0xFF007AFF) else Color(0xFFF0F0F0),
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (isUser) 16.dp else 0.dp,
                bottomEnd = if (isUser) 0.dp else 16.dp
            )
        ) {
            Text(
                text = message.text,
                modifier = Modifier.padding(12.dp),
                color = if (isUser) Color.White else Color.Black,
                fontSize = 15.sp
            )
        }
    }
}

@Composable
fun AboutScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_app_logo),
            contentDescription = "Logo",
            modifier = Modifier.size(120.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text("MyPocketModels", fontSize = 26.sp, fontWeight = FontWeight.Bold)
        Text("v1.0.0-Beta", color = Color.Gray)

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            "Asisten AI lokal untuk menjalankan model Hugging Face & Ollama dengan privasi penuh di saku Anda.",
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
        )

        Spacer(modifier = Modifier.height(40.dp))

        Text("Dikembangkan Oleh:", fontSize = 12.sp, color = Color.Gray)
        Text("Nasa (hastagaming)", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF007AFF))

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF101820))
        ) {
            Text("Kembali ke Chat", color = Color.White)
        }
    }
}
