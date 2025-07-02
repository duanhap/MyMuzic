package com.example.mymuzic.data.remote

import com.example.mymuzic.data.model.ArtistsResponse
import com.example.mymuzic.data.model.SpotifyTokenResponse
import com.example.mymuzic.data.model.SpotifyUserProfile
import com.example.mymuzic.data.model.RecentlyPlayedResponse
import com.example.mymuzic.data.model.SpotifyArtist
import com.example.mymuzic.data.model.TopTracksResponse
import retrofit2.http.*

interface SpotifyAuthApi {
    
    @FormUrlEncoded
    @POST("https://accounts.spotify.com/api/token")
    suspend fun exchangeCodeForToken(
        @Field("client_id") clientId: String,
        @Field("grant_type") grantType: String = "authorization_code",
        @Field("code") code: String,
        @Field("redirect_uri") redirectUri: String,
        @Field("code_verifier") codeVerifier: String
    ): SpotifyTokenResponse
    
    @FormUrlEncoded
    @POST("https://accounts.spotify.com/api/token")
    suspend fun refreshToken(
        @Field("client_id") clientId: String,
        @Field("grant_type") grantType: String = "refresh_token",
        @Field("refresh_token") refreshToken: String
    ): SpotifyTokenResponse
    
    @GET("https://api.spotify.com/v1/me")
    suspend fun getUserProfile(
        @Header("Authorization") authorization: String
    ): SpotifyUserProfile

    @GET("v1/me/player/recently-played")
    suspend fun getRecentlyPlayed(
        @Header("Authorization") authorization: String,
        @Query("limit") limit: Int = 10
    ): RecentlyPlayedResponse

    @GET("v1/me/top/tracks")
    suspend fun getTopTracks(
        @Header("Authorization") authHeader: String,
        @Query("time_range") timeRange: String = "short_term",
        @Query("limit") limit: Int = 10
    ): TopTracksResponse
    @GET("v1/artists")
    suspend fun getArtists(
        @Header("Authorization") authHeader: String,
        @Query("ids") ids: String
    ): ArtistsResponse
} 