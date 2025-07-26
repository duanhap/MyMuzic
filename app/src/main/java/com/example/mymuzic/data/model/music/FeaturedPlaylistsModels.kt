package com.example.mymuzic.data.model.music

import com.google.gson.annotations.SerializedName

data class FeaturedPlaylistsResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("playlists")
    val playlists: Playlists
)

data class Playlists(
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

data class PlaylistItem(
    @SerializedName("collaborative")
    val collaborative: Boolean,
    @SerializedName("description")
    val description: String,
    @SerializedName("external_urls")
    val external_urls: ExternalUrls,
    @SerializedName("href")
    val href: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("images")
    val images: List<SpotifyImage>,
    @SerializedName("name")
    val name: String,
    @SerializedName("owner")
    val owner: PlaylistOwner,
    @SerializedName("public")
    val public: Boolean,
    @SerializedName("snapshot_id")
    val snapshot_id: String,
    @SerializedName("tracks")
    val tracks: PlaylistTracks,
    @SerializedName("type")
    val type: String,
    @SerializedName("uri")
    val uri: String
)

data class PlaylistOwner(

    @SerializedName("external_urls")
    val external_urls: ExternalUrls,
    @SerializedName("href")
    val href: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("uri")
    val uri: String,
    @SerializedName("display_name")
    val display_name: String
)

data class PlaylistTracks(
    @SerializedName("href")
    val href: String,
    @SerializedName("total")
    val total: Int
)

data class PlaylistDetail(
    @SerializedName("collaborative")
    val collaborative: Boolean,
    @SerializedName("description")
    val description: String?,
    @SerializedName("external_urls")
    val external_urls: ExternalUrls?,
    @SerializedName("href")
    val href: String?,
    @SerializedName("id")
    val id: String,
    @SerializedName("images")
    val images: List<SpotifyImage>?,
    @SerializedName("name")
    val name: String,
    @SerializedName("owner")
    val owner: PlaylistOwner?,
    @SerializedName("public")
    val public: Boolean?,
    @SerializedName("snapshot_id")
    val snapshot_id: String?,
    @SerializedName("tracks")
    val tracks: PlaylistTracksResponse?,
    @SerializedName("type")
    val type: String?,
    @SerializedName("uri")
    val uri: String?
)

data class PlaylistTracksResponse(
    @SerializedName("href")
    val href: String?,
    @SerializedName("limit")
    val limit: Int?,
    @SerializedName("next")
    val next: String?,
    @SerializedName("offset")
    val offset: Int?,
    @SerializedName("previous")
    val previous: String?,
    @SerializedName("total")
    val total: Int?,
    @SerializedName("items")
    val items: List<PlaylistTrackItem>?
)

data class PlaylistTrackItem(
    @SerializedName("added_at")
    val added_at: String?,
    @SerializedName("track")
    val track: SpotifyTrack?
)

