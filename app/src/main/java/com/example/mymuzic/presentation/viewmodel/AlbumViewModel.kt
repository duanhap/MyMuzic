package com.example.mymuzic.presentation.screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymuzic.data.model.music.SpotifyTrack
import com.example.mymuzic.data.model.music.SpotifyAlbum
import com.example.mymuzic.domain.usecase.music.GetAlbumTracksUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AlbumUiState(
    val isLoading: Boolean = false,
    val album: SpotifyAlbum? = null,
    val tracks: List<SpotifyTrack> = emptyList(),
    val error: String? = null,
    val showAllTracks: Boolean = false
)

sealed class AlbumEvent {
    data class LoadAlbumData(val albumId: String, val album: SpotifyAlbum? = null) : AlbumEvent()
    data class ToggleShowAllTracks(val show: Boolean) : AlbumEvent()
    data class PlayTrack(val track: SpotifyTrack) : AlbumEvent()
}

class AlbumViewModel(
    private val getAlbumTracksUseCase: GetAlbumTracksUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AlbumUiState())
    val uiState: StateFlow<AlbumUiState> = _uiState.asStateFlow()

    fun handleEvent(event: AlbumEvent) {
        when (event) {
            is AlbumEvent.LoadAlbumData -> {
                loadAlbumData(event.albumId, event.album)
            }
            is AlbumEvent.ToggleShowAllTracks -> {
                _uiState.value = _uiState.value.copy(showAllTracks = event.show)
            }
            is AlbumEvent.PlayTrack -> {
                // Handle play track - could integrate with player
                Log.d("AlbumViewModel", "Playing track: ${event.track.name}")
            }
        }
    }

    private fun loadAlbumData(albumId: String, album: SpotifyAlbum?) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, error = null)
                
                // Set album info if provided
                if (album != null) {
                    _uiState.value = _uiState.value.copy(album = album)
                }
                
                // Load album tracks
                val tracksResult = getAlbumTracksUseCase(albumId)
                if (tracksResult.isSuccess) {
                    _uiState.value = _uiState.value.copy(tracks = tracksResult.getOrNull()?.items ?: emptyList())
                } else {
                    throw tracksResult.exceptionOrNull() ?: Exception("Failed to load album tracks")
                }

                _uiState.value = _uiState.value.copy(isLoading = false)
                
            } catch (e: Exception) {
                Log.e("AlbumViewModel", "Error loading album data", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to load album data"
                )
            }
        }
    }
} 