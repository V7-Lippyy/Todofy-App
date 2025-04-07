package com.example.todofy.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Event
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissState
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todofy.data.local.entity.TodoEntity
import com.example.todofy.ui.theme.Blue
import com.example.todofy.ui.theme.Green
import com.example.todofy.ui.theme.Orange
import com.example.todofy.ui.theme.Red
import com.example.todofy.utils.calculateRemainingTime
import com.example.todofy.utils.formatToDisplayDate
import com.example.todofy.utils.formatToDisplayDateTime
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoItem(
    todo: TodoEntity,
    categoryColor: Color,
    categoryName: String,
    onCheckedChange: (TodoEntity) -> Unit,
    onEditClick: (TodoEntity) -> Unit,
    onDeleteClick: (TodoEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    // Gradient color untuk latar belakang
    val gradientColor = categoryColor.copy(alpha = 0.05f)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(16.dp), // Corner radius ditingkatkan
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (todo.isCompleted) 1.dp else 3.dp // Elevation berbeda untuk todo yang selesai
        ),
        colors = CardDefaults.cardColors(
            containerColor = if (todo.isCompleted)
                MaterialTheme.colorScheme.surface.copy(alpha = 0.8f) // Sedikit transparan untuk todo yang selesai
            else
                MaterialTheme.colorScheme.surface
        ),
        onClick = { onEditClick(todo) }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(gradientColor, Color.Transparent),
                        startX = 0f,
                        endX = 300f
                    )
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, top = 14.dp, end = 14.dp, bottom = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Indikator prioritas dengan bentuk yang lebih variatif
                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(categoryColor.copy(alpha = 0.2f)), // Lighter background
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .clip(CircleShape)
                            .background(categoryColor)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Isi Todo
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = todo.title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold, // Lebih bold
                            letterSpacing = (-0.2).sp // Letter spacing yang lebih ketat
                        ),
                        textDecoration = if (todo.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                        color = if (todo.isCompleted)
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        else
                            MaterialTheme.colorScheme.onSurface
                    )

                    if (todo.description.isNotBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = todo.description,
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = if (todo.isCompleted) 0.4f else 0.7f),
                            textDecoration = if (todo.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                        )
                    }

                    // Informasi waktu dalam container khusus jika ada deadline
                    todo.dueDate?.let { dueDate ->
                        Spacer(modifier = Modifier.height(8.dp))

                        val isTerlambat = dueDate.before(Date()) && !todo.isCompleted
                        val displayDate = if (todo.hasTime) dueDate.formatToDisplayDateTime() else dueDate.formatToDisplayDate()

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = if (isTerlambat)
                                MaterialTheme.colorScheme.errorContainer
                            else
                                MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.7f),
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (isTerlambat) Icons.Default.Error else Icons.Filled.Event,
                                    contentDescription = null,
                                    tint = if (isTerlambat)
                                        MaterialTheme.colorScheme.error
                                    else
                                        MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier.size(14.dp)
                                )

                                Spacer(modifier = Modifier.width(4.dp))

                                Text(
                                    text = displayDate,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (isTerlambat)
                                        MaterialTheme.colorScheme.error
                                    else
                                        MaterialTheme.colorScheme.secondary
                                )
                            }
                        }
                    }
                }

                // Checkbox dengan style yang lebih modern
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(
                            if (todo.isCompleted) categoryColor.copy(alpha = 0.9f)
                            else MaterialTheme.colorScheme.surface
                        )
                        .border(
                            width = 1.5.dp,
                            color = if (todo.isCompleted) categoryColor else categoryColor.copy(alpha = 0.5f),
                            shape = CircleShape
                        )
                        .clickable { onCheckedChange(todo) },
                    contentAlignment = Alignment.Center
                ) {
                    if (todo.isCompleted) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Completed",
                            tint = MaterialTheme.colorScheme.surface,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeableTodoItem(
    todo: TodoEntity,
    categoryColor: Color,
    categoryName: String,
    onCheckedChange: (TodoEntity) -> Unit,
    onEditClick: (TodoEntity) -> Unit,
    onDeleteClick: (TodoEntity) -> Unit,
    dismissState: DismissState
) {
    SwipeToDismiss(
        state = dismissState,
        modifier = Modifier.padding(vertical = 4.dp),
        directions = setOf(DismissDirection.EndToStart, DismissDirection.StartToEnd),
        background = {
            val direction = dismissState.dismissDirection ?: return@SwipeToDismiss
            val color by animateColorAsState(
                when (dismissState.targetValue) {
                    DismissValue.Default -> MaterialTheme.colorScheme.surface
                    DismissValue.DismissedToEnd -> MaterialTheme.colorScheme.primaryContainer
                    DismissValue.DismissedToStart -> MaterialTheme.colorScheme.errorContainer
                }
            )
            val alignment = when (direction) {
                DismissDirection.StartToEnd -> Alignment.CenterStart
                DismissDirection.EndToStart -> Alignment.CenterEnd
            }
            val icon = when (direction) {
                DismissDirection.StartToEnd -> Icons.Default.Done
                DismissDirection.EndToStart -> Icons.Default.Delete
            }
            val iconTint = when (direction) {
                DismissDirection.StartToEnd -> MaterialTheme.colorScheme.primary
                DismissDirection.EndToStart -> MaterialTheme.colorScheme.error
            }
            val scale by animateFloatAsState(
                if (dismissState.targetValue == DismissValue.Default) 0.8f else 1.2f
            )

            Box(
                Modifier
                    .fillMaxSize()
                    .background(color)
                    .padding(horizontal = 20.dp),
                contentAlignment = alignment
            ) {
                // Background dengan efek card rounded
                Card(
                    modifier = Modifier
                        .size(40.dp),
                    shape = CircleShape,
                    colors = CardDefaults.cardColors(
                        containerColor = when (direction) {
                            DismissDirection.StartToEnd -> MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                            DismissDirection.EndToStart -> MaterialTheme.colorScheme.error.copy(alpha = 0.8f)
                        }
                    )
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            icon,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier
                                .scale(scale)
                                .size(20.dp)
                        )
                    }
                }
            }
        },
        dismissContent = {
            TodoItem(
                todo = todo,
                categoryColor = categoryColor,
                categoryName = categoryName,
                onCheckedChange = onCheckedChange,
                onEditClick = onEditClick,
                onDeleteClick = onDeleteClick
            )
        }
    )
}