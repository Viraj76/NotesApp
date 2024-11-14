package com.appsv.notesapp.core.di

import android.content.Context
import androidx.room.Room
import com.appsv.notesapp.core.data.local.room.GoogleAuthResultDao
import com.appsv.notesapp.core.data.local.room.NotesAppRoomDB
import com.appsv.notesapp.core.data.repository.LoggedInUserRepositoryImpl
import com.appsv.notesapp.core.domain.repositories.LoggedInUserRepository

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


fun provideRoomDatabase(context: Context) =
    Room.databaseBuilder(context, NotesAppRoomDB::class.java, "Task DataBase")
        .allowMainThreadQueries()
        .fallbackToDestructiveMigration()
        .build()

fun provideDao(notesAppRoomDB: NotesAppRoomDB) = notesAppRoomDB.googleAuthResultDao()


fun provideTaskRepository(googleAuthResultDao: GoogleAuthResultDao): LoggedInUserRepository {
    return LoggedInUserRepositoryImpl(googleAuthResultDao)
}



val coreModule = module {
    single { provideRoomDatabase(androidContext()) }
    single { provideDao(get()) }
    single { provideTaskRepository(get()) }
}
