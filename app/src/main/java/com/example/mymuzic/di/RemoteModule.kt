package com.example.mymuzic.di

import com.example.mymuzic.data.remote.SpotifyRemoteDataSource
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val RemoteModule = module {
    single {
        SpotifyRemoteDataSource(
            context = androidContext(),
            clientId = "c227a0aaa5d54c4881c1ad98e8dad3ec",
            redirectUri = "com.example.mymuzic://callback"
        )
    }
} 