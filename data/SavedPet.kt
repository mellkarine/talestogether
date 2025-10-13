package com.example.talestogether.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_pets")
data class SavedPet(
    val name: String,
    val breed: String,
    val age: Int,
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    // A chave primária é gerada automaticamente. [2]
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)