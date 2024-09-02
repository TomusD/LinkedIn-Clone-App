package com.example.front.screens.basic_screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.front.R
import com.example.front.ui.theme.FrontEndTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    FrontEndTheme {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                DrawerContent()
            },
            content = {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        AsyncImage(
                                            model = "https://creazilla-store.fra1.digitaloceanspaces.com/cliparts/7772861/business-man-laptop-computer-clipart-md.png",
                                            contentDescription = "image",
                                            modifier = Modifier
                                                .size(45.dp)
                                                .clip(RoundedCornerShape(5.dp))
                                                .background(Color.LightGray)
                                                .clickable {
                                                    scope.launch {
                                                        drawerState.open()
                                                    }
                                                },
                                        )

                                        var search_text by remember { mutableStateOf("") }
                                        Spacer(modifier = Modifier.width(16.dp))
                                        TextField(
                                            value = search_text,
                                            onValueChange = {newText -> search_text = newText},
                                            placeholder = {
                                                Text(text = "Search...", Modifier.fillMaxWidth().size(20.dp)) },
                                            leadingIcon = {
                                                Icon(Icons.Default.Search, contentDescription = "Search")
                                            },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(10.dp))
                                                .background(Color.LightGray)
                                                .padding(horizontal = 0.dp),
                                            colors = TextFieldDefaults.textFieldColors(
                                                containerColor = Color.LightGray,
                                                focusedIndicatorColor = Color.Transparent,
                                                unfocusedIndicatorColor = Color.Transparent,

                                            )
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(5.dp))

                                    IconButton(
                                        onClick = {
                                            // Handle settings click
                                        }
                                    ) {
                                        Icon(
                                            Icons.Default.Settings,
                                            contentDescription = "Settings"
                                        )
                                    }
                                }
                            },
                            colors = TopAppBarDefaults.smallTopAppBarColors(
                                containerColor = Color.Transparent,
                                titleContentColor = Color.Black,
                                actionIconContentColor = Color.Black
                            )
                        )
                    },
                    content = { paddingValues ->
                        // Your main content here
                        Box(

                            modifier = Modifier
                                .fillMaxSize()
                                .padding(paddingValues)
                        ) {
                            Text("Home Screen Content", fontSize = 24.sp, modifier = Modifier.align(Alignment.Center))
                        }
                    }
                )
            }
        )
    }
}

@Composable
fun DrawerContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Text("Drawer Item 1", fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Drawer Item 2", fontSize = 18.sp)
        // Add more drawer items as needed
    }
}