package com.example.mymuzic.di

import com.example.mymuzic.data.repository.AuthRepositoryImpl
import com.example.mymuzic.data.repository.SpotifyRemoteRepositoryImpl
import com.example.mymuzic.domain.repository.AuthRepository
import com.example.mymuzic.domain.repository.SpotifyRemoteRepository
import org.koin.dsl.module

val RepositoryModule = module {
    single<AuthRepository> {
        AuthRepositoryImpl(
            authApi = get(),
            localDataSource = get(),
            clientId = "c227a0aaa5d54c4881c1ad98e8dad3ec",
            redirectUri = "com.example.mymuzic://callback"
        )
    }
    single<SpotifyRemoteRepository> {
        SpotifyRemoteRepositoryImpl(get())
    }
} 