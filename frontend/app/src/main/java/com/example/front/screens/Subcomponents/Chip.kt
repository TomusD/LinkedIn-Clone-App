package com.example.front.screens.Subcomponents

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em


@Composable
fun Chip(text: String, onDelete: () -> Unit, jobSkill: Boolean = false) {
    if (!jobSkill) {
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = Color(255, 209, 102),
            modifier = Modifier.padding(4.dp).height(50.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = text,
                    color = Color.Black,
                    modifier = Modifier.padding(end = 8.dp)
                )
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Remove skill",
                        tint = Color.Black
                    )
                }
            }
        }
    } else {
        Surface(
            shape = RoundedCornerShape(5.dp),
            color = Color(7, 59, 76),
            modifier = Modifier.padding(2.dp).height(30.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 5.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = text,
                    color = Color.White,
                    fontSize = 2.2.em,
                    modifier = Modifier.padding(end = 2.dp)

                )
            }
        }
    }
}