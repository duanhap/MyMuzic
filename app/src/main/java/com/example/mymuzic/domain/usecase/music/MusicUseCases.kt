package com.example.mymuzic.domain.usecase.music

import com.example.mymuzic.data.model.response.RecentlyPlayedItem
import com.example.mymuzic.data.model.music.SpotifyArtist
import com.example.mymuzic.data.model.music.SpotifyTrack
import com.example.mymuzic.data.model.response.ArtistDetailResponse
import com.example.mymuzic.data.model.response.ArtistAlbumsResponse
import com.example.mymuzic.data.model.response.ArtistTopTracksResponse
import com.example.mymuzic.data.model.response.AlbumTracksResponse
import com.example.mymuzic.domain.repository.MusicRepository

class GetRecentlyPlayedTracksUseCase(
    private val musicRepository: MusicRepository
) {
    suspend operator fun invoke(limit: Int = 10): Result<List<RecentlyPlayedItem>> {
        return musicRepository.getRecentlyPlayedTracks(limit)
    }
}

class GetTopTracksUseCase(
    private val musicRepository: MusicRepository
) {
    suspend operator fun invoke(timeRange: String = "short_term", limit: Int = 10): Result<List<SpotifyTrack>> {
        return musicRepository.getTopTracks(timeRange, limit)
    }
}

class GetArtistsByIdsUseCase(
    private val musicRepository: MusicRepository
) {
    suspend operator fun invoke(ids: List<String>): Result<List<SpotifyArtist>> {
        return musicRepository.getArtistsByIds(ids)
    }
}

class GetTrackDetailUseCase(
    private val musicRepository: MusicRepository
) {
    suspend operator fun invoke(id: String): Result<SpotifyTrack> {
        return musicRepository.getTrackDetail(id)
    }
}

// Artist related use cases
class GetArtistDetailUseCase(
    private val musicRepository: MusicRepository
) {
    suspend operator fun invoke(id: String): Result<ArtistDetailResponse> {
        return musicRepository.getArtistDetail(id)
    }
}

class GetArtistAlbumsUseCase(
    private val musicRepository: MusicRepository
) {
    suspend operator fun invoke(id: String, limit: Int = 20, offset: Int = 0): Result<ArtistAlbumsResponse> {
        return musicRepository.getArtistAlbums(id, limit, offset)
    }
}

class GetArtistTopTracksUseCase(
    private val musicRepository: MusicRepository
) {
    suspend operator fun invoke(id: String): Result<ArtistTopTracksResponse> {
        return musicRepository.getArtistTopTracks(id)
    }
}

class GetRelatedArtistsUseCase(
    private val musicRepository: MusicRepository
) {
    suspend operator fun invoke(id: String): Result<List<SpotifyArtist>> {
        return musicRepository.getRelatedArtists(id)
    }
}

class GetAlbumTracksUseCase(
    private val musicRepository: MusicRepository
) {
    suspend operator fun invoke(id: String, market: String = "US", limit: Int = 50, offset: Int = 0): Result<AlbumTracksResponse> {
        return musicRepository.getAlbumTracks(id, market, limit, offset)
    }
} 