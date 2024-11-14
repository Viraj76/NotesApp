package com.appsv.notesapp.core.domain.repositories

import com.appsv.notesapp.core.domain.models.LoggedInUserDetail

interface LoggedInUserRepository {

    suspend fun saveUser(authResult: LoggedInUserDetail)
    suspend fun getUserById(userId: String): LoggedInUserDetail?
    suspend fun deleteUserById(userId: String)
    suspend fun deleteAllUsers()
}