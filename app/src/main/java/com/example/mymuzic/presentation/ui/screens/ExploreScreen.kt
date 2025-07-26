package com.example.mymuzic.presentation.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.mymuzic.data.model.music.PlaylistDetail
import com.example.mymuzic.data.model.music.SpotifyAlbum
import com.example.mymuzic.data.model.music.SpotifyCategory
import com.example.mymuzic.data.model.music.SpotifyTrack
import com.example.mymuzic.data.model.response.RecentlyPlayedItem
import com.example.mymuzic.presentation.screen.ExploreViewModel
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material3.placeholder
import com.google.accompanist.placeholder.material3.shimmer
import org.koin.androidx.compose.koinViewModel
import com.example.mymuzic.data.model.music.PlaylistTrackItem
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import java.net.URLEncoder

@Composable
fun ExploreScreen(
    navController: NavController,
    viewModel: ExploreViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val newReleases = uiState.newReleases
    val isLoading = uiState.isLoading
    val categoryUiState by viewModel.categoryUiState.collectAsState()
    val categories = categoryUiState.categories
    val isCategoryLoading = categoryUiState.isLoading

    // Thêm state cho 3 playlist
    val viralHits by viewModel.viralHits.collectAsState()
    val todaysTopHits by viewModel.todaysTopHits.collectAsState()
    val globalTop50 by viewModel.globalTop50.collectAsState()
    val isPlaylistLoading by viewModel.isPlaylistLoading.collectAsState()

    val keyboardController = LocalSoftwareKeyboardController.current
    var searchQuery by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF18181A))
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Logo + Search bar
        Spacer(modifier = Modifier.height(30.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Thay bằng logo của bạn nếu có
            Text(
                text = "𝄞",
                color = Color.Cyan,
                fontSize = 29.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Explore",
                color = Color.Cyan,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Search bar
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
                        navController.navigate("search/" + URLEncoder.encode(searchQuery, "UTF-8"))
                        keyboardController?.hide()
                    }
                }
            )
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .background(Color(0xFF18181A))
        ) {
            ExploreReleaseSection(
                albums = newReleases,
                isLoading = isLoading,
                navController = navController
            )

            Spacer(modifier = Modifier.height(16.dp))
            // Categories Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = "Categories",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                )
                TextButton(
                    onClick = { navController.navigate("category_list") },
                ) {
                    Text("See all", color = Color.Cyan, style = MaterialTheme.typography.bodyLarge)
                }

            }

            if (isCategoryLoading) {
                CategoriesGridShimmer(count = 8)
            } else {
                CategoriesGrid(categories.take(8), onCategoryClick = { category ->
                    navController.navigate("category_playlists/${category.id}/${category.name}")
                })

            }
            // Thêm 3 section playlist ở dưới Categories
            Spacer(modifier = Modifier.height(32.dp))
            ExplorePlaylistSection(
                title = "Viral Hits",
                playlist = viralHits,
                isLoading = isPlaylistLoading,
                navController = navController
            )
            Spacer(modifier = Modifier.height(32.dp))
            ExplorePlaylistSection(
                title = "Today's Top Hits",
                playlist = todaysTopHits,
                isLoading = isPlaylistLoading,
                navController = navController
            )
            Spacer(modifier = Modifier.height(32.dp))
            ExplorePlaylistSection(
                title = "Global Top 50",
                playlist = globalTop50,
                isLoading = isPlaylistLoading,
                navController = navController
            )
            Spacer(modifier = Modifier.height(200.dp))
        }

    }
}


@Composable
fun ExplorePlaylistSection(
    title: String,
    playlist: PlaylistDetail?,
    isLoading: Boolean = false,
    navController: NavController
) {
    Text(
        text = title,
        color = Color.White,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(bottom = 8.dp)
    )
    if (isLoading || playlist == null) {
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(5) { ReleaseShimmer() }
        }
    } else {
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(playlist.tracks?.items?.take(10) ?: emptyList()) { trackItem ->
                PlaylistCard(track = trackItem) {
                    navController.navigate(
                        "playlist_detail/${playlist.id}?name=${playlist.name}" +
                                "&imageUrl=${playlist.images?.firstOrNull()?.url ?: ""}" +
                                "&description=${playlist.description ?: ""}"
                    )
                }
            }
        }
    }
}

