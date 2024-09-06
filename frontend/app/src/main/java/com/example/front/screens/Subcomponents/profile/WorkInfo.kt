package com.example.front.screens.Subcomponents.profile

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.front.data.response.WorkResponse

@Composable
fun WorkInfo(work: WorkResponse) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
            .clip(RoundedCornerShape(10.dp)),
        shape = MaterialTheme.shapes.medium,
    ) {
        // Organization
        Text(
            text = work.organization,
            modifier = Modifier.padding(10.dp, 2.dp),
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp

            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Role
        Text(
            text = work.role,
            modifier = Modifier.padding(10.dp, 0.dp),
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.SemiBold
            )
        )

        Spacer(modifier = Modifier.height(4.dp))

        Row {
            val dateEnd = if (work.date_ended.isNullOrEmpty()) "Present" else "${work.date_ended}"
            Text(
                text = "${work.date_started.replace("-", "/")} - ${dateEnd.replace("-", "/")}",
                modifier = Modifier.padding(10.dp, 2.dp),
                style = MaterialTheme.typography.bodySmall
            )

        }
    }
}
