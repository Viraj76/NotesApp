package com.appsv.notesapp.notes.home.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.appsv.notesapp.auth.splash.domain.repository.LoginStatusRepository
import com.appsv.notesapp.core.domain.repositories.LoggedInUserRepository
import com.appsv.notesapp.core.presentation.sign_in.GoogleAuthenticator
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



    fun onLoggingOutUser(id : String){
        clearCurrentUser()
//        clearCurrentUserDetails(id)
    }

//    private fun clearCurrentUserDetails(id: String) {
//        viewModelScope.launch {
//            loggedInUserRepository.deleteUserById(id)
//        }
//    }

    private fun clearCurrentUser() {
        loginStatusRepository.saveUser(null)
    }


}