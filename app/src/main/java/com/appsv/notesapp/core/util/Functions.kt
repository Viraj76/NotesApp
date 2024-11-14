package com.appsv.notesapp.core.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import kotlin.random.Random


fun getCurrentLocalDateAndTimeInLong(): Long {
    return System.currentTimeMillis()
}

fun getUUIDRandomUniqueString(): String {
    return UUID.randomUUID().toString()
}

fun getTimeAndDateInString(dateInLong: Long) : String {
    val formatter = SimpleDateFormat("hh:mm a, dd-MM-yyyy", Locale.getDefault())
    val date = Date(dateInLong)
    return formatter.format(date)
}