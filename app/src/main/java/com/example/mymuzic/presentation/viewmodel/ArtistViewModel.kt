package com.example.mymuzic.presentation.screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymuzic.data.model.music.SpotifyArtist
import com.example.mymuzic.data.model.music.SpotifyTrack
import com.example.mymuzic.data.model.music.SpotifyAlbum
import com.example.mymuzic.data.model.response.ArtistDetailResponse
import com.example.mymuzic.data.model.response.ArtistAlbumsResponse
import com.example.mymuzic.data.model.response.ArtistTopTracksResponse
import com.example.mymuzic.domain.usecase.music.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ArtistUiState(
    val isLoading: Boolean = false,
    val artistDetail: ArtistDetailResponse? = null,
    val albums: List<SpotifyAlbum> = emptyList(),
    val topTracks: List<SpotifyTrack> = emptyList(),
    val relatedArtists: List<SpotifyArtist> = emptyList(),
    val error: String? = null,
    val showAllAlbums: Boolean = false,
    val showAllTopTracks: Boolean = false,
    val showAllRelatedArtists: Boolean = false
)

sealed class ArtistEvent {
    data class LoadArtistData(val artistId: String) : ArtistEvent()
    data class ToggleShowAllAlbums(val show: Boolean) : ArtistEvent()
    data class ToggleShowAllTopTracks(val show: Boolean) : ArtistEvent()
    data class ToggleShowAllRelatedArtists(val show: Boolean) : ArtistEvent()
    data class PlayTrack(val track: SpotifyTrack) : ArtistEvent()
}

class ArtistViewModel(
    private val getArtistDetailUseCase: GetArtistDetailUseCase,
    private val getArtistAlbumsUseCase: GetArtistAlbumsUseCase,
    private val getArtistTopTracksUseCase: GetArtistTopTracksUseCase,
    private val getRelatedArtistsUseCase: GetRelatedArtistsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ArtistUiState())
    val uiState: StateFlow<ArtistUiState> = _uiState.asStateFlow()

    fun handleEvent(event: ArtistEvent) {
        when (event) {
            is ArtistEvent.LoadArtistData -> {
                loadArtistData(event.artistId)
            }
            is ArtistEvent.ToggleShowAllAlbums -> {
                _uiState.value = _uiState.value.copy(showAllAlbums = event.show)
            }
            is ArtistEvent.ToggleShowAllTopTracks -> {
                _uiState.value = _uiState.value.copy(showAllTopTracks = event.show)
            }
            is ArtistEvent.ToggleShowAllRelatedArtists -> {
                _uiState.value = _uiState.value.copy(showAllRelatedArtists = event.show)
            }
            is ArtistEvent.PlayTrack -> {
                // Handle play track - could integrate with player
                Log.d("ArtistViewModel", "Playing track: ${event.track.name}")
            }
        }
    }

    private fun loadArtistData(artistId: String) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, error = null)
                
                // Load artist detail
                val artistDetailResult = getArtistDetailUseCase(artistId)
                if (artistDetailResult.isSuccess) {
                    _uiState.value = _uiState.value.copy(artistDetail = artistDetailResult.getOrNull())
                } else {
                    throw artistDetailResult.exceptionOrNull() ?: Exception("Failed to load artist detail")
                }

                // Load albums
                val albumsResult = getArtistAlbumsUseCase(artistId, limit = 10)
                if (albumsResult.isSuccess) {
                    _uiState.value = _uiState.value.copy(albums = albumsResult.getOrNull()?.items ?: emptyList())
                }

                // Load top tracks
                val topTracksResult = getArtistTopTracksUseCase(artistId)
                if (topTracksResult.isSuccess) {
                    _uiState.value = _uiState.value.copy(topTracks = topTracksResult.getOrNull()?.tracks ?: emptyList())
                }

                // Load related artists
                val relatedArtistsResult = getRelatedArtistsUseCase(artistId)
                if (relatedArtistsResult.isSuccess) {
                    _uiState.value = _uiState.value.copy(relatedArtists = relatedArtistsResult.getOrNull() ?: emptyList())
                }

                _uiState.value = _uiState.value.copy(isLoading = false)
                
            } catch (e: Exception) {
                Log.e("ArtistViewModel", "Error loading artist data", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to load artist data"
                )
            }
        }
    }
} 