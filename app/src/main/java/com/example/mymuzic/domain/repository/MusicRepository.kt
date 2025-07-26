package com.example.mymuzic.domain.repository

import com.example.mymuzic.data.model.response.RecentlyPlayedItem
import com.example.mymuzic.data.model.music.SpotifyArtist
import com.example.mymuzic.data.model.music.SpotifyTrack
import com.example.mymuzic.data.model.music.SpotifyAlbum
import com.example.mymuzic.data.model.response.ArtistDetailResponse
import com.example.mymuzic.data.model.response.ArtistAlbumsResponse
import com.example.mymuzic.data.model.response.ArtistTopTracksResponse
import com.example.mymuzic.data.model.response.AlbumTracksResponse
import com.example.mymuzic.data.model.response.CategoriesContent
import com.example.mymuzic.data.model.music.PlaylistDetail
import com.example.mymuzic.data.model.music.SearchResult

interface MusicRepository {
    suspend fun getRecentlyPlayedTracks(limit: Int = 10): Result<List<RecentlyPlayedItem>>
    suspend fun getTopTracks(timeRange: String = "short_term", limit: Int = 10): Result<List<SpotifyTrack>>
    suspend fun getArtistsByIds(ids: List<String>): Result<List<SpotifyArtist>>
    suspend fun getTrackDetail(id: String): Result<SpotifyTrack>
    
    // Artist related methods
    suspend fun getArtistDetail(id: String): Result<ArtistDetailResponse>
    suspend fun getArtistAlbums(id: String, limit: Int = 20, offset: Int = 0): Result<ArtistAlbumsResponse>
    suspend fun getArtistTopTracks(id: String): Result<ArtistTopTracksResponse>
    suspend fun getRelatedArtists(id: String): Result<List<SpotifyArtist>>
    
    // Album related methods
    suspend fun getAlbumTracks(id: String, market: String = "US", limit: Int = 50, offset: Int = 0): Result<AlbumTracksResponse>

    // New methods to replace deprecated featured playlists
    suspend fun getNewReleases(limit: Int = 10, offset: Int = 0, country: String = "US"): Result<com.example.mymuzic.data.model.response.ReleasesResponseContent>
    suspend fun getCategories(limit: Int = 20, offset: Int = 0, country: String = "US"): Result<com.example.mymuzic.data.model.response.CategoriesContent>
    suspend fun getCategoryPlaylists(categoryId: String, limit: Int = 10, offset: Int = 0, country: String = "US"): Result<com.example.mymuzic.data.model.music.FeaturedPlaylistsResponse>
    suspend fun searchPlaylists(query: String, limit: Int = 20, offset: Int = 0): Result<com.example.mymuzic.data.model.music.FeaturedPlaylistsResponse>
    suspend fun getPlaylistDetail(playlistId: String): Result<com.example.mymuzic.data.model.music.PlaylistDetail>

    suspend fun getPlaylistTracks(
        playlistId: String,
        limit: Int,
        offset: Int,
        market: String = "VN"
    ): Result<com.example.mymuzic.data.model.music.PlaylistTracksResponse>

    suspend fun searchSpotify(query: String): SearchResult
} 