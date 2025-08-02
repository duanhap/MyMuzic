package com.example.mymuzic.presentation.screen

import android.content.Context
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.util.Log
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.mymuzic.R
import com.example.mymuzic.data.model.music.SpotifyAlbum
import com.example.mymuzic.data.model.music.SpotifyArtist
import com.example.mymuzic.data.model.music.SpotifyImage
import com.example.mymuzic.data.model.music.SpotifyTrack
import org.koin.androidx.compose.koinViewModel
import com.example.mymuzic.presentation.screen.MusicViewModel
import com.example.mymuzic.presentation.screen.MusicEvent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.palette.graphics.Palette
import coil.request.ImageRequest
import coil.request.SuccessResult

@Composable
fun PlaySongScreen(
    navController: NavController,
    id: String,
    name: String,
    imageUrl: String,
    artist: String,
    durationMs: Int = 0,
    viewModel: PlaySongViewModel = koinViewModel(),
    musicViewModel: MusicViewModel
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val isConnected by viewModel.isConnected.collectAsState()

    val musicUiState by musicViewModel.uiState.collectAsState()
    var isFavorite by remember { mutableStateOf(false) }
    var lyrics by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(id) {
        viewModel.handleEvent(PlaySongEvent.FetchTrackDetail(id))
    }

    val displayTrack = uiState.spotifyTrack ?: SpotifyTrack(
        id = id,
        name = name,
        album = SpotifyAlbum(
            id = "", 
            name = "", 
            images = listOf(SpotifyImage(imageUrl, 0, 0)),
            albumType = null,
            totalTracks = null,
            releaseDate = null,
            releaseDatePrecision = null,
            artists = null,
            externalUrls = null,
            uri = null,
            albumGroup = null
        ),
        artists = listOf(SpotifyArtist(id = "", name = artist)),
        duration_ms = durationMs,
        external_urls = mapOf(),
        uri = "",
        preview_url = null,
        explicit = false,
        popularity = 0,
        is_playable = true,
        track_number = 1
    )

    // Sửa logic: chỉ bài nào là currentPlayingTrack mới hiển thị đúng trạng thái
    val isCurrentTrack = musicUiState.currentPlayingTrack?.id == displayTrack.id
    val isPlaying = isCurrentTrack && musicUiState.isCurrentlyPlaying

    // Giả lập progress
    val durationSec = (displayTrack.duration_ms / 1000f).coerceAtLeast(1f)
    var progress by remember { mutableStateOf(0f) }

    // Reset progress khi đổi bài
    LaunchedEffect(displayTrack.id) {
        progress = 0f
    }
    // Tăng progress khi đang phát và là current track
    LaunchedEffect(isPlaying, isCurrentTrack) {
        if (isPlaying && isCurrentTrack) {
            while (progress < 1f) {
                kotlinx.coroutines.delay(1000)
                progress = (progress + 1f / durationSec).coerceAtMost(1f)
            }
        }
    }

    // Tính current time string
    val currentTimeSec = (progress * durationSec).toInt()
    val currentMinutes = currentTimeSec / 60
    val currentSeconds = currentTimeSec % 60
    val currentTimeStr = String.format("%d:%02d", currentMinutes, currentSeconds)

    fun handlePlayPause() {
        if (!isPlaying) {
            displayTrack.uri.takeIf { it.isNotBlank() }?.let {
                viewModel.playTrack(it)
            }
        }
        // Cập nhật current playing track trong MusicViewModel
        musicViewModel.handleEvent(MusicEvent.UpdateCurrentPlayingTrack(displayTrack, !isPlaying))
    }

    PlaySongContent(
        track = displayTrack,
        isPlaying = isPlaying,
        progress = progress,
        currentTimeStr = currentTimeStr,
        onPlayPause = { handlePlayPause() },
        onNext = {},
        onPrev = {},
        onSeek = { value -> progress = value },
        onToggleFavorite = { isFavorite = !isFavorite },
        isFavorite = isFavorite,
        lyrics = lyrics,
        onDownload = {},
        onShare = {},
        onBackClick = navController::popBackStack,
        onSpotifyClick = {
            // Cập nhật current playing track khi nhấn "Nghe trên Spotify"
            musicViewModel.handleEvent(MusicEvent.UpdateCurrentPlayingTrack(displayTrack, true))
        },
        context= context
    )

//    // Hiển thị trạng thái kết nối Spotify
//    if (!isConnected) {
//        Text(
//            text = "Chưa kết nối Spotify App Remote",
//            color = Color.Red,
//            modifier = Modifier.padding(horizontal = 20.dp, vertical = 17.dp)
//            modifier = Modifier.padding(horizontal = 20.dp, vertical = 17.dp)
//        )
//    }
}

