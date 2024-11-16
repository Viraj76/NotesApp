package com.appsv.notesapp.notes.domain.repository

import com.appsv.notesapp.notes.domain.models.Notes
import kotlinx.coroutines.flow.Flow




interface NotesRepository {
    suspend fun saveNote(note: Notes)
    fun getNotesByEmailId(emailId: String): Flow<List<Notes>>
    suspend fun deleteNoteById(noteId: Int)
    suspend fun updateNote(note: Notes)
}