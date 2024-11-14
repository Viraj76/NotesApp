package com.appsv.notesapp.notes.di

import com.appsv.notesapp.core.data.local.room.NotesAppRoomDB
import com.appsv.notesapp.core.data.local.room.NotesDao
import com.appsv.notesapp.core.data.repository.NotesRepositoryImpl
import com.appsv.notesapp.core.domain.repositories.NotesRepository
import org.koin.dsl.module

fun provideNotesDao(notesAppRoomDB: NotesAppRoomDB) = notesAppRoomDB.notesDao()

fun provideNotesRepository(notesDao: NotesDao) : NotesRepository{
    return NotesRepositoryImpl(notesDao)
}


val notesModule = module {

    single { provideNotesDao(get()) }
    single { provideNotesRepository(get()) }

}