@Composable
fun PlaySongContent(
    track: SpotifyTrack,
    isPlaying: Boolean,
    progress: Float,
    currentTimeStr: String,
    onPlayPause: () -> Unit,
    onNext: () -> Unit,
    onPrev: () -> Unit,
    onSeek: (Float) -> Unit,
    onToggleFavorite: () -> Unit,
    isFavorite: Boolean,
    lyrics: String? = null,
    onDownload: () -> Unit,
    onShare: () -> Unit,
    onBackClick: () -> Unit,
    onSpotifyClick: () -> Unit,
    context: Context
) {
    val duration = track.duration_ms
    val minutes = duration / 60000
    val seconds = (duration % 60000) / 1000
    val durationStr = if (duration > 0) "$minutes:${seconds.toString().padStart(2, '0')}" else "--:--"
    var dynamicGradient by remember { mutableStateOf<Brush?>(null) }
            if (track.album?.images?.firstOrNull()?.url != null) {
            LaunchedEffect(track.album?.images?.firstOrNull()?.url) {
                val imageUrl = track.album?.images?.firstOrNull()?.url
            if (!imageUrl.isNullOrEmpty()) {
                try {
                    val request = ImageRequest.Builder(context)
                        .data(imageUrl)
                        .allowHardware(false) // ⚠️ BẮT BUỘC để lấy bitmap
                        .build()

                    val result = coil.ImageLoader(context).execute(request)

                    if (result is SuccessResult) {
                        val bitmap = (result.drawable as? BitmapDrawable)?.bitmap
                        if (bitmap != null) {
                            val palette = Palette.from(bitmap).generate()
                            val dominantColor = palette.getDominantColor(Color(0xFF1E3A8A).toArgb())
                            val vibrantColor = palette.getVibrantColor(dominantColor)
                            val mutedColor = palette.getMutedColor(dominantColor)

                            dynamicGradient = Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFF000000),
                                    Color(0xFF000000),
                                    //Color(mutedColor).copy(alpha = 0.8f),
//                                    Color(dominantColor),
                                    Color(vibrantColor),
   //                               Color(mutedColor).copy(alpha = 0.3f)
                                )
                            )
                        }
                    }
                } catch (e: Exception) {
                    dynamicGradient = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF128693),
                            Color(0xFF1CD5EA),
                            Color(0xFF128693)
                        )
                    )
                }
            }
        }

    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                dynamicGradient ?: Brush.verticalGradient(
                    colors = listOf(Color(0xFF000000),Color(0xFF727272))
                ))
            .padding(horizontal = 24.dp)
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Row {
            IconButton(onClick = onBackClick, modifier = Modifier.offset(x = -12.dp)) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
            Column {
                Text(
                    text = "PLAYING FROM PLAYLIST:",
                    color = Color.Gray,
                    fontSize = 12.sp,
                    letterSpacing = 1.2.sp
                )
                Text(
                    text = track.album?.name ?: "",
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
        Box {
            Image(
                painter = rememberAsyncImagePainter(track.album?.images?.firstOrNull()?.url),
                contentDescription = track.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )
            Box(modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomEnd)
                .padding(5.dp)) {
                var expanded by remember { mutableStateOf(false) }
                val scale by animateFloatAsState(if (expanded) 1.1f else 1f)
                Row(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Icon Spotify tròn
                    val rotation by animateFloatAsState(
                        targetValue = if (expanded) 180f else 0f,
                        animationSpec = tween(
                            durationMillis = 600, // thời gian xoay (ms)
                            easing = FastOutSlowInEasing // chuyển động mượt
                        )
                    )
                    IconButton(
                        onClick = { expanded = !expanded },
                        modifier = Modifier
                            .size(48.dp)
                            .background(Color.White, shape = CircleShape)
                            .scale(scale)
                            .graphicsLayer {
                                rotationZ = rotation // Góc xoay theo trục Z
                            }

                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.spotify),
                            contentDescription = "Spotify",
                            tint = Color.Unspecified,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    AnimatedVisibility(
                        visible = expanded,
                        enter = fadeIn() + expandHorizontally(),
                        exit = fadeOut() + shrinkHorizontally()
                    ) {
                        Button(
                            onClick = {
                                val intent = Intent(Intent.ACTION_VIEW).apply {
                                    data = Uri.parse("spotify:track:${track.id}")
                                    putExtra(Intent.EXTRA_REFERRER, Uri.parse("android-app://" + context.packageName))
                                }
                                context.startActivity(intent)
                                onSpotifyClick()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF1DB954),
                                contentColor = Color.White
                            ),
                            modifier = Modifier.padding(start = 8.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.spotify),
                                contentDescription = null,
                                tint = Color.Unspecified,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Nghe trên Spotify")
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        // Tên bài hát & nghệ sĩ & explicit
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = track.name,
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            if (track.explicit) {
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "E",
                    color = Color.Red,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .background(Color.White, shape = RoundedCornerShape(4.dp))
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                )
            }
        }
        Text(
            text = track.artists.joinToString(", ") { it.name },
            color = Color.Gray,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        // Popularity
//        if (track.popularity > 0) {
//            Text(
//                text = "Popularity: ${track.popularity}",
//                color = Color.Yellow,
//                fontSize = 14.sp
//            )
//        }


        Spacer(modifier = Modifier.height(8.dp))

        // Nút share & favorite
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            IconButton(onClick = onShare) {
                Icon(Icons.Default.Share, contentDescription = "Share", tint = Color.White)
            }
            // Nút download
            IconButton(onClick = onDownload) {
                Icon(Icons.Default.Download, contentDescription = "Download", tint = Color.White)
            }
            Spacer(modifier = Modifier.weight(1f)) // <-- đúng nè
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
            Text(currentTimeStr, color = Color.White, fontSize = 12.sp)
            Text(durationStr, color = Color.White, fontSize = 12.sp)
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
                //enabled = track.is_playable == true,
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
            images = listOf(SpotifyImage("https://i.imgur.com/your_image.jpg", 300, 300)),
            albumType = null,
            totalTracks = null,
            releaseDate = null,
            releaseDatePrecision = null,
            artists = null,
            externalUrls = null,
            uri = null,
            albumGroup = null
        ),
        artists = listOf(SpotifyArtist("1", "moody.")),
        duration_ms = 163000,
        external_urls = mapOf(),
        uri = "",
        preview_url = null,
        explicit = false,
        popularity = 0,
        is_playable = true,
        track_number = 1
    )
    PlaySongContent(
        track = mockTrack,
        isPlaying = false,
        progress = 0.0f,
        currentTimeStr = "0:00",
        onPlayPause = {},
        onNext = {},
        onPrev = {},
        onSeek = {},
        onToggleFavorite = {},
        isFavorite = false,
        lyrics = "You never look at the sky\nCause you think it's too high...",
        onDownload = {},
        onShare = {},
        onBackClick = {},
        onSpotifyClick = {},
        context = LocalContext.current
    )
}