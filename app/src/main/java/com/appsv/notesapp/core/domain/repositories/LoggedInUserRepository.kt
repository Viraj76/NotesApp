package com.appsv.notesapp.core.domain.repositories

import com.appsv.notesapp.core.domain.models.LoggedInUserDetail
import kotlinx.coroutines.flow.Flow

interface LoggedInUserRepository {

    suspend fun saveUser(authResult: LoggedInUserDetail)
    suspend fun getUserById(userId: String): Flow<LoggedInUserDetail?>
    suspend fun deleteUserById(userId: String)
    suspend fun deleteAllUsers()
}