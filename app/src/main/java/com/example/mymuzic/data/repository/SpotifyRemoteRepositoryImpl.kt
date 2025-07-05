package com.example.mymuzic.data.repository

import com.example.mymuzic.data.remote.SpotifyRemoteDataSource
import com.example.mymuzic.domain.repository.SpotifyRemoteRepository
import kotlinx.coroutines.flow.StateFlow

class SpotifyRemoteRepositoryImpl(
    private val remoteDataSource: SpotifyRemoteDataSource
) : SpotifyRemoteRepository {
    override fun connect(onSuccess: () -> Unit, onError: (Throwable) -> Unit) =
        remoteDataSource.connect(onSuccess, onError)
    override fun play(uri: String) = remoteDataSource.play(uri)
    override fun disconnect() = remoteDataSource.disconnect()
    override val isConnected: StateFlow<Boolean>
        get() = remoteDataSource.isConnected

} 