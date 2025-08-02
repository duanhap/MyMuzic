package com.example.mymuzic.di

import com.example.mymuzic.presentation.screen.AuthViewModel
import com.example.mymuzic.presentation.screen.MusicViewModel
import com.example.mymuzic.presentation.screen.PlaySongViewModel
import com.example.mymuzic.presentation.screen.ArtistViewModel
import com.example.mymuzic.presentation.screen.AlbumViewModel
import com.example.mymuzic.presentation.screen.ExploreViewModel
import com.example.mymuzic.presentation.viewmodel.CategoryPlaylistsViewModel
import com.example.mymuzic.presentation.viewmodel.LibraryViewModel
import com.example.mymuzic.presentation.viewmodel.PlaylistDetailViewModel
import com.example.mymuzic.presentation.viewmodel.SearchViewModel
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
            getAuthStateUseCase = get()
        )
    }
    viewModel {
        MusicViewModel(
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
    viewModel {
        ArtistViewModel(
            getArtistDetailUseCase = get(),
            getArtistAlbumsUseCase = get(),
            getArtistTopTracksUseCase = get(),
            getRelatedArtistsUseCase = get()
        )
    }
    viewModel {
        AlbumViewModel(
            getAlbumTracksUseCase = get()
        )
    }
    viewModel { ExploreViewModel(get(), get(),get()) }
    viewModel { CategoryPlaylistsViewModel(get()) }
    viewModel { PlaylistDetailViewModel(get(), get()) }
    viewModel {SearchViewModel(get())}
    viewModel{ LibraryViewModel(get()) }

} 