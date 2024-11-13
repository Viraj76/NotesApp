package com.appsv.notesapp.auth.di

import android.content.Context
import com.appsv.notesapp.auth.AuthViewModel
import com.appsv.notesapp.auth.splash.data.repository.LoginStatusRepositoryImpl
import com.appsv.notesapp.auth.splash.domain.repository.LoginStatusRepository
import com.appsv.notesapp.core.presentation.sign_in.GoogleAuthenticator
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val authModule = module{

    single<LoginStatusRepository> { LoginStatusRepositoryImpl(androidContext()) }

    single {(context: Context)->
        GoogleAuthenticator(context)
    }


}