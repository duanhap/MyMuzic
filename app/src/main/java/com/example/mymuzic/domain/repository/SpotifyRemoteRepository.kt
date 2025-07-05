package com.example.mymuzic.domain.repository

import kotlinx.coroutines.flow.StateFlow

interface SpotifyRemoteRepository {
    fun connect(onSuccess: () -> Unit = {}, onError: (Throwable) -> Unit = {})
    fun play(uri: String)
    fun disconnect()
    val isConnected: StateFlow<Boolean>
} 