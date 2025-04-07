package com.example.todofy.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissState
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
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
    // Gunakan warna langsung berdasarkan nama kategori
    val priorityColor = when (categoryName) {
        "Sangat Rendah" -> Green
        "Rendah" -> Blue
        "Tinggi" -> Orange
        "Sangat Tinggi" -> Red
        else -> categoryColor // Fallback ke warna yang diberikan
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = { onEditClick(todo) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, top = 12.dp, end = 12.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Indikator prioritas
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .padding(4.dp)
                    .clip(CircleShape)
                    .background(priorityColor)
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Isi Todo
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = todo.title,
                    style = MaterialTheme.typography.titleMedium,
                    textDecoration = if (todo.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                )

                if (todo.description.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = todo.description,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        textDecoration = if (todo.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                    )
                }

                // Tanggal mulai jika ada
                todo.startDate?.let { startDate ->
                    if (todo.dueDate == null || startDate.time != todo.dueDate.time) { // Tampilkan hanya jika berbeda dengan tenggat
                        Spacer(modifier = Modifier.height(4.dp))
                        val displayDate = if (todo.hasTime) startDate.formatToDisplayDateTime() else startDate.formatToDisplayDate()
                        Text(
                            text = "Mulai: $displayDate",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }

                // Tanggal jatuh tempo
                todo.dueDate?.let { dueDate ->
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val isTerlambat = dueDate.before(Date()) && !todo.isCompleted
                        val displayDate = if (todo.hasTime) dueDate.formatToDisplayDateTime() else dueDate.formatToDisplayDate()

                        Text(
                            text = "Batas: $displayDate",
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isTerlambat)
                                MaterialTheme.colorScheme.error
                            else
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }

                    // Tampilkan estimasi waktu atau status
                    if (!todo.isCompleted) {
                        Spacer(modifier = Modifier.height(2.dp))
                        val remainingTime = calculateRemainingTime(todo.startDate ?: Date(), dueDate)
                        Text(
                            text = remainingTime,
                            style = MaterialTheme.typography.labelSmall,
                            color = if (remainingTime == "Terlambat!")
                                MaterialTheme.colorScheme.error
                            else
                                MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            // Checkbox
            Checkbox(
                checked = todo.isCompleted,
                onCheckedChange = { onCheckedChange(todo) }
            )
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
            val scale by animateFloatAsState(
                if (dismissState.targetValue == DismissValue.Default) 0.8f else 1.2f
            )

            Box(
                Modifier
                    .fillMaxSize()
                    .background(color) // Use the animated color directly
                    .padding(horizontal = 20.dp),
                contentAlignment = alignment
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    modifier = Modifier.scale(scale)
                )
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