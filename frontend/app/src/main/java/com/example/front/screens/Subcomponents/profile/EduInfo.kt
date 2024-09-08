package com.example.front.screens.Subcomponents.profile

import androidx.compose.foundation.layout.Arrangement
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
import com.example.front.data.response.EducationResponse

@Composable
fun EduInfo(edu: EducationResponse) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
            .clip(RoundedCornerShape(10.dp)),
        shape = MaterialTheme.shapes.medium,
    ) {
        // Organization
        Text(
            text = edu.organization,
            modifier = Modifier.padding(10.dp, 2.dp),
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp

            )
        )

        Spacer(modifier = Modifier.height(8.dp))
        Row (horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth())
        {
            Text(
                text = "Science field: ${edu.science_field}",
                modifier = Modifier.padding(10.dp, 0.dp),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold
                )
            )

            Text(
                text = if (edu.degree==null) "GPA: N/A" else "GPA: ${edu.degree} avg.",
                modifier = Modifier.padding(10.dp, 0.dp),
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Row {
            val dateEnd = if (edu.date_ended.isNullOrEmpty()) "Present" else "${edu.date_ended}"
            Text(
                text = "${edu.date_started.replace("-", "/")} - ${dateEnd.replace("-", "/")}",
                modifier = Modifier.padding(10.dp, 2.dp),
                style = MaterialTheme.typography.bodySmall
            )

        }
    }
}
