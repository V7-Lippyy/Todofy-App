package com.example.todofy.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "todos")
data class TodoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val categoryId: Int,
    val isCompleted: Boolean = false,
    val startDate: Date? = null,       // Tanggal mulai (opsional)
    val dueDate: Date? = null,         // Tanggal tenggat
    val hasTime: Boolean = false,      // Flag apakah waktu (jam & menit) diatur
    val createdAt: Date = Date(),
    val updatedAt: Date = Date()
)