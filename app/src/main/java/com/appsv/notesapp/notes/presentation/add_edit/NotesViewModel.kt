package com.appsv.notesapp.notes.presentation.add_edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appsv.notesapp.auth.domain.repository.LoginStatusRepository
import com.appsv.notesapp.notes.domain.models.Notes
import com.appsv.notesapp.notes.domain.repository.NotesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class NotesViewModel : ViewModel(), KoinComponent {

    private  val notesRepository: NotesRepository by inject()

    private val _notes = MutableStateFlow<List<Notes>>(emptyList())
    val notes: StateFlow<List<Notes>> = _notes


    private val loginStatusRepository: LoginStatusRepository by inject()

    private var _deleteDialogState = MutableStateFlow(false)
    val deleteDialogState = _deleteDialogState.asStateFlow()



    fun showDeleteConfirmationDialogState(){
        _deleteDialogState.value = true
    }

    fun hideDeleteConfirmationDialogState() {
        _deleteDialogState.value = false
    }

    fun getUserId(): String? {
        return loginStatusRepository.getUser()
    }

    fun saveOrUpdateNote(note: Notes) {
        viewModelScope.launch {
            notesRepository.saveNote(note)
        }
    }
    fun deleteNoteById(noteId: Int) {
        viewModelScope.launch {
            notesRepository.deleteNoteById(noteId)
        }
    }

    fun updateNote(note: Notes) {
        viewModelScope.launch {
            notesRepository.updateNote(note)
        }
    }

}
