package com.example.mymuzic.domain.usecase.music

import com.example.mymuzic.data.model.music.FeaturedPlaylistsResponse
import com.example.mymuzic.domain.repository.MusicRepository

class GetCategoryPlaylistsUseCase(private val musicRepository: MusicRepository) {
    suspend operator fun invoke(categoryId: String, limit: Int = 20, offset: Int = 0, country: String = "US"): Result<FeaturedPlaylistsResponse> {
        return musicRepository.getCategoryPlaylists(categoryId, limit, offset, country)
    }
} 