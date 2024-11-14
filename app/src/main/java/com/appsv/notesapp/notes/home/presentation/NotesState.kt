package com.appsv.notesapp.notes.home.presentation

import com.appsv.notesapp.core.domain.Notes

data class NotesState(
    val isLoading : Boolean = true,
    val notesList : List<Notes> = emptyList()
)
