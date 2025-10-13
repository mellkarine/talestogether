package com.example.talestogether.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.talestogether.data.PetRepository
import com.example.talestogether.data.SavedPet
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

// O Estado da UI: o que a tela consome.
data class SavedPetsUiState(
    val petList: List<SavedPet> = emptyList()
)

class SavedPetViewModel(private val repository: PetRepository) : ViewModel() {

    // **PONTO CHAVE:** Converte o Flow do Room em StateFlow (Observável pelo Compose).
    val uiState: StateFlow<SavedPetsUiState> =
        repository.getAllPetsStream()
            .map { list ->
                SavedPetsUiState(petList = list)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5.seconds.inWholeMilliseconds),
                initialValue = SavedPetsUiState()
            )

    // --- Funções CRUD ---

    fun savePet(pet: SavedPet) {
        // Uso o viewModelScope para garantir que a operação IO aconteça em background.
        viewModelScope.launch {
            repository.savePet(pet)
        }
    }

    fun deletePet(pet: SavedPet) {
        viewModelScope.launch {
            repository.deletePet(pet)
        }
    }
}