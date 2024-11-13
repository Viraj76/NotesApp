package com.appsv.notesapp.auth.splash.domain.repository

interface LoginStatusRepository {
    fun isLoggedIn(): Boolean
    fun setLoggedIn(loggedIn: Boolean)
}