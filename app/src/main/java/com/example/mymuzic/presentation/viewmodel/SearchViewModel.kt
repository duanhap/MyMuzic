package com.example.mymuzic.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymuzic.data.model.music.SpotifyTrack
import com.example.mymuzic.data.model.music.SpotifyArtist
import com.example.mymuzic.data.model.music.SpotifyAlbum
import com.example.mymuzic.data.model.music.PlaylistItem
import com.example.mymuzic.domain.usecase.music.SearchSpotifyUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// State cho UI Search
data class SearchUiState(
    val isLoading: Boolean = false,
    val tracks: List<SpotifyTrack> = emptyList(),
    val artists: List<SpotifyArtist> = emptyList(),
    val albums: List<SpotifyAlbum> = emptyList(),
    val playlists: List<PlaylistItem> = emptyList(),
    val error: String? = null
)

class SearchViewModel(
    private val searchSpotifyUseCase: SearchSpotifyUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    fun search(query: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val result = searchSpotifyUseCase(query)
            if (result.isSuccess) {
                val data = result.getOrNull()
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    tracks = data?.tracks ?: emptyList(),
                    artists = data?.artists ?: emptyList(),
                    albums = data?.albums ?: emptyList(),
                    playlists = data?.playlists ?: emptyList(),
                    error = null
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = result.exceptionOrNull()?.message ?: "Search failed"
                )
            }
        }
    }
} 