@file:Suppress("UNUSED_EXPRESSION")

package com.example.mymuzic.presentation.screen

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.mymuzic.R
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.collectAsState
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import androidx.navigation.NavController
import com.example.mymuzic.data.model.response.RecentlyPlayedItem
import com.example.mymuzic.data.model.music.SpotifyArtist
import com.example.mymuzic.data.model.music.SpotifyTrack
import com.example.mymuzic.presentation.navigation.Routes
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material3.placeholder
import com.google.accompanist.placeholder.material3.shimmer


// --- Data models ---
data class UserProfile(val name: String, val avatarUrl: String)




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val viewModel: AuthViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    var showProfileSheet = remember { mutableStateOf(false) }

    // Lấy thông tin user từ Spotify API
    val userProfile = uiState.userProfile
    val displayName = userProfile?.displayName ?: "User"
    val avatarUrl = userProfile?.images?.firstOrNull()?.url ?: ""
    val isLoading = userProfile == null || uiState.isLoading

    // Gọi fetchRecentlyPlayed khi HomeScreen được mở
    LaunchedEffect(Unit) {
        viewModel.handleEvent(AuthEvent.FetchRecentlyPlayed)
        viewModel.handleEvent(AuthEvent.FetchTopTracks)
        viewModel.handleEvent(AuthEvent.FetchRecentArtists)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF18181A))
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        HomeHeader(
            user = UserProfile(name = displayName, avatarUrl = avatarUrl),
            onAvatarClick = { showProfileSheet.value = true },
            isLoading = isLoading
        )
        Spacer(modifier = Modifier.height(16.dp))
        ContinueListeningSection(
            playlists = uiState.recentlyPlayed,
            isLoading = isLoading,
            navController = navController
        )
        Spacer(modifier = Modifier.height(24.dp))
        TopMixesSection(
            mixes = uiState.topTracks,
            isLoading = isLoading,
            navController = navController
        )
        Spacer(modifier = Modifier.height(24.dp))
        RecentListeningSection(
            artists = uiState.recentArtists,
            isLoading = isLoading
        )
    }

    if (showProfileSheet.value) {
        ModalBottomSheet(
            onDismissRequest = { showProfileSheet.value = false },
            sheetState = rememberModalBottomSheetState()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = rememberAsyncImagePainter(avatarUrl),
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = displayName, color = Color.Black, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(24.dp))
                OutlinedButton(
                    onClick = { 
                        showProfileSheet.value = false
                        navController.navigate(Routes.PROFILE)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Hồ sơ", fontWeight = FontWeight.Medium)
                }
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = {
                        coroutineScope.launch {
                            viewModel.handleEvent(AuthEvent.Logout)
                            showProfileSheet.value = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1DB954), contentColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Log out", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun HomeHeader(user: UserProfile, onAvatarClick: () -> Unit, isLoading: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = if (isLoading || user.avatarUrl.isEmpty()) painterResource(R.drawable.ic_launcher_foreground)
                      else rememberAsyncImagePainter(user.avatarUrl),
            contentDescription = "Avatar",
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .clickable(enabled = !isLoading) { onAvatarClick() }
                .placeholder(
                    visible = isLoading,
                    highlight = PlaceholderHighlight.shimmer()
                )
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = if (isLoading) "\u200B" else "Welcome back !",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.placeholder(visible = isLoading, highlight = PlaceholderHighlight.shimmer())
            )
            Text(
                text = if (isLoading) "\u200B" else user.name,
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 14.sp,
                modifier = Modifier.placeholder(visible = isLoading, highlight = PlaceholderHighlight.shimmer())
            )
        }
        IconButton(onClick = { /* TODO: Analytics */ }, enabled = !isLoading) {
            Icon(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Analytics",
                tint = Color.White
            )
        }
        IconButton(onClick = { /* TODO: Notifications */ }, enabled = !isLoading) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Notifications",
                tint = Color.White
            )
        }
        IconButton(onClick = { /* TODO: Settings */ }, enabled = !isLoading) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Settings",
                tint = Color.White
            )
        }
    }
}

@Composable
fun ContinueListeningSection(playlists: List<RecentlyPlayedItem>, isLoading: Boolean = false,navController: NavController) {
    Text(
        text = "Continue Listening",
        color = Color.White,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(bottom = 8.dp)
    )
    if (isLoading || playlists.isEmpty()) {
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(5) { PlaylistCardShimmer() }
        }
    } else {
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(playlists) { item ->
                PlaylistCard(item){
                    navController.navigate("play_song/${item.track.id}?name=${item.track.name}" +
                            "&imageUrl=${item.track.album.images?.firstOrNull()?.url ?: ""}" +
                            "&artist=${item.track.artists.joinToString(", ") { it.name }}")
                }
            }
        }
    }
}

