
package com.appsv.notesapp.auth.presentation

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appsv.notesapp.auth.domain.models.GoogleAuthResult
import com.appsv.notesapp.core.presentation.sign_in.GoogleAuthenticator
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf

class SignInViewModel(ctx: Context) : ViewModel(), KoinComponent {

    private val googleSignIn: GoogleAuthenticator by inject { parametersOf(ctx) }

    private val _authResult = MutableLiveData<GoogleAuthResult?>()
    val authResult: LiveData<GoogleAuthResult?> get() = _authResult

    fun signInWithGoogle() {
        viewModelScope.launch {
            val credential = googleSignIn.authenticate()
            _authResult.value = if (credential != null) {
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

