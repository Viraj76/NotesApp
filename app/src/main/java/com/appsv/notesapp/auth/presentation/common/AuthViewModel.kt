package com.appsv.notesapp.auth.presentation.common

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appsv.notesapp.core.domain.models.LoggedInUserDetail
import com.appsv.notesapp.auth.domain.repository.LoginStatusRepository
import com.appsv.notesapp.core.domain.repositories.LoggedInUserRepository
import com.appsv.notesapp.core.utils.GoogleAuthenticator
import com.appsv.notesapp.core.utils.NetworkManager
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf

class AuthViewModel(
    ctx: Context
) : ViewModel(), KoinComponent {

    private val googleAuthenticator: GoogleAuthenticator by inject { parametersOf(ctx) }
    private val loginStatusRepository: LoginStatusRepository by inject()
    private val loggedInUserRepository: LoggedInUserRepository by inject()
    private val networkManager : NetworkManager by inject()


    private val _navigateToHome = MutableStateFlow(false)
    val navigateToHome = _navigateToHome.asStateFlow()

    fun navigateToHome(){
        _navigateToHome.value = true
    }

    /**
     * we need each event that's why shared flow, using StateFlow will not produce the initial value (false here) again.
     */
    private val _authResult = MutableSharedFlow<Boolean>()
    val authResult: SharedFlow<Boolean> get() = _authResult


    private var _signInWaitDialog = MutableStateFlow(false)
    val signInWaitDialog = _signInWaitDialog.asStateFlow()

    private var _signInDoneDialog = MutableStateFlow(false)
    val signInDoneDialog = _signInDoneDialog.asStateFlow()

    private val _internetState = MutableStateFlow(false)
    val internetState = _internetState.asStateFlow()




    fun showSignInWaitDialog(){
        _signInWaitDialog.value = true
    }

    fun hideSignInWaitDialog() {
        _signInWaitDialog.value = false
    }


    fun showSignInDoneDialog(){
        _signInDoneDialog.value = true
    }

    fun hideSignInDoneDialog() {
        _signInDoneDialog.value = false
    }


    private fun updateLiveData(isLoggedIn: Boolean) {
        viewModelScope.launch {
            _authResult.emit(isLoggedIn)
        }
    }

    fun internetConnectionState(){
        viewModelScope.launch {
            networkManager.observeNetworkStatus().collect{internetState->
                _internetState.value = internetState
            }
        }

    }

    fun signInWithGoogle() {
        viewModelScope.launch {
            googleAuthenticator.authenticate().collect{credential->
                if (credential != null) {
                    saveUserInRoomDB(credential)
                    saveUserIDInSharedPref(credential.id)
                    updateLiveData(true)
                } else {
                    updateLiveData(false)
                }
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
}
