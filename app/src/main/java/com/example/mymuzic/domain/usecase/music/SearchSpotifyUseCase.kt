package com.example.mymuzic.domain.usecase.music

import com.example.mymuzic.data.model.music.SpotifyTrack
import com.example.mymuzic.data.model.music.SpotifyArtist
import com.example.mymuzic.data.model.music.SpotifyAlbum
import com.example.mymuzic.data.model.music.PlaylistItem
import com.example.mymuzic.data.model.music.SearchResult
import com.example.mymuzic.domain.repository.MusicRepository
import com.google.gson.annotations.SerializedName



class SearchSpotifyUseCase(private val musicRepository: MusicRepository) {
    suspend operator fun invoke(query: String): Result<SearchResult> {
        return try {
            val result = musicRepository.searchSpotify(query)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
} 