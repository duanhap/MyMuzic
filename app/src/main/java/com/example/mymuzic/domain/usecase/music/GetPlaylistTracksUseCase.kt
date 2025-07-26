package com.example.mymuzic.domain.usecase.music

import com.example.mymuzic.data.model.music.PlaylistTracksResponse
import com.example.mymuzic.domain.repository.MusicRepository

class GetPlaylistTracksUseCase(private val musicRepository: MusicRepository) {
    suspend operator fun invoke(
        playlistId: String,
        limit: Int,
        offset: Int,
        market: String = "VN"
    ): Result<PlaylistTracksResponse> {
        return musicRepository.getPlaylistTracks(playlistId, limit, offset, market)
    }
} 