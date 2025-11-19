package com.example.talestogether.repository

import com.example.talestogether.data.local.dao.PetDao
import com.example.talestogether.data.local.entity.PetEntity
import kotlinx.coroutines.flow.Flow

class PetRepositoryLocal(private val petDao: PetDao) {

    fun getAllPets(): Flow<List<PetEntity>> {
        return petDao.getAllPets()
    }

    fun getPetById(id: String): Flow<PetEntity?> {
        return petDao.getPetById(id)
    }

    suspend fun insert(pet: PetEntity) {
        petDao.insert(pet)
    }

    suspend fun update(pet: PetEntity) {
        petDao.update(pet)
    }

    suspend fun deleteById(id: String) {
        petDao.deleteById(id)
    }
}
