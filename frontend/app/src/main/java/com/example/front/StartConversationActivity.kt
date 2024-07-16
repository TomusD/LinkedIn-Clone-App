package com.example.front

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.front.ui.theme.FrontEndTheme

class StartConversationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FrontEndTheme {
                StartConversationScreen()
            }
        }
    }

    @Composable
    fun StartConversationScreen() {

        // Variables of Text Fields and Latest friends that talked with
        var to by remember { mutableStateOf("") }
        var message by remember { mutableStateOf("") }
        var lastFriends by remember { mutableStateOf(listOf("Friend 1", "Friend 2", "Friend 3","Friend 4")) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {

            Text(
                "Start New Conversation",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // To Text field
            OutlinedTextField(
                value = to,
                onValueChange = { to = it },
                label = { Text("To") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Message Text field
            OutlinedTextField(
                value = message,
                onValueChange = { message = it },
                label = { Text("Message") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { /* Handle Send Message button click */ }, modifier = Modifier.fillMaxWidth()
            ) {
                Text("Send Message")
            }

            Text(
                "Last Users",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            //List of latest friends talked to
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(lastFriends) { friend ->
                    LastFriendsItem(friend)
                }
            }
        }
    }

    // Last friends list of cards
    @Composable
    fun LastFriendsItem(friend: String) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    val intent =
                        Intent(this@StartConversationActivity, MessagesActivity::class.java)
                    startActivity(intent)
                },
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Text(
                friend,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}