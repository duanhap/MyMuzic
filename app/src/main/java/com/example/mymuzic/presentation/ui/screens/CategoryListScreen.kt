package com.example.mymuzic.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.mymuzic.data.model.music.SpotifyCategory
import com.example.mymuzic.presentation.screen.ExploreViewModel
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material3.shimmer
import org.koin.androidx.compose.koinViewModel
import androidx.navigation.NavController
import androidx.compose.foundation.Image
import com.google.accompanist.placeholder.material3.placeholder
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.draw.shadow

@Composable
fun CategoryListScreen(
    viewModel: ExploreViewModel = koinViewModel(),
    navController: NavController
) {
    val categoryUiState = viewModel.categoryUiState.collectAsState().value
    val categories = categoryUiState.categories
    val isLoading = categoryUiState.isLoading

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF18181A))
            .padding(horizontal = 16.dp)
    ) {
        // Header với nút Back
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, bottom = 8.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.padding(start = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
            Text(
                text = "All Categories",
                color = Color.White,
                fontSize = 24.sp,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        if (isLoading) {
            CategoriesGridShimmer(count = 12)
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxHeight()
            ) {
                items(categories) { category ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFF232326))
                            .clickable {
                                navController.navigate("category_playlists/${category.id}/${category.name}")
                            }
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
                                contentScale = androidx.compose.ui.layout.ContentScale.Crop
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = category.name,
                            color = Color.White,
                            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                            maxLines = 2,
                            fontSize = 16.sp,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                }
            }
        }
    }
}

//@Composable
//fun CategoriesGridShimmer(count: Int = 12) {
//    LazyVerticalGrid(
//        columns = GridCells.Fixed(2),
//        modifier = Modifier.fillMaxHeight(),
//        horizontalArrangement = Arrangement.spacedBy(16.dp),
//        verticalArrangement = Arrangement.spacedBy(16.dp)
//    ) {
//        items(count) {
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .clip(RoundedCornerShape(16.dp))
//                    .background(Color(0xFF232326))
//                    .padding(16.dp),
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                Box(
//                    modifier = Modifier
//                        .size(64.dp)
//                        .clip(RoundedCornerShape(12.dp))
//                        .placeholder(
//                            visible = true,
//                            highlight = PlaceholderHighlight.shimmer()
//                        )
//                )
//                Spacer(modifier = Modifier.height(12.dp))
//                Box(
//                    modifier = Modifier
//                        .height(16.dp)
//                        .fillMaxWidth(0.7f)
//                        .placeholder(
//                            visible = true,
//                            highlight = PlaceholderHighlight.shimmer()
//                        )
//                )
//            }
//        }
//    }
//}