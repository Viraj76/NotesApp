package com.appsv.notesapp.auth.presentation.di

import android.content.Context
import com.appsv.notesapp.core.presentation.sign_in.GoogleAuthenticator
import org.koin.dsl.module

val authPresentationModule = module {
    single {(ctx: Context)->
        GoogleAuthenticator(ctx)
    }
}