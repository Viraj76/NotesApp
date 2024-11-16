package com.appsv.notesapp.auth.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.appsv.notesapp.auth.domain.repository.LoginStatusRepository

class LoginStatusRepositoryImpl(context: Context) : LoginStatusRepository {

    private val prefs: SharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    override fun saveUser(user: String?) {
        prefs.edit().putString("user", user).apply()
    }

    override fun getUser(): String? {
        return prefs.getString("user", null)
    }

}
