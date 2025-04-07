package com.example.todofy.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Pending
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun StatisticsCard(
    title: String,
    value: String,
    icon: ImageVector,
    iconTint: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp), // Corners lebih rounded
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp), // Elevation ditingkatkan
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp) // Ukuran box lebih besar
                    .clip(CircleShape)
                    .background(iconTint.copy(alpha = 0.15f)), // Background dengan opacity
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(28.dp) // Ukuran icon sedikit lebih besar
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = value,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold // Font weight yang lebih bold
                    )
                )
            }
        }
    }
}

@Composable
fun CompletedTasksCard(
    count: Int,
    modifier: Modifier = Modifier
) {
    StatisticsCard(
        title = "Tugas Selesai",
        value = count.toString(),
        icon = Icons.Default.CheckCircle,
        iconTint = MaterialTheme.colorScheme.primary,
        modifier = modifier
    )
}

@Composable
fun PendingTasksCard(
    count: Int,
    modifier: Modifier = Modifier
) {
    StatisticsCard(
        title = "Tugas Tertunda",
        value = count.toString(),
        icon = Icons.Default.Pending,
        iconTint = MaterialTheme.colorScheme.secondary,
        modifier = modifier
    )
}

@Composable
fun OverdueTasksCard(
    count: Int,
    modifier: Modifier = Modifier
) {
    StatisticsCard(
        title = "Tugas Terlambat",
        value = count.toString(),
        icon = Icons.Default.Error,
        iconTint = MaterialTheme.colorScheme.error,
        modifier = modifier
    )
}