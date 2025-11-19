package com.example.talestogether.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.talestogether.data.local.dao.PetDao // Importe o PetDao
import com.example.talestogether.data.local.dao.PostDao // Importe o PostDao
import com.example.talestogether.data.local.entity.PetEntity
import com.example.talestogether.data.local.entity.PostEntity

@Database(entities = [PostEntity::class, PetEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase() {

    abstract fun postDao(): PostDao
    abstract fun petDao(): PetDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    // Se você aumentou a versão de 1 para 2, precisa de uma estratégia de migração.
                    // Para desenvolvimento (vai apagar os dados antigos), use:
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
