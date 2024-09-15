package com.example.front.screens.Subcomponents

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.*
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.front.data.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import com.example.front.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(drawerState: DrawerState, scope: CoroutineScope, onSearch: (String) -> Unit,) {
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }

    Spacer(modifier = Modifier.height(30.dp))
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)
                ) {
                    AsyncImage(
                        model = SessionManager(LocalContext.current).getUserInfo(SessionManager.USER_IMAGE_URL),
                        contentDescription = "image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(50.dp).clip(CircleShape).border(BorderStroke(2.dp, Color.Blue), CircleShape).background(Color.LightGray)
                            .clickable {
                                scope.launch {
                                    drawerState.open()
                                } })

                    Spacer(modifier = Modifier.width(16.dp))
                    // Search input field
                    TextField(
                        value = searchQuery,
                        onValueChange = { newQuery ->
                            searchQuery = newQuery
                        },
                        placeholder = { Text("Search...") },
                        singleLine = true,
                        modifier = Modifier
                            .weight(1f) // This makes the TextField take up all the available space
                    )

                    // Search button (when clicked, perform search)
                    IconButton(
                        onClick = {
                            onSearch(searchQuery.text) // Trigger search only on button click
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search Button"
                        )
                    }
                }
                Spacer(modifier = Modifier.width(5.dp))

                IconButton(
                    onClick = {}
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.chat_icon_2),
                        contentDescription = "Settings",
                        modifier = Modifier.size(25.dp)
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            titleContentColor = Color.Black,
            actionIconContentColor = Color.Black
        ),
    )
}
