package com.example.mymuzic.domain.usecase.music

import com.example.mymuzic.data.model.music.PlaylistDetail
import com.example.mymuzic.domain.repository.MusicRepository

class GetPlaylistDetailUseCase(private val musicRepository: MusicRepository) {
    suspend operator fun invoke(playlistId: String): Result<PlaylistDetail> {
        return musicRepository.getPlaylistDetail(playlistId)
    }
} 