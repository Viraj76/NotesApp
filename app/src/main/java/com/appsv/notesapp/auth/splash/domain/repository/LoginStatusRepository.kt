package com.appsv.notesapp.auth.splash.domain.repository

interface LoginStatusRepository {

    fun saveUser(user: String?)
    fun getUser(): String?
}

