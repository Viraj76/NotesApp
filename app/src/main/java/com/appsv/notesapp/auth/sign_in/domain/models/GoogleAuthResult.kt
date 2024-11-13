package com.appsv.notesapp.auth.sign_in.domain.models

data class GoogleAuthResult(
    val idToken: String?,
    val givenName: String?,
    val id: String?,
    val displayName: String?
)
