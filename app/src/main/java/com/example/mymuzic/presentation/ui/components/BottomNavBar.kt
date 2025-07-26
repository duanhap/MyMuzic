package com.example.mymuzic.presentation.screen

import android.graphics.drawable.BitmapDrawable
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.example.mymuzic.data.model.response.RecentlyPlayedItem
import androidx.palette.graphics.Palette
import com.example.mymuzic.data.model.music.SpotifyTrack
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.example.mymuzic.presentation.screen.MusicViewModel
import com.example.mymuzic.presentation.screen.MusicEvent

sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    object Home : BottomNavItem("home", Icons.Filled.Home, "Home")
    object Explore : BottomNavItem("explore", Icons.Filled.Explore, "Explore")
    object Library : BottomNavItem("library", Icons.Filled.LibraryMusic, "Library")
}

val bottomNavItems = listOf(
    BottomNavItem.Home,
    BottomNavItem.Explore,
    BottomNavItem.Library
)

@Composable
fun BottomNavBar(
    navController: NavController, 
    currentRoute: String?,
    musicViewModel: MusicViewModel
) {
    val musicUiState by musicViewModel.uiState.collectAsState()
    val recentlyPlayed = musicUiState.recentlyPlayed
    val currentPlayingTrack = musicUiState.currentPlayingTrack
    
    // Debug log để kiểm tra instance
    LaunchedEffect(Unit) {
        Log.d("BottomNavBar", "=== BottomNavBar được khởi tạo ===")
        Log.d("BottomNavBar", "MusicViewModel instance hash: ${musicViewModel.hashCode()}")
        Log.d("BottomNavBar", "Current playing track: ${currentPlayingTrack?.name}")
        Log.d("BottomNavBar", "Is currently playing: ${musicUiState.isCurrentlyPlaying}")
    }
    
    // Fetch recently played data
    LaunchedEffect(Unit) {
        musicViewModel.handleEvent(MusicEvent.FetchRecentlyPlayed)
    }
    
    // Debug log khi state thay đổi
    LaunchedEffect(currentPlayingTrack, musicUiState.isCurrentlyPlaying) {
        Log.d("BottomNavBar", "=== State thay đổi trong BottomNavBar ===")
        Log.d("BottomNavBar", "Current playing track: ${currentPlayingTrack?.name}")
        Log.d("BottomNavBar", "Is currently playing: ${musicUiState.isCurrentlyPlaying}")
        Log.d("BottomNavBar", "MusicViewModel instance hash: ${musicViewModel.hashCode()}")
    }
    
    // Debug log
    android.util.Log.d("BottomNavBar", "Recently played size: ${recentlyPlayed.size}")
    android.util.Log.d("BottomNavBar", "Current playing track: ${currentPlayingTrack?.name}")
    
    Column {
        // Mini Player Bar - ưu tiên currentPlayingTrack, fallback về recentlyPlayed.first()
        val trackToShow = currentPlayingTrack ?: recentlyPlayed.firstOrNull()?.track
        var dynamicGradient by remember { mutableStateOf<Brush?>(null) }
        val context = LocalContext.current
        if (trackToShow != null) {
            LaunchedEffect(trackToShow.album?.images?.firstOrNull()?.url) {
                val imageUrl = trackToShow.album?.images?.firstOrNull()?.url
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
                                        //Color(mutedColor).copy(alpha = 0.8f),
                                        Color(dominantColor),
                                        //Color(vibrantColor),
                                        Color(mutedColor).copy(alpha = 0.8f)
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

            android.util.Log.d("BottomNavBar", "Showing MiniPlayerBar for: ${trackToShow.name}")
            MiniPlayerBar(
                dynamicGradient = dynamicGradient,
                track = trackToShow,
                isPlaying = musicUiState.isCurrentlyPlaying,
                onPlayPauseClick = {
                    Log.d("BottomNavBar", "Play/Pause button được nhấn")
                    musicViewModel.handleEvent(MusicEvent.TogglePlayPause)
                },
                navController = navController
            )
        } else {
            android.util.Log.d("BottomNavBar", "No track to show")
        }

        
        // Bottom Navigation
        NavigationBar(
            containerColor = Color.Black
        ) {
            bottomNavItems.forEach { item ->
                val selected = currentRoute == item.route
                NavigationBarItem(
                    selected = selected,
                    onClick = {
                        if (!selected) {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                    icon = {
                        Icon(
                            item.icon,
                            contentDescription = item.label,
                            tint = if (selected) Color(0xFF1DE9FF) else Color.White
                        )
                    },
                    label = {
                        Text(
                            item.label,
                            color = if (selected) Color(0xFF1DE9FF) else Color.White
                        )
                    },
                    alwaysShowLabel = true,
                    colors = androidx.compose.material3.NavigationBarItemDefaults.colors(
                        indicatorColor = Color.Transparent
                    )
                )
            }
        }
    }
}

@Composable
fun MiniPlayerBar(
    dynamicGradient: Brush?,
    track: SpotifyTrack, 
    isPlaying: Boolean,
    onPlayPauseClick: () -> Unit,
    navController: NavController
) {


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                dynamicGradient ?: Brush.verticalGradient(
                    colors = listOf(
//                        Color(0xFF128693),
//                        Color(0xFF1CD5EA),
//                        Color(0xFF128693)
                        Color(0xFF000000),
                        Color(0xFF000000),
                        Color(0xFF313131)

                    )
                )
            )
            .clickable {
                navController.navigate(
                    "play_song/${track.id}?name=${track.name}" +
                            "&imageUrl=${track.album?.images?.firstOrNull()?.url ?: ""}" +
                            "&artist=${track.artists.joinToString(", ") { it.name }}"
                )
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Album Image
            Image(
                painter = rememberAsyncImagePainter(track.album?.images?.firstOrNull()?.url),
                contentDescription = track.name,
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(4.dp)),
                contentScale = ContentScale.Crop
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            // Track Info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = track.name,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1
                )
                Text(
                    text = track.artists.joinToString(", ") { it.name },
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 12.sp,
                    maxLines = 1
                )
            }
            
            // Play/Pause Button
            IconButton(
                onClick = { onPlayPauseClick() }
            ) {
                Icon(
                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (isPlaying) "Pause" else "Play",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
} 