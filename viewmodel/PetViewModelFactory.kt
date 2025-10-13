package com.example.talestogether.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.talestogether.data.PetRepository

// Minha Factory personalizada para injetar o Repositório na ViewModel.
class PetViewModelFactory(private val repository: PetRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SavedPetViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SavedPetViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}