package com.example.todofy.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.todofy.data.local.entity.CategoryEntity
import com.example.todofy.utils.Converters

@Composable
fun CategoryChip(
    category: CategoryEntity,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val converters = Converters()
    val categoryColor = converters.toColor(category.colorHex)

    Surface(
        modifier = modifier
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        color = if (isSelected) categoryColor.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surface,
        tonalElevation = if (isSelected) 0.dp else 4.dp,
        shadowElevation = if (isSelected) 0.dp else 2.dp,
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 6.dp)
                .border(
                    width = 1.dp,
                    color = if (isSelected) categoryColor else Color.Transparent,
                    shape = RoundedCornerShape(16.dp)
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(categoryColor)
            )

            Spacer(modifier = Modifier.width(6.dp))

            Text(
                text = category.name,
                style = MaterialTheme.typography.labelMedium,
                color = if (isSelected) categoryColor else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}