@Composable
fun PlaylistCard(
    track: PlaylistTrackItem,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(160.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF232326))
            .clickable { onClick() }
            .padding(8.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(track.track?.album?.images?.firstOrNull()?.url),
            contentDescription = track.track?.name,
            modifier = Modifier
                .height(120.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = track.track?.name ?: "Unknown Track",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            maxLines = 1
        )
        Text(
            text = track.track?.artists?.joinToString(", ") { it.name } ?: "Unknown Artist",
            color = Color.Gray,
            fontSize = 12.sp,
            maxLines = 1
        )
    }
}
@Composable
fun  ExploreReleaseSection(albums: List<SpotifyAlbum>, isLoading: Boolean = false, navController: NavController) {
    Text(
        text = "New Releases",
        color = Color.White,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(bottom = 8.dp)
    )
    if (isLoading || albums.isEmpty()) {
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(5) { ReleaseShimmer() }
        }
    } else {
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(albums) { album ->
                ReleaseCard(album){
                    navController.navigate(
                        "album/${album.id}?name=${album.name}" +
                                "&imageUrl=${album.images?.firstOrNull()?.url ?: ""}" +
                                "&artist=${album.artists?.joinToString(", ") { it.name }}"
                    )
                }
            }
        }
    }
}

@Composable
fun ReleaseCard(album: SpotifyAlbum,onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .width(160.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF232326))
            .clickable {onClick() }
            .padding(8.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(album.images?.firstOrNull()?.url),
            contentDescription = album.name,
            modifier = Modifier
                .height(120.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = album.name,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            maxLines = 1
        )
        Text(
            text = album.artists?.joinToString(", ") { it.name } ?: "",
            color = Color.Gray,
            fontSize = 12.sp,
            maxLines = 1
        )
    }


}
@Composable
fun ReleaseShimmer() {
    Column(
        modifier = Modifier
            .width(160.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF232326))
            .padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .height(120.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .placeholder(
                    visible = true,
                    highlight = PlaceholderHighlight.shimmer()
                )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .height(16.dp)
                .fillMaxWidth(0.7f)
                .placeholder(
                    visible = true,
                    highlight = PlaceholderHighlight.shimmer()
                )
        )
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .height(12.dp)
                .fillMaxWidth(0.5f)
                .placeholder(
                    visible = true,
                    highlight = PlaceholderHighlight.shimmer()
                )
        )
    }
} 

@Composable
fun CategoriesGrid(categories: List<SpotifyCategory>, onCategoryClick: (SpotifyCategory) -> Unit) {
//    val rowCount = (categories.size + 1) / 2
//    val gridHeight = (rowCount * 120).dp + ((rowCount - 1) * 16).dp + 80.dp // 80dp cho mini bottom bar
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.heightIn(max = Dp(1000f)),
        userScrollEnabled = false,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(categories) { category ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF232326))
                    .clickable { onCategoryClick(category) }
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val iconUrl = category.icons.firstOrNull()?.url
                if (iconUrl != null) {
                    Image(
                        painter = rememberAsyncImagePainter(iconUrl),
                        contentDescription = category.name,
                        modifier = Modifier
                            .size(64.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = category.name,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    fontSize = 16.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

@Composable
fun CategoriesGridShimmer(count: Int = 6) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.height(180.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(count) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF232326))
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .placeholder(visible = true, highlight = PlaceholderHighlight.shimmer())
                )
                Spacer(modifier = Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .height(16.dp)
                        .fillMaxWidth(0.7f)
                        .placeholder(visible = true, highlight = PlaceholderHighlight.shimmer())
                )
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun ExplorePreview() {
    val navController = rememberNavController()
    ExploreScreen(navController =  navController, viewModel = koinViewModel())
}
