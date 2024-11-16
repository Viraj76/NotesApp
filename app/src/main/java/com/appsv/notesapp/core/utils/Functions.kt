package com.appsv.notesapp.core.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


fun getCurrentLocalDateAndTimeInLong(): Long {
    return System.currentTimeMillis()
}

fun getTimeAndDateInString(dateInLong: Long) : String {
    val formatter = SimpleDateFormat("hh:mm a, dd-MM-yyyy", Locale.getDefault())
    val date = Date(dateInLong)
    return formatter.format(date)
}




