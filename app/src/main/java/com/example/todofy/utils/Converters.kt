package com.example.todofy.utils

import androidx.compose.ui.graphics.Color
import androidx.room.TypeConverter
import java.util.Date

/**
 * Type converters untuk Room Database
 */
class Converters {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromColorToString(color: Color): String {
        return String.format("#%06X", 0xFFFFFF and color.value.toInt())
    }

    @TypeConverter
    fun toColor(colorString: String): Color {
        return try {
            Color(android.graphics.Color.parseColor(colorString))
        } catch (e: Exception) {
            Color.Gray
        }
    }
}