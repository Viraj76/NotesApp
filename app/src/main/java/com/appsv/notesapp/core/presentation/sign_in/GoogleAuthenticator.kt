package com.appsv.notesapp.core.presentation.sign_in

import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import java.security.MessageDigest
import java.util.UUID

/**
 * Handles user authentication via Gmail accounts.
 * Displays all logged-in emails, and authenticates the user upon selection.
 */

class GoogleAuthenticator(private val context: Context) {

    private val clientID: String =
        "401047126786-i6cqr63g6153nvpvd2vc2kco89j2omai.apps.googleusercontent.com"

    private val credentialManager = CredentialManager.create(context)


    private val request: GetCredentialRequest = GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(false) // initial true, gives error
        .setServerClientId(clientID)
//        .setAutoSelectEnabled(true)
        .build().let {
            GetCredentialRequest.Builder()
                .addCredentialOption(it)
                .build()
        }


    suspend fun logOut(){
        credentialManager.clearCredentialState(ClearCredentialStateRequest())
    }

    suspend fun authenticate(): GoogleIdTokenCredential? = try {
        val result = credentialManager.getCredential(request = request, context = context)
        processSignIn(result)
    } catch (e: androidx.credentials.exceptions.GetCredentialCancellationException) {
        null
    } catch (e: Exception) {
        null
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
