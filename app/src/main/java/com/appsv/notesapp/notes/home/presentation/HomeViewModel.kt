package com.appsv.notesapp.notes.home.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appsv.notesapp.auth.splash.domain.repository.LoginStatusRepository
import com.appsv.notesapp.core.domain.models.LoggedInUserDetail
import com.appsv.notesapp.core.domain.repositories.LoggedInUserRepository
import com.appsv.notesapp.core.domain.repositories.NotesRepository
import com.appsv.notesapp.core.presentation.sign_in.GoogleAuthenticator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf

class HomeViewModel(
    context : Context
) : ViewModel(), KoinComponent {


    private val googleSignIn: GoogleAuthenticator by inject { parametersOf(context) }
    private val loggedInUserRepository: LoggedInUserRepository by inject()
    private val loginStatusRepository: LoginStatusRepository by inject()
    private  val notesRepository: NotesRepository by inject()

    private val _notes = MutableStateFlow(NotesState())
    val notes  = _notes.asStateFlow()

    private val _currentUser = MutableStateFlow<String?>(null) // Allow null values
    val currentUser = _currentUser.asStateFlow()

    private var _logOutDialogState = MutableStateFlow(false)
    val logOutDialogState = _logOutDialogState.asStateFlow()

    fun showLogOutConfirmationDialog(){
        _logOutDialogState.value = true
    }

    fun hideLogOutConfirmationDialog() {
        _logOutDialogState.value = false
    }

    init {
        // get currentUser email Id
        getUserId()

        // get saved notes of the loggedIn User
        viewModelScope.launch {
            currentUser.collect { userID ->
                userID?.let {
                    getNotesByEmailId(it)
                }
            }
        }


    }




    private fun getUserId() {
        _currentUser.value = loginStatusRepository.getUser()
    }
    fun getNotesByEmailId(emailId: String) {
        viewModelScope.launch {
            notesRepository.getNotesByEmailId(emailId).collect { notesList ->

                _notes.value = notes.value.copy(isLoading = false,notesList = notesList)
            }
        }
    }


    fun onLoggingOutUser(){
        clearCurrentUser()
        clearCredentialManagerState()
    }

    fun clearCredentialManagerState() {
        viewModelScope.launch {
            googleSignIn.clearCredentialState()
        }
    }


    private fun clearCurrentUser() {
        loginStatusRepository.saveUser(null)
    }

    suspend fun getUserById(id: String): Flow<LoggedInUserDetail?> {
        return loggedInUserRepository.getUserById(id)
    }




}