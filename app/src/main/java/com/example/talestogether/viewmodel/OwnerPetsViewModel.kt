package com.example.talestogether.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talestogether.data.local.entity.PetEntity
import com.example.talestogether.repository.PetRepositoryLocal
import com.example.talestogether.repository.PetRepositoryRemote
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class OwnerPetsUiState(
    val pets: List<PetEntity> = emptyList(),
    val selectedPet: PetEntity? = null,
    val error: String? = null
)

class OwnerPetsViewModel(
    private val localRepo: PetRepositoryLocal,
    private val remoteRepo: PetRepositoryRemote
) : ViewModel() {

    private val _uiState = MutableStateFlow(OwnerPetsUiState())
    val uiState: StateFlow<OwnerPetsUiState> = _uiState

    init {
        loadPets()
    }

    // ------------------------
    // CARREGAR LISTA DE PETS
    // ------------------------
    fun loadPets() {
        viewModelScope.launch {
            localRepo.getAllPets().collect { list ->
                _uiState.value = _uiState.value.copy(pets = list)
            }
        }
    }

    // ------------------------
    // BUSCAR PET POR ID
    // ------------------------
    fun getPetById(id: String) {
        viewModelScope.launch {
            localRepo.getPetById(id).collect { pet ->
                _uiState.value = _uiState.value.copy(selectedPet = pet)
            }
        }
    }

    // ------------------------
    // ADICIONAR PET (CREATE)
    // ------------------------
    fun addPet(pet: PetEntity) {
        viewModelScope.launch {
            localRepo.insert(pet)
            remoteRepo.insert(pet)
            loadPets()
        }
    }

    // ------------------------
    // ATUALIZAR PET (UPDATE)
    // ------------------------
    fun updatePet(pet: PetEntity) {
        viewModelScope.launch {
            localRepo.update(pet)
            remoteRepo.update(pet)
            loadPets()
        }
    }

    // ------------------------
    // DELETAR PET POR ID
    // AGORA COMPATÍVEL COM OwnerPetListScreen
    // ------------------------
    fun deletePet(id: String) {
        viewModelScope.launch {
            localRepo.deleteById(id)
            remoteRepo.delete(id)
            loadPets()
        }
    }
}
