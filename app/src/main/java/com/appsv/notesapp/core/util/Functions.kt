package com.appsv.notesapp.core.util

import java.util.UUID
import kotlin.random.Random


fun getCurrentLocalDateAndTimeInLong() : Long{
    return System.currentTimeMillis()
}

fun getUUIDRandomUniqueString() : String{
    return UUID.randomUUID().toString()
}