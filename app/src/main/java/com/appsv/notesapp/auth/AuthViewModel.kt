package com.appsv.notesapp.auth

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appsv.notesapp.auth.sign_in.domain.models.GoogleAuthResult
import com.appsv.notesapp.auth.splash.domain.repository.LoginStatusRepository
import com.appsv.notesapp.core.presentation.sign_in.GoogleAuthenticator
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf

class AuthViewModel(
    ctx: Context
) : ViewModel(), KoinComponent {

    private val googleSignIn: GoogleAuthenticator by inject { parametersOf(ctx) }
    private val loginStatusRepository : LoginStatusRepository by inject()

    private val _authResult = MutableLiveData<GoogleAuthResult?>()
    val authResult: LiveData<GoogleAuthResult?> get() = _authResult

    fun isUserLoggedIn(): Boolean {
        return loginStatusRepository.isLoggedIn()
    }

    fun setLoggedIn(loggedIn: Boolean) {
        loginStatusRepository.setLoggedIn(loggedIn)
    }

    fun signInWithGoogle() {
        viewModelScope.launch {
            val credential = googleSignIn.authenticate()
            _authResult.value = if (credential != null) {
                loginStatusRepository.setLoggedIn(true)
                Log.d("CurrentUser", credential.displayName.toString())

                GoogleAuthResult(
                    idToken = credential.idToken,
                    givenName = credential.givenName,
                    id = credential.id,
                    displayName = credential.displayName
                )
            } else {
                null
            }
        }
    }
}


