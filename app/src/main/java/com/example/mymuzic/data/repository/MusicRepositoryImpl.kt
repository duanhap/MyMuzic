package com.example.mymuzic.data.repository

import com.example.mymuzic.data.model.response.RecentlyPlayedItem
import com.example.mymuzic.data.model.music.SpotifyArtist
import com.example.mymuzic.data.model.music.SpotifyTrack
import com.example.mymuzic.data.model.response.ArtistDetailResponse
import com.example.mymuzic.data.model.response.ArtistAlbumsResponse
import com.example.mymuzic.data.model.response.ArtistTopTracksResponse
import com.example.mymuzic.data.model.response.AlbumTracksResponse
import com.example.mymuzic.data.remote.SpotifyAuthApi
import com.example.mymuzic.domain.repository.MusicRepository
import com.example.mymuzic.data.local.AuthLocalDataSource
import com.example.mymuzic.data.model.music.SearchResult

class MusicRepositoryImpl(
    private val authApi: SpotifyAuthApi,
    private val localDataSource: AuthLocalDataSource
) : MusicRepository {
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

    // Artist related implementations
    override suspend fun getArtistDetail(id: String): Result<ArtistDetailResponse> {
        return try {
            val accessToken = getValidAccessToken() ?: throw Exception("No valid access token")
            val response = authApi.getArtistDetail("Bearer $accessToken", id)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getArtistAlbums(id: String, limit: Int, offset: Int): Result<ArtistAlbumsResponse> {
        return try {
            val accessToken = getValidAccessToken() ?: throw Exception("No valid access token")
            val response = authApi.getArtistAlbums("Bearer $accessToken", id, limit = limit, offset = offset)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getArtistTopTracks(id: String): Result<ArtistTopTracksResponse> {
        return try {
            val accessToken = getValidAccessToken() ?: throw Exception("No valid access token")
            val response = authApi.getArtistTopTracks("Bearer $accessToken", id)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getRelatedArtists(id: String): Result<List<SpotifyArtist>> {
        return try {
            val accessToken = getValidAccessToken() ?: throw Exception("No valid access token")
            val response = authApi.getRelatedArtists("Bearer $accessToken", id)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getAlbumTracks(id: String, market: String, limit: Int, offset: Int): Result<AlbumTracksResponse> {
        return try {
            val accessToken = getValidAccessToken() ?: throw Exception("No valid access token")
            val response = authApi.getAlbumTracks("Bearer $accessToken", id, market, limit, offset)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getNewReleases(limit: Int, offset: Int, country: String): Result<com.example.mymuzic.data.model.response.ReleasesResponseContent> {
        return try {
            val accessToken = getValidAccessToken() ?: throw Exception("No valid access token")
            val response = authApi.getNewReleases("Bearer $accessToken", limit, offset, country)
            Result.success(response.albums)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getCategories(limit: Int, offset: Int, country: String): Result<com.example.mymuzic.data.model.response.CategoriesContent> {
        return try {
            val accessToken = getValidAccessToken() ?: throw Exception("No valid access token")
            val response = authApi.getCategories("Bearer $accessToken", limit, offset, country)
            Result.success(response.categories)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getCategoryPlaylists(categoryId: String, limit: Int, offset: Int, country: String): Result<com.example.mymuzic.data.model.music.FeaturedPlaylistsResponse> {
        return try {
            val accessToken = getValidAccessToken() ?: throw Exception("No valid access token")
            val response = authApi.getCategoryPlaylists("Bearer $accessToken", categoryId, limit, offset, country)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun searchPlaylists(query: String, limit: Int, offset: Int): Result<com.example.mymuzic.data.model.music.FeaturedPlaylistsResponse> {
        return try {
            val accessToken = getValidAccessToken() ?: throw Exception("No valid access token")
            val response = authApi.searchPlaylists("Bearer $accessToken", query, "playlist", limit, offset)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getPlaylistDetail(playlistId: String): Result<com.example.mymuzic.data.model.music.PlaylistDetail> {
        return try {
            val accessToken = getValidAccessToken() ?: throw Exception("No valid access token")
            val response = authApi.getPlaylistDetail("Bearer $accessToken", playlistId)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getPlaylistTracks(
        playlistId: String,
        limit: Int,
        offset: Int,
        market: String
    ): Result<com.example.mymuzic.data.model.music.PlaylistTracksResponse> {
        return try {
            val accessToken = getValidAccessToken() ?: throw Exception("No valid access token")
            val response = authApi.getPlaylistTracks("Bearer $accessToken", playlistId, limit, offset, market)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun searchSpotify(query: String): SearchResult {
        val token = getValidAccessToken() ?: throw Exception(" No access token")
        val response = authApi.searchSpotifyMulti(
            authHeader = "Bearer $token",
            query = query,
            type = "artist,album,playlist,track",
            limit = 10,
            market = "VN"
        )
        return SearchResult(
            tracks = response.tracks?.items ?: emptyList(),
            artists = response.artists?.items ?: emptyList(),
            albums = response.albums?.items ?: emptyList(),
            playlists = response.playlists?.items?.filterNotNull() ?: emptyList()

        )
    }

    private suspend fun getValidAccessToken(): String? {
        val accessToken = localDataSource.getAccessToken()
        if (accessToken == null) return null
        if (localDataSource.isTokenExpired()) {
            // Không refresh ở đây, chỉ trả null nếu hết hạn
            return null
        }
        return accessToken
    }
} 