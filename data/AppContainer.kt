package com.example.talestogether.data

import android.content.Context
import androidx.room.Room
import kotlinx.coroutines.flow.Flow

// Minha interface de Repositório (Contrato)
interface PetRepository {
    fun getAllPetsStream(): Flow<List<SavedPet>>
    suspend fun savePet(pet: SavedPet)
    suspend fun deletePet(pet: SavedPet)
}

// A implementação que abstrai o Room da ViewModel.
class OfflinePetRepository(private val petDao: SavedPetDao) : PetRepository {
    override fun getAllPetsStream(): Flow<List<SavedPet>> = petDao.getAllPets()
    override suspend fun savePet(pet: SavedPet) = petDao.upsertPet(pet)
    override suspend fun deletePet(pet: SavedPet) = petDao.deletePet(pet)
}


// O Contrato de Dependências
interface AppContainer {
    val petRepository: PetRepository
}

// A implementação Singleton do DB
class AppDataContainer(private val context: Context) : AppContainer {
    private val database: PetDatabase by lazy {
        Room.databaseBuilder(
            context.applicationContext, // Uso Application Context [3]
            PetDatabase::class.java,
            "pet_database"
        ).build()
    }

    override val petRepository: PetRepository by lazy {
        OfflinePetRepository(database.savedPetDao())
    }
}