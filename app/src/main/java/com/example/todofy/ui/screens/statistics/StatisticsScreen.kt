package com.example.todofy.ui.screens.statistics

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material.icons.rounded.Pending
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.todofy.ui.theme.Green
import com.example.todofy.ui.theme.Blue
import com.example.todofy.ui.theme.Orange
import com.example.todofy.ui.theme.Red
import com.example.todofy.utils.Converters

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    onNavigateBack: () -> Unit,
    viewModel: StatisticsViewModel = hiltViewModel()
) {
    val statisticsState by viewModel.statisticsState.collectAsStateWithLifecycle()
    val converters = Converters()

    // Animasi untuk tampilan
    var isVisible by remember { mutableStateOf(false) }
    LaunchedEffect(key1 = true) {
        isVisible = true
    }

    // Background gradient
    val gradientBackground = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.background,
            MaterialTheme.colorScheme.background,
            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Statistik",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = "Kembali",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradientBackground)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                // Progress Lingkaran untuk persentase penyelesaian tugas dengan animasi
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(initialAlpha = 0f) +
                            slideInVertically(
                                initialOffsetY = { -100 },
                                animationSpec = tween(durationMillis = 500)
                            )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicatorWithText(
                            progress = statisticsState.completionRate,
                            text = "${(statisticsState.completionRate * 100).toInt()}%",
                            size = 220.dp,
                            strokeWidth = 24.dp,
                            color = MaterialTheme.colorScheme.primary,
                            backgroundColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                        )
                    }
                }

                // Card Informasi Total Tugas dengan styling modern
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(
                        initialAlpha = 0f,
                        animationSpec = tween(durationMillis = 500, delayMillis = 300)
                    ) +
                            slideInVertically(
                                initialOffsetY = { 100 },
                                animationSpec = tween(durationMillis = 500, delayMillis = 300)
                            )
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .shadow(
                                elevation = 8.dp,
                                shape = RoundedCornerShape(24.dp),
                                spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                            ),
                        shape = RoundedCornerShape(24.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp)
                        ) {
                            Text(
                                text = "Ringkasan Tugas",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            // Statistik card lebih visual
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                StatItem(
                                    count = statisticsState.completedTodos,
                                    label = "Selesai",
                                    icon = Icons.Rounded.CheckCircle,
                                    color = Green
                                )

                                StatItem(
                                    count = statisticsState.pendingTodos,
                                    label = "Tertunda",
                                    icon = Icons.Rounded.Pending,
                                    color = Blue
                                )

                                StatItem(
                                    count = statisticsState.overdueTodos,
                                    label = "Terlambat",
                                    icon = Icons.Rounded.Error,
                                    color = Red
                                )
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            // Card dengan info jumlah total tugas
                            Surface(
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                                shape = RoundedCornerShape(20.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Total Tugas",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Medium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )

                                    Surface(
                                        color = MaterialTheme.colorScheme.primaryContainer,
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Text(
                                            text = statisticsState.totalTodos.toString(),
                                            style = MaterialTheme.typography.headlineSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Distribusi Kategori dengan styling modern
                if (statisticsState.categoriesWithTodoCount.isNotEmpty()) {
                    AnimatedVisibility(
                        visible = isVisible,
                        enter = fadeIn(
                            initialAlpha = 0f,
                            animationSpec = tween(durationMillis = 500, delayMillis = 600)
                        ) +
                                slideInVertically(
                                    initialOffsetY = { 100 },
                                    animationSpec = tween(durationMillis = 500, delayMillis = 600)
                                )
                    ) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .shadow(
                                    elevation = 8.dp,
                                    shape = RoundedCornerShape(24.dp),
                                    spotColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f)
                                ),
                            shape = RoundedCornerShape(24.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(20.dp)
                            ) {
                                Text(
                                    text = "Prioritas Tugas",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )

                                Spacer(modifier = Modifier.height(20.dp))

                                statisticsState.categoriesWithTodoCount.forEach { categoryWithCount ->
                                    // Tentukan warna berdasarkan nama kategori prioritas
                                    val priorityColor = when (categoryWithCount.category.name) {
                                        "Sangat Rendah" -> Green
                                        "Rendah" -> Blue
                                        "Tinggi" -> Orange
                                        "Sangat Tinggi" -> Red
                                        else -> converters.toColor(categoryWithCount.category.colorHex)
                                    }

                                    val totalCount = if (statisticsState.totalTodos > 0)
                                        statisticsState.totalTodos else 1
                                    val percentage = (categoryWithCount.todoCount.toFloat() / totalCount) * 100

                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(16.dp)
                                                        .clip(CircleShape)
                                                        .background(priorityColor)
                                                )

                                                Spacer(modifier = Modifier.width(12.dp))

                                                Text(
                                                    text = categoryWithCount.category.name,
                                                    style = MaterialTheme.typography.bodyLarge,
                                                    fontWeight = FontWeight.Medium,
                                                    color = MaterialTheme.colorScheme.onSurface
                                                )
                                            }

                                            Row(
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = categoryWithCount.todoCount.toString(),
                                                    style = MaterialTheme.typography.bodyLarge,
                                                    fontWeight = FontWeight.Bold,
                                                    color = MaterialTheme.colorScheme.onSurface
                                                )

                                                Spacer(modifier = Modifier.width(8.dp))

                                                Text(
                                                    text = "(${String.format("%.1f", percentage)}%)",
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(8.dp))

                                        // Progress bar dengan warna prioritas yang benar
                                        LinearProgressIndicator(
                                            progress = categoryWithCount.todoCount.toFloat() / totalCount,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(8.dp)
                                                .clip(RoundedCornerShape(4.dp)),
                                            color = priorityColor,
                                            trackColor = MaterialTheme.colorScheme.surfaceVariant
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Spacer bawah
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun CircularProgressIndicatorWithText(
    progress: Float,
    text: String,
    size: androidx.compose.ui.unit.Dp,
    strokeWidth: androidx.compose.ui.unit.Dp,
    color: Color,
    backgroundColor: Color
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(size)
    ) {
        Canvas(
            modifier = Modifier.size(size)
        ) {
            // Background circle
            drawCircle(
                color = backgroundColor,
                radius = size.toPx() / 2 - strokeWidth.toPx() / 2,
                style = Stroke(width = strokeWidth.toPx())
            )

            // Progress arc dengan gradient
            val sweepAngle = progress * 360f
            val brush = Brush.linearGradient(
                colors = listOf(
                    color,
                    color.copy(alpha = 0.8f)
                ),
                start = Offset(0f, 0f),
                end = Offset(size.toPx(), size.toPx())
            )

            drawArc(
                brush = brush,
                startAngle = -90f,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
            )
        }

        // Text container dengan efek
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = text,
                fontSize = 36.sp,
                fontWeight = FontWeight.ExtraBold,
                color = color
            )

            Text(
                text = "Penyelesaian",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
fun StatItem(
    count: Int,
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Icon dengan background
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(
                    color.copy(alpha = 0.15f)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(26.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Jumlah dengan style menarik
        Text(
            text = count.toString(),
            style = MaterialTheme.typography.headlineMedium,
            color = color,
            fontWeight = FontWeight.ExtraBold
        )

        // Label
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}