package com.example.front.screens.Subcomponents

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.front.activity.HomeViewModel
import com.example.front.activity.formatDateTime
import com.example.front.data.SessionManager
import com.example.front.data.base.UserLittleDetail
import com.example.front.data.request.CommentCreate
import com.example.front.data.response.Comments
import java.time.LocalDateTime


@Composable
fun PostCard(
    post_id: Int,
    user: UserLittleDetail,
    input_text: String,
    image_url: String?,
    video_url: String?,
    audio_url: String?,
    date_uploaded: String,
    likes: Int,
    comments: List<Comments>,
    user_liked: Boolean,
    onLikeClicked: (Int) -> Unit,
    onCommentClicked: (Comments, Int) -> Unit,
    viewModel: HomeViewModel,
) {
    val context = LocalContext.current
    fun sm(info: String):  String? = SessionManager(context).getUserInfo(info)
    var showComments by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically){
                AsyncImage(
                    model = user.image_url,
                    contentDescription = "Image",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop,

                )
                Spacer(modifier = Modifier.width(8.dp))
                // Username
                Column {
                    Text(text = user.user_fullname)
                    Text(text = formatDateTime(date_uploaded), color = Color.Gray)
                }
            }

            HorizontalDivider(
                Modifier
                    .height(10.dp)
                    .padding(vertical = 5.dp))
            // Post Text
            Spacer(modifier = Modifier.height(5.dp))
            Text(text = input_text)

            // Image and Video
            Spacer(modifier = Modifier.height(8.dp))

            if (image_url != null) {
                AsyncImage(model = image_url, contentDescription = null, contentScale = ContentScale.Fit, modifier = Modifier.size(150.dp))
            }

            if (video_url != null) {
                VideoPlayerWithControls(video_url)
            }

            // Audio
            if (audio_url != null) {
                AudioPlayerWithProgressBar(audio_url)
            }

            // Like and Comment Buttons
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
            ) {
                Row (verticalAlignment = Alignment.CenterVertically)
                {
                    if (user_liked) {
                        Button(onClick = {
                            onLikeClicked(post_id)
                            viewModel.likePost(context, post_id)
                            Log.d("MYTEST", "Liked ${post_id}")
                        }
                                         ,
                            shape = RoundedCornerShape(5.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(17, 138, 178)
                            )
                        ) {
                            Text(text = "Like")
                        }
                    } else {
                        OutlinedButton(onClick = {
                            onLikeClicked(post_id)
                            viewModel.likePost(context, post_id)
                            Log.d("MYTEST", "Liked ${post_id}")
                        },
                            shape = RoundedCornerShape(5.dp),
                            border = BorderStroke(1.dp, Color(17, 138, 178)),

                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent,
                                contentColor = Color(17, 138, 178)
                            )) {
                            Text(text = "Like")
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = "$likes likes", color = Color.Black)
                    }

                Button(onClick = { showComments = true },
                    shape = RoundedCornerShape(5.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(17, 59, 76)
                    )
                ) {
                    Text(text = "Comments")
                }
            }
        }


        if (showComments) {
            CommentModal(
                post_id = post_id,
                postText = input_text,
                comments = comments,
                onDismiss = { showComments = false },
                onSendComment = {
                    val comment = Comments(
                        user_id = sm(SessionManager.USER_ID)!!.toInt(),
                        user_fullname = "${sm(SessionManager.USER_NAME)} ${sm(SessionManager.USER_SURNAME)}",
                        user_image_url = sm(SessionManager.USER_IMAGE_URL).toString(),
                        comment_text = it,
                        date_commented = LocalDateTime.now().toString()
                    )
                    onCommentClicked(comment, post_id)
                },
                viewModel
            )
        }
    }
}


@Composable
fun CommentModal(
    post_id: Int,
    postText: String,
    comments: List<Comments>,
    onDismiss: () -> Unit,
    onSendComment: (String) -> Unit,
    viewModel: HomeViewModel
) {

    val context = LocalContext.current
    var commentText by remember { mutableStateOf(TextFieldValue("")) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

//    comments.sortedBy { it.date_commented }
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f)
        ) {
            Column {
                // Close Modal Button
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(3.dp)
                ) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }

                // Scrollable Comments
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp)
                ) {
                    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                        if (comments.isNotEmpty()) {
                            comments.forEach { comment ->
                                Spacer(modifier = Modifier.height(10.dp))
                                Column(
                                    modifier = Modifier
                                        .background(Color.LightGray, RoundedCornerShape(8.dp))
                                        .padding(5.dp)
                                ) {
                                    Text(
                                        text = comment.user_fullname,
                                        modifier = Modifier.padding(2.dp)
                                    )
                                    HorizontalDivider(
                                        modifier = Modifier.padding(2.dp),
                                        color = Color.Black
                                    )
                                    Text(
                                        text = comment.comment_text,
                                        modifier = Modifier.padding(2.dp)
                                    )
                                    Text(
                                        text = formatDateTime(comment.date_commented.replace(regex = Regex("\\.\\d*"), replacement = "")),
                                        modifier = Modifier.padding(2.dp),
                                        color = Color.Gray
                                    )
                                }
                            }
                        } else {
                            Text(text = "Post the first comment!", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
                        }
                    }
                }

                if (!errorMessage.isNullOrEmpty()) {
                    Text(text = errorMessage!!, color = Color.Red, fontSize = 14.sp,
                        modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
                }
                // Comment Input Field
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BasicTextField(
                        value = commentText,
                        onValueChange = { commentText = it },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .background(Color.LightGray, RoundedCornerShape(8.dp))
                            .padding(8.dp)
                    )
                    IconButton(onClick = {
                        if (commentText.text == "") {
                            errorMessage = "Comment can't be empty"
                        } else {

                            Log.d("MYTEST", "COMMENTING")
                            onSendComment(commentText.text)
                            viewModel.postComment(context, post_id, CommentCreate(commentText.text))
                            commentText = TextFieldValue("")
                            errorMessage = ""

                        }
                    }) {
                        Icon(imageVector = Icons.Default.Send, contentDescription = "Send Comment")
                    }
                }

            }
        }
    }
}

@Composable
fun AudioPlayer(audioUrl: String) {
    // Basic representation of an audio player
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(Color.LightGray),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "Audio Playing...", modifier = Modifier.padding(start = 8.dp))
        Row {
            IconButton(onClick = { /* Handle play */ }) {
                Icon(Icons.Default.PlayArrow, contentDescription = "Play")
            }
            IconButton(onClick = { /* Handle pause */ }) {
                Icon(Icons.Default.Close, contentDescription = "Pause")
            }
        }
    }
}