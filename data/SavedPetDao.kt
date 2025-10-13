package com.example.talestogether.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedPetDao {

    // Operação de Escrita (Create/Update): Uso 'suspend' para rodar em background.
    @Upsert // Insere ou Atualiza se o ID já existir.
    suspend fun upsertPet(pet: SavedPet)

    @Delete
    suspend fun deletePet(pet: SavedPet)

    // Operação de Leitura (Read): Retorna Flow para que a UI observe em tempo real.
    @Query("SELECT * FROM saved_pets ORDER BY name ASC")
    fun getAllPets(): Flow<List<SavedPet>>
}