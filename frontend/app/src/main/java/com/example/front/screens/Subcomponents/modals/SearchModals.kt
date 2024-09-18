package com.example.front.screens.Subcomponents.modals

import UserCard
import android.util.Log
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.example.front.activity.SearchViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.front.activity.HomeViewModel
import com.example.front.screens.Subcomponents.PostCard

@Composable
fun SearchPostsModal(query: String, viewModel: HomeViewModel = viewModel(), onDismiss: () -> Unit) {

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        Log.d("MYTEST", "FUK")
        viewModel.searchPosts(context, query)
    }
    var posts = viewModel.searchPosts.collectAsState().value
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(8.dp).fillMaxWidth(),
        ) {
            Text(text = "Search Results", fontSize = 5.em)
            // Close Modal Button
            IconButton(
                onClick = onDismiss,
                modifier = Modifier.padding(3.dp)
            ) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
            }
        }
        Surface {
            if (posts.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                ) {
                    itemsIndexed(posts) { index, post ->
                        PostCard(
                            post_id = post.post_id,
                            user = post.user,
                            input_text = post.input_text,
                            image_url = post.image_url,
                            video_url = post.video_url,
                            audio_url = post.audio_url,
                            date_uploaded = post.date_uploaded,
                            comments = post.comments,
                            likes = post.likes,
                            user_liked = post.user_liked,
                            onCommentClicked = viewModel::updateComment,
                            onLikeClicked = viewModel::updateLike,
                            viewModel = viewModel,
                        )
                        Spacer(modifier = Modifier.height(16.dp)) // Space between posts
                    }
                }
            } else {
                Text("No results 🌐🌍", Modifier.padding(5.dp))
            }

        }
    }
}

@Composable
fun SearchUsersModal(
    query: String,
    viewModel: SearchViewModel = viewModel(),
    onDismiss: () -> Unit
) {

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        Log.d("MYTEST", "FUK2222222")
        viewModel.searchUsers(context, query)
    }

    val users = viewModel.users.collectAsState().value

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(600.dp),
        color = MaterialTheme.colorScheme.background,

        ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.padding(8.dp).fillMaxWidth(),
            ) {
                Text(text = "Search Results", fontSize = 5.em)
                // Close Modal Button
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.padding(3.dp)
                ) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }
            if (users.isNotEmpty()) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(users) { user ->
                        UserCard(user)
                    }
                }
            }
        }

    }
}
