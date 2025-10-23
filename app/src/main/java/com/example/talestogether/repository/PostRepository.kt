package com.example.talestogether.repository

import com.example.talestogether.database.PostDao
import com.example.talestogether.database.PostEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PostRepository(private val postDao: PostDao) {

    suspend fun getAllPosts(): List<PostEntity> = withContext(Dispatchers.IO) {
        postDao.getAllPosts()
    }

    suspend fun insert(post: PostEntity) = withContext(Dispatchers.IO) {
        postDao.insert(post)
    }

    suspend fun update(post: PostEntity) = withContext(Dispatchers.IO) {
        postDao.update(post)
    }

    suspend fun delete(post: PostEntity) = withContext(Dispatchers.IO) {
        postDao.delete(post)
    }
}
