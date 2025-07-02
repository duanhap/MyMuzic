package com.example.mymuzic.domain.repository

import com.example.mymuzic.data.model.AuthState
import com.example.mymuzic.data.model.SpotifyTokenResponse
import com.example.mymuzic.data.model.SpotifyUserProfile
import com.example.mymuzic.data.model.RecentlyPlayedItem
import com.example.mymuzic.data.model.SpotifyArtist
import com.example.mymuzic.data.model.SpotifyTrack
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
    suspend fun getRecentlyPlayedTracks(limit: Int = 10): Result<List<RecentlyPlayedItem>>
    suspend fun getTopTracks(timeRange: String = "short_term", limit: Int = 10): Result<List<SpotifyTrack>>
    suspend fun getArtistsByIds(ids: List<String>): Result<List<SpotifyArtist>>

} 