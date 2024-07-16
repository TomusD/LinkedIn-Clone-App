package com.example.front

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.front.ui.theme.FrontEndTheme

class ChatActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FrontEndTheme {
                ChatScreen()
            }
        }
    }

    @Composable
    fun ChatScreen() {
        var conversations by remember { mutableStateOf(listOf("Conversation with Friend 1", "Conversation with Friend 2", "Conversation with Friend 3", "Conversation with Friend 4")) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {

            Text(
                "Chat",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val intent = Intent(this@ChatActivity, StartConversationActivity::class.java)
                    startActivity(intent)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Start new Conversation")
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(conversations) { conversation ->
                    ConversationItem(conversation)
                }
            }
        }
    }

    @Composable
    fun ConversationItem(conversation: String) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    val intent = Intent(this@ChatActivity, MessagesActivity::class.java)
                    startActivity(intent)
                },
            elevation = CardDefaults.cardElevation(4.dp)
        ) {

            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(
                    painter = rememberAsyncImagePainter(
                        model = R.drawable.profile_placeholder
                    ),
                    contentDescription = "Profile Photo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(conversation, style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}