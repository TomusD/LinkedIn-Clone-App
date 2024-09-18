package com.example.front.screens.basic_screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.front.activity.HomeViewModel
import com.example.front.screens.Subcomponents.PostCard

@Composable
fun HomeScreen(viewModel: HomeViewModel = viewModel()) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.fetchPosts(context)
    }

    var posts = viewModel.posts.collectAsState().value
    posts = posts.sortedByDescending { it.date_uploaded }


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
            Text("No posts yet. Make connections! 🌐🌍", Modifier.padding(5.dp))
        }

    }
}




