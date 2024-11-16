package com.appsv.notesapp.auth.di

import android.content.Context
import com.appsv.notesapp.auth.data.repository.LoginStatusRepositoryImpl
import com.appsv.notesapp.auth.domain.repository.LoginStatusRepository
import com.appsv.notesapp.core.utils.GoogleAuthenticator
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val authModule = module{

    single<LoginStatusRepository> { LoginStatusRepositoryImpl(androidContext()) }

    single {(context: Context)->
        GoogleAuthenticator(context)
    }

}