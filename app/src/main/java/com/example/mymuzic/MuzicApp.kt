package com.example.mymuzic

import android.app.Application
import com.example.mymuzic.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MuzicApp: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MuzicApp)
            modules(listOf(appModule))
        }
    }
}