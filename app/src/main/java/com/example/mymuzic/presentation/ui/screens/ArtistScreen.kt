package com.example.mymuzic.presentation.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.mymuzic.data.model.music.SpotifyAlbum
import com.example.mymuzic.data.model.music.SpotifyArtist
import com.example.mymuzic.data.model.music.SpotifyTrack
import com.example.mymuzic.data.model.response.ArtistDetailResponse
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material3.placeholder
import com.google.accompanist.placeholder.material3.shimmer
import org.koin.androidx.compose.koinViewModel
import com.example.mymuzic.presentation.screen.ArtistViewModel
import com.example.mymuzic.presentation.screen.ArtistEvent
import com.example.mymuzic.presentation.screen.ArtistUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtistScreen(
    navController: NavController,
    artistId: String
) {
    val artistViewModel: ArtistViewModel = koinViewModel()
    val uiState by artistViewModel.uiState.collectAsState()
    LaunchedEffect(artistId) {
        artistViewModel.handleEvent(ArtistEvent.LoadArtistData(artistId))
    }

    Scaffold(
        containerColor = Color(0xFF020100),
    ) { paddingValues ->
        if (uiState.isLoading) {
            ArtistScreenShimmer()
        } else if (uiState.error != null) {
            ErrorContent(
                error = uiState.error!!,
                onRetry = { artistViewModel.handleEvent(ArtistEvent.LoadArtistData(artistId)) }
            )
        } else {
            ArtistContent(
                uiState = uiState,
                onToggleShowAllAlbums = { show ->
                    artistViewModel.handleEvent(ArtistEvent.ToggleShowAllAlbums(show))
                },
                onToggleShowAllTopTracks = { show ->
                    artistViewModel.handleEvent(ArtistEvent.ToggleShowAllTopTracks(show))
                },
                onToggleShowAllRelatedArtists = { show ->
                    artistViewModel.handleEvent(ArtistEvent.ToggleShowAllRelatedArtists(show))
                },
                onPlayTrack = { track ->
                    navController.navigate(
                        "play_song/${track.id}?name=${track.name}" +
                                "&imageUrl=${track.album?.images?.firstOrNull()?.url ?: ""}" +
                                "&artist=${track.artists.joinToString(", ") { it.name }}"
                    )
                },
                onAlbumClick = { album ->
                    navController.navigate(
                        "album/${album.id}?name=${album.name}" +
                                "&imageUrl=${album.images?.firstOrNull()?.url ?: ""}" +
                                "&artist=${album.artists?.joinToString(", ") { it.name }}"
                    )
                },
                onArtistClick = { artist ->
                    navController.navigate("artist/${artist.id}")
                },
                onBack = { navController.popBackStack() },
                modifier = Modifier.padding(paddingValues)

            )
        }
    }
}

