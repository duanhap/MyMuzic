package com.example.mymuzic.data.model.music

import com.google.gson.annotations.SerializedName
// Model SearchResult tổng hợp
data class SearchResult(
    val tracks: List<SpotifyTrack> = emptyList(),
    val artists: List<SpotifyArtist> = emptyList(),
    val albums: List<SpotifyAlbum> = emptyList(),
    val playlists: List<PlaylistItem> = emptyList()
)

data class SpotifyTrack(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("album")
    val album: SpotifyAlbum?,
    @SerializedName("artists")
    val artists: List<SpotifyArtist>,
    @SerializedName("duration_ms")
    val duration_ms: Int,
    @SerializedName("external_urls")
    val external_urls: Map<String, String>,
    @SerializedName("uri")
    val uri: String,
    @SerializedName("preview_url")
    val preview_url: String?,           // <-- phát thử 30s
    @SerializedName("explicit")
    val explicit: Boolean,              // <-- cảnh báo nội dung nhạy cảm
    @SerializedName("popularity")
    val popularity: Int,                // <-- độ phổ biến
    @SerializedName("is_playable")
    val is_playable: Boolean?,          // <-- có thể phát được không
    @SerializedName("track_number")
    val track_number: Int               // <-- số thứ tự trong album
)

data class SpotifyAlbum(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("images")
    val images: List<SpotifyImage>?,
    @SerializedName("album_type")
    val albumType: String?,
    @SerializedName("total_tracks")
    val totalTracks: Int?,
    @SerializedName("release_date")
    val releaseDate: String?,
    @SerializedName("release_date_precision")
    val releaseDatePrecision: String?,
    @SerializedName("artists")
    val artists: List<SpotifyArtist>?,
    @SerializedName("external_urls")
    val externalUrls: Map<String, String>?,
    @SerializedName("uri")
    val uri: String?,
    @SerializedName("album_group")
    val albumGroup: String?
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

data class SpotifyImage(
    @SerializedName("url")
    val url: String,
    
    @SerializedName("height")
    val height: Int?,
    
    @SerializedName("width")
    val width: Int?
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

data class SpotifyCategory(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("icons")
    val icons: List<SpotifyImage>,
    @SerializedName("href")
    val href: String
) 