package com.example.front.screens.Subcomponents

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.front.FriendProfileActivity
import com.example.front.activity.formatDateTime
import com.example.front.data.base.Notification
import com.example.front.data.base.User


@Composable
fun FriendRequestCard(user: User, onAnswer: (uid: Int, answer: Boolean) -> Unit) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .width(300.dp)
            .padding(10.dp),
    ) {

        Column(
            modifier = Modifier
                .padding(15.dp)
                .fillMaxWidth()
        ) {

            Row(horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 10.dp)) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(user.imagePath)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(45.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = "${user.name} ${user.surname}",
                    fontSize = 5.em,
                    fontWeight = FontWeight.Bold
                )
            }

            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                IconButton(
                    colors = IconButtonDefaults.iconButtonColors(containerColor = Color.White),
                    modifier = Modifier.background(Color.White, RoundedCornerShape(10.dp)),
                    onClick = {
                    onAnswer(user.id.toInt(), true)
                },
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Accept",
                        tint = Color.Green
                    )
                }

                IconButton(
                    colors = IconButtonDefaults.iconButtonColors(containerColor = Color.White),
                    modifier = Modifier.background(Color.White, RoundedCornerShape(10.dp)),
                    onClick = {
                        onAnswer(user.id.toInt(), false)
                              })
                {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Decline",
                        tint = Color.Red
                    )
                }

                Button(
                    onClick = {
                        val intent = Intent(context, FriendProfileActivity::class.java)
                        intent.putExtra("user", user)
                        context.startActivity(intent)
                    },
                    shape = RoundedCornerShape(4.dp),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.White,
                        containerColor = Color(17, 138, 178)),
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    Text(text = "View\nProfile", textAlign = TextAlign.Center, fontWeight = FontWeight(300))
                }

            }


        }
    }
}


@Composable
fun NotificationCard(notification: Notification) {
    val msg = if (notification.notification_type == "like_post") "liked" else "commented on"


    Box(
        modifier = Modifier
            .fillMaxHeight()
            .width(350.dp)
            .padding(10.dp),
        content = {

        Row(horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(notification.notifier.image_url)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(10.dp))

            Column {
                Text(
                    text = "${notification.notifier.user_fullname.split(" ")[0]} $msg your post!",
                    textAlign = TextAlign.Start,
                    fontWeight = FontWeight(400)
                )
                Text(color = Color.Gray, text = formatDateTime(notification.date_created.replace(regex = Regex("\\.\\d*"), replacement = "")))
            }
            HorizontalDivider(modifier = Modifier
                .width(10.dp)
                .padding(5.dp), color = Color.DarkGray)

        }
    })
}