@Composable
fun ArtistContent(
    uiState: ArtistUiState,
    onToggleShowAllAlbums: (Boolean) -> Unit,
    onToggleShowAllTopTracks: (Boolean) -> Unit,
    onToggleShowAllRelatedArtists: (Boolean) -> Unit,
    onPlayTrack: (SpotifyTrack) -> Unit,
    onAlbumClick: (SpotifyAlbum) -> Unit,
    onArtistClick: (SpotifyArtist) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val artistDetail = uiState.artistDetail

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            // Artist Header
            item {
                ArtistHeader(
                    artistDetail = artistDetail,
                    onPlayArtist = {
                        if (uiState.topTracks.isNotEmpty()) {
                            onPlayTrack(uiState.topTracks.first())
                        }
                    }
                )
            }

            // Popular Tracks Section
            item {
                if (uiState.topTracks.isNotEmpty()) {
                    PopularTracksSection(
                        tracks = uiState.topTracks,
                        showAll = uiState.showAllTopTracks,
                        onToggleShowAll = onToggleShowAllTopTracks,
                        onPlayTrack = onPlayTrack
                    )
                }
            }

            // Albums Section
            item {
                if (uiState.albums.isNotEmpty()) {
                    AlbumsSection(
                        albums = uiState.albums,
                        showAll = uiState.showAllAlbums,
                        onToggleShowAll = onToggleShowAllAlbums,
                        onAlbumClick = onAlbumClick
                    )
                }
            }

            // Related Artists Section
            item {
                if (uiState.relatedArtists.isNotEmpty()) {
                    RelatedArtistsSection(
                        artists = uiState.relatedArtists,
                        showAll = uiState.showAllRelatedArtists,
                        onToggleShowAll = onToggleShowAllRelatedArtists,
                        onArtistClick = onArtistClick
                    )
                }
            }
        }
        
        // Back button positioned at top-left
        IconButton(
            onClick = onBack,
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

@Composable
fun ArtistHeader(
    artistDetail: ArtistDetailResponse?,
    onPlayArtist: () -> Unit = {}
) {
    if (artistDetail == null) return
    
    val imageUrl = artistDetail.images.firstOrNull()?.url ?: ""
    val gradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF1DE9FF),
            Color(0xFF191414)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
    ) {
        // Background gradient
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient)
        )
        
        // Artist image
        Image(
            painter = rememberAsyncImagePainter(imageUrl),
            contentDescription = artistDetail.name,
            modifier = Modifier
                .size(200.dp)
                .align(Alignment.Center)
                .offset(y = (-75).dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        
        // Artist info overlay
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = artistDetail.name,
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Genres
            if (artistDetail.genres.isNotEmpty()) {
                Text(
                    text = artistDetail.genres.take(3).joinToString(" • "),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Followers
            Text(
                text = "${formatNumber(artistDetail.followers.total)} followers",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.7f)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Play button
            Button(
                onClick = onPlayArtist,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Play",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Play", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun PopularTracksSection(
    tracks: List<SpotifyTrack>,
    showAll: Boolean,
    onToggleShowAll: (Boolean) -> Unit,
    onPlayTrack: (SpotifyTrack) -> Unit
) {
    val displayTracks = if (showAll) tracks else tracks.take(5)
    
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Popular",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            
            if (tracks.size > 5) {
                TextButton(
                    onClick = { onToggleShowAll(!showAll) }
                ) {
                    Text(
                        text = if (showAll) "Show less" else "See all",
                        color = Color(0xFF1DE9FF)
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            displayTracks.forEach { track ->
                TrackItem(
                    track = track,
                    onPlayTrack = onPlayTrack
                )
            }
        }
    }
}

@Composable
fun TrackItem(
    track: SpotifyTrack,
    onPlayTrack: (SpotifyTrack) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onPlayTrack(track) }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Track image
        val imageUrl = track.album?.images?.firstOrNull()?.url ?: ""
        Image(
            painter = rememberAsyncImagePainter(imageUrl),
            contentDescription = track.name,
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(4.dp)),
            contentScale = ContentScale.Crop
        )
        
        Spacer(modifier = Modifier.width(12.dp))
        
        // Track info
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = track.name,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            
            Text(
                text = track.artists.joinToString(", ") { it.name },
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.7f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        
        // Play button
        IconButton(
            onClick = { onPlayTrack(track) }
        ) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "Play",
                tint = Color.White
            )
        }
    }
}

@Composable
fun AlbumsSection(
    albums: List<SpotifyAlbum>,
    showAll: Boolean,
    onToggleShowAll: (Boolean) -> Unit,
    onAlbumClick: (SpotifyAlbum) -> Unit
) {
    val displayAlbums = if (showAll) albums else albums.take(10)
    
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Albums",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            
            if (albums.size > 10) {
                TextButton(
                    onClick = { onToggleShowAll(!showAll) }
                ) {
                    Text(
                        text = if (showAll) "Show less" else "See all",
                        color = Color(0xFF1DE9FF)
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(displayAlbums) { album ->
                AlbumItem(
                    album = album,
                    onAlbumClick = onAlbumClick
                )
            }
        }
    }
}

@Composable
fun AlbumItem(
    album: SpotifyAlbum,
    onAlbumClick: (SpotifyAlbum) -> Unit
) {
    val imageUrl = album.images?.firstOrNull()?.url ?: ""
    
    Column(
        modifier = Modifier
            .width(140.dp)
            .clickable { onAlbumClick(album) }
    ) {
        Image(
            painter = rememberAsyncImagePainter(imageUrl),
            contentDescription = album.name,
            modifier = Modifier
                .size(140.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = album.name,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White,
            fontWeight = FontWeight.Medium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        
        Text(
            text = album.releaseDate?.substring(0, 4) ?: "",
            style = MaterialTheme.typography.bodySmall,
            color = Color.White.copy(alpha = 0.7f)
        )
    }
}

@Composable
fun RelatedArtistsSection(
    artists: List<SpotifyArtist>,
    showAll: Boolean,
    onToggleShowAll: (Boolean) -> Unit,
    onArtistClick: (SpotifyArtist) -> Unit
) {
    val displayArtists = if (showAll) artists else artists.take(10)
    
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Fans also like",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            
            if (artists.size > 10) {
                TextButton(
                    onClick = { onToggleShowAll(!showAll) }
                ) {
                    Text(
                        text = if (showAll) "Show less" else "See all",
                        color = Color(0xFF1DE9FF)
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(displayArtists) { artist ->
                RelatedArtistItem(
                    artist = artist,
                    onArtistClick = onArtistClick
                )
            }
        }
    }
}

@Composable
fun RelatedArtistItem(
    artist: SpotifyArtist,
    onArtistClick: (SpotifyArtist) -> Unit
) {
    val imageUrl = artist.images?.firstOrNull()?.url ?: ""
    
    Column(
        modifier = Modifier
            .width(120.dp)
            .clickable { onArtistClick(artist) },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = rememberAsyncImagePainter(imageUrl),
            contentDescription = artist.name,
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = artist.name,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun ArtistScreenShimmer() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        // Header shimmer - giống ArtistHeader thật
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF1DE9FF),
                                Color(0xFF191414)
                            )
                        )
                    )
            ) {
                // Artist image shimmer (hình tròn ở giữa với offset)
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .align(Alignment.Center)
                        .offset(y = (-75).dp)
                        .clip(CircleShape)
                        .background(Color.Gray)
                        .placeholder(
                            visible = true,
                            highlight = PlaceholderHighlight.shimmer()
                        )
                )
                
                // Artist info shimmer ở dưới
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Artist name shimmer
                    Box(
                        modifier = Modifier
                            .height(32.dp)
                            .width(200.dp)
                            .background(Color.Gray)
                            .placeholder(
                                visible = true,
                                highlight = PlaceholderHighlight.shimmer()
                            )
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Genres shimmer
                    Box(
                        modifier = Modifier
                            .height(20.dp)
                            .width(150.dp)
                            .background(Color.Gray)
                            .placeholder(
                                visible = true,
                                highlight = PlaceholderHighlight.shimmer()
                            )
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Followers shimmer
                    Box(
                        modifier = Modifier
                            .height(16.dp)
                            .width(100.dp)
                            .background(Color.Gray)
                            .placeholder(
                                visible = true,
                                highlight = PlaceholderHighlight.shimmer()
                            )
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Play button shimmer
                    Box(
                        modifier = Modifier
                            .height(48.dp)
                            .width(120.dp)
                            .background(Color.Gray)
                            .placeholder(
                                visible = true,
                                highlight = PlaceholderHighlight.shimmer()
                            )
                    )
                }
            }
        }
        
        // Popular Tracks Section shimmer
        item {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .height(24.dp)
                            .width(80.dp)
                            .background(Color.Gray)
                            .placeholder(
                                visible = true,
                                highlight = PlaceholderHighlight.shimmer()
                            )
                    )
                    
                    Box(
                        modifier = Modifier
                            .height(20.dp)
                            .width(60.dp)
                            .background(Color.Gray)
                            .placeholder(
                                visible = true,
                                highlight = PlaceholderHighlight.shimmer()
                            )
                    )
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // Track items shimmer
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    repeat(5) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(Color.Gray)
                                    .placeholder(
                                        visible = true,
                                        highlight = PlaceholderHighlight.shimmer()
                                    )
                            )
                            
                            Spacer(modifier = Modifier.width(12.dp))
                            
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .height(16.dp)
                                        .width(200.dp)
                                        .background(Color.Gray)
                                        .placeholder(
                                            visible = true,
                                            highlight = PlaceholderHighlight.shimmer()
                                        )
                                )
                                
                                Spacer(modifier = Modifier.height(4.dp))
                                
                                Box(
                                    modifier = Modifier
                                        .height(14.dp)
                                        .width(150.dp)
                                        .background(Color.Gray)
                                        .placeholder(
                                            visible = true,
                                            highlight = PlaceholderHighlight.shimmer()
                                        )
                                )
                            }
                            
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .background(Color.Gray)
                                    .placeholder(
                                        visible = true,
                                        highlight = PlaceholderHighlight.shimmer()
                                    )
                            )
                        }
                    }
                }
            }
        }
        
        // Albums Section shimmer
        item {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .height(24.dp)
                            .width(80.dp)
                            .background(Color.Gray)
                            .placeholder(
                                visible = true,
                                highlight = PlaceholderHighlight.shimmer()
                            )
                    )
                    
                    Box(
                        modifier = Modifier
                            .height(20.dp)
                            .width(60.dp)
                            .background(Color.Gray)
                            .placeholder(
                                visible = true,
                                highlight = PlaceholderHighlight.shimmer()
                            )
                    )
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    repeat(5) {
                        Column {
                            Box(
                                modifier = Modifier
                                    .size(140.dp)
                                    .background(Color.Gray)
                                    .placeholder(
                                        visible = true,
                                        highlight = PlaceholderHighlight.shimmer()
                                    )
                            )
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            Box(
                                modifier = Modifier
                                    .height(16.dp)
                                    .width(120.dp)
                                    .background(Color.Gray)
                                    .placeholder(
                                        visible = true,
                                        highlight = PlaceholderHighlight.shimmer()
                                    )
                            )
                            
                            Box(
                                modifier = Modifier
                                    .height(14.dp)
                                    .width(40.dp)
                                    .background(Color.Gray)
                                    .placeholder(
                                        visible = true,
                                        highlight = PlaceholderHighlight.shimmer()
                                    )
                            )
                        }
                    }
                }
            }
        }
        
        // Related Artists Section shimmer
        item {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .height(24.dp)
                            .width(120.dp)
                            .background(Color.Gray)
                            .placeholder(
                                visible = true,
                                highlight = PlaceholderHighlight.shimmer()
                            )
                    )
                    
                    Box(
                        modifier = Modifier
                            .height(20.dp)
                            .width(60.dp)
                            .background(Color.Gray)
                            .placeholder(
                                visible = true,
                                highlight = PlaceholderHighlight.shimmer()
                            )
                    )
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    repeat(5) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(120.dp)
                                    .clip(CircleShape)
                                    .background(Color.Gray)
                                    .placeholder(
                                        visible = true,
                                        highlight = PlaceholderHighlight.shimmer()
                                    )
                            )
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            Box(
                                modifier = Modifier
                                    .height(16.dp)
                                    .width(100.dp)
                                    .background(Color.Gray)
                                    .placeholder(
                                        visible = true,
                                        highlight = PlaceholderHighlight.shimmer()
                                    )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ErrorContent(
    error: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Error",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = error,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF1DE9FF)
            )
        ) {
            Text("Retry")
        }
    }
}

private fun formatNumber(number: Int): String {
    return when {
        number >= 1_000_000 -> "${number / 1_000_000}M"
        number >= 1_000 -> "${number / 1_000}K"
        else -> number.toString()
    }
}