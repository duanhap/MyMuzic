package com.example.mymuzic.domain.usecase

import com.example.mymuzic.data.model.AuthState
import com.example.mymuzic.data.model.RecentlyPlayedItem
import com.example.mymuzic.data.model.SpotifyArtist
import com.example.mymuzic.data.model.SpotifyTokenResponse
import com.example.mymuzic.data.model.SpotifyTrack
import com.example.mymuzic.data.model.SpotifyUserProfile
import com.example.mymuzic.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class GenerateAuthUrlUseCase(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): String {
        return authRepository.generateAuthUrl()
    }
}

class HandleAuthCallbackUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(uri: String): Result<SpotifyTokenResponse> {
        return authRepository.handleAuthCallback(uri)
    }
}

class RefreshTokenUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<SpotifyTokenResponse> {
        return authRepository.refreshAccessToken()
    }
}

class GetUserProfileUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Result<SpotifyUserProfile> {
        return authRepository.getUserProfile()
    }
}

class GetValidAccessTokenUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): String? {
        return authRepository.getValidAccessToken()
    }
}

class IsAuthenticatedUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Boolean {
        return authRepository.isAuthenticated()
    }
}

class LogoutUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke() {
        authRepository.logout()
    }
}

class GetAuthStateUseCase(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): Flow<AuthState> {
        return authRepository.getAuthState()
    }
}

class GetRecentlyPlayedTracksUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(limit: Int = 10): Result<List<RecentlyPlayedItem>> {
        return authRepository.getRecentlyPlayedTracks(limit)
    }
}

class GetTopTracksUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(timeRange: String = "short_term", limit: Int = 10):Result<List<SpotifyTrack>> {
        return repository.getTopTracks(timeRange, limit)
    }
}
class GetArtistsByIdsUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(ids: List<String>): Result<List<SpotifyArtist>> {
        return repository.getArtistsByIds(ids)
    }
}

