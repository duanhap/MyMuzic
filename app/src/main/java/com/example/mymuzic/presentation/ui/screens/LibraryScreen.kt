@file:Suppress("IMPLICIT_CAST_TO_ANY")

package com.example.mymuzic.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mymuzic.R
import com.example.mymuzic.presentation.ui.screens.CategoriesGrid
import com.example.mymuzic.presentation.ui.screens.CategoriesGridShimmer
import com.example.mymuzic.presentation.ui.screens.ExplorePlaylistSection
import com.example.mymuzic.presentation.ui.screens.ExploreReleaseSection
import java.net.URLEncoder

@Composable
fun LibraryScreen(modifier: Modifier = Modifier) {
    var selectedFilter by remember { mutableStateOf("Track") }

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
                text = "Librabry",
                color = Color.Cyan,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        LibraryFilterBar(selected = selectedFilter) { newFilter ->
            selectedFilter = newFilter
        }
        when (selectedFilter) {
            "Playlists" -> ShowPlaylists()
            "Artists" -> ShowArtists()
            "Albums" -> ShowAlbums()
            "Track" -> ShowTracks()
            "Podcasts & Shows" -> ShowPodcasts()
        }





    }
}
@Composable
fun ShowArtists() {
    Text("Artist content here", color = Color.White)
}

@Composable
fun ShowPlaylists() {
    Spacer(modifier = Modifier.height(24.dp))
    CircleIconWithText(painterResource(R.drawable.ic_add_library), "Add New Playlist")
    Spacer(modifier = Modifier.height(24.dp))
    CircleIconWithText(painterResource(R.drawable.ic_love_librabry), "Your Liked Playlists")
    Text("Playlist content here", color = Color.White)
}
@Composable
fun ShowAlbums() {
    Text("Albums content here", color = Color.White)
}
@Composable
fun ShowTracks() {
    Spacer(modifier = Modifier.height(24.dp))
    CircleIconWithText(painterResource(R.drawable.ic_love_librabry), "Your Liked Songs")
    Text("Tracks content here", color = Color.White)
}
@Composable
fun ShowPodcasts() {
    Spacer(modifier = Modifier.height(24.dp))
    Text("Podcasts content here", color = Color.White)
}

@Composable
fun LibraryFilterBar(
    selected: String,
    onFilterSelected: (String) -> Unit
) {
    val filters = listOf("Track", "Playlists", "Artists", "Albums", "Podcasts & Shows")

    LazyRow(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(filters) { filter ->
            val isSelected = selected == filter
            Text(
                text = filter,
                fontSize = 12.sp,
                modifier = Modifier
                    .clickable { onFilterSelected(filter) }
                    .background(
                        if (isSelected) {
                            Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF326D75),
                                Color(0xFF00C2CB)
                            ))
                        }
                        else  {
                            Brush.verticalGradient(listOf(Color.Transparent, Color.Transparent))
                        },
                        shape = RoundedCornerShape(50)
                    )
                    .border(if (!isSelected) 1.dp else -1.dp, Color.White, RoundedCornerShape(50))
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                color = if (isSelected) Color.White else Color.White
            )
        }
    }
}
@Composable
fun CircleIconWithText(icon: Painter, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFFA6F3FF), Color(0xFF00C2CB))
                )
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = icon,
                contentDescription = null,
                tint = Color.Black, // hoặc White tùy style bạn muốn
                modifier = Modifier.size(25.dp)
            )
        }

        Spacer(modifier = Modifier.width(20.dp))

        Text(
            text = text,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
    }
}
