package com.appsv.notesapp.core.domain.models


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "LoggedInUserDetail")
data class LoggedInUserDetail(
    @PrimaryKey val id: String,
    val idToken: String? = null,
    val givenName: String? = null,
    val displayName: String? = null,
    val profilePictureUri: String? = null
)
