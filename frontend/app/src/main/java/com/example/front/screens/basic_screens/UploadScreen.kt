package com.example.front.screens.basic_screens

import android.graphics.Bitmap
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.front.activity.PostViewModel
import com.example.front.data.request.PostCreate
import com.example.front.screens.pre_auth_screens.LoadingIndicator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.FileNotFoundException

@Composable
fun UploadScreen(viewModel: PostViewModel = viewModel()) {

    val isLoading by viewModel.isLoading.observeAsState(false)


    // State variables
    var postText by remember { mutableStateOf(TextFieldValue("")) }
    var selectedImage by remember { mutableStateOf<Uri?>(null) }
    var selectedVideo by remember { mutableStateOf<Uri?>(null) }
    var selectedAudio by remember { mutableStateOf<Uri?>(null) }
    var videoThumbnail by remember { mutableStateOf<Bitmap?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // Image picker
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri -> if (uri != null) selectedImage = uri }
    )

    // Video picker
    val videoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            if (uri != null) {
                selectedVideo = uri
                // Generate thumbnail when video is selected
                coroutineScope.launch {
                    generateVideoThumbnail(context, uri) { thumbnail ->
                        videoThumbnail = thumbnail
                    }
                }
            }
        }
    )

    // Audio picker
    val audioPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri -> if (uri != null) selectedAudio = uri }
    )

    Spacer(modifier = Modifier.height(30.dp))

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Make a Post",
                fontSize = 5.em,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(5.dp))

            // Post TextField
            OutlinedTextField(
                value = postText,
                onValueChange = { postText = it },
                placeholder = { Text(text = "What are you thinking?") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                isError = postText.text.isEmpty() && errorMessage != null,
            )

            Spacer(modifier = Modifier.height(8.dp))

                // Row of media buttons equally spaced
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Image Button
                    Button(
                        shape = RoundedCornerShape(5.dp),
                        onClick = {
                            imagePickerLauncher.launch("image/*")
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                    ) {
                        Icon(Icons.Default.AccountBox, contentDescription = "Add Image")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Video Button
                    Button(
                        shape = RoundedCornerShape(5.dp),
                        onClick = {
                            videoPickerLauncher.launch("video/*")
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = "Add Video")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Audio Button
                    Button(
                        shape = RoundedCornerShape(5.dp),
                        onClick = {
                            audioPickerLauncher.launch("audio/*")
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add Audio")
                    }
                }


            // Display uploaded media with delete option
            Column(modifier = Modifier.padding(top = 8.dp)) {
                // Display selected image with a delete button
                if (selectedImage != null) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { selectedImage = null },) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete Image", tint = Color.Red)
                        }
                        Text(text = "Image: ${selectedImage?.lastPathSegment}", color = Color.Blue)
                        Spacer(modifier = Modifier.width(8.dp))

                        Image(
                            painter = rememberAsyncImagePainter(selectedImage),
                            contentDescription = "Selected Image",
                            modifier = Modifier.size(50.dp),
                            contentScale = ContentScale.Crop
                        )
                    }
                }


                Spacer(modifier = Modifier.height(8.dp))

                // Display selected video thumbnail with a delete button
                if (selectedVideo != null && videoThumbnail != null) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { selectedVideo = null }) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Delete Video",
                                tint = Color.Red
                            )
                        }
                        Text(text = "Video: ${selectedVideo?.lastPathSegment}", color = Color.Blue)
                        Spacer(modifier = Modifier.width(8.dp))
                        Image(
                                bitmap = videoThumbnail!!.asImageBitmap(),
                                contentDescription = "Video Thumbnail",
                                modifier = Modifier
                                    .size(50.dp),
                                contentScale = ContentScale.Crop
                            )
                        Spacer(modifier = Modifier.width(8.dp))


                    }
                }


                // Display selected audio name with a delete button
                if (selectedAudio != null) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { selectedAudio = null }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete Audio", tint = Color.Red)
                        }
                        Text(text = "Audio: ${selectedAudio?.lastPathSegment}", color = Color.Blue)
                    }
                }

            }

//            Spacer(modifier = Modifier.height(16.dp))

            // Submit button
            Button(
                shape = RoundedCornerShape(10.dp),
                onClick = {
                    if (postText.text.isEmpty()) {
                        errorMessage = "Text cannot be empty"
                    } else {
                        // Handle submission
                        errorMessage = null
                        Log.d("MYTEST", "select ==== " + selectedVideo.toString())
                        Log.d("MYTEST", "select ==== " + selectedAudio.toString())
                        val post = PostCreate(postText.text, selectedImage, selectedVideo, selectedAudio)
                        viewModel.createPost(context, post)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(17, 138, 178))
            ) {
                Text(text = "Post")
            }

            // Error message
            if (errorMessage != null) {
                Text(text = errorMessage!!, color = Color.Red)
            }
        }


        LoadingIndicator(viewModel.isLoading)
    }

}

suspend fun generateVideoThumbnail(context: android.content.Context, uri: Uri, onThumbnailReady: (Bitmap?) -> Unit) {
    withContext(Dispatchers.IO) {
        try {
            val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                context.contentResolver.loadThumbnail(uri, android.util.Size(640, 480), null)
            } else {
                @Suppress("DEPRECATION")
                ThumbnailUtils.createVideoThumbnail(
                    uri.path ?: "",
                    MediaStore.Video.Thumbnails.MINI_KIND
                )
            }
            withContext(Dispatchers.Main) {
                onThumbnailReady(bitmap)
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            withContext(Dispatchers.Main) {
                onThumbnailReady(null)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPostFormWithMediaPreview() {
    UploadScreen()
}
