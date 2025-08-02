package com.example.mymuzic.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymuzic.data.model.music.SpotifyArtist
import com.example.mymuzic.data.model.music.SpotifyTrack
import com.example.mymuzic.data.model.response.RecentlyPlayedItem
import com.example.mymuzic.domain.usecase.music.GetRecentlyPlayedTracksUseCase
import com.example.mymuzic.presentation.screen.MusicEvent
import com.example.mymuzic.presentation.screen.MusicUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


data class LibraryUiState(
    val isLoading: Boolean = false,
    val recentlyPlayed: List<RecentlyPlayedItem> = emptyList(),
    val tracks: List<SpotifyTrack> = emptyList(),
    val error: String? = null
)
sealed class LibraryEvent {
    object FetchRecentlyPlayed : LibraryEvent()
}

class LibraryViewModel (
    private val getRecentlyPlayedTracksUseCase: GetRecentlyPlayedTracksUseCase,
): ViewModel() {
    private val _uiState = MutableStateFlow(LibraryUiState())
    val uiState: StateFlow<LibraryUiState> = _uiState.asStateFlow()

    fun handleEvent(event: LibraryEvent) {
        when (event) {
            is LibraryEvent.FetchRecentlyPlayed -> fetchRecentlyPlayed()
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
                        recentlyPlayed = result.getOrNull() ?: emptyList(),
                        tracks = result.getOrNull()?.map { it.track } ?: emptyList()
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
}