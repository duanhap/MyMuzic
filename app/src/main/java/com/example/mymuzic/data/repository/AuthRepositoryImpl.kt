package com.example.mymuzic.data.repository

import com.example.mymuzic.data.local.AuthLocalDataSource
import com.example.mymuzic.data.model.auth.AuthState
import com.example.mymuzic.data.model.response.RecentlyPlayedItem
import com.example.mymuzic.data.model.music.SpotifyArtist
import com.example.mymuzic.data.model.auth.SpotifyTokenResponse
import com.example.mymuzic.data.model.music.SpotifyTrack
import com.example.mymuzic.data.model.auth.SpotifyUserProfile
import com.example.mymuzic.data.remote.SpotifyAuthApi
import com.example.mymuzic.domain.repository.AuthRepository
import com.example.mymuzic.utils.PKCEUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

class AuthRepositoryImpl(
    private val authApi: SpotifyAuthApi,
    private val localDataSource: AuthLocalDataSource,
    private val clientId: String,
    private val redirectUri: String
) : AuthRepository {
    
    override fun generateAuthUrl(): String {
        val codeVerifier = PKCEUtils.generateCodeVerifier()
        val codeChallenge = PKCEUtils.generateCodeChallenge(codeVerifier)
        
        // Lưu code_verifier để sử dụng sau
        kotlinx.coroutines.runBlocking {
            localDataSource.saveCodeVerifier(codeVerifier)
        }
        
        return PKCEUtils.createAuthorizationUrl(
            clientId = clientId,
            redirectUri = redirectUri,
            codeChallenge = codeChallenge
        )
    }
    
    override suspend fun handleAuthCallback(uri: String): Result<SpotifyTokenResponse> {
        return try {
            val code = extractCodeFromUri(uri)
            val codeVerifier = localDataSource.getCodeVerifier()
            
            if (code == null || codeVerifier == null) {
                return Result.failure(Exception("Invalid authorization code or code verifier"))
            }
            
            val tokenResponse = authApi.exchangeCodeForToken(
                clientId = clientId,
                code = code,
                redirectUri = redirectUri,
                codeVerifier = codeVerifier
            )
            
            // Lưu token và thông tin
            saveTokenData(tokenResponse)
            
            Result.success(tokenResponse)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun refreshAccessToken(): Result<SpotifyTokenResponse> {
        return try {
            val refreshToken = localDataSource.getRefreshToken()
            if (refreshToken == null) {
                return Result.failure(Exception("No refresh token available"))
            }
            
            val tokenResponse = authApi.refreshToken(
                clientId = clientId,
                refreshToken = refreshToken
            )
            
            // Lưu token mới
            saveTokenData(tokenResponse)
            
            Result.success(tokenResponse)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getUserProfile(): Result<SpotifyUserProfile> {
        return try {
            val accessToken = getValidAccessToken() ?: throw Exception("No valid access token")
            
            val profile = authApi.getUserProfile("Bearer $accessToken")
            
            // Lưu profile
            localDataSource.saveUserProfile(profile)
            
            Result.success(profile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getValidAccessToken(): String? {
        val accessToken = localDataSource.getAccessToken()
        if (accessToken == null) return null
        
        // Kiểm tra token có hết hạn không
        if (localDataSource.isTokenExpired()) {
            // Thử refresh token
            val refreshResult = refreshAccessToken()
            return if (refreshResult.isSuccess) {
                refreshResult.getOrNull()?.accessToken
            } else {
                null
            }
        }
        
        return accessToken
    }
    
    override suspend fun isAuthenticated(): Boolean {
        return getValidAccessToken() != null
    }
    
    override suspend fun logout() {
        localDataSource.clearAuthData()
    }
    
    override fun getAuthState(): Flow<AuthState> = flow {
        val isAuthenticated = isAuthenticated()
        val accessToken = localDataSource.getAccessToken()
        val refreshToken = localDataSource.getRefreshToken()
        val userProfile = localDataSource.getUserProfile()
        val codeVerifier = localDataSource.getCodeVerifier()
        
        emit(
            AuthState(
                codeVerifier = codeVerifier ?: "",
                isAuthenticated = isAuthenticated,
                accessToken = accessToken,
                refreshToken = refreshToken,
                userProfile = userProfile
            )
        )
    }
    
    override suspend fun getRecentlyPlayedTracks(limit: Int): Result<List<RecentlyPlayedItem>> {
        return try {
            val accessToken = getValidAccessToken() ?: throw Exception("No valid access token")
            val response = authApi.getRecentlyPlayed("Bearer $accessToken", limit)
            Result.success(response.items)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getTopTracks(timeRange: String, limit: Int): Result<List<SpotifyTrack>> {
        return try {
            val accessToken = getValidAccessToken() ?: throw Exception("No valid access token")
            val response = authApi.getTopTracks("Bearer $accessToken", timeRange, limit)
            Result.success(response.items)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getArtistsByIds(ids: List<String>): Result<List<SpotifyArtist>> {
        return try {
            val accessToken = getValidAccessToken() ?: throw Exception("No valid access token")
            val idsParam = ids.joinToString(",")
            val response = authApi.getArtists("Bearer $accessToken", idsParam)
            Result.success(response.artists)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getTrackDetail(id: String): Result<SpotifyTrack> {
        return try {
            val accessToken = getValidAccessToken() ?: throw Exception("No valid access token")
            val response = authApi.getTrackDetail("Bearer $accessToken", id)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun saveTokenData(tokenResponse: SpotifyTokenResponse) {
        localDataSource.saveAccessToken(tokenResponse.accessToken)
        tokenResponse.refreshToken?.let { localDataSource.saveRefreshToken(it) }
        
        // Tính thời gian hết hạn
        val expiryTime = System.currentTimeMillis() + (tokenResponse.expiresIn * 1000L)
        localDataSource.saveTokenExpiry(expiryTime)
    }
    
    private fun extractCodeFromUri(uri: String): String? {
        return try {
            val decodedUri = URLDecoder.decode(uri, StandardCharsets.UTF_8.name())
            val codeIndex = decodedUri.indexOf("code=")
            if (codeIndex != -1) {
                val codeStart = codeIndex + 5
                val codeEnd = decodedUri.indexOf("&", codeStart)
                if (codeEnd != -1) {
                    decodedUri.substring(codeStart, codeEnd)
                } else {
                    decodedUri.substring(codeStart)
                }
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
} 