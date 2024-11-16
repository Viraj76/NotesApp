package com.appsv.notesapp.notes.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.appsv.notesapp.core.utils.enums.Priority



@Entity(tableName = "Notes")
data class Notes(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var title: String,
    var subTitle: String,
    var notes: String,
    var date: Long,
    var priority: Priority,
    var emailId: String
)
