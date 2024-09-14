package com.example.front.screens.Subcomponents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.front.R
import kotlinx.coroutines.delay

@Composable
fun AudioPlayerWithProgressBar(audioUrl: String) {
    val context = LocalContext.current
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            val mediaItem = MediaItem.fromUri(audioUrl)
            setMediaItem(mediaItem)
            prepare()
        }
    }

    // State to track playback progress
    var isPlaying by remember { mutableStateOf(false) }
    val playbackPosition = remember { mutableStateOf(0L) }
    val duration = remember { mutableStateOf(0L) }

    // Setup PlayerListener to update playback progress
    LaunchedEffect(exoPlayer) {
        exoPlayer.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                if (state == Player.STATE_READY) {
                    duration.value = exoPlayer.duration
                }
            }

            override fun onIsPlayingChanged(playing: Boolean) {
                isPlaying = playing
            }
        })
    }

    // Continuously update progress bar while audio is playing
    LaunchedEffect(isPlaying) {
        if (isPlaying) {
            while (isPlaying) {
                playbackPosition.value = exoPlayer.currentPosition
                delay(1000L) // Update every second
            }
        }
    }

    Spacer(modifier = Modifier.height(10.dp))
    // UI: Button to Play/Pause audio and Progress bar
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Play/Pause Button
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = {
                    if (isPlaying) {
                        exoPlayer.pause()
                    } else {
                        exoPlayer.play()
                    }
                },
                modifier = Modifier.size(25.dp)
            ) {
//                Text(if (isPlaying) "Pause" else "Play")
                if (isPlaying) {
                    Icon(painter = painterResource(id = R.drawable.pause_icon), contentDescription ="Pause", Modifier.size(15.dp) )
                } else {
                    Icon(painter = painterResource(id = R.drawable.play_icon), contentDescription ="Play", Modifier.size(15.dp) )

                }
            }

            // Progress Bar
            val progress = remember(playbackPosition.value, duration.value) {
                if (duration.value > 0L) {
                    (playbackPosition.value.toFloat() / duration.value.toFloat()).coerceIn(0f, 1f)
                } else {
                    0f
                }
            }
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
                    .height(20.dp)
                    .background(color = Color.Gray, CircleShape),
                color = Color.Black,
                trackColor = Color.Gray
            )
        }

        // Show time passed and total duration
        Text(
            text = "${playbackPosition.value / 1000} s / ${duration.value / 1000} s",
            style = MaterialTheme.typography.bodyMedium
        )
    }

    // Release ExoPlayer when the composable leaves the composition
    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }
}
