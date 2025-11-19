package com.example.talestogether.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pets")
data class PetEntity(
    @PrimaryKey val id: String,        // Mesmo ID usado no Firebase
    val name: String,
    val species: String,
    val ownerName: String,
    val imageUrl: String? = null
)
