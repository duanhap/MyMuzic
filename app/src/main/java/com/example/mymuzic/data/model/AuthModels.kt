package com.example.mymuzic.data.model

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

data class RecentlyPlayedResponse(
    @SerializedName("items")
    val items: List<RecentlyPlayedItem>
)

data class RecentlyPlayedItem(
    @SerializedName("track")
    val track: SpotifyTrack,
    @SerializedName("played_at")
    val playedAt: String
)
data class TopTracksResponse(
    @SerializedName("items")
    val items: List<SpotifyTrack>
)

data class SpotifyTrack(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("album")
    val album: SpotifyAlbum,
    @SerializedName("artists")
    val artists: List<SpotifyArtist>,
    @SerializedName("duration_ms")
    val duration_ms: Int,
    @SerializedName("external_urls")
    val external_urls: Map<String, String>,
    @SerializedName("uri")
    val uri: String
)

data class SpotifyAlbum(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("images")
    val images: List<SpotifyImage>?
)
data class ArtistsResponse(
    @SerializedName("artists")
    val artists: List<SpotifyArtist>
)
data class SpotifyArtist(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("external_urls")
    val externalUrls: ExternalUrls? = null,
    @SerializedName("followers")
    val followers: Followers? = null,
    @SerializedName("genres")
    val genres: List<String>? = null,
    @SerializedName("href")
    val href: String? = null,
    @SerializedName("images")
    val images: List<SpotifyImage>? = null,
    @SerializedName("popularity")
    val popularity: Int? = null,
    @SerializedName("type")
    val type: String? = null,
    @SerializedName("uri")
    val uri: String? = null
)
