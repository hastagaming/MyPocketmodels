package com.hastagaming.mypocketmodels

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.hastagaming.mypocketmodels.data.MessageEntity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                ChatScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen() {
    // State untuk menampung teks yang sedang diketik
    var textFieldValue by remember { mutableStateOf("") }
    
    // List pesan (di awal kosong sesuai permintaanmu)
    val messages = remember { mutableStateListOf<MessageEntity>() }

    Column(modifier = Modifier.fillMaxSize()) {
        // 1. Top Bar / Header
        TopAppBar(
            title = { Text("MyPocketModels", color = Color.White) },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF101820))
        )

        // 2. Area Chat (Kosong di awal)
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            reverseLayout = false
        ) {
            items(messages) { message ->
                ChatBubble(message)
            }
        }

        // 3. Input Bar (Gaya ChatGPT/Gemini)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            TextField(
                value = textFieldValue,
                onValueChange = { textFieldValue = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Tanya MyPocketModels...") },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                )
            )
            
            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                onClick = {
                    if (textFieldValue.isNotBlank()) {
                        // Tambah pesan user ke list (Memori sementara)
                        messages.add(MessageEntity(sender = "user", text = textFieldValue))
                        textFieldValue = ""
                        // Nanti di sini panggil fungsi untuk AI merespon
                    }
                },
                enabled = textFieldValue.isNotBlank()
            ) {
                Icon(Icons.Default.Send, contentDescription = "Kirim", tint = MaterialTheme.colorScheme.primary)
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
            .padding(vertical = 4.dp),
        horizontalAlignment = if (isUser) androidx.compose.ui.Alignment.End else androidx.compose.ui.Alignment.Start
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = if (isUser) Color(0xFF007AFF) else Color(0xFFE9E9EB)
            ),
            shape = MaterialTheme.shapes.medium
        ) {
            Text(
                text = message.text,
                modifier = Modifier.padding(12.dp),
                color = if (isUser) Color.White else Color.Black
            )
        }
    }
}