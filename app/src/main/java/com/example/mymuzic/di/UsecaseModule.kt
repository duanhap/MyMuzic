package com.example.mymuzic.di

import com.example.mymuzic.domain.usecase.auth.*
import com.example.mymuzic.domain.usecase.music.*
import com.example.mymuzic.domain.usecase.player.*
import com.example.mymuzic.domain.repository.AuthRepository
import com.example.mymuzic.domain.repository.MusicRepository
import org.koin.dsl.module

val UsecaseModule = module {
    // Auth use cases
    single { GenerateAuthUrlUseCase(get<AuthRepository>()) }
    single { HandleAuthCallbackUseCase(get<AuthRepository>()) }
    single { RefreshTokenUseCase(get<AuthRepository>()) }
    single { GetUserProfileUseCase(get<AuthRepository>()) }
    single { GetValidAccessTokenUseCase(get<AuthRepository>()) }
    single { IsAuthenticatedUseCase(get<AuthRepository>()) }
    single { LogoutUseCase(get<AuthRepository>()) }
    single { GetAuthStateUseCase(get<AuthRepository>()) }
    
    // Music use cases
    single { GetRecentlyPlayedTracksUseCase(get<MusicRepository>()) }
    single { GetTopTracksUseCase(get<MusicRepository>()) }
    single { GetArtistsByIdsUseCase(get<MusicRepository>()) }
    single { GetTrackDetailUseCase(get<MusicRepository>()) }

    single { GetNewReleasesUseCase(get()) }
    single { GetCategoriesUseCase(get()) }
    single { GetCategoryPlaylistsUseCase(get()) }
    single { SearchPlaylistsUseCase(get()) }
    
    // Artist use cases
    single { GetArtistDetailUseCase(get()) }
    single { GetArtistAlbumsUseCase(get()) }
    single { GetArtistTopTracksUseCase(get()) }
    single { GetRelatedArtistsUseCase(get()) }
    
    // Album use cases
    single { GetAlbumTracksUseCase(get()) }
    
    // Player use cases
    single { PlaySpotifyTrackUseCase(get()) }

    // Playlist use cases
    single { GetPlaylistDetailUseCase(get()) }
    single { GetPlaylistTracksUseCase(get()) }
    single { SearchSpotifyUseCase(get()) }


} 