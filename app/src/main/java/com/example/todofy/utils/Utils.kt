package com.example.todofy.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Extension function untuk memformat Date ke string yang mudah dibaca
 */
fun Date.formatToDisplayDate(): String {
    val format = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
    return format.format(this)
}