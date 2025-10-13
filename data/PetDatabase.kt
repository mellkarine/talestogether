package com.example.talestogether.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    // CRÍTICO: Minha dupla precisa adicionar as Entidades Owner aqui (Owner::class, OwnerPost::class).
    entities = ,
    version = 1,
    exportSchema = false
)
abstract class PetDatabase : RoomDatabase() {
    // Método para fornecer o DAO.
    abstract fun savedPetDao(): SavedPetDao
}