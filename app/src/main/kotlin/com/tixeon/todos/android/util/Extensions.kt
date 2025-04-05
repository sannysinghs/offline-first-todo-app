package com.tixeon.todos.android.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

const val Task_DATE_TIME_FORMAT = "dd/mm/yy, hh:mm a"
const val Task_REMOTE_DATE_TIME_FORMAT = "yyyy-mm-dd'T'hh:mm:ss.SSS'Z'" // 2017-01-23T18:00:00.511Z

fun String.formatDate(): String {
    if (this.isEmpty()) throw IllegalArgumentException("Invalid date format")

    val outputFormat = SimpleDateFormat(com.tixeon.todos.android.util.Task_DATE_TIME_FORMAT, Locale.US)
    val inputFormat = SimpleDateFormat(com.tixeon.todos.android.util.Task_REMOTE_DATE_TIME_FORMAT, Locale.US).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

    return outputFormat.format(
        inputFormat.parse(this) ?: throw IllegalArgumentException("Invalid date format")
    )
}

fun String.toTimeInMills(): Long {
    if (this.isEmpty()) throw IllegalArgumentException("Invalid date format")
    val inputFormat = SimpleDateFormat(com.tixeon.todos.android.util.Task_REMOTE_DATE_TIME_FORMAT, Locale.US).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

    val date = inputFormat.parse(this) ?: throw IllegalArgumentException("Invalid date format")
    return date.time
}

fun Long.toDateString(): String {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return dateFormat.format(calendar.time)
}