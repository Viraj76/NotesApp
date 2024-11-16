package com.appsv.notesapp.notes.presentation.home.presentation

import com.appsv.notesapp.notes.domain.models.Notes

// notes states
data class NotesState(
    val isLoading : Boolean = true,
    val notesList : List<Notes> = emptyList()
)
