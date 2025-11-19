package com.example.talestogether.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.talestogether.data.local.entity.PetEntity
import com.example.talestogether.viewmodel.OwnerPetsViewModel
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OwnerPetListScreen(
    viewModel: OwnerPetsViewModel,
    onEdit: (String?) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Meus Pets") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFD0BCFF))
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onEdit(null) },
                containerColor = Color(0xFFD0BCFF)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Novo Pet")
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            if (uiState.pets.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Você ainda não cadastrou nenhum pet.")
                }
            } else {
                LazyColumn {
                    items(uiState.pets) { pet ->
                        PetItemCard(
                            pet = pet,
                            onEdit = { onEdit(pet.id) },
                            onDelete = { viewModel.deletePet(pet.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PetItemCard(
    pet: PetEntity,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {

            Text(pet.name, fontWeight = FontWeight.Bold)
            Text(pet.species, color = Color.Gray)

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {

                Button(
                    onClick = onEdit,
                    colors = ButtonDefaults.buttonColors(Color(0xFFD0BCFF))
                ) {
                    Text("Editar")
                }

                Button(
                    onClick = onDelete,
                    colors = ButtonDefaults.buttonColors(Color(0xFFFFB4AB))
                ) {
                    Text("Excluir")
                }
            }
        }
    }
}
