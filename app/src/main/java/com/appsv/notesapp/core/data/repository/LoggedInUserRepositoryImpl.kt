
package com.appsv.notesapp.core.data.repository

import com.appsv.notesapp.core.data.local.room.GoogleAuthResultDao
import com.appsv.notesapp.core.domain.models.LoggedInUserDetail
import com.appsv.notesapp.core.domain.repositories.LoggedInUserRepository

class LoggedInUserRepositoryImpl(
    private val dao: GoogleAuthResultDao
) : LoggedInUserRepository {

    override suspend fun saveUser(authResult: LoggedInUserDetail) {
        dao.insertOrUpdateUser(authResult)
    }

    override suspend fun getUserById(userId: String): LoggedInUserDetail? {
        return dao.getUserById(userId)
    }

    override suspend fun deleteUserById(userId: String) {
        dao.deleteUserById(userId)
    }

    override suspend fun deleteAllUsers() {
        dao.deleteAllUsers()
    }
}
