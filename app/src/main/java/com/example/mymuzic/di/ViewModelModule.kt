package com.example.mymuzic.di

import com.example.mymuzic.presentation.screen.AuthViewModel
import com.example.mymuzic.presentation.screen.PlaySongViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val ViewModelModule = module {
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
    viewModel {
        PlaySongViewModel(
            getTrackDetailUseCase = get(),
            playSpotifyTrackUseCase = get(),
            spotifyRemoteRepository = get()
        )
    }
} 