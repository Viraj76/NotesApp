package com.appsv.notesapp.base
import android.app.Application
import com.appsv.notesapp.auth.presentation.di.authPresentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class MyApplication : Application(){

    override fun onCreate() {
        super.onCreate()

        startKoin{
            modules(
                authPresentationModule
            )
            androidContext(this@MyApplication)
        }
    }
}