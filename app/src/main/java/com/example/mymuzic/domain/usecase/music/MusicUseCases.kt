package com.example.mymuzic.domain.usecase.music

import com.example.mymuzic.data.model.response.RecentlyPlayedItem
import com.example.mymuzic.data.model.music.SpotifyArtist
import com.example.mymuzic.data.model.music.SpotifyTrack
import com.example.mymuzic.domain.repository.AuthRepository

class GetRecentlyPlayedTracksUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(limit: Int = 10): Result<List<RecentlyPlayedItem>> {
        return authRepository.getRecentlyPlayedTracks(limit)
    }
}

class GetTopTracksUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(timeRange: String = "short_term", limit: Int = 10): Result<List<SpotifyTrack>> {
        return repository.getTopTracks(timeRange, limit)
    }
}

class GetArtistsByIdsUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(ids: List<String>): Result<List<SpotifyArtist>> {
        return repository.getArtistsByIds(ids)
    }
}

class GetTrackDetailUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(id: String): Result<SpotifyTrack> {
        return repository.getTrackDetail(id)
    }
} 