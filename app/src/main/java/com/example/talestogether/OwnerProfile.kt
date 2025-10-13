package com.example.talestogether

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.talestogether.database.AppDatabase
import com.example.talestogether.database.PostDao
import com.example.talestogether.database.PostEntity
import com.example.talestogether.ui.theme.PastelColorScheme
import kotlinx.coroutines.launch

class OwnerProfile : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = AppDatabase.getDatabase(this)
        val postDao = db.postDao()

        setContent {
            OwnerProfileScreen(postDao)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OwnerProfileScreen(postDao: PostDao) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var posts by remember { mutableStateOf(listOf<PostEntity>()) }
    var newPostText by remember { mutableStateOf("") }
    var editingPostId by remember { mutableStateOf<Int?>(null) }
    var editingText by remember { mutableStateOf("") }

    // Carrega posts ao iniciar
    LaunchedEffect(Unit) {
        posts = postDao.getAllPosts()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PastelColorScheme.primary)
            )
        },
        bottomBar = {
            BottomAppBar(containerColor = PastelColorScheme.primary) {
                IconButton(onClick = { Toast.makeText(context, "Home clicado", Toast.LENGTH_SHORT).show() }) {
                    Icon(Icons.Default.Home, contentDescription = "Home", tint = PastelColorScheme.onPrimary)
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(PastelColorScheme.background)
                .padding(innerPadding)
        ) {
            item {
                // Header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(PastelColorScheme.primary, PastelColorScheme.secondary)
                            )
                        )
                ) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(Color.Gray)
                            .align(Alignment.BottomCenter)
                            .offset(y = 50.dp)
                    )
                }

                Spacer(modifier = Modifier.height(60.dp))

                // Nome e bio
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Text("Mell Karine", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = PastelColorScheme.onBackground)
                    Text("@mellkarine", fontSize = 16.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Amante de pets, café e programação. Sempre compartilhando momentos com meus bichinhos.",
                        fontSize = 14.sp,
                        color = PastelColorScheme.onBackground,
                        modifier = Modifier.padding(horizontal = 16.dp),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

                // Input de novo post ou edição
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = editingPostId?.let { editingText } ?: newPostText,
                        onValueChange = { value ->
                            if (editingPostId != null) editingText = value
                            else newPostText = value
                        },
                        placeholder = { Text("O que você quer compartilhar?") },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        scope.launch {
                            if (editingPostId != null) {
                                // Atualiza post existente
                                val postToEdit = posts.find { it.id == editingPostId } ?: return@launch
                                postDao.update(postToEdit.copy(text = editingText))
                                posts = postDao.getAllPosts()
                                editingPostId = null
                                editingText = ""
                                Toast.makeText(context, "Post editado!", Toast.LENGTH_SHORT).show()
                            } else if (newPostText.isNotBlank()) {
                                postDao.insert(PostEntity(text = newPostText))
                                posts = postDao.getAllPosts()
                                newPostText = ""
                                Toast.makeText(context, "Post publicado!", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }) {
                        Text(if (editingPostId != null) "Salvar" else "Postar")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Feed de posts
            items(posts) { post ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp, horizontal = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .background(PastelColorScheme.surface)
                            .padding(12.dp)
                    ) {
                        Text(post.text, color = PastelColorScheme.onSurface)
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            InteractionButton(label = "❤️ ${post.likes}") {
                                scope.launch {
                                    postDao.update(post.copy(likes = post.likes + 1))
                                    posts = postDao.getAllPosts()
                                }
                            }
                            InteractionButton(label = "💬 ${post.comments}") {
                                scope.launch {
                                    postDao.update(post.copy(comments = post.comments + 1))
                                    posts = postDao.getAllPosts()
                                }
                            }
                            InteractionButton(label = "🔁 ${post.reposts}") {
                                scope.launch {
                                    postDao.update(post.copy(reposts = post.reposts + 1))
                                    posts = postDao.getAllPosts()
                                }
                            }
                            InteractionButton(label = "✏️") {
                                editingPostId = post.id
                                editingText = post.text
                            }
                            InteractionButton(label = "🗑️") {
                                scope.launch {
                                    postDao.delete(post)
                                    posts = postDao.getAllPosts()
                                }
                            }
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}

@Composable
fun InteractionButton(label: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = PastelColorScheme.primary)
    ) {
        Text(label, color = PastelColorScheme.onPrimary)
    }
}
