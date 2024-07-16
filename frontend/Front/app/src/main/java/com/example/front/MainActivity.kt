package com.example.front

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.front.ui.theme.FrontEndTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FrontEndTheme {
                MainScreen()
            }
        }
    }

    @Composable
    fun MainScreen() {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally )
        {
            Text(
                "All Screens",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(intent)
            }) {
                Text("Login Screen")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                val intent = Intent(this@MainActivity, RegisterActivity::class.java)
                startActivity(intent)
            }) {
                Text("Register Screen")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                val intent = Intent(this@MainActivity, ProfileActivity::class.java)
                startActivity(intent)
            }) {
                Text("Profile Screen")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                val intent = Intent(this@MainActivity, SettingsActivity::class.java)
                startActivity(intent)
            }) {
                Text("Settings Screen")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                val intent = Intent(this@MainActivity, PostActivity::class.java)
                startActivity(intent)
            }) {
                Text("Post Screen")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                val intent = Intent(this@MainActivity, AdvertisementsActivity::class.java)
                startActivity(intent)
            }) {
                Text("Advertisements Screen")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                val intent = Intent(this@MainActivity, NotificationsActivity::class.java)
                startActivity(intent)
            }) {
                Text("Notifications Screen")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                val intent = Intent(this@MainActivity, NetworkActivity::class.java)
                startActivity(intent)
            }) {
                Text("Network Screen")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                val intent = Intent(this@MainActivity, ChatActivity::class.java)
                startActivity(intent)
            }) {
                Text("Chat Screen")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                val intent = Intent(this@MainActivity, StartConversationActivity::class.java)
                startActivity(intent)
            }) {
                Text("Start Conversation Screen")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                val intent = Intent(this@MainActivity, MessagesActivity::class.java)
                startActivity(intent)
            }) {
                Text("Messages Screen")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                val intent = Intent(this@MainActivity, HomeActivity::class.java)
                startActivity(intent)
            }) {
                Text("Home Screen")
            }
        }
    }
}
