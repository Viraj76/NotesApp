package com.appsv.notesapp.auth

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.appsv.notesapp.notes.home.presentation.HomeViewModel

class ViewModelFactoryForActivityContext(
    private val context: Context
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                AuthViewModel(context) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(context) as T
            }
//            modelClass.isAssignableFrom(AnotherViewModel::class.java) -> {
//                AnotherViewModel(context) as T
//            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
