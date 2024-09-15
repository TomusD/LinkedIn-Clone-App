package com.example.front.screens.basic_screens

import UserCard
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.front.activity.BasicViewModel
import com.example.front.ui.theme.FrontEndTheme


@Composable
fun NetworkScreen(navController: NavController, viewModel: BasicViewModel = viewModel())  {
    val context = LocalContext.current

    // Call fetchUsers when the screen is first launched
    LaunchedEffect(Unit) {
        viewModel.fetchUsers(context)
    }

    val users = viewModel.users.collectAsState().value

    FrontEndTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {

            if (users.isNotEmpty()) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(users) { user ->
                        UserCard(user)
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(15.dp),
                ) {

                    Text(
                        "Network Screen",
                        style = MaterialTheme.typography.titleLarge,
                    )
                }
            }

        }

    }
}
