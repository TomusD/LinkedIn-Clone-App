package com.example.front.screens.user

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.front.ChatWebSocketListener
import com.example.front.activity.FriendsViewModel
import com.example.front.data.ApiClient

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalChatScreen(navController: NavController, userId: Int, friendVM: FriendsViewModel = viewModel()) {
    val context = LocalContext.current
    val apiClient = ApiClient()

    LaunchedEffect(Unit) {
        friendVM.getUser(context, userId)
        friendVM.getUserMessages(context, userId)
    }

    val friend = friendVM.user.collectAsState().value
    val messages = friendVM.userMsg.collectAsState().value


    var message by remember { mutableStateOf(TextFieldValue("")) }
    var chatMessages by remember { mutableStateOf(listOf<String>()) }

    // WebSocket listener
    val chatWebSocketListener = remember {
        ChatWebSocketListener().apply {
            onMessageReceived = { newMessage ->
                chatMessages = chatMessages + newMessage
            }
        }
    }

    // Create WebSocket connection
    val webSocket = remember { apiClient.createWebSocket(chatWebSocketListener, context, user_id = 50) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "${friend?.name} ${friend?.surname}",
                        fontSize = 5.em,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {navController.navigateUp()}) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color.Black,
                    navigationIconContentColor = Color.Black
                ),
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxWidth(),

                )
        },
        content = {
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(16.dp))
            {
                // Display chat messages
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    chatMessages.forEach { msg ->
                        Text(msg)
                    }
                }

                // Input and send message
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    BasicTextField(
                        value = message,
                        onValueChange = { message = it },
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(onClick = {
                        webSocket.send(message.text) // Send message via WebSocket
                        message = TextFieldValue("") // Clear input field after sending
                    }) {
                        Text("Send")
                    }
                }
            }

        }
    )
}
