package com.example.front.screens.basic_screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.front.activity.FriendsViewModel
import com.example.front.screens.Subcomponents.FriendRequestCard
import com.example.front.screens.Subcomponents.NotificationCard
import com.example.front.ui.theme.FrontEndTheme


@Composable
fun NotificationsScreen(navController: NavController, viewModel: FriendsViewModel = viewModel()) {
    val context = LocalContext.current

    LaunchedEffect (Unit) {
        viewModel.getFRNotifications(context)
        viewModel.getOtherNotifications(context)
    }

    var friendRequestsNotifications = viewModel.pendingRequests.collectAsState().value
    var otherNotifications = viewModel.pendingOtherRequests.collectAsState().value


    FrontEndTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column (
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(start = 15.dp, top = 40.dp)
                    .fillMaxSize()

            ) {

                // Applications Section
                Text(text = "Pending friend requests",
                    fontSize = 5.em,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 20.dp)
                )

                Row(
                    modifier = Modifier
                        .horizontalScroll(rememberScrollState())
                        .fillMaxWidth(),
                ) {
                    if (friendRequestsNotifications!!.isNotEmpty())
                        friendRequestsNotifications.forEach { fr ->
                            FriendRequestCard(fr, onAnswer = { uid, acc ->
                                viewModel.answerFriendRequest(context, uid, acc)
                            })
                        } else {
                        Text(text = "You don't have any friend requests", Modifier.padding(start = 20.dp, bottom = 15.dp))
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Jobs Created Section
                Text(text = "Other Notifications",
                    fontSize = 5.em,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 20.dp)
                )
                if (otherNotifications!!.isNotEmpty())
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                            items(otherNotifications) { n ->
                                NotificationCard(notification = n)
                            }
                } else {
                    Text(text = "Not much going on 🍃", Modifier.padding(start=20.dp, bottom = 15.dp))
                }

            }
        }
    }
}
