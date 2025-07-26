package com.example.mymuzic.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material3.placeholder
import com.google.accompanist.placeholder.material3.shimmer

// Header dùng chung cho Album/Playlist
@Composable
fun MusicHeader(
    imageUrl: String?,
    title: String,
    subtitle: String?,
    info: String?,
    onPlay: (() -> Unit)? = null
) {
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
        // Image
        if (imageUrl != null && imageUrl.isNotBlank()) {
            Image(
                painter = rememberAsyncImagePainter(imageUrl),
                contentDescription = title,
                modifier = Modifier
                    .size(200.dp)
                    .align(Alignment.Center)
                    .offset(y = (-75).dp)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )
        } else {
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .align(Alignment.Center)
                    .offset(y = (-75).dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.Gray)
            )
        }
        // Info overlay
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            if (!subtitle.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center
                )
            }
            if (!info.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = info,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }
            if (onPlay != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onPlay,
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
}

// Track item dùng chung
@Composable
fun TrackItem(
    trackNumber: Int?,
    imageUrl: String?,
    name: String,
    artists: String,
    duration: String,
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .let { if (onClick != null) it.clickable { onClick() } else it }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Track number
        if (trackNumber != null) {
            Text(
                text = trackNumber.toString(),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.7f),
                modifier = Modifier.width(32.dp)
            )
        }
        // Track image
        if (!imageUrl.isNullOrBlank()) {
            Image(
                painter = rememberAsyncImagePainter(imageUrl),
                contentDescription = name,
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(4.dp)),
                contentScale = ContentScale.Crop
            )
        } else {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color.Gray)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        // Track info
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = artists,
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.7f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        // Duration
        Text(
            text = duration,
            style = MaterialTheme.typography.bodySmall,
            color = Color.White.copy(alpha = 0.7f),
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        // Play button
        if (onClick != null) {
            IconButton(
                onClick = onClick
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Play",
                    tint = Color.White
                )
            }
        }
    }
}

// Track list dùng chung
@Composable
fun TrackList(
    tracks: List<Any>, // List<SpotifyTrack> hoặc List<PlaylistTrackItem>
    getTrackNumber: (Any) -> Int?,
    getImageUrl: (Any) -> String?,
    getName: (Any) -> String,
    getArtists: (Any) -> String,
    getDuration: (Any) -> String,
    onTrackClick: (Any) -> Unit
) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 0.dp),
    ) {
        tracks.forEachIndexed { index, track ->
            TrackItem(
                trackNumber = getTrackNumber(track),
                imageUrl = getImageUrl(track),
                name = getName(track),
                artists = getArtists(track),
                duration = getDuration(track),
                onClick = { onTrackClick(track) }
            )
        }
    }
}

// Shimmer cho header
@Composable
fun MusicHeaderShimmer() {
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
        Box(
            modifier = Modifier
                .size(200.dp)
                .align(Alignment.Center)
                .offset(y = (-75).dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.Gray)
                .placeholder(
                    visible = true,
                    highlight = PlaceholderHighlight.shimmer()
                )
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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

// Shimmer cho track list
@Composable
fun TrackListShimmer(count: Int = 10) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(count) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .width(32.dp)
                        .height(16.dp)
                        .background(Color.Gray)
                        .placeholder(
                            visible = true,
                            highlight = PlaceholderHighlight.shimmer()
                        )
                )
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
                        .height(14.dp)
                        .width(40.dp)
                        .background(Color.Gray)
                        .placeholder(
                            visible = true,
                            highlight = PlaceholderHighlight.shimmer()
                        )
                )
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