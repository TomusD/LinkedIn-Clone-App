package com.example.front

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.front.ui.theme.FrontEndTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FrontEndTheme {
                HomeScreen()
            }
        }
    }

    @Composable
    fun HomeScreen() {

        // List of home screens articles
        var articles by remember {
            mutableStateOf(
                listOf(
                    "Article 1",
                    "Article 2",
                    "Article 3",
                    "Article 4",
                    "Article 5",
                    "Article 6",
                    "Article 7"
                )
            )
        }

        // Add Comment and Search Text Field Variables
        var addComment by remember { mutableStateOf("") }
        var search by remember { mutableStateOf("") }
        // Immutable variables for drawer
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val coroutineScope = rememberCoroutineScope()

        // Drawer state control and Side Menu
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {

                ModalDrawerSheet {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.Start
                    ) {

                        Text(
                            text = "Navigation",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        // Go to Profile Button
                        Button(onClick = {
                            val intent = Intent(this@HomeActivity, ProfileActivity::class.java)
                            startActivity(intent)
                            coroutineScope.launch { drawerState.close() }
                        }) {
                            Image(
                                painter = rememberAsyncImagePainter(
                                    model = R.drawable.profile_icon
                                ),
                                contentDescription = "Profile picture",
                                modifier = Modifier.size(35.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Go to Settings Button
                        Button(onClick = {
                            val intent = Intent(this@HomeActivity, SettingsActivity::class.java)
                            startActivity(intent)
                            coroutineScope.launch { drawerState.close() }
                        }) {
                            Image(
                                painter = rememberAsyncImagePainter(
                                    model = R.drawable.settings_icon
                                ),
                                contentDescription = "Settings picture",
                                modifier = Modifier.size(35.dp)
                            )
                        }
                    }
                }
            },
            content = {
                MainContent(drawerState, coroutineScope, articles, addComment, search,
                    onSearchChange = { search = it }, onCommentChange = { addComment = it })
            }
        )
    }

    // The Main Content of the Home Screen
    @Composable
    fun MainContent(
        drawerState: DrawerState,
        coroutineScope: CoroutineScope,
        articles: List<String>,
        addComment: String,
        search: String,
        onSearchChange: (String) -> Unit,
        onCommentChange: (String) -> Unit
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Text("HOME", style = MaterialTheme.typography.bodyLarge)

            Spacer(modifier = Modifier.height(8.dp))

            // Profile Image that opens the Drawer
            Image(
                painter = rememberAsyncImagePainter(
                    model = R.drawable.profile_placeholder
                ),
                contentDescription = "Profile Photo",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(75.dp)
                    .clip(CircleShape)
                    .clickable {
                        coroutineScope.launch { drawerState.open() }
                    }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Article search Text Field and Search/Chat Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                //Search Text Field
                OutlinedTextField(
                    value = search,
                    onValueChange = onSearchChange,
                    label = { Text("Search") },
                )

                Button(
                    onClick = { /* Handle Search click */ },
                    modifier = Modifier.size(60.dp).align(Alignment.CenterVertically)
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(
                            model = R.drawable.search_icon
                        ),
                        contentDescription = "Search picture",
                        modifier = Modifier.size(35.dp)
                    )
                }

                //Chat Button
                Button(
                    onClick = {
                        val intent = Intent(this@HomeActivity, ChatActivity::class.java)
                        startActivity(intent)
                    },
                    modifier = Modifier.size(60.dp).align(Alignment.CenterVertically)
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(
                            model = R.drawable.chat_icon
                        ),
                        contentDescription = "Chat picture",
                        modifier = Modifier.size(35.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            //Article cards
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(articles) { article ->
                    ArticleItem(article)

                }
            }

            //Nav Bar possible gonna be changed
            BottomAppBar {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {

                    //Home Button
                    Button(
                        onClick = {
                            val intent = Intent(this@HomeActivity, HomeActivity::class.java)
                            startActivity(intent)
                        },
                        modifier = Modifier.size(60.dp)
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = R.drawable.home_icon
                            ),
                            contentDescription = "Home picture",
                            modifier = Modifier.size(35.dp)
                        )
                    }

                    //Post Button
                    Button(
                        onClick = {
                            val intent = Intent(this@HomeActivity, PostActivity::class.java)
                            startActivity(intent)
                        },
                        modifier = Modifier.size(60.dp)
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = R.drawable.post_icon
                            ),
                            contentDescription = "Post picture",
                            modifier = Modifier.size(35.dp)
                        )
                    }

                    //Network Button
                    Button(
                        onClick = {
                            val intent = Intent(this@HomeActivity, NetworkActivity::class.java)
                            startActivity(intent)
                        },
                        modifier = Modifier.size(60.dp)
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = R.drawable.networking_icon
                            ),
                            contentDescription = "Network picture",
                            modifier = Modifier.size(35.dp)
                        )
                    }

                    //Advertisement Button
                    Button(
                        onClick = {
                            val intent = Intent(this@HomeActivity, AdvertisementsActivity::class.java)
                            startActivity(intent)
                        },
                        modifier = Modifier.size(60.dp)
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = R.drawable.advertisement_icon
                            ),
                            contentDescription = "Advertisements picture",
                            modifier = Modifier.size(35.dp)
                        )
                    }

                    //Notifications Button
                    Button(
                        onClick = {
                            val intent = Intent(this@HomeActivity, NotificationsActivity::class.java)
                            startActivity(intent)
                        },
                        modifier = Modifier.size(60.dp)
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = R.drawable.notification_icon
                            ),
                            contentDescription = "Notifications picture",
                            modifier = Modifier.size(35.dp)
                        )
                    }
                }
            }
        }
    }

    // Articles cards
    @Composable
    fun ArticleItem(article: String) {
        var comments by remember { mutableStateOf(listOf("Comment 1", "Comment 2", "Comment 3", "Comment 4")) }
        var addComment by remember { mutableStateOf("") }

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(article, style = MaterialTheme.typography.bodyLarge)

                Spacer(modifier = Modifier.height(8.dp))

                Box(modifier = Modifier.height(150.dp)) {

                    // Comments List
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        items(comments) { comment ->
                            CommentItem(comment)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(onClick = { /*Interested click Button*/ }) {
                    Image(
                        painter = rememberAsyncImagePainter(
                            model = R.drawable.like_icon
                        ),
                        contentDescription = "Like picture",
                        modifier = Modifier.size(30.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                //Add Comment Text Field and Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    OutlinedTextField(
                        value = addComment,
                        onValueChange = { addComment = it },
                        modifier = Modifier.weight(1f).background(Color.White),
                        label = { Text("Add Comment") }
                    )
                    Button(onClick = {
                        if (addComment != "") {
                            comments = comments + addComment
                            addComment = ""
                        }
                    }, modifier = Modifier.align(Alignment.CenterVertically)) {
                        Text("Comment")
                    }
                }
            }
        }
    }

    // Comments List
    @Composable
    fun CommentItem(comment: String) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(comment, style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
