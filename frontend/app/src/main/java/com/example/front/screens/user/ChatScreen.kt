package com.example.front.screens.user

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.front.ChatScreens
import com.example.front.Screens
import com.example.front.activity.FriendsViewModel
import com.example.front.data.base.UserLittleDetail


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(navController: NavController, viewModel: FriendsViewModel = viewModel()) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.getChats(context)
    }

    val chats = viewModel.chats.collectAsState().value

    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Chats",
                            fontSize = 5.em,
                            fontWeight = FontWeight.Bold
                        )
                        IconButton(onClick = { navController.navigate(ChatScreens.PersonalChat.route) }) {
                            Icon(
                                imageVector = Icons.Filled.AddCircle,
                                contentDescription = "Start chat"
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(Screens.Home.route) }) {
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
                    .fillMaxWidth()
            )
        },
        content = {

            Column (
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()

            ) {
            Spacer(modifier = Modifier.height(100.dp))
                if (chats.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        items(chats.size) { index ->
                            HorizontalDivider(modifier = Modifier.height(5.dp).fillMaxWidth(0.8f))
                            ChatUserCard(navController, other_user = chats[index].other_user)
                        }
                    }
                } else {
                    Text(text = "Begin a discussion 💬")
                }
                HorizontalDivider(modifier = Modifier.height(5.dp).fillMaxWidth(0.8f))

            }
        }
    )
}

@Composable
fun ChatUserCard(navController: NavController, other_user: UserLittleDetail) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth(0.85f)
            .padding(10.dp)
    ) {
        Row {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(other_user.image_url)  // Replace with your image URL
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(10.dp))

            Column(
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text =
                    (other_user.user_fullname.split(" ")[0].lowercase().replaceFirstChar(Char::titlecase))
                        .plus(" ")
                        .plus(
                            other_user.user_fullname.split(" ")[1].lowercase().replaceFirstChar(Char::titlecase)
                        ),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
        }

        Button(
            onClick = {
                Log.d("MYTEST", "Navigating to chat with userId: ${other_user.user_id}")
                navController.navigate("personal_chat_route/${other_user.user_id}")

            },
            shape = RoundedCornerShape(4.dp),
            colors = ButtonDefaults.buttonColors(
                contentColor = Color.White,
                containerColor = Color.Black),
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            Text(text = "Send text", textAlign = TextAlign.Center, fontWeight = FontWeight(500))
        }
    }
}

