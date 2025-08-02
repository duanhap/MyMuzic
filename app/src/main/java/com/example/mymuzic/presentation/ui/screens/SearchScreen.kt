package com.example.mymuzic.presentation.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.mymuzic.presentation.viewmodel.SearchViewModel
import com.example.mymuzic.data.model.music.PlaylistItem
import com.example.mymuzic.data.model.music.SpotifyArtist
import com.example.mymuzic.data.model.music.SpotifyTrack
import com.example.mymuzic.data.model.music.SpotifyAlbum
import org.koin.androidx.compose.koinViewModel
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.draw.clip
import androidx.navigation.NavController

@Composable
fun SearchScreen(
    navController: NavController,
    query: String,
    viewModel: SearchViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    var searchQuery by remember { mutableStateOf(query) }
    val keyboardController = LocalSoftwareKeyboardController.current

    // Gọi search khi screen được mở hoặc khi user sửa query
    LaunchedEffect(searchQuery) {
        if (searchQuery.isNotBlank()) viewModel.search(searchQuery)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF18181A))
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(30.dp))
        // Header + Search bar
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "𝄞",
                color = Color.Cyan,
                fontSize = 29.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Search",
                color = Color.Cyan,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search for songs, artists, or playlists") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Cyan,
                unfocusedBorderColor = Color.Gray,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                cursorColor = Color.Cyan
            ),
            shape = RoundedCornerShape(24.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = {
                    if (searchQuery.isNotBlank()) {
                        viewModel.search(searchQuery)
                        keyboardController?.hide()
                    }
                }
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        when {
            uiState.isLoading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
                    CircularProgressIndicator(
                        color = Color.Cyan,
                        strokeWidth = 6.dp,
                        modifier = Modifier.size(64.dp)
                    )
                }
            }
            uiState.error != null -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Error: ${uiState.error}", color = Color.Red)
                }
            }
            else -> {
                if (uiState.artists.isNotEmpty()) {
                    SectionHeader("Artists")
                    LazyRow(modifier = Modifier.fillMaxWidth()) {
                        items(uiState.artists) { artist ->
                            ArtistItem(
                                artist = artist,
                                onClick = { navController.navigate("artist/${artist.id}") },
                                modifier = Modifier.padding(end = 20.dp)
                            )
                        }
                    }
                }
                if (uiState.tracks.isNotEmpty()) {
                    SectionHeader("Tracks")
                    LazyRow(modifier = Modifier.fillMaxWidth()) {
                        items(uiState.tracks) { track ->
                            TrackItem(
                                track = track,
                                onClick = {
                                    navController.navigate(
                                        "play_song/${track.id}?name=${track.name}" +
                                                "&imageUrl=${track.album?.images?.firstOrNull()?.url ?: ""}" +
                                                "&artist=${track.artists.joinToString(", ") { it.name }}"
                                    )
                                },
                                modifier = Modifier.padding(end = 20.dp)
                            )
                        }
                    }
                }
                if (uiState.playlists.isNotEmpty()) {
                    SectionHeader("Playlists")
                    LazyRow(modifier = Modifier.fillMaxWidth()) {
                        items(uiState.playlists) { playlist ->
                            PlaylistItemView(
                                playlist = playlist,
                                onClick = { navController.navigate("playlist_detail/${playlist.id}/${playlist.name}") },
                                modifier = Modifier.padding(end = 20.dp)
                            )
                        }
                    }
                }
                if (uiState.albums.isNotEmpty()) {
                    SectionHeader("Albums")
                    LazyRow(modifier = Modifier.fillMaxWidth()) {
                        items(uiState.albums) { album ->
                            AlbumItem(
                                album = album,
                                onClick = {
                                    navController.navigate(
                                        "album/${album.id}?name=${album.name}" +
                                                "&imageUrl=${album.images?.firstOrNull()?.url ?: ""}" +
                                                "&artist=${album.artists?.joinToString(", ") { it.name }}"
                                    )
                                },
                                modifier = Modifier.padding(end = 20.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        color = Color.White,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
fun ArtistItem(artist: SpotifyArtist, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .width(200.dp)
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val imageUrl = artist.images?.firstOrNull()?.url
        if (imageUrl != null) {
            Image(
                painter = rememberAsyncImagePainter(imageUrl),
                contentDescription = artist.name,
                modifier = Modifier.size(80.dp).clip(CircleShape)
            )
        }
        Spacer(Modifier.width(16.dp))
        Text(artist.name, color = Color.White, fontWeight = FontWeight.Medium, maxLines = 1)
    }
}

@Composable
fun TrackItem(track: SpotifyTrack, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .width(260.dp)
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val imageUrl = track.album?.images?.firstOrNull()?.url
        if (imageUrl != null) {
            Image(
                painter = rememberAsyncImagePainter(imageUrl),
                contentDescription = track.name,
                modifier = Modifier.size(80.dp).clip(RoundedCornerShape(20.dp))
            )
        }
        Spacer(Modifier.width(16.dp))
        Column {
            Text(track.name, color = Color.White, fontWeight = FontWeight.Medium, maxLines = 1)
            Text(track.artists.joinToString(", ") { it.name }, color = Color.Gray, fontSize = 13.sp, maxLines = 1)
        }
    }
}

@Composable
fun PlaylistItemView(playlist: PlaylistItem, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .width(260.dp)
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val imageUrl = playlist.images.firstOrNull()?.url
        if (imageUrl != null) {
            Image(
                painter = rememberAsyncImagePainter(imageUrl),
                contentDescription = playlist.name,
                modifier = Modifier.size(80.dp).clip(RoundedCornerShape(20.dp))
            )
        }
        Spacer(Modifier.width(16.dp))
        Column {
            Text(playlist.name, color = Color.White, fontWeight = FontWeight.Medium, maxLines = 1)
            Text(playlist.description, color = Color.Gray, fontSize = 13.sp, maxLines = 1)
        }
    }
}

@Composable
fun AlbumItem(album: SpotifyAlbum, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .width(260.dp)
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val imageUrl = album.images?.firstOrNull()?.url
        if (imageUrl != null) {
            Image(
                painter = rememberAsyncImagePainter(imageUrl),
                contentDescription = album.name,
                modifier = Modifier.size(80.dp).clip(RoundedCornerShape(20.dp))
            )
        }
        Spacer(Modifier.width(16.dp))
        Column {
            Text(album.name, color = Color.White, fontWeight = FontWeight.Medium, maxLines = 1)
            Text(album.artists?.joinToString(", ") { it.name } ?: "", color = Color.Gray, fontSize = 13.sp, maxLines = 1)
        }
    }
} 