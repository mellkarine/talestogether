package com.example.talestogether.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.talestogether.TalesTogetherApplication
import com.example.talestogether.data.SavedPet
import com.example.talestogether.ui.theme.PastelColorScheme
import com.example.talestogether.viewmodel.PetViewModelFactory
import com.example.talestogether.viewmodel.SavedPetViewModel
import androidx.compose.runtime.saveable.rememberSaveable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetEntryScreen(
    onSaveSuccess: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val application = context.applicationContext as TalesTogetherApplication
    val petViewModel: SavedPetViewModel = viewModel(
        factory = PetViewModelFactory(application.container.petRepository)
    )

    // O estado de entrada usa 'rememberSaveable' para sobreviver à rotação.
    var name by rememberSaveable { mutableStateOf("") }
    var breed by rememberSaveable { mutableStateOf("") }
    var ageText by rememberSaveable { mutableStateOf("") }
    var postText by rememberSaveable { mutableStateOf("") }

    // Validação simples:
    val isFormValid = name.isNotBlank() && breed.isNotBlank() && ageText.toIntOrNull()!= null && postText.isNotBlank()

    fun onSaveClicked() {
        val ageInt = ageText.toIntOrNull()?: return

        val newPet = SavedPet(
            name = name,
            breed = breed,
            age = ageInt,
            text = postText,
            id = 0 // ID 0 garante que o @Upsert insira um novo registro (Create)
        )

        petViewModel.savePet(newPet) // Envio o evento para a ViewModel.
        onSaveSuccess()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Adicionar Novo Pet (Create)") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PastelColorScheme.primary),
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            // TextFields
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nome do Pet") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = breed,
                onValueChange = { breed = it },
                label = { Text("Raça") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = ageText,
                onValueChange = { ageText = it.filter { char -> char.isDigit() } },
                label = { Text("Idade (em anos)") },
                keyboardOptions = androidx.compose.ui.text.input.KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )

            OutlinedTextField(
                value = postText,
                onValueChange = { postText = it },
                label = { Text("Conte sobre o dia do Pet (Postagem)") },
                minLines = 3,
                maxLines = 5,
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            )

            // Botão Salvar
            Button(
                onClick = ::onSaveClicked,
                enabled = isFormValid, // Habilitado pela validação
                colors = ButtonDefaults.buttonColors(containerColor = PastelColorScheme.primary),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Salvar Pet", color = PastelColorScheme.onPrimary)
            }
        }
    }
}