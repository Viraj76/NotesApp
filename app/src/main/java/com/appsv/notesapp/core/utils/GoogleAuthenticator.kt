package com.appsv.notesapp.core.utils

import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialCancellationException
import com.appsv.notesapp.core.utils.Constants.CLIENT_ID
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow


/**
 * Handles user authentication via Gmail accounts.
 * Displays all logged-in emails, and authenticates the user upon selection.
 */

class GoogleAuthenticator(private val context: Context) {

    private val credentialManager = CredentialManager.create(context)

    private val request: GetCredentialRequest = GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(false)
        .setServerClientId(CLIENT_ID)
        .setAutoSelectEnabled(false)
        .build().let {
            GetCredentialRequest.Builder()
                .addCredentialOption(it)
                .build()
        }

    suspend fun clearCredentialState() {
        credentialManager.clearCredentialState(ClearCredentialStateRequest())
    }

    fun authenticate(): Flow<GoogleIdTokenCredential?> = callbackFlow {
        try {
            val result = credentialManager.getCredential(request = request, context = context)
            val credential = processSignIn(result)
            trySend(credential)
        } catch (e: GetCredentialCancellationException) {
            trySend(null)
        } catch (e: Exception) {
            trySend(null)
        }
        awaitClose {}
    }

    private fun processSignIn(result: GetCredentialResponse): GoogleIdTokenCredential? {
        val credential = result.credential
        return if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            try {
                GoogleIdTokenCredential.createFrom(credential.data)
            } catch (e: GoogleIdTokenParsingException) {
                null
            }
        } else {
            null
        }
    }

}
