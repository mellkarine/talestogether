package com.example.talestogether

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.lifecycle.viewmodel.compose.viewModel

import com.example.talestogether.data.local.database.AppDatabase
import com.example.talestogether.repository.PostRepository
import com.example.talestogether.repository.PetRepositoryLocal
import com.example.talestogether.repository.PetRepositoryRemote

import com.example.talestogether.ui.screens.FeedScreen
import com.example.talestogether.ui.screens.ProfileScreen
import com.example.talestogether.ui.screens.OwnerProfileScreen
import com.example.talestogether.ui.screens.OwnerPetListScreen
import com.example.talestogether.ui.screens.OwnerEditPetScreen

import com.example.talestogether.viewmodel.PostViewModelFactory
import com.example.talestogether.viewmodel.OwnerPetsViewModelFactory
import com.example.talestogether.viewmodel.PostViewModel
import com.example.talestogether.viewmodel.OwnerPetsViewModel
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold


sealed class Screen(val route: String, val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    object Feed : Screen("feed", "Feed", Icons.Default.Home)
    object Profile : Screen("profile", "Perfil", Icons.Default.Person)
    object Owner : Screen("owner", "Meu Perfil", Icons.Default.AccountCircle)
    object Pets : Screen("pets", "Meus Pets", Icons.Default.Favorite)
    object EditPet : Screen("edit_pet", "Editar Pet", Icons.Default.Edit)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // database
        val db = AppDatabase.getDatabase(this)

        // repositories
        val postRepo = PostRepository(db.postDao())
        val petLocalRepo = PetRepositoryLocal(db.petDao())
        val petRemoteRepo = PetRepositoryRemote()

        // factories
        val postFactory = PostViewModelFactory(postRepo)
        val petsFactory = OwnerPetsViewModelFactory(petLocalRepo, petRemoteRepo)

        setContent {
            MaterialTheme {
                MainNavHost(postFactory, petsFactory)
            }
        }
    }
}

@Composable
fun MainNavHost(
    postFactory: PostViewModelFactory,
    petsFactory: OwnerPetsViewModelFactory
) {
    val navController = rememberNavController()

    val bottomItems = listOf(
        Screen.Feed,
        Screen.Profile,
        Screen.Owner,
        Screen.Pets
    )

    Scaffold(
        bottomBar = {
            val navBackStack by navController.currentBackStackEntryAsState()
            val route = navBackStack?.destination?.route

            NavigationBar {
                bottomItems.forEach { screen ->
                    NavigationBarItem(
                        icon = { androidx.compose.material3.Icon(screen.icon, contentDescription = screen.label) },
                        label = { Text(screen.label) },
                        selected = route == screen.route,
                        onClick = {
                            if (route != screen.route) {
                                navController.navigate(screen.route)
                            }
                        }
                    )
                }
            }
        }
    ) { padding ->

        NavHost(
            navController = navController,
            startDestination = Screen.Feed.route,
            modifier = Modifier.padding(padding)
        ) {

            // FEED
            composable(Screen.Feed.route) {
                FeedScreen(openProfile = { navController.navigate(Screen.Profile.route) })
            }

            // PERFIL PUBLICO DO PET
            composable(Screen.Profile.route) {
                ProfileScreen()
            }

            // MEU PERFIL (posts)
            composable(Screen.Owner.route) {
                val vm: PostViewModel = viewModel(factory = postFactory)
                OwnerProfileScreen(vm)
            }

            // LISTA MEUS PETS
            composable(Screen.Pets.route) {
                val vm: OwnerPetsViewModel = viewModel(factory = petsFactory)
                OwnerPetListScreen(
                    viewModel = vm,
                    onEdit = { petId ->
                        navController.navigate("${Screen.EditPet.route}/${petId ?: "new"}")
                    }
                )
            }

            // EDITAR PET
            composable(
                route = Screen.EditPet.route + "/{petId}"
            ) { backStack ->
                val vm: OwnerPetsViewModel = viewModel(factory = petsFactory)

                val id = backStack.arguments?.getString("petId")
                val petId = if (id == "new") null else id

                OwnerEditPetScreen(
                    viewModel = vm,
                    petId = petId,
                    onSaved = { navController.popBackStack() }
                )
            }
        }
    }
}