@Composable
fun PlaylistCardShimmer() {
    Column(
        modifier = Modifier
            .width(140.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF232326))
            .padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .height(80.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .placeholder(visible = true, highlight = PlaceholderHighlight.shimmer())
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .height(16.dp)
                .fillMaxWidth(0.7f)
                .placeholder(visible = true, highlight = PlaceholderHighlight.shimmer())
        )
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .height(12.dp)
                .fillMaxWidth(0.5f)
                .placeholder(visible = true, highlight = PlaceholderHighlight.shimmer())
        )
    }
}

@Composable
fun PlaylistCard(item: RecentlyPlayedItem,onClick: () -> Unit) {
    val track = item.track
    val albumImage = track.album.images?.firstOrNull()?.url ?: ""
    val artistNames = track.artists.joinToString(", ") { it.name }
    
    Column(
        modifier = Modifier
            .width(140.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF232326))
            .padding(8.dp)
            .clickable { 
                android.util.Log.d("Navigation", "PlaylistCard clicked!")
                onClick()
            }
    ) {
        Image(
            painter = rememberAsyncImagePainter(albumImage),
            contentDescription = track.name,
            modifier = Modifier
                .height(80.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = track.name,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1
        )
        Text(
            text = artistNames,
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 12.sp,
            maxLines = 1
        )
    }
}

@Composable
fun TopMixesSection(mixes: List<SpotifyTrack>,isLoading: Boolean = false,navController: NavController) {
    Text(
        text = "Your Top Mixes",
        color = Color.White,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(bottom = 8.dp)
    )
    if (isLoading || mixes.isEmpty()) {
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(5) { MixCardShimmer() }
        }
    } else {
        LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            items(mixes) { mix ->
                MixCard(mix){
                    navController.navigate(
                        "play_song/${mix.id}?name=${mix.name}" +
                                "&imageUrl=${mix.album.images?.firstOrNull()?.url ?: ""}" +
                                "&artist=${mix.artists.joinToString(", ") { it.name }}"
                    )
                }
            }
        }
    }

}

@Composable
fun MixCard(mix: SpotifyTrack, onClick: () -> Unit ) {
    val albumImage = mix.album.images?.firstOrNull()?.url ?: ""
    Box(
        modifier = Modifier
            .width(160.dp)
            .height(180.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.DarkGray)
            .clickable( onClick = onClick)
    ) {
        Image(
            painter = rememberAsyncImagePainter(albumImage),
            contentDescription = mix.album.name,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .background(Color.Black.copy(alpha = 0.5f))
                .fillMaxWidth()
        ) {
            Text(
                text = mix.album.name,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}
@Composable
fun MixCardShimmer() {
    Box(
        modifier = Modifier
            .width(160.dp)
            .height(180.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.DarkGray)
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(8.dp))
                .placeholder(visible = true, highlight = PlaceholderHighlight.shimmer())
        )
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .background(Color.Black.copy(alpha = 0.5f))
                .fillMaxWidth()
        ) {

            Box(
                modifier = Modifier
                    .height(16.dp)
                    .padding(8.dp)
                    .fillMaxWidth(0.7f)
                    .placeholder(visible = true, highlight = PlaceholderHighlight.shimmer())
            )
        }
    }
}


@Composable
fun RecentListeningSection(artists: List<SpotifyArtist> ,isLoading: Boolean) {
    Text(
        text = "Based on your recent listening",
        color = Color.White,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(bottom = 8.dp)
    )
    if (isLoading || artists.isEmpty()) {
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(5) { ArtistItemShimmer() }
        }
    } else {
        LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            items(artists) { artist ->
                ArtistItem(artist)
            }
        }
    }

}

@Composable
fun ArtistItem(artist: SpotifyArtist) {
    val albumImage = artist.images?.firstOrNull()?.url ?: ""
    Column(
        modifier = Modifier
            .width(100.dp)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = rememberAsyncImagePainter(albumImage),
            contentDescription = artist.name,
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = artist.name,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 2,
            color = Color.White,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}
@Composable
fun ArtistItemShimmer() {
    Column(
        modifier = Modifier
            .width(100.dp)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Hình tròn shimmer
        Box(
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .background(Color.Gray)
                .placeholder(
                    visible = true,
                    highlight = PlaceholderHighlight.shimmer()
                )
        )
        Spacer(modifier = Modifier.height(8.dp))
        // Tên shimmer
        Box(
            modifier = Modifier
                .height(16.dp)
                .width(60.dp)
                .placeholder(
                    visible = true,
                    highlight = PlaceholderHighlight.shimmer()
                )
        )
    }
}