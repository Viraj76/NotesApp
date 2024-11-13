package com.appsv.notesapp.splash.domain.repository

interface LoginStatusRepository {
    fun isLoggedIn(): Boolean
    fun setLoggedIn(loggedIn: Boolean)
}