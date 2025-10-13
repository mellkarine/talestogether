package com.example.talestogether.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.talestogether.TalesTogetherApplication
import com.example.talestogether.data.SavedPet
import com.example.talestogether.ui.theme.PastelColorScheme
import com.example.talestogether.viewmodel.PetViewModelFactory
import com.example.talestogether.viewmodel.SavedPetViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedPetsScreen(
    onNavigateToNewPet: () -> Unit
) {
    // Inicialização da ViewModel, pegando o Repositório do meu AppContainer.
    val context = LocalContext.current
    val application = context.applicationContext as TalesTogetherApplication
    val petViewModel: SavedPetViewModel = viewModel(
        factory = PetViewModelFactory(application.container.petRepository)
    )

    // Observo o Estado reativo do DB. [4]
    val uiState by petViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pets Salvos (CRUD: Read/Delete)") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PastelColorScheme.primary)
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToNewPet, containerColor = PastelColorScheme.secondary) {
                Icon(Icons.Filled.Add, contentDescription = "Adicionar Novo Pet", tint = PastelColorScheme.onSecondary)
            }
        }
    ) { paddingValues ->

        if (uiState.petList.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("Nenhum Pet Salvo no DB. Clique no '+' para adicionar!", color = PastelColorScheme.onBackground)
            }
        } else {
            // LazyColumn para exibir a lista. [4]
            LazyColumn(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
                items(uiState.petList, key = { it.id }) { pet ->
                    SavedPetItem(
                        pet = pet,
                        onDeleteClick = { petViewModel.deletePet(pet) }, // Envio o evento Delete.
                        onEditClick = { /* Edição futura */ }
                    )
                    Divider()
                }
            }
        }
    }
}

@Composable
fun SavedPetItem(
    pet: SavedPet,
    onDeleteClick: () -> Unit,
    onEditClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onEditClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(pet.name, style = MaterialTheme.typography.titleMedium, color = PastelColorScheme.onSurface)
            Text("ID: ${pet.id}, Raça: ${pet.breed}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        }
        IconButton(onClick = onDeleteClick) {
            Icon(Icons.Filled.Delete, contentDescription = "Excluir", tint = PastelColorScheme.secondary)
        }
    }
}