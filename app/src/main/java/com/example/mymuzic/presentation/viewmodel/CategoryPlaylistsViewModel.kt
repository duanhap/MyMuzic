package com.example.mymuzic.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymuzic.data.model.music.PlaylistItem
import com.example.mymuzic.domain.usecase.music.SearchPlaylistsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CategoryPlaylistsUiState(
    val isLoading: Boolean = false,
    val playlists: List<PlaylistItem> = emptyList(),
    val error: String? = null
)

class CategoryPlaylistsViewModel(
    private val searchPlaylistsUseCase: SearchPlaylistsUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(CategoryPlaylistsUiState())
    val uiState: StateFlow<CategoryPlaylistsUiState> = _uiState.asStateFlow()

    fun loadPlaylists(categoryName: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val result = searchPlaylistsUseCase(categoryName)
                if (result.isSuccess) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        playlists = result.getOrNull()?.playlists?.items ?: emptyList(),
                        error = null
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.exceptionOrNull()?.message ?: "Failed to load playlists"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to load playlists"
                )
            }
        }
    }
} 