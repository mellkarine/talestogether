package com.example.talestogether.repository

import com.example.talestogether.data.local.entity.PetEntity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class PetRepositoryRemote {

    private val firestore = FirebaseFirestore.getInstance()
    private val pets = firestore.collection("pets")

    suspend fun fetchPets(): Result<List<PetEntity>> {
        return try {
            val list = pets.get().await().documents.mapNotNull { doc ->
                doc.toObject(PetEntity::class.java)?.copy(id = doc.id)
            }
            Result.success(list)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun insert(pet: PetEntity): Result<Unit> {
        return try {
            pets.document(pet.id).set(pet).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun update(pet: PetEntity): Result<Unit> {
        return try {
            pets.document(pet.id).set(pet).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun delete(petId: String): Result<Unit> {
        return try {
            pets.document(petId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
