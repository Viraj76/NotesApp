package com.appsv.notesapp.core.presentation.sign_in

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import com.appsv.notesapp.BuildConfig
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
*Handles user authentication via Gmail accounts.
*Displays all logged-in emails, and authenticates the user upon selection.
 */

class GoogleAuthenticator(private val context: Context) {

    private val  clientID: String = BuildConfig.GOOGLE_CLIENT_ID

    private val credentialManager = CredentialManager.create(context)

    private val request = GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(false)
        .setServerClientId(clientID)
        .setAutoSelectEnabled(true)
        .build().let {
            GetCredentialRequest.Builder()
                .addCredentialOption(it)
                .build()
        }

    fun authenticate(callback: (GoogleIdTokenCredential?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = credentialManager.getCredential(request = request, context = context)
                processSignIn(callback, result)
            } catch (e: Exception) {
                e.printStackTrace()
                callback(null)
            }
        }
    }

    private fun processSignIn(
        callback: (GoogleIdTokenCredential?) -> Unit,
        result: GetCredentialResponse
    ) {
        val credential = result.credential
        if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            try {
                val googleCredential = GoogleIdTokenCredential.createFrom(credential.data)
                callback(googleCredential)
            } catch (e: GoogleIdTokenParsingException) {
                callback(null)
            }
        } else {
            callback(null)
        }
    }
}
