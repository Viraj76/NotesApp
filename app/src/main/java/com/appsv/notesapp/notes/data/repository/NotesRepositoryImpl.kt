package com.appsv.notesapp.notes.data.repository

import com.appsv.notesapp.notes.domain.models.Notes
import com.appsv.notesapp.notes.data.local.room.NotesDao
import com.appsv.notesapp.notes.domain.repository.NotesRepository
import kotlinx.coroutines.flow.Flow

class NotesRepositoryImpl(private val notesDao: NotesDao) : NotesRepository {

    override suspend fun saveNote(note: Notes) {
        notesDao.insertOrUpdateNote(note)
    }

    override fun getNotesByEmailId(emailId: String): Flow<List<Notes>> {
        return notesDao.getNotesByEmailId(emailId)
    }

    override suspend fun deleteNoteById(noteId: Int) {
        notesDao.deleteNoteById(noteId)
    }

    override suspend fun updateNote(note: Notes) {
        notesDao.updateNoteById(note)
    }
}