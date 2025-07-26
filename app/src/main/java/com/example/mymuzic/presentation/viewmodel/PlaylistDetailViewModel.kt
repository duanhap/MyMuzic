package com.example.mymuzic.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymuzic.data.model.music.PlaylistDetail
import com.example.mymuzic.data.model.music.PlaylistTrackItem
import com.example.mymuzic.domain.usecase.music.GetPlaylistDetailUseCase
import com.example.mymuzic.domain.usecase.music.GetPlaylistTracksUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class PlaylistDetailUiState(
    val isLoading: Boolean = false,
    val playlist: PlaylistDetail? = null,
    val tracks: List<PlaylistTrackItem> = emptyList(),
    val error: String? = null,
    val isLoadingMore: Boolean = false,
    val hasMore: Boolean = true,
    val currentOffset: Int = 0
)

class PlaylistDetailViewModel(
    private val getPlaylistDetailUseCase: GetPlaylistDetailUseCase,
    private val getPlaylistTracksUseCase: GetPlaylistTracksUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(PlaylistDetailUiState())
    val uiState: StateFlow<PlaylistDetailUiState> = _uiState.asStateFlow()

    private var playlistId: String? = null
    private val pageSize = 20

    fun loadPlaylist(playlistId: String) {
        this.playlistId = playlistId
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null, tracks = emptyList(), currentOffset = 0, hasMore = true)
            try {
                val result = getPlaylistDetailUseCase(playlistId)
                if (result.isSuccess) {
                    val playlist = result.getOrNull()
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        playlist = playlist,
                        error = null
                    )
                    // Load first page of tracks
                    loadMoreTracks()
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.exceptionOrNull()?.message ?: "Failed to load playlist detail"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to load playlist detail"
                )
            }
        }
    }

    fun loadMoreTracks() {
        val id = playlistId ?: return
        val state = _uiState.value
        if (state.isLoadingMore || !state.hasMore) return
        viewModelScope.launch {
            _uiState.value = state.copy(isLoadingMore = true)
            val offset = state.currentOffset
            val result = getPlaylistTracksUseCase(id, pageSize, offset)
            if (result.isSuccess) {
                val response = result.getOrNull()
                val newTracks = response?.items?.filterNotNull() ?: emptyList()
                val allTracks = state.tracks + newTracks
                val total = response?.total ?: 0
                val hasMore = allTracks.size < total
                _uiState.value = state.copy(
                    tracks = allTracks,
                    isLoadingMore = false,
                    currentOffset = allTracks.size,
                    hasMore = hasMore
                )
            } else {
                _uiState.value = state.copy(isLoadingMore = false, hasMore = false, error = result.exceptionOrNull()?.message)
            }
        }
    }
} 