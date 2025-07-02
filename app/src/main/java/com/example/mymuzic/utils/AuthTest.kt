package com.example.mymuzic.utils

import android.util.Log

object AuthTest {
    
    fun testPKCEFlow() {
        // Test tạo code_verifier
        val codeVerifier = PKCEUtils.generateCodeVerifier()
        Log.d("AuthTest", "Code Verifier: $codeVerifier")
        
        // Test tạo code_challenge
        val codeChallenge = PKCEUtils.generateCodeChallenge(codeVerifier)
        Log.d("AuthTest", "Code Challenge: $codeChallenge")
        
        // Test tạo authorization URL
        val authUrl = PKCEUtils.createAuthorizationUrl(
            clientId = "c227a0aaa5d54c4881c1ad98e8dad3ec",
            redirectUri = "com.example.mymuzic://callback",
            codeChallenge = codeChallenge
        )
        Log.d("AuthTest", "Authorization URL: $authUrl")
        
        // Test URL parsing
        val testCallbackUrl = "com.example.mymuzic://callback?code=test_code_123&state=test_state"
        val code = extractCodeFromCallback(testCallbackUrl)
        Log.d("AuthTest", "Extracted Code: $code")
    }
    
    private fun extractCodeFromCallback(uri: String): String? {
        return try {
            val codeIndex = uri.indexOf("code=")
            if (codeIndex != -1) {
                val codeStart = codeIndex + 5
                val codeEnd = uri.indexOf("&", codeStart)
                if (codeEnd != -1) {
                    uri.substring(codeStart, codeEnd)
                } else {
                    uri.substring(codeStart)
                }
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
} 