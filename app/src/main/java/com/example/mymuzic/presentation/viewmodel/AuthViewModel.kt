package com.example.mymuzic.presentation.screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymuzic.data.model.auth.AuthState
import com.example.mymuzic.data.model.response.RecentlyPlayedItem
import com.example.mymuzic.data.model.music.SpotifyArtist
import com.example.mymuzic.data.model.auth.SpotifyTokenResponse
import com.example.mymuzic.data.model.music.SpotifyTrack
import com.example.mymuzic.data.model.auth.SpotifyUserProfile
import com.example.mymuzic.domain.usecase.auth.*
import com.example.mymuzic.domain.usecase.music.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AuthUiState(
    val isLoading: Boolean = false,
    val isAuthenticated: Boolean = false,
    val userProfile: SpotifyUserProfile? = null,
    val error: String? = null,
    val authUrl: String? = null
)

sealed class AuthEvent {
    object SignInWithSpotify : AuthEvent()
    data class HandleAuthCallback(val uri: String) : AuthEvent()
    object Logout : AuthEvent()
    object CheckAuthState : AuthEvent()
}

class AuthViewModel(
    private val generateAuthUrlUseCase: GenerateAuthUrlUseCase,
    private val handleAuthCallbackUseCase: HandleAuthCallbackUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val isAuthenticatedUseCase: IsAuthenticatedUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val getAuthStateUseCase: GetAuthStateUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()
    
    init {
        checkAuthState()
    }
    
    fun handleEvent(event: AuthEvent) {
        when (event) {
            is AuthEvent.SignInWithSpotify -> signInWithSpotify()
            is AuthEvent.HandleAuthCallback -> handleAuthCallback(event.uri)
            is AuthEvent.Logout -> logout()
            is AuthEvent.CheckAuthState -> checkAuthState()
        }
    }
    
    private fun signInWithSpotify() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, error = null)
                
                val authUrl = generateAuthUrlUseCase()
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    authUrl = authUrl
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to generate auth URL"
                )
            }
        }
    }
    
    private fun handleAuthCallback(uri: String) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, error = null)
                
                val result = handleAuthCallbackUseCase(uri)
                if (result.isSuccess) {
                    // Lấy user profile sau khi đăng nhập thành công
                    val profileResult = getUserProfileUseCase()
                    if (profileResult.isSuccess) {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isAuthenticated = true,
                            userProfile = profileResult.getOrNull(),
                            authUrl = null
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = profileResult.exceptionOrNull()?.message ?: "Failed to get user profile"
                        )
                    }
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.exceptionOrNull()?.message ?: "Authentication failed"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to handle auth callback"
                )
            }
        }
    }
    
    private fun logout() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, error = null)
                
                logoutUseCase()
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isAuthenticated = false,
                    userProfile = null,
                    authUrl = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to logout"
                )
            }
        }
    }
    
    private fun checkAuthState() {
        viewModelScope.launch {
            try {
                val isAuthenticated = isAuthenticatedUseCase()
                if (isAuthenticated) {
                    val profileResult = getUserProfileUseCase()
                    if (profileResult.isSuccess) {
                        _uiState.value = _uiState.value.copy(
                            isAuthenticated = true,
                            userProfile = profileResult.getOrNull()
                        )
                    }
                }
            } catch (e: Exception) {
                // Ignore errors when checking auth state
            }
        }
    }
} 