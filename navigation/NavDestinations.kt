package com.example.talestogether.navigation

// Definição das rotas (strings únicas) que o NavHost usará. [1]
object NavDestinations {
    // Rotas originais do projeto
    const val FEED = "feed"
    const val PET_PROFILE = "pet_profile"
    const val OWNER_PROFILE = "owner_profile"
    const val MATCHES = "matches"

    // Minhas Rotas CRUD
    const val SAVED_PETS = "saved_pets" // Tela de Listagem (Read/Delete)
    const val PET_ENTRY = "pet_entry"   // Tela de Criação (Create)
}