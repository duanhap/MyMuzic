package com.example.mymuzic.presentation.screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymuzic.data.model.response.RecentlyPlayedItem
import com.example.mymuzic.data.model.music.SpotifyArtist
import com.example.mymuzic.data.model.music.SpotifyTrack
import com.example.mymuzic.data.model.auth.SpotifyUserProfile
import com.example.mymuzic.domain.usecase.auth.GenerateAuthUrlUseCase
import com.example.mymuzic.domain.usecase.music.GetArtistsByIdsUseCase
import com.example.mymuzic.domain.usecase.auth.GetAuthStateUseCase
import com.example.mymuzic.domain.usecase.music.GetRecentlyPlayedTracksUseCase
import com.example.mymuzic.domain.usecase.music.GetTopTracksUseCase
import com.example.mymuzic.domain.usecase.music.GetTrackDetailUseCase
import com.example.mymuzic.domain.usecase.auth.GetUserProfileUseCase
import com.example.mymuzic.domain.usecase.auth.HandleAuthCallbackUseCase
import com.example.mymuzic.domain.usecase.auth.IsAuthenticatedUseCase
import com.example.mymuzic.domain.usecase.auth.LogoutUseCase
import com.example.mymuzic.domain.usecase.player.PlaySpotifyTrackUseCase
import com.example.mymuzic.domain.repository.SpotifyRemoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


data class PlaySongUiState(
    val isLoading: Boolean = false,
    val spotifyTrack: SpotifyTrack? = null,
    val error: String? = null,
)

sealed class PlaySongEvent {
    data class FetchTrackDetail(val id: String) : PlaySongEvent()
}

class PlaySongViewModel(
    private val getTrackDetailUseCase: GetTrackDetailUseCase,
    private val playSpotifyTrackUseCase: PlaySpotifyTrackUseCase,
    private val spotifyRemoteRepository: SpotifyRemoteRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PlaySongUiState())
    val uiState: StateFlow<PlaySongUiState> = _uiState.asStateFlow()

    val isConnected: StateFlow<Boolean> = spotifyRemoteRepository.isConnected

    fun handleEvent(event: PlaySongEvent) {
        when (event) {
            is PlaySongEvent.FetchTrackDetail -> fetchTrackDetail(event.id)
        }
    }

    fun fetchTrackDetail(id: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val trackResult = getTrackDetailUseCase(id)
                if (trackResult.isSuccess) {
                    _uiState.value = _uiState.value.copy(isLoading = false, spotifyTrack = trackResult.getOrNull(), error = null)
                } else {
                    _uiState.value = _uiState.value.copy(isLoading = false, error = trackResult.exceptionOrNull()?.message)
                }

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
            }
        }
    }



    fun playTrack(uri: String) {
        viewModelScope.launch {
            playSpotifyTrackUseCase(uri)
        }
    }
    fun connectSpotify() {
        spotifyRemoteRepository.connect(
            onSuccess = {
                /* update UI state if needed */
                Log.d("PlaySongViewModel", "Connected to Spotify App Remote")
            },
            onError = {
                /* handle error */
                Log.e("PlaySongViewModel", "Error connecting to Spotify App Remote: $it")
            }
        )
    }

    fun disconnectSpotify() {
        spotifyRemoteRepository.disconnect()
    }
}