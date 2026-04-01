package com.hastagaming.mypocketmodels.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

// 1. Tabel untuk menyimpan pesan (Otak/Memori)
@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val sender: String, // "user" atau "ai"
    val text: String,
    val timestamp: Long = System.currentTimeMillis()
)

// 2. Interface untuk interaksi dengan Database
@Dao
interface ChatDao {
    @Query("SELECT * FROM messages ORDER BY timestamp ASC")
    fun getAllMessages(): Flow<List<MessageEntity>>

    @Insert
    suspend fun insertMessage(message: MessageEntity)

    @Query("DELETE FROM messages")
    suspend fun clearChat()
}

// 3. Instance Database Utama
@Database(entities = [MessageEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun chatDao(): ChatDao
}