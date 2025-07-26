package com.example.mymuzic.data.model.response

import com.example.mymuzic.data.model.music.FeaturedPlaylistsResponse
import com.example.mymuzic.data.model.music.PlaylistItem
import com.example.mymuzic.data.model.music.SpotifyArtist
import com.example.mymuzic.data.model.music.SpotifyTrack
import com.example.mymuzic.data.model.music.SpotifyAlbum
import com.google.gson.annotations.SerializedName

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

data class ArtistsResponse2(
    @SerializedName("items")
    val items: List<SpotifyArtist>
)
data class ArtistsResponse(
    @SerializedName("artists")
    val artists: List<SpotifyArtist>
)


// Artist Detail Response
data class ArtistDetailResponse(
    @SerializedName("external_urls")
    val externalUrls: ExternalUrls,
    @SerializedName("followers")
    val followers: Followers,
    @SerializedName("genres")
    val genres: List<String>,
    @SerializedName("href")
    val href: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("images")
    val images: List<SpotifyImage>,
    @SerializedName("name")
    val name: String,
    @SerializedName("popularity")
    val popularity: Int,
    @SerializedName("type")
    val type: String,
    @SerializedName("uri")
    val uri: String
)

// Artist Albums Response
data class ArtistAlbumsResponse(
    @SerializedName("href")
    val href: String,
    @SerializedName("limit")
    val limit: Int,
    @SerializedName("next")
    val next: String?,
    @SerializedName("offset")
    val offset: Int,
    @SerializedName("previous")
    val previous: String?,
    @SerializedName("total")
    val total: Int,
    @SerializedName("items")
    val items: List<SpotifyAlbum>
)

// Artist Top Tracks Response
data class ArtistTopTracksResponse(
    @SerializedName("tracks")
    val tracks: List<SpotifyTrack>
)

// Related Artists Response
data class RelatedArtistsResponse(
    val artists: List<SpotifyArtist>
)

// Album Tracks Response
data class AlbumTracksResponse(
    @SerializedName("href")
    val href: String,
    @SerializedName("limit")
    val limit: Int,
    @SerializedName("next")
    val next: String?,
    @SerializedName("offset")
    val offset: Int,
    @SerializedName("previous")
    val previous: String?,
    @SerializedName("total")
    val total: Int,
    @SerializedName("items")
    val items: List<SpotifyTrack>
)
data class AlbumResponse(
    @SerializedName("href")
    val href: String,
    @SerializedName("limit")
    val limit: Int,
    @SerializedName("next")
    val next: String?,
    @SerializedName("offset")
    val offset: Int,
    @SerializedName("previous")
    val previous: String?,
    @SerializedName("total")
    val total: Int,
    @SerializedName("items")
    val items: List<SpotifyAlbum>
)
data class PlaylistResponse(
    @SerializedName("href")
    val href: String,
    @SerializedName("limit")
    val limit: Int,
    @SerializedName("next")
    val next: String?,
    @SerializedName("offset")
    val offset: Int,
    @SerializedName("previous")
    val previous: String?,
    @SerializedName("total")
    val total: Int,
    @SerializedName("items")
    val items: List<PlaylistItem>
)

// Shared models
data class ExternalUrls(
    @SerializedName("spotify")
    val spotify: String
)

data class Followers(
    @SerializedName("href")
    val href: String?,
    @SerializedName("total")
    val total: Int
)

data class SpotifyImage(
    @SerializedName("url")
    val url: String,
    @SerializedName("height")
    val height: Int?,
    @SerializedName("width")
    val width: Int?
)

// New Releases Response
data class NewReleasesResponse(
    @SerializedName("albums")
    val albums: ReleasesResponseContent,
)
data class ReleasesResponseContent(
    @SerializedName("href")
    val href: String,
    @SerializedName("limit")
    val limit: Int,
    @SerializedName("next")
    val next: String?,
    @SerializedName("offset")
    val offset: Int,
    @SerializedName("previous")
    val previous: String?,
    @SerializedName("total")
    val total: Int,
    @SerializedName("items")
    val items: List<SpotifyAlbum>
)



// Categories Response
data class CategoriesResponse(
    @SerializedName("categories")
    val categories: CategoriesContent
)

data class CategoriesContent(
    @SerializedName("href")
    val href: String,
    @SerializedName("limit")
    val limit: Int,
    @SerializedName("next")
    val next: String?,
    @SerializedName("offset")
    val offset: Int,
    @SerializedName("previous")
    val previous: String?,
    @SerializedName("total")
    val total: Int,
    @SerializedName("items")
    val items: List<com.example.mymuzic.data.model.music.SpotifyCategory>
)

data class CategoryItem(
    @SerializedName("href")
    val href: String,
    @SerializedName("icons")
    val icons: List<SpotifyImage>,
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String
)

data class SearchMultiResponse(
    @SerializedName("tracks")
    val tracks: TopTracksResponse? = null,
    @SerializedName("artists")
    val artists: ArtistsResponse2? = null,
    @SerializedName("albums")
    val albums: AlbumResponse? = null,
    @SerializedName("playlists")
    val playlists: PlaylistResponse? = null
) 