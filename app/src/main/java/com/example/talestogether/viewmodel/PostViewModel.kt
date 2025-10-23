package com.example.talestogether.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talestogether.database.PostEntity
import com.example.talestogether.repository.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PostViewModel(private val repository: PostRepository) : ViewModel() {

    private val _posts = MutableStateFlow<List<PostEntity>>(emptyList())
    val posts: StateFlow<List<PostEntity>> = _posts

    init {
        carregarPosts()
    }

    fun carregarPosts() {
        viewModelScope.launch {
            _posts.value = repository.getAllPosts()
        }
    }

    fun adicionarPost(text: String) {
        if (text.isBlank()) return
        viewModelScope.launch {
            repository.insert(PostEntity(text = text))
            carregarPosts()
        }
    }

    fun atualizarPost(post: PostEntity, novoTexto: String) {
        viewModelScope.launch {
            repository.update(post.copy(text = novoTexto))
            carregarPosts()
        }
    }

    fun curtirPost(post: PostEntity) {
        viewModelScope.launch {
            repository.update(post.copy(likes = post.likes + 1))
            carregarPosts()
        }
    }

    fun comentarPost(post: PostEntity) {
        viewModelScope.launch {
            repository.update(post.copy(comments = post.comments + 1))
            carregarPosts()
        }
    }

    fun repostar(post: PostEntity) {
        viewModelScope.launch {
            repository.update(post.copy(reposts = post.reposts + 1))
            carregarPosts()
        }
    }

    fun deletarPost(post: PostEntity) {
        viewModelScope.launch {
            repository.delete(post)
            carregarPosts()
        }
    }
}
