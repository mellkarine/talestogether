package com.example.talestogether.data.local.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.talestogether.data.local.entity.PetEntity

@Dao
interface PetDao {

    @Query("SELECT * FROM pets")
    fun getAllPets(): Flow<List<PetEntity>>

    @Query("SELECT * FROM pets WHERE id = :id LIMIT 1")
    fun getPetById(id: String): Flow<PetEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pet: PetEntity)

    @Update
    suspend fun update(pet: PetEntity)

    @Delete
    suspend fun delete(pet: PetEntity)


    @Query("DELETE FROM pets WHERE id = :petId")
    suspend fun deleteById(petId: String)

}
