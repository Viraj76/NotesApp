package com.appsv.notesapp.auth.domain.models

data class GoogleAuthResult(
    val idToken: String?,
    val givenName: String?,
    val id: String?,
    val displayName: String?
)
