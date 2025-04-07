package com.example.todofy.data.model

import androidx.compose.ui.graphics.Color

data class TodoCategory(
    val id: Int = 0,
    val name: String,
    val color: Color
)

// Prioritas default untuk ToDo dengan warna yang jelas
val defaultCategories = listOf(
    TodoCategory(1, "Sangat Rendah", Color(0xFF4CAF50)), // Green
    TodoCategory(2, "Rendah", Color(0xFF2196F3)),        // Blue
    TodoCategory(3, "Tinggi", Color(0xFFFF9800)),        // Orange
    TodoCategory(4, "Sangat Tinggi", Color(0xFFF44336))  // Red
)