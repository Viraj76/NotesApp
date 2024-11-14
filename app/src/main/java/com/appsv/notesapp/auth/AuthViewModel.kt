package com.appsv.notesapp.auth

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appsv.notesapp.core.domain.models.LoggedInUserDetail
import com.appsv.notesapp.auth.splash.domain.repository.LoginStatusRepository
import com.appsv.notesapp.core.data.repository.LoggedInUserRepositoryImpl
import com.appsv.notesapp.core.domain.repositories.LoggedInUserRepository
import com.appsv.notesapp.core.presentation.sign_in.GoogleAuthenticator
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf

class AuthViewModel(
    ctx: Context
) : ViewModel(), KoinComponent {

    private val googleSignIn: GoogleAuthenticator by inject { parametersOf(ctx) }
    private val loginStatusRepository : LoginStatusRepository by inject()
    private val loggedInUserRepository : LoggedInUserRepository by inject()

    private val _authResult = MutableLiveData<LoggedInUserDetail?>()
    val authResult: LiveData<LoggedInUserDetail?> get() = _authResult

    fun isUserLoggedIn(): Boolean {
        return loginStatusRepository.isLoggedIn()
    }

    fun setLoggedIn(loggedIn: Boolean) {
        loginStatusRepository.setLoggedIn(loggedIn)
    }

    fun signInWithGoogle() {
        viewModelScope.launch {

            val credential: GoogleIdTokenCredential? = googleSignIn.authenticate()

            if (credential != null){
                updateLiveData(credential)
                saveUserInRoomDB()
                updateLoginStateInSharedPref() // navigate to HomeFragment
            }
             else {
                _authResult.value =null
            }
        }
    }

    private fun saveUserInRoomDB() {
        viewModelScope.launch {
            loggedInUserRepository.saveUser(authResult.value!!)
        }
    }

    private fun updateLiveData(credential: GoogleIdTokenCredential) {

        _authResult.value =
            LoggedInUserDetail(
                idToken = credential.idToken,
                givenName = credential.givenName,
                id = credential.id,
                displayName = credential.displayName,
                profilePictureUri = credential.profilePictureUri.toString(),
            )
    }

    private fun updateLoginStateInSharedPref() {
        loginStatusRepository.setLoggedIn(true)
    }
}


