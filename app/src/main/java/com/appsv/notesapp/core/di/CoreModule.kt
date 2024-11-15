package com.appsv.notesapp.core.di

import android.content.Context
import androidx.room.Room
import com.appsv.notesapp.core.data.local.room.LoggedInUserDao
import com.appsv.notesapp.core.data.local.room.NotesAppRoomDB
import com.appsv.notesapp.core.data.repository.LoggedInUserRepositoryImpl
import com.appsv.notesapp.core.domain.repositories.LoggedInUserRepository
import com.appsv.notesapp.core.util.NetworkManager

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


fun provideRoomDatabase(context: Context) =
    Room.databaseBuilder(context, NotesAppRoomDB::class.java, "Notes DataBase")
        .allowMainThreadQueries()
        .fallbackToDestructiveMigration()
        .build()

fun provideDao(notesAppRoomDB: NotesAppRoomDB) = notesAppRoomDB.loggedInUserDetailDao()


fun provideLoggedInUserRepository(loggedInUserDao: LoggedInUserDao): LoggedInUserRepository {
    return LoggedInUserRepositoryImpl(loggedInUserDao)
}

fun provideNetManagerInstance(context: Context) : NetworkManager{
    return NetworkManager(context)
}



val coreModule = module {
    single { provideRoomDatabase(androidContext()) }
    single { provideDao(get()) }
    single { provideLoggedInUserRepository(get()) }
    single { provideNetManagerInstance(get()) }
}
