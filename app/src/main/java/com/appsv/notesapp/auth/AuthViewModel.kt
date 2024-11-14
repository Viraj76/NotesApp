package com.appsv.notesapp.auth

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appsv.notesapp.core.domain.models.LoggedInUserDetail
import com.appsv.notesapp.auth.splash.domain.repository.LoginStatusRepository
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
    private val loginStatusRepository: LoginStatusRepository by inject()
    private val loggedInUserRepository: LoggedInUserRepository by inject()

    private val _authResult = MutableLiveData<Boolean>()
    val authResult: LiveData<Boolean> get() = _authResult

    fun signInWithGoogle() {
        viewModelScope.launch {

            val credential: GoogleIdTokenCredential? = googleSignIn.authenticate()

            if (credential != null) {

                saveUserInRoomDB(credential)
                saveUserIDInSharedPref(credential.id)
                updateLiveData()
            } else {
                _authResult.value = false
            }
        }
    }

    private fun saveUserIDInSharedPref(id: String) {
        loginStatusRepository.saveUser(id)
    }

    fun getUserId(): String? {
        return loginStatusRepository.getUser()
    }

    private fun saveUserInRoomDB(credential: GoogleIdTokenCredential) {
        viewModelScope.launch {
            val loggedInUserDetail = LoggedInUserDetail(
                idToken = credential.idToken,
                givenName = credential.givenName,
                id = credential.id,
                displayName = credential.displayName,
                profilePictureUri = credential.profilePictureUri.toString(),
            )
            loggedInUserRepository.saveUser(loggedInUserDetail)
        }
    }

    private fun updateLiveData() {
        _authResult.value = true
    }
}


