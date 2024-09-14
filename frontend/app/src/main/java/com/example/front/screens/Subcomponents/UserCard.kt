import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
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
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.front.FriendProfileActivity
import com.example.front.data.base.User

@Composable
fun UserCard(user: User) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = MaterialTheme.shapes.medium,
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Row(){
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(user.imagePath)  // Replace with your image URL
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                )

                Spacer(modifier = Modifier.width(10.dp))

                Column(
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text =
                        (user.name.lowercase().replaceFirstChar(Char::titlecase))
                            .plus(" ")
                            .plus(
                                user.surname.lowercase().replaceFirstChar(Char::titlecase)
                            ),  // Replace with your user's name
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = user.email,  // Replace with your user's email
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
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
