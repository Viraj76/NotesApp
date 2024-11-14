package com.appsv.notesapp.core.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.appsv.notesapp.core.domain.models.LoggedInUserDetail

@Database(entities = [LoggedInUserDetail::class], version = 1, exportSchema = false)
abstract class NotesAppRoomDB : RoomDatabase() {
    abstract fun googleAuthResultDao(): GoogleAuthResultDao
}
