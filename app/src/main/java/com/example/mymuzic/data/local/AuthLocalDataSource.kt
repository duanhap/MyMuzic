package com.example.mymuzic.data.local

import android.content.Context
import android.content.SharedPreferences
import com.example.mymuzic.data.model.SpotifyUserProfile
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthLocalDataSource(context: Context) {
    
    private val prefs: SharedPreferences = context.getSharedPreferences(
        "spotify_auth_prefs",
        Context.MODE_PRIVATE
    )
    
    private val gson = Gson()
    
    companion object {
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_REFRESH_TOKEN = "refresh_token"
        private const val KEY_TOKEN_EXPIRY = "token_expiry"
        private const val KEY_USER_PROFILE = "user_profile"
        private const val KEY_CODE_VERIFIER = "code_verifier"
    }
    
    suspend fun saveAccessToken(token: String) = withContext(Dispatchers.IO) {
        prefs.edit().putString(KEY_ACCESS_TOKEN, token).apply()
    }
    
    suspend fun getAccessToken(): String? = withContext(Dispatchers.IO) {
        prefs.getString(KEY_ACCESS_TOKEN, null)
    }
    
    suspend fun saveRefreshToken(token: String) = withContext(Dispatchers.IO) {
        prefs.edit().putString(KEY_REFRESH_TOKEN, token).apply()
    }
    
    suspend fun getRefreshToken(): String? = withContext(Dispatchers.IO) {
        prefs.getString(KEY_REFRESH_TOKEN, null)
    }
    
    suspend fun saveTokenExpiry(expiryTime: Long) = withContext(Dispatchers.IO) {
        prefs.edit().putLong(KEY_TOKEN_EXPIRY, expiryTime).apply()
    }
    
    suspend fun getTokenExpiry(): Long = withContext(Dispatchers.IO) {
        prefs.getLong(KEY_TOKEN_EXPIRY, 0L)
    }
    
    suspend fun saveUserProfile(profile: SpotifyUserProfile) = withContext(Dispatchers.IO) {
        val json = gson.toJson(profile)
        prefs.edit().putString(KEY_USER_PROFILE, json).apply()
    }
    
    suspend fun getUserProfile(): SpotifyUserProfile? = withContext(Dispatchers.IO) {
        val json = prefs.getString(KEY_USER_PROFILE, null)
        json?.let { gson.fromJson(it, SpotifyUserProfile::class.java) }
    }
    
    suspend fun saveCodeVerifier(codeVerifier: String) = withContext(Dispatchers.IO) {
        prefs.edit().putString(KEY_CODE_VERIFIER, codeVerifier).apply()
    }
    
    suspend fun getCodeVerifier(): String? = withContext(Dispatchers.IO) {
        prefs.getString(KEY_CODE_VERIFIER, null)
    }
    
    suspend fun clearAuthData() = withContext(Dispatchers.IO) {
        prefs.edit().apply {
            remove(KEY_ACCESS_TOKEN)
            remove(KEY_REFRESH_TOKEN)
            remove(KEY_TOKEN_EXPIRY)
            remove(KEY_USER_PROFILE)
            remove(KEY_CODE_VERIFIER)
        }.apply()
    }
    
    suspend fun isTokenExpired(): Boolean = withContext(Dispatchers.IO) {
        val expiryTime = getTokenExpiry()
        System.currentTimeMillis() >= expiryTime
    }
} 