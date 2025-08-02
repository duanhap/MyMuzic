package com.example.mymuzic.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mymuzic.presentation.ui.components.MusicHeader
import com.example.mymuzic.presentation.ui.components.MusicHeaderShimmer
import com.example.mymuzic.presentation.ui.components.TrackList
import com.example.mymuzic.presentation.ui.components.TrackListShimmer
import com.example.mymuzic.presentation.viewmodel.PlaylistDetailViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import org.koin.androidx.compose.koinViewModel

@Composable
fun PlaylistDetailScreen(
    navController: NavController,
    playlistId: String,
    playlistName: String,
    viewModel: PlaylistDetailViewModel = koinViewModel()
) {
    val uiState = viewModel.uiState.collectAsState().value
    val playlist = uiState.playlist
    val tracks = uiState.tracks
    val isLoading = uiState.isLoading
    val isLoadingMore = uiState.isLoadingMore
    val hasMore = uiState.hasMore

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // Lazy load: detect scroll to end
    LaunchedEffect(tracks.size, hasMore) {
        if (hasMore) {
            snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
                .filter { lastVisible ->
                    lastVisible != null && lastVisible >= tracks.size - 3 && !isLoadingMore
                }
                .distinctUntilChanged()
                .collectLatest {
                    viewModel.loadMoreTracks()
                }
        }
    }

    LaunchedEffect(playlistId) {
        viewModel.loadPlaylist(playlistId)
    }

    Scaffold(
        containerColor = Color(0xFF020100),
    ) { paddingValues ->
        if (isLoading) {
            Column(modifier = Modifier.padding(paddingValues)) {
                MusicHeaderShimmer()
                Spacer(modifier = Modifier.height(24.dp))
                TrackListShimmer(count = 10)
            }
        } else if (playlist == null) {
            // Error or empty state
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Playlist not found",
                    color = Color.White
                )
            }
        } else {
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 80.dp),
                    state = listState
                ) {
                    item {
                        MusicHeader(
                            imageUrl = playlist.images?.firstOrNull()?.url,
                            title = playlist.name ?: playlistName,
                            subtitle = playlist.description,
                            info = "By ${playlist.owner?.display_name ?: "Unknown"} • ${playlist.tracks?.total ?: tracks.size} tracks",
                            onPlay = {
                                // Play first track if available
                                val firstTrack = tracks.firstOrNull()?.track
                                if (firstTrack != null) {
                                    navController.navigate(
                                        "play_song/${firstTrack.id}?name=${firstTrack.name}" +
                                                "&imageUrl=${firstTrack.album?.images?.firstOrNull()?.url ?: ""}" +
                                                "&artist=${firstTrack.artists.joinToString(", ") { it.name }}"
                                    )
                                }
                            }
                        )
                    }
                    item {
                        Text(
                            text = "Tracks",
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                    items(tracks) { trackItem ->
                        TrackList(
                            tracks = listOf(trackItem),
                            getTrackNumber = { tracks.indexOf(it) + 1 },
                            getImageUrl = { (it as com.example.mymuzic.data.model.music.PlaylistTrackItem).track?.album?.images?.firstOrNull()?.url ?: "" },
                            getName = { (it as com.example.mymuzic.data.model.music.PlaylistTrackItem).track?.name ?: "" },
                            getArtists = { (it as com.example.mymuzic.data.model.music.PlaylistTrackItem).track?.artists?.joinToString(", ") { artist -> artist.name } ?: "" },
                            getDuration = { millisToMinSec((it as com.example.mymuzic.data.model.music.PlaylistTrackItem).track?.duration_ms) },
                            onTrackClick = { track ->
                                val t = (track as com.example.mymuzic.data.model.music.PlaylistTrackItem).track
                                if (t != null) {
                                    navController.navigate(
                                        "play_song/${t.id}?name=${t.name}" +
                                                "&imageUrl=${t.album?.images?.firstOrNull()?.url ?: ""}" +
                                                "&artist=${t.artists.joinToString(", ") { it.name }}"
                                    )
                                }
                            }
                        )
                    }
                    // Loading indicator at the end
                    if (isLoadingMore) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(color = Color.White)
                            }
                        }
                    }
                    // Error indicator at the end (loadMore error)
                    if (!isLoadingMore && uiState.error != null && tracks.isNotEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(text = "Đã xảy ra lỗi khi tải thêm bài hát", color = Color.Red, fontSize = 14.sp)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    androidx.compose.material3.Button(onClick = { viewModel.loadMoreTracks() }) {
                                        Text("Thử lại")
                                    }
                                }
                            }
                        }
                    }
                }
                // Nút Back ở góc trên bên trái
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.TopStart)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

fun millisToMinSec(ms: Int?): String {
    if (ms == null) return "--:--"
    val min = ms / 60000
    val sec = (ms % 60000) / 1000
    return "%d:%02d".format(min, sec)
} 