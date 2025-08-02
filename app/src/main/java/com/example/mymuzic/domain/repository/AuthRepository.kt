package com.example.mymuzic.domain.repository

import com.example.mymuzic.data.model.auth.AuthState
import com.example.mymuzic.data.model.auth.SpotifyTokenResponse
import com.example.mymuzic.data.model.auth.SpotifyUserProfile
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun generateAuthUrl(): String
    suspend fun handleAuthCallback(uri: String): Result<SpotifyTokenResponse>
    suspend fun refreshAccessToken(): Result<SpotifyTokenResponse>
    suspend fun getUserProfile(): Result<SpotifyUserProfile>
    suspend fun getValidAccessToken(): String?
    suspend fun isAuthenticated(): Boolean
    suspend fun logout()
    fun getAuthState(): Flow<AuthState>
} 