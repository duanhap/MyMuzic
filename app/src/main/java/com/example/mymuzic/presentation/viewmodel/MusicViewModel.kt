package com.example.mymuzic.presentation.screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymuzic.data.model.response.RecentlyPlayedItem
import com.example.mymuzic.data.model.music.SpotifyArtist
import com.example.mymuzic.data.model.music.SpotifyTrack
import com.example.mymuzic.domain.usecase.music.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class MusicUiState(
    val isLoading: Boolean = false,
    val recentlyPlayed: List<RecentlyPlayedItem> = emptyList(),
    val topTracks: List<SpotifyTrack> = emptyList(),
    val recentArtists: List<SpotifyArtist> = emptyList(),
    val currentPlayingTrack: SpotifyTrack? = null,
    val isCurrentlyPlaying: Boolean = false,
    val error: String? = null
)

sealed class MusicEvent {
    object FetchRecentlyPlayed : MusicEvent()
    object FetchTopTracks : MusicEvent()
    object FetchRecentArtists : MusicEvent()
    data class UpdateCurrentPlayingTrack(val track: SpotifyTrack, val isPlaying: Boolean) : MusicEvent()
    object TogglePlayPause : MusicEvent()
}

class MusicViewModel(
    private val getRecentlyPlayedTracksUseCase: GetRecentlyPlayedTracksUseCase,
    private val getTopTracksUseCase: GetTopTracksUseCase,
    private val getArtistsByIdsUseCase: GetArtistsByIdsUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(MusicUiState())
    val uiState: StateFlow<MusicUiState> = _uiState.asStateFlow()
    
    fun handleEvent(event: MusicEvent) {
        when (event) {
            is MusicEvent.FetchRecentlyPlayed -> fetchRecentlyPlayed()
            is MusicEvent.FetchTopTracks -> fetchTopTracks()
            is MusicEvent.FetchRecentArtists -> fetchRecentArtists()
            is MusicEvent.UpdateCurrentPlayingTrack -> updateCurrentPlayingTrack(event.track, event.isPlaying)
            is MusicEvent.TogglePlayPause -> togglePlayPause()
        }
    }
    
    private fun fetchRecentlyPlayed(limit: Int = 10) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, error = null)
                val result = getRecentlyPlayedTracksUseCase(limit)
                if (result.isSuccess) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        recentlyPlayed = result.getOrNull() ?: emptyList()
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.exceptionOrNull()?.message ?: "Failed to fetch recently played"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to fetch recently played"
                )
            }
        }
    }
    
    private fun fetchTopTracks(timeRange: String = "short_term", limit: Int = 10) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, error = null)
                val result = getTopTracksUseCase.invoke(timeRange, limit)
                if (result.isSuccess) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        topTracks = result.getOrNull() ?: emptyList()
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.exceptionOrNull()?.message ?: "Failed to fetch top tracks"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to fetch top tracks"
                )
            }
        }
    }
    
    private fun fetchRecentArtists() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, error = null)
                val recentlyPlayed = getRecentlyPlayedTracksUseCase(20).getOrNull() ?: emptyList()
                val artistIds = recentlyPlayed
                    .flatMap { it.track.artists }
                    .map { it.id }
                    .distinct()
                    .take(10)
                val artistsResult = getArtistsByIdsUseCase(artistIds)
                if (artistsResult.isSuccess) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        recentArtists = artistsResult.getOrNull() ?: emptyList()
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = artistsResult.exceptionOrNull()?.message ?: "Failed to fetch artists"
                    )
                }
            } catch (e: Exception) {
                Log.e("MusicViewModel", "fetchRecentArtists error", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to fetch artists"
                )
            }
        }
    }

    private fun updateCurrentPlayingTrack(track: SpotifyTrack, isPlaying: Boolean) {
        viewModelScope.launch {
            Log.d("MusicViewModel", "=== Cập nhật current playing track ===")
            Log.d("MusicViewModel", "Track: ${track.name}")
            Log.d("MusicViewModel", "Is playing: $isPlaying")
            Log.d("MusicViewModel", "Instance hash: ${this.hashCode()}")
            
            _uiState.value = _uiState.value.copy(
                currentPlayingTrack = track,
                isCurrentlyPlaying = isPlaying
            )
            
            Log.d("MusicViewModel", "State đã được cập nhật")
            Log.d("MusicViewModel", "Current track trong state: ${_uiState.value.currentPlayingTrack?.name}")
            Log.d("MusicViewModel", "Is playing trong state: ${_uiState.value.isCurrentlyPlaying}")
        }
    }

    private fun togglePlayPause() {
        viewModelScope.launch {
            Log.d("MusicViewModel", "=== Toggle play/pause ===")
            Log.d("MusicViewModel", "Trạng thái hiện tại: ${_uiState.value.isCurrentlyPlaying}")
            Log.d("MusicViewModel", "Instance hash: ${this.hashCode()}")
            
            _uiState.value = _uiState.value.copy(
                isCurrentlyPlaying = !_uiState.value.isCurrentlyPlaying
            )
            
            Log.d("MusicViewModel", "Trạng thái sau toggle: ${_uiState.value.isCurrentlyPlaying}")
        }
    }
} 