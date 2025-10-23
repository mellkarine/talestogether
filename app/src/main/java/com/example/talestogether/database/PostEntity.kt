package com.example.talestogether.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val text: String,
    val likes: Int = 0,
    val comments: Int = 0,
    val reposts: Int = 0
)