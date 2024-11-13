package com.appsv.notesapp.auth.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import com.appsv.notesapp.core.presentation.sign_in.GoogleAuthenticator
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf

class SignInViewModel(ctx: Context) : ViewModel(), KoinComponent {

    private val googleSignIn: GoogleAuthenticator by inject { parametersOf(ctx) }

    fun signInWithGoogle(callback: GoogleIdTokenCredential.() -> Unit) {
        googleSignIn.authenticate {

        }
    }
}