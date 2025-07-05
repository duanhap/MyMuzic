package com.example.mymuzic.domain.usecase.auth

import com.example.mymuzic.data.model.auth.AuthState
import com.example.mymuzic.data.model.auth.SpotifyTokenResponse
import com.example.mymuzic.data.model.auth.SpotifyUserProfile
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