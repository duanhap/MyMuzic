package com.example.mymuzic.di

import com.example.mymuzic.data.local.AuthLocalDataSource
import com.example.mymuzic.data.remote.SpotifyAuthApi
import com.example.mymuzic.data.repository.AuthRepositoryImpl
import com.example.mymuzic.domain.repository.AuthRepository
import com.example.mymuzic.domain.usecase.*
import com.example.mymuzic.presentation.screen.AuthViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val appModule = module {
    
    // Network dependencies
    single {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }
    
    single {
        Retrofit.Builder()
            .baseUrl("https://api.spotify.com/")
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    
    // API interfaces
    single<SpotifyAuthApi> {
        get<Retrofit>().create(SpotifyAuthApi::class.java)
    }
    
    // Local data sources
    single {
        AuthLocalDataSource(androidContext())
    }
    
    // Repository implementations
    single<AuthRepository> {
        AuthRepositoryImpl(
            authApi = get(),
            localDataSource = get(),
            clientId = "c227a0aaa5d54c4881c1ad98e8dad3ec",
            redirectUri = "com.example.mymuzic://callback"
        )
    }
    
    // Use cases
    single {
        GenerateAuthUrlUseCase(get())
    }
    
    single {
        HandleAuthCallbackUseCase(get())
    }
    
    single {
        RefreshTokenUseCase(get())
    }
    
    single {
        GetUserProfileUseCase(get())
    }
    
    single {
        GetValidAccessTokenUseCase(get())
    }
    
    single {
        IsAuthenticatedUseCase(get())
    }
    
    single {
        LogoutUseCase(get())
    }
    
    single {
        GetAuthStateUseCase(get())
    }
    
    single { GetRecentlyPlayedTracksUseCase(get()) }
    single { GetTopTracksUseCase(get()) }
    single { GetArtistsByIdsUseCase(get()) }

    
    // ViewModels
    viewModel {
        AuthViewModel(
            generateAuthUrlUseCase = get(),
            handleAuthCallbackUseCase = get(),
            getUserProfileUseCase = get(),
            isAuthenticatedUseCase = get(),
            logoutUseCase = get(),
            getAuthStateUseCase = get(),
            getRecentlyPlayedTracksUseCase = get(),
            getTopTracksUseCase = get(),
            getArtistsByIdsUseCase = get()
        )
    }
} 