package com.example.mymuzic.domain.usecase.music

import com.example.mymuzic.data.model.music.FeaturedPlaylistsResponse
import com.example.mymuzic.domain.repository.MusicRepository

class SearchPlaylistsUseCase(private val musicRepository: MusicRepository) {
    suspend operator fun invoke(query: String, limit: Int = 20, offset: Int = 0): Result<FeaturedPlaylistsResponse> {
        return musicRepository.searchPlaylists(query, limit, offset)
    }
} 