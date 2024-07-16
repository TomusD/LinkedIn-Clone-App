package com.example.front

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.front.ui.theme.FrontEndTheme
import org.w3c.dom.Comment

class NotificationsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FrontEndTheme {
                NotificationsScreen()
            }
        }
    }

    @Composable
    fun  NotificationsScreen() {
        var friendReqs by remember { mutableStateOf(listOf("Friend request 1", "Friend request 2", "Friend request 3"))}
        var myArticles by remember { mutableStateOf(listOf("My Article 1", "My Article 2")) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                "Notifications",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Friend Requests",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // List of Friend Requests
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(friendReqs) { friendReq ->
                    FriendsRequestItem(friendReq)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Articles",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // List of Articles
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(myArticles) { myArticle ->
                    ArticleItem(myArticle)
                }
            }
        }
    }
    @Composable
    fun FriendsRequestItem(friendReq: String) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(friendReq, style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = { /* Handle More Information */ }) {
                    Text("More Information")
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = { /* Handle Accept */ }) {
                    Text("Accept Request")
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = { /* Handle Decline */ }) {
                    Text("Decline Request")
                }
            }
        }
    }

    @Composable
    fun ArticleItem(myArticle: String) {
        var comments by remember { mutableStateOf(listOf("Comment 1", "Comment 2", "Comment 3", "Comment 4")) }

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {

            Column(
                modifier = Modifier.padding(16.dp)
            ) {

                Text(myArticle, style = MaterialTheme.typography.bodyLarge)

                Spacer(modifier = Modifier.height(8.dp))

                Text("Interested", style = MaterialTheme.typography.bodyMedium)

                Spacer(modifier = Modifier.height(8.dp))

                Box(modifier = Modifier.height(150.dp)) {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        items(comments) { comment ->
                            CommentItem(comment) }
                    } }

                Spacer(modifier = Modifier.height(8.dp))

            }
        }
    }

    @Composable
    fun CommentItem(comment: String) {

            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(comment, style = MaterialTheme.typography.bodyMedium)
            }
    }
}