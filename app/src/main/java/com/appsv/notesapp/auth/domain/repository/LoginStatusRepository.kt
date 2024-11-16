package com.appsv.notesapp.auth.domain.repository

interface LoginStatusRepository {

    fun saveUser(user: String?)

    fun getUser(): String?
}

