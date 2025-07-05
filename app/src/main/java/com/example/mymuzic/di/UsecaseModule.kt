package com.example.mymuzic.di

import com.example.mymuzic.domain.usecase.auth.*
import com.example.mymuzic.domain.usecase.music.*
import com.example.mymuzic.domain.usecase.player.*
import org.koin.dsl.module

val UsecaseModule = module {
    // Auth use cases
    single { GenerateAuthUrlUseCase(get()) }
    single { HandleAuthCallbackUseCase(get()) }
    single { RefreshTokenUseCase(get()) }
    single { GetUserProfileUseCase(get()) }
    single { GetValidAccessTokenUseCase(get()) }
    single { IsAuthenticatedUseCase(get()) }
    single { LogoutUseCase(get()) }
    single { GetAuthStateUseCase(get()) }
    
    // Music use cases
    single { GetRecentlyPlayedTracksUseCase(get()) }
    single { GetTopTracksUseCase(get()) }
    single { GetArtistsByIdsUseCase(get()) }
    single { GetTrackDetailUseCase(get()) }
    
    // Player use cases
    single { PlaySpotifyTrackUseCase(get()) }
} 