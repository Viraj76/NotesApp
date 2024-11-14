package com.appsv.notesapp.core.domain

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.appsv.notesapp.core.util.enums.Priority
import kotlinx.parcelize.Parcelize


@Parcelize
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
):Parcelable
