package com.appsv.notesapp.notes.home.presentation

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appsv.notesapp.auth.splash.domain.repository.LoginStatusRepository
import com.appsv.notesapp.core.domain.models.LoggedInUserDetail
import com.appsv.notesapp.core.domain.repositories.LoggedInUserRepository
import com.appsv.notesapp.core.presentation.sign_in.GoogleAuthenticator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf

class HomeViewModel(
    context : Context
) : ViewModel(), KoinComponent {

    private var _loggedInUser = MutableStateFlow(LoggedInUserDetail(id = ""))
    val loggedInUserDetail = _loggedInUser.asStateFlow()

    private val googleSignIn: GoogleAuthenticator by inject { parametersOf(context) }
    private val loggedInUserRepository: LoggedInUserRepository by inject()
    private val loginStatusRepository: LoginStatusRepository by inject()



    fun onLoggingOutUser(){
        clearCurrentUser()
        clearCredentialManagerState()
    }

    private fun clearCredentialManagerState() {
        viewModelScope.launch {
            googleSignIn.logOut()
        }
    }


    private fun clearCurrentUser() {
        loginStatusRepository.saveUser(null)
    }

    suspend fun getUserById(id: String): Flow<LoggedInUserDetail?> {

        return loggedInUserRepository.getUserById(id)
    }



}