package com.example.todofy.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.todofy.data.local.entity.CategoryEntity
import com.example.todofy.ui.theme.Blue
import com.example.todofy.ui.theme.Green
import com.example.todofy.ui.theme.Orange
import com.example.todofy.ui.theme.Red

@Composable
fun CategoryChip(
    category: CategoryEntity,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Gunakan warna langsung berdasarkan nama kategori
    val categoryColor = when (category.name) {
        "Sangat Rendah" -> Green
        "Rendah" -> Blue
        "Tinggi" -> Orange
        "Sangat Tinggi" -> Red
        "Semua" -> MaterialTheme.colorScheme.tertiary // Warna khusus untuk "Semua"
        else -> MaterialTheme.colorScheme.primary
    }

    // Efek animasi untuk selection
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) {
            categoryColor.copy(alpha = 0.15f)
        } else {
            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
        },
        animationSpec = tween(durationMillis = 300)
    )

    val borderWidth by animateDpAsState(
        targetValue = if (isSelected) 2.dp else 0.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    val textColor by animateColorAsState(
        targetValue = if (isSelected) {
            categoryColor
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        },
        animationSpec = tween(durationMillis = 300)
    )

    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(bounded = true, color = categoryColor),
                onClick = onClick
            ),
        shape = RoundedCornerShape(20.dp), // Lebih rounded
        color = backgroundColor,
        tonalElevation = if (isSelected) 2.dp else 0.dp // Efek elevasi untuk chip yang dipilih
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .border(
                    width = borderWidth,
                    color = if (isSelected) categoryColor else Color.Transparent,
                    shape = RoundedCornerShape(20.dp)
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Indikator warna prioritas dengan efek yang lebih menarik
            Box(
                modifier = Modifier
                    .size(18.dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                categoryColor,
                                categoryColor.copy(alpha = 0.8f)
                            )
                        ),
                        shape = if (category.name == "Semua") RoundedCornerShape(4.dp) else CircleShape
                    )
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = category.name,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                ),
                color = textColor
            )
        }
    }
}