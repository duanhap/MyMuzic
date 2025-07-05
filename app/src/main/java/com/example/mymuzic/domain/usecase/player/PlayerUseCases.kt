package com.example.mymuzic.domain.usecase.player

import com.example.mymuzic.domain.repository.SpotifyRemoteRepository

class PlaySpotifyTrackUseCase(
    private val repo: SpotifyRemoteRepository
) {
    operator fun invoke(uri: String) = repo.play(uri)
} 