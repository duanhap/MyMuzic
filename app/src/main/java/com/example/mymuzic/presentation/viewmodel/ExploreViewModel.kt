package com.example.mymuzic.presentation.screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymuzic.data.model.music.SpotifyAlbum
import com.example.mymuzic.domain.usecase.music.GetNewReleasesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.mymuzic.domain.usecase.music.GetCategoriesUseCase
import com.example.mymuzic.data.model.music.SpotifyCategory
import com.example.mymuzic.data.model.music.PlaylistDetail
import com.example.mymuzic.domain.usecase.music.GetPlaylistDetailUseCase

data class ExploreUiState(
    val isLoading: Boolean = false,
    val newReleases: List<SpotifyAlbum> = emptyList(),
    val error: String? = null
)

sealed class ExploreEvent {
    object LoadNewReleases : ExploreEvent()
}

data class ExploreCategoryUiState(
    val isLoading: Boolean = false,
    val categories: List<SpotifyCategory> = emptyList(),
    val error: String? = null
)

sealed class ExploreCategoryEvent {
    object LoadCategories : ExploreCategoryEvent()
}

class ExploreViewModel(
    private val getNewReleasesUseCase: GetNewReleasesUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getPlaylistDetailUseCase: GetPlaylistDetailUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(ExploreUiState())
    val uiState: StateFlow<ExploreUiState> = _uiState.asStateFlow()

    private val _categoryUiState = MutableStateFlow(ExploreCategoryUiState())
    val categoryUiState: StateFlow<ExploreCategoryUiState> = _categoryUiState.asStateFlow()

    // State for 3 playlists
    private val _viralHits = MutableStateFlow<PlaylistDetail?>(null)
    val viralHits: StateFlow<PlaylistDetail?> = _viralHits

    private val _todaysTopHits = MutableStateFlow<PlaylistDetail?>(null)
    val todaysTopHits: StateFlow<PlaylistDetail?> = _todaysTopHits

    private val _globalTop50 = MutableStateFlow<PlaylistDetail?>(null)
    val globalTop50: StateFlow<PlaylistDetail?> = _globalTop50

    private val _isPlaylistLoading = MutableStateFlow(false)
    val isPlaylistLoading: StateFlow<Boolean> = _isPlaylistLoading

    init {
        handleEvent(ExploreEvent.LoadNewReleases)
        handleCategoryEvent(ExploreCategoryEvent.LoadCategories)
        fetchExplorePlaylists()
    }

    fun handleEvent(event: ExploreEvent) {
        when (event) {
            is ExploreEvent.LoadNewReleases -> fetchNewReleases()
        }
    }

    private fun fetchNewReleases() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val result = getNewReleasesUseCase(10)
                if (result.isSuccess) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        newReleases = result.getOrNull()?.items ?: emptyList(),
                        error = null
                    )
                    Log.d("ExploreViewModel", "New releases loaded successfully")
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.exceptionOrNull()?.message ?: "Failed to load new releases"
                    )
                    Log.e("ExploreViewModel", "Error loading new releases", result.exceptionOrNull())
                }
            } catch (e: Exception) {
                Log.e("ExploreViewModel", "Error loading new releases", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to load new releases"
                )
            }
        }
    }

    fun handleCategoryEvent(event: ExploreCategoryEvent) {
        when (event) {
            is ExploreCategoryEvent.LoadCategories -> fetchCategories()
        }
    }

    private fun fetchCategories() {
        viewModelScope.launch {
            _categoryUiState.value = _categoryUiState.value.copy(isLoading = true, error = null)
            try {
                val result = getCategoriesUseCase(20)
                if (result.isSuccess) {
                    _categoryUiState.value = _categoryUiState.value.copy(
                        isLoading = false,
                        categories = result.getOrNull()?.items ?: emptyList(),
                        error = null
                    )
                } else {
                    _categoryUiState.value = _categoryUiState.value.copy(
                        isLoading = false,
                        error = result.exceptionOrNull()?.message ?: "Failed to load categories"
                    )
                }
            } catch (e: Exception) {
                _categoryUiState.value = _categoryUiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to load categories"
                )
            }
        }
    }

    fun fetchExplorePlaylists() {
        viewModelScope.launch {
            _isPlaylistLoading.value = true
            try {
                val viral = getPlaylistDetailUseCase("37i9dQZEVXbM4UZuIrvHvA")
                val today = getPlaylistDetailUseCase("37i9dQZF1DXcBWIGoYBM5M")
                val global = getPlaylistDetailUseCase("37i9dQZEVXbMDoHDwVN2tF")
                _viralHits.value = viral.getOrNull()
                _todaysTopHits.value = today.getOrNull()
                _globalTop50.value = global.getOrNull()
            } catch (e: Exception) {
                // handle error nếu muốn
            }
            _isPlaylistLoading.value = false
        }
    }
} 