package com.example.talestogether.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.talestogether.data.local.entity.PetEntity
import com.example.talestogether.viewmodel.OwnerPetsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OwnerEditPetScreen(
    viewModel: OwnerPetsViewModel,
    petId: String?,
    onSaved: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    var name by remember { mutableStateOf("") }
    var species by remember { mutableStateOf("") }
    var ownerName by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }

    // Carrega dados do pet quando abrir
    LaunchedEffect(petId) {
        if (petId != null) {
            viewModel.getPetById(petId)
        }
    }

    // Preenche form se for edição
    LaunchedEffect(uiState.selectedPet) {
        uiState.selectedPet?.let {
            name = it.name
            species = it.species
            ownerName = it.ownerName
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (petId == null) "Novo Pet" else "Editar Pet") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFD0BCFF))
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nome") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = species,
                onValueChange = { species = it },
                label = { Text("Raça") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = ownerName,
                onValueChange = { ownerName = it },
                label = { Text("Dono") },
                modifier = Modifier.fillMaxWidth()
            )


            Spacer(modifier = Modifier.height(20.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    val pet = PetEntity(
                        id = petId ?: System.currentTimeMillis().toString(),
                        name = name,
                        species = species,
                        ownerName = ownerName
                    )

                    if (petId == null) {
                        viewModel.addPet(pet)
                    } else {
                        viewModel.updatePet(pet)
                    }

                    onSaved()
                }
            ) {
                Text("Salvar")
            }

            if (petId != null) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFB4AB)),
                    onClick = {
                        viewModel.deletePet(petId)
                        onSaved()
                    }
                ) {
                    Text("Excluir")
                }
            }
        }
    }
}
