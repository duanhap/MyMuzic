package com.example.mymuzic.utils

import android.util.Base64
import java.security.MessageDigest
import java.security.SecureRandom

object PKCEUtils {
    
    /**
     * Tạo code_verifier ngẫu nhiên theo chuẩn PKCE
     */
    fun generateCodeVerifier(): String {
        val secureRandom = SecureRandom()
        val codeVerifier = ByteArray(32)
        secureRandom.nextBytes(codeVerifier)
        return Base64.encodeToString(codeVerifier, Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP)
    }
    
    /**
     * Tạo code_challenge từ code_verifier bằng SHA-256
     */
    fun generateCodeChallenge(codeVerifier: String): String {
        val bytes = codeVerifier.toByteArray(charset("US-ASCII"))
        val messageDigest = MessageDigest.getInstance("SHA-256")
        messageDigest.update(bytes)
        val digest = messageDigest.digest()
        return Base64.encodeToString(digest, Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP)
    }
    
    /**
     * Tạo URL authorization cho Spotify
     */
    fun createAuthorizationUrl(
        clientId: String,
        redirectUri: String,
        codeChallenge: String,
        scopes: List<String> = listOf(
            "user-read-private",
            "user-read-email",
            "user-read-recently-played",
            "user-top-read",
            "user-read-playback-state",
            "user-modify-playback-state",
            "playlist-read-private",
            "playlist-read-collaborative",
            "user-library-read"
        )
    ): String {
        val scopeString = scopes.joinToString(" ")
        return "https://accounts.spotify.com/authorize?" +
                "client_id=$clientId&" +
                "response_type=code&" +
                "redirect_uri=$redirectUri&" +
                "code_challenge_method=S256&" +
                "code_challenge=$codeChallenge&" +
                "scope=$scopeString"
    }
} 