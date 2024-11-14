
package com.appsv.notesapp.core.data.repository

import com.appsv.notesapp.core.data.local.room.GoogleAuthResultDao
import com.appsv.notesapp.core.domain.models.LoggedInUserDetail
import com.appsv.notesapp.core.domain.repositories.LoggedInUserRepository
import kotlinx.coroutines.flow.Flow


class LoggedInUserRepositoryImpl(
    private val googleAuthResultDao: GoogleAuthResultDao
) : LoggedInUserRepository {

    override suspend fun saveUser(authResult: LoggedInUserDetail) {
        googleAuthResultDao.insertOrUpdateUser(authResult)
    }

    override suspend fun getUserById(userId: String): Flow<LoggedInUserDetail?> {
        return googleAuthResultDao.getUserById(userId)
    }

    override suspend fun deleteUserById(userId: String) {
        googleAuthResultDao.deleteUserById(userId)
    }

    override suspend fun deleteAllUsers() {
        googleAuthResultDao.deleteAllUsers()
    }
}
