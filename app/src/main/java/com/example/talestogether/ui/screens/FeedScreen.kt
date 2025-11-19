package com.example.talestogether.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.talestogether.ui.theme.PastelColorScheme
import java.text.SimpleDateFormat
import java.util.*

data class FeedPost(
    val id: Int,
    val name: String,
    val text: String,
    val time: Long = System.currentTimeMillis()
)

private val samplePosts = listOf(
    FeedPost(1, "Luna", "Hoje brinquei no parque!", System.currentTimeMillis() - 600_000),
    FeedPost(2, "Mimi", "Dormir no sol é vida...", System.currentTimeMillis() - 3_600_000),
    FeedPost(3, "Rex", "Aprendi um novo truque hoje!", System.currentTimeMillis() - 7_200_000)
)

private fun formatTime(ts: Long): String {
    val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
    return sdf.format(Date(ts))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(openProfile: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("TailTogether") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PastelColorScheme.primary,
                    titleContentColor = PastelColorScheme.onPrimary
                ),
                actions = {
                    IconButton(onClick = openProfile) {
                        Icon(Icons.Default.Person, contentDescription = "Perfil", tint = PastelColorScheme.onPrimary)
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(PastelColorScheme.background)
        ) {
            items(samplePosts) { post ->
                FeedCard(post)
            }
        }
    }
}

@Composable
fun FeedCard(post: FeedPost) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = post.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = formatTime(post.time),
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = post.text,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
