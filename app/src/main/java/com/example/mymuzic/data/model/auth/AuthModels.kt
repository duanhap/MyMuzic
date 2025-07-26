package com.example.mymuzic.data.model.auth

import com.google.gson.annotations.SerializedName

data class SpotifyTokenResponse(
    @SerializedName("access_token")
    val accessToken: String,
    
    @SerializedName("token_type")
    val tokenType: String,
    
    @SerializedName("expires_in")
    val expiresIn: Int,
    
    @SerializedName("refresh_token")
    val refreshToken: String?,
    
    @SerializedName("scope")
    val scope: String
)

data class SpotifyUserProfile(
    @SerializedName("id")
    val id: String,
    
    @SerializedName("display_name")
    val displayName: String,
    
    @SerializedName("email")
    val email: String?,
    
    @SerializedName("images")
    val images: List<SpotifyImage>?,
    
    @SerializedName("country")
    val country: String?,
    
    @SerializedName("product")
    val product: String?,
    
    @SerializedName("explicit_content")
    val explicitContent: ExplicitContent?,
    
    @SerializedName("external_urls")
    val externalUrls: ExternalUrls?,
    
    @SerializedName("followers")
    val followers: Followers?,
    
    @SerializedName("href")
    val href: String?,
    
    @SerializedName("type")
    val type: String?,
    
    @SerializedName("uri")
    val uri: String?
)

data class SpotifyImage(
    @SerializedName("url")
    val url: String,
    
    @SerializedName("height")
    val height: Int?,
    
    @SerializedName("width")
    val width: Int?
)

data class ExplicitContent(
    @SerializedName("filter_enabled")
    val filterEnabled: Boolean,
    
    @SerializedName("filter_locked")
    val filterLocked: Boolean
)

data class ExternalUrls(
    @SerializedName("spotify")
    val spotify: String?
)

data class Followers(
    @SerializedName("href")
    val href: String?,
    
    @SerializedName("total")
    val total: Int
)

data class AuthState(
    val codeVerifier: String,
    val isAuthenticated: Boolean = false,
    val accessToken: String? = null,
    val refreshToken: String? = null,
    val userProfile: SpotifyUserProfile? = null
) 