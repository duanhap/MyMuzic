package com.example.mymuzic.data.remote

import com.example.mymuzic.data.model.response.ArtistsResponse
import com.example.mymuzic.data.model.response.ArtistDetailResponse
import com.example.mymuzic.data.model.response.ArtistAlbumsResponse
import com.example.mymuzic.data.model.response.ArtistTopTracksResponse
import com.example.mymuzic.data.model.response.RelatedArtistsResponse
import com.example.mymuzic.data.model.response.AlbumTracksResponse
import com.example.mymuzic.data.model.auth.SpotifyTokenResponse
import com.example.mymuzic.data.model.auth.SpotifyUserProfile
import com.example.mymuzic.data.model.response.RecentlyPlayedResponse
import com.example.mymuzic.data.model.music.SpotifyArtist
import com.example.mymuzic.data.model.music.SpotifyTrack
import com.example.mymuzic.data.model.response.TopTracksResponse
import com.example.mymuzic.data.model.music.FeaturedPlaylistsResponse
import com.example.mymuzic.data.model.response.NewReleasesResponse
import com.example.mymuzic.data.model.response.CategoriesResponse
import com.example.mymuzic.data.model.music.PlaylistDetail
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
    
    @GET("v1/tracks/{id}")
    suspend fun getTrackDetail(
        @Header("Authorization") authHeader: String,
        @Path("id") id: String
    ): SpotifyTrack

    // Artist Detail
    @GET("v1/artists/{id}")
    suspend fun getArtistDetail(
        @Header("Authorization") authHeader: String,
        @Path("id") id: String
    ): ArtistDetailResponse

    // Artist Albums
    @GET("v1/artists/{id}/albums")
    suspend fun getArtistAlbums(
        @Header("Authorization") authHeader: String,
        @Path("id") id: String,
        @Query("include_groups") includeGroups: String = "album,single,compilation",
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0
    ): ArtistAlbumsResponse

    // Artist Top Tracks
    @GET("v1/artists/{id}/top-tracks")
    suspend fun getArtistTopTracks(
        @Header("Authorization") authHeader: String,
        @Path("id") id: String,
        @Query("market") market: String = "US"
    ): ArtistTopTracksResponse

    // Related Artists
    @GET("v1/artists/{id}/related-artists")
    suspend fun getRelatedArtists(
        @Header("Authorization") authHeader: String,
        @Path("id") id: String
    ): List<SpotifyArtist>

    // Album Tracks
    @GET("v1/albums/{id}/tracks")
    suspend fun getAlbumTracks(
        @Header("Authorization") authHeader: String,
        @Path("id") id: String,
        @Query("market") market: String = "US",
        @Query("limit") limit: Int = 50,
        @Query("offset") offset: Int = 0
    ): AlbumTracksResponse

    @GET("v1/browse/new-releases")
    suspend fun getNewReleases(
        @Header("Authorization") authHeader: String,
        @Query("limit") limit: Int = 10,
        @Query("offset") offset: Int = 0,
        @Query("country") country: String = "US"
    ): NewReleasesResponse

    @GET("v1/browse/categories")
    suspend fun getCategories(
        @Header("Authorization") authHeader: String,
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0,
        @Query("country") country: String = "US"
    ): CategoriesResponse

    @GET("v1/browse/categories/{category_id}/playlists")
    suspend fun getCategoryPlaylists(
        @Header("Authorization") authHeader: String,
        @Path("category_id") categoryId: String,
        @Query("limit") limit: Int = 10,
        @Query("offset") offset: Int = 0,
        @Query("country") country: String = "US"
    ): FeaturedPlaylistsResponse

    @GET("v1/search")
    suspend fun searchPlaylists(
        @Header("Authorization") authHeader: String,
        @Query("q") query: String,
        @Query("type") type: String = "playlist",
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0
    ): FeaturedPlaylistsResponse

    @GET("v1/search")
    suspend fun searchSpotifyMulti(
        @Header("Authorization") authHeader: String,
        @Query("q") query: String,
        @Query("type") type: String = "track,artist,album,playlist",
        @Query("limit") limit: Int = 10,
        @Query("market") market: String = "VN"
    ): com.example.mymuzic.data.model.response.SearchMultiResponse

    @GET("v1/playlists/{playlist_id}")
    suspend fun getPlaylistDetail(
        @Header("Authorization") authHeader: String,
        @Path("playlist_id") playlistId: String
    ): PlaylistDetail

    @GET("v1/playlists/{playlist_id}/tracks")
    suspend fun getPlaylistTracks(
        @Header("Authorization") authHeader: String,
        @Path("playlist_id") playlistId: String,
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0,
        @Query("market") market: String = "VN"
    ): com.example.mymuzic.data.model.music.PlaylistTracksResponse
} 