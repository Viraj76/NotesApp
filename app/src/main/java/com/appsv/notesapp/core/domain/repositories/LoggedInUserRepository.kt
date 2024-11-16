package com.appsv.notesapp.core.domain.repositories

import com.appsv.notesapp.core.domain.Notes
import com.appsv.notesapp.core.domain.models.LoggedInUserDetail
import kotlinx.coroutines.flow.Flow

interface LoggedInUserRepository {
    suspend fun saveUser(authResult: LoggedInUserDetail)
    suspend fun getUserById(userId: String): Flow<LoggedInUserDetail?>
    suspend fun getAllUsers() : Flow<List<LoggedInUserDetail?>>
    suspend fun deleteUserById(userId: String)
    suspend fun deleteAllUsers()
}


interface NotesRepository {
    suspend fun saveNote(note: Notes)
    fun getNotesByEmailId(emailId: String): Flow<List<Notes>>
    suspend fun deleteNoteById(noteId: Int)
    suspend fun updateNote(note: Notes)
}