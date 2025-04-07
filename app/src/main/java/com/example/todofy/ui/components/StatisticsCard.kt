package com.example.todofy.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material.icons.rounded.Pending
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todofy.ui.theme.Green
import com.example.todofy.ui.theme.Blue
import com.example.todofy.ui.theme.Orange
import com.example.todofy.ui.theme.Red
import kotlinx.coroutines.delay

@Composable
fun StatisticsCard(
    title: String,
    value: String,
    icon: ImageVector,
    iconTint: Color,
    modifier: Modifier = Modifier
) {
    // Animasi angka bertambah
    var animatedValue by remember { mutableStateOf(0f) }
    val numericValue = value.toFloatOrNull() ?: 0f

    LaunchedEffect(numericValue) {
        animatedValue = 0f
        delay(300) // Delay sedikit sebelum mulai animasi
        animatedValue = numericValue
    }

    val animatedFloat by animateFloatAsState(
        targetValue = animatedValue,
        animationSpec = tween(durationMillis = 1000),
        label = "ValueAnimation"
    )

    val displayValue = if (numericValue > 0) {
        animatedFloat.toInt().toString()
    } else {
        "0"
    }

    Card(
        modifier = modifier
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(24.dp),
                spotColor = iconTint.copy(alpha = 0.15f),
                ambientColor = iconTint.copy(alpha = 0.05f)
            ),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier.padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon dengan latar belakang dan efek gradasi
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                iconTint.copy(alpha = 0.15f),
                                iconTint.copy(alpha = 0.05f)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = iconTint,
                    modifier = Modifier.size(30.dp)
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
                    text = displayValue,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.ExtraBold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
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
        icon = Icons.Rounded.CheckCircle,
        iconTint = Green,
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
        icon = Icons.Rounded.Pending,
        iconTint = Blue,
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
        icon = Icons.Rounded.Error,
        iconTint = Red,
        modifier = modifier
    )
}

@Composable
fun StatisticsSummaryRow(
    completedCount: Int,
    pendingCount: Int,
    overdueCount: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
    ) {
        CompletedTasksCard(
            count = completedCount,
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.width(12.dp))

        PendingTasksCard(
            count = pendingCount,
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.width(12.dp))

        OverdueTasksCard(
            count = overdueCount,
            modifier = Modifier.weight(1f)
        )
    }
}

// Komponen visual untuk kategori
@Composable
fun CategoryProgressItem(
    categoryName: String,
    count: Int,
    totalCount: Int,
    color: Color,
    modifier: Modifier = Modifier
) {
    // Hitung persentase
    val percentage = if (totalCount > 0) (count.toFloat() / totalCount) * 100 else 0f

    // Animasi progress
    var progress by remember { mutableStateOf(0f) }
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 1000),
        label = "ProgressAnimation"
    )

    LaunchedEffect(count, totalCount) {
        progress = 0f
        delay(500)
        progress = if (totalCount > 0) count.toFloat() / totalCount else 0f
    }

    Column(
        modifier = modifier.padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Indikator warna
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(color)
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Nama kategori
            Text(
                text = categoryName,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.weight(1f)
            )

            // Jumlah dan persentase
            Text(
                text = "$count (${String.format("%.1f", percentage)}%)",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = color
            )
        }

        // Progress bar dengan animasi
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(animatedProgress)
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(color)
            )
        }
    }
}