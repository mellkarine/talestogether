package com.example.talestogether.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.talestogether.repository.PetRepositoryLocal
import com.example.talestogether.repository.PetRepositoryRemote

class OwnerPetsViewModelFactory(
    private val localRepo: PetRepositoryLocal,
    private val remoteRepo: PetRepositoryRemote
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OwnerPetsViewModel::class.java)) {
            return OwnerPetsViewModel(localRepo, remoteRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
