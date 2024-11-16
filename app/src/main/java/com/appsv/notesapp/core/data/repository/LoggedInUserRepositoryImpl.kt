
package com.appsv.notesapp.core.data.repository

import com.appsv.notesapp.core.data.local.room.LoggedInUserDao
import com.appsv.notesapp.core.data.local.room.NotesDao
import com.appsv.notesapp.core.domain.Notes
import com.appsv.notesapp.core.domain.models.LoggedInUserDetail
import com.appsv.notesapp.core.domain.repositories.LoggedInUserRepository
import com.appsv.notesapp.core.domain.repositories.NotesRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow


class LoggedInUserRepositoryImpl(
    private val loggedInUserDao: LoggedInUserDao
) : LoggedInUserRepository {




    override suspend fun saveUser(authResult: LoggedInUserDetail) {
        loggedInUserDao.insertOrUpdateUser(authResult)
    }

    override suspend fun getUserById(userId: String): Flow<LoggedInUserDetail?> = callbackFlow {
        trySend(loggedInUserDao.getUserById(userId))
        awaitClose{}
    }

    override suspend fun getAllUsers(): Flow<List<LoggedInUserDetail?>> {
        return loggedInUserDao.getAllUsers()
    }

    override suspend fun deleteUserById(userId: String) {
        loggedInUserDao.deleteUserById(userId)
    }

    override suspend fun deleteAllUsers() {
        loggedInUserDao.deleteAllUsers()
    }
}

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

