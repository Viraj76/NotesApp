package com.appsv.notesapp.auth.presentation.di

import android.content.Context
import com.appsv.notesapp.auth.presentation.SignInViewModel
import com.appsv.notesapp.core.presentation.sign_in.GoogleAuthenticator
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val authPresentationModule = module {
    single {(context: Context)->
        GoogleAuthenticator(context)
    }
    viewModel { (context:Context)->
        SignInViewModel(context)
    }
}