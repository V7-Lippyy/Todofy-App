package com.example.todofy.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlin.math.abs

/**
 * Extension function untuk memformat Date ke string yang mudah dibaca
 */
fun Date.formatToDisplayDate(): String {
    val format = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
    return format.format(this)
}

/**
 * Extension function untuk memformat Date dengan waktu (jam & menit)
 */
fun Date.formatToDisplayDateTime(): String {
    val format = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale("id", "ID"))
    return format.format(this)
}

/**
 * Extension function untuk memformat hanya waktu (jam & menit)
 */
fun Date.formatToDisplayTime(): String {
    val format = SimpleDateFormat("HH:mm", Locale("id", "ID"))
    return format.format(this)
}

/**
 * Function untuk menghitung durasi antara dua tanggal dalam format yang mudah dibaca
 */
fun calculateRemainingTime(startDate: Date?, dueDate: Date?): String {
    if (dueDate == null) return "Tidak ada tenggat"

    val now = startDate ?: Date()
    val diff = dueDate.time - now.time

    if (diff < 0) return "Terlambat!"

    val days = TimeUnit.MILLISECONDS.toDays(diff)
    val hours = TimeUnit.MILLISECONDS.toHours(diff) % 24
    val minutes = TimeUnit.MILLISECONDS.toMinutes(diff) % 60

    val result = StringBuilder()

    if (days > 0) {
        result.append("$days hari")
    }

    if (hours > 0) {
        if (result.isNotEmpty()) result.append(", ")
        result.append("$hours jam")
    }

    if (minutes > 0 || (days == 0L && hours == 0L)) {
        if (result.isNotEmpty()) result.append(", ")
        result.append("$minutes menit")
    }

    return result.toString()
}

/**
 * Function untuk mengatur jam dan menit pada sebuah Date
 */
fun Date.setTime(hour: Int, minute: Int): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.set(Calendar.HOUR_OF_DAY, hour)
    calendar.set(Calendar.MINUTE, minute)
    calendar.set(Calendar.SECOND, 0)
    return calendar.time
}