package com.example.mymuzic.data.remote

import android.content.Context
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import android.util.Log

class SpotifyRemoteDataSource(
    private val context: Context,
    private val clientId: String,
    private val redirectUri: String
) {
    private var spotifyAppRemote: SpotifyAppRemote? = null
    private val _isConnected = MutableStateFlow(false)
    val isConnected: StateFlow<Boolean> = _isConnected

    fun connect(onSuccess: () -> Unit = {}, onError: (Throwable) -> Unit = {}) {
        val connectionParams = ConnectionParams.Builder(clientId)
            .setRedirectUri(redirectUri)
            .showAuthView(true)
            .build()
        SpotifyAppRemote.connect(context, connectionParams, object : Connector.ConnectionListener {
            override fun onConnected(appRemote: SpotifyAppRemote) {
                spotifyAppRemote = appRemote
                _isConnected.value = true
                Log.d("SpotifyRemote", "Connected to Spotify App Remote!")
                onSuccess()
            }
            override fun onFailure(throwable: Throwable) {
                _isConnected.value = false
                Log.e("SpotifyRemote", "Failed to connect: ${throwable.message}", throwable)
                onError(throwable)
            }
        })
    }

    fun play(uri: String) {
        spotifyAppRemote?.playerApi?.play(uri)
    }

    fun disconnect() {
        spotifyAppRemote?.let { SpotifyAppRemote.disconnect(it) }
        _isConnected.value = false
    }
} 