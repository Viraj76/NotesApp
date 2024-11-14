package com.appsv.notesapp.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appsv.notesapp.auth.splash.domain.repository.LoginStatusRepository
import com.appsv.notesapp.core.domain.Notes
import com.appsv.notesapp.core.domain.repositories.NotesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.java.KoinJavaComponent.inject

class NotesViewModel() : ViewModel(), KoinComponent {

    private  val notesRepository: NotesRepository by inject()

    private val _notes = MutableStateFlow<List<Notes>>(emptyList())
    val notes: StateFlow<List<Notes>> = _notes

    private val _noteAddedOrUpdated = MutableStateFlow(false)
    val noteAddedOrUpdated: StateFlow<Boolean> = _noteAddedOrUpdated

    private val loginStatusRepository: LoginStatusRepository by inject()


    fun getUserId(): String? {
        return loginStatusRepository.getUser()
    }


    fun saveOrUpdateNote(note: Notes) {
        viewModelScope.launch {
            notesRepository.saveNote(note)
            _noteAddedOrUpdated.value = true
        }
    }

    fun deleteNoteById(noteId: Int) {
        viewModelScope.launch {
            notesRepository.deleteNoteById(noteId)
            _noteAddedOrUpdated.value = true
        }
    }

    fun updateNote(note: Notes) {
        viewModelScope.launch {
            notesRepository.updateNote(note)
            _noteAddedOrUpdated.value = true
        }
    }
}
