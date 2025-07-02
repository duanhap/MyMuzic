package com.example.mymuzic.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.mymuzic.data.model.SpotifyAlbum
import com.example.mymuzic.data.model.SpotifyArtist
import com.example.mymuzic.data.model.SpotifyImage
import com.example.mymuzic.data.model.SpotifyTrack

@Composable
fun PlaySongScreen(
    track: SpotifyTrack,
    isPlaying: Boolean,
    progress: Float, // 0f..1f
    onPlayPause: () -> Unit,
    onNext: () -> Unit,
    onPrev: () -> Unit,
    onSeek: (Float) -> Unit,
    onToggleFavorite: () -> Unit,
    isFavorite: Boolean,
    lyrics: String? = null,
    onDownload: () -> Unit,
    onShare: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(horizontal = 24.dp)
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Row {
            Column {
                Text(
                    text = "PLAYING FROM PLAYLIST:",
                    color = Color.Gray,
                    fontSize = 12.sp,
                    letterSpacing = 1.2.sp
                )
                Text(
                    text = "Lofi Loft", // Có thể truyền động
                    color = Color.Cyan,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = onShare) {
                Icon(Icons.Default.MoreVert, contentDescription = "Share", tint = Color.White)
            }

        }

        Spacer(modifier = Modifier.height(16.dp))
        // Ảnh bìa
        Image(
            painter = rememberAsyncImagePainter(track.album.images?.firstOrNull()?.url),
            contentDescription = track.name,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(24.dp))
        // Tên bài hát & nghệ sĩ
        Text(
            text = track.name,
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = track.artists.joinToString(", ") { it.name },
            color = Color.Gray,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        // Nút share & favorite
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onShare) {
                Icon(Icons.Default.Share, contentDescription = "Share", tint = Color.White)
            }
            IconButton(onClick = onToggleFavorite) {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite",
                    tint = if (isFavorite) Color.Red else Color.White
                )
            }
        }
        // Thanh progress
        Slider(
            value = progress,
            onValueChange = onSeek,
            modifier = Modifier.fillMaxWidth(),
            colors = SliderDefaults.colors(
                thumbColor = Color.Cyan,
                activeTrackColor = Color.Cyan
            )
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("0:00", color = Color.White, fontSize = 12.sp)
            Text("2:43", color = Color.White, fontSize = 12.sp) // Có thể truyền động
        }
        Spacer(modifier = Modifier.height(8.dp))
        // Nút điều khiển
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { /* repeat */ }) {
                Icon(Icons.Default.Repeat, contentDescription = "Repeat", tint = Color.White)
            }
            IconButton(onClick = onPrev) {
                Icon(Icons.Default.SkipPrevious, contentDescription = "Previous", tint = Color.White)
            }
            IconButton(
                onClick = onPlayPause,
                modifier = Modifier
                    .size(64.dp)
                    .background(Color.Cyan, shape = CircleShape)
            ) {
                Icon(
                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = "Play/Pause",
                    tint = Color.White,
                    modifier = Modifier.size(40.dp)
                )
            }
            IconButton(onClick = onNext) {
                Icon(Icons.Default.SkipNext, contentDescription = "Next", tint = Color.White)
            }
            IconButton(onClick = { /* volume */ }) {
                Icon(Icons.Default.VolumeUp, contentDescription = "Volume", tint = Color.White)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        // Nút download
        IconButton(onClick = onDownload, modifier = Modifier.align(Alignment.End)) {
            Icon(Icons.Default.Download, contentDescription = "Download", tint = Color.White)
        }
        Spacer(modifier = Modifier.height(8.dp))
        // Lyrics
        if (!lyrics.isNullOrEmpty()) {
            Text(
                text = "LYRICS",
                color = Color.Gray,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF3DD0E1), shape = RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Text(
                    text = lyrics,
                    color = Color.White,
                    fontSize = 16.sp
                )
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun PlaySongScreenPreview() {
    val mockTrack = SpotifyTrack(
        id = "1",
        name = "grainy days",
        album = SpotifyAlbum(
            id = "1",
            name = "Lofi Loft",
            images = listOf(SpotifyImage("https://i.imgur.com/your_image.jpg", 300, 300))
        ),
        artists = listOf(SpotifyArtist("1", "moody.")),
        duration_ms = 163000,
        external_urls = mapOf(),
        uri = ""
    )
    PlaySongScreen(
        track = mockTrack,
        isPlaying = false,
        progress = 0.0f,
        onPlayPause = {},
        onNext = {},
        onPrev = {},
        onSeek = {},
        onToggleFavorite = {},
        isFavorite = false,
        lyrics = "You never look at the sky\nCause you think it's too high...",
        onDownload = {},
        onShare = {}
    )
}