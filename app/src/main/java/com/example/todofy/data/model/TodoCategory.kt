package com.example.todofy.data.model

import androidx.compose.ui.graphics.Color
import com.example.todofy.ui.theme.Blue
import com.example.todofy.ui.theme.Green
import com.example.todofy.ui.theme.Orange
import com.example.todofy.ui.theme.Purple

data class TodoCategory(
    val id: Int = 0,
    val name: String,
    val color: Color
)

// Kategori default untuk ToDo
val defaultCategories = listOf(
    TodoCategory(1, "Pekerjaan", Blue),
    TodoCategory(2, "Pribadi", Purple),
    TodoCategory(3, "Belanja", Orange),
    TodoCategory(4, "Penting", Green)
)