package com.example.mymuzic.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mymuzic.presentation.screen.SplashScreen
import com.example.mymuzic.presentation.screen.WelcomeScreen
import com.example.mymuzic.presentation.screen.LoginOptionsScreen
import com.example.mymuzic.presentation.screen.LoginWithPasswordScreen
import com.example.mymuzic.presentation.screen.AuthScreen
import com.example.mymuzic.presentation.screen.AuthViewModel
import com.example.mymuzic.presentation.screen.MainScreen
import com.example.mymuzic.presentation.screen.PlaySongScreen
import com.example.mymuzic.presentation.ui.screens.ArtistScreen
import com.example.mymuzic.presentation.ui.screens.AlbumScreen
import com.example.mymuzic.presentation.ui.screens.CategoryListScreen
import com.example.mymuzic.presentation.ui.screens.CategoryPlaylistsScreen
import com.example.mymuzic.presentation.ui.screens.PlaylistDetailScreen
import org.koin.androidx.compose.koinViewModel

object Routes {
    const val SPLASH = "splash"
    const val WELCOME = "welcome"
    const val LOGIN_OPTIONS = "login_options"
    const val LOGIN_PASSWORD = "login_password"
    const val AUTH = "auth"
    const val HOME = "home"
    const val EXPLORE = "explore"
    const val LIBRARY = "library"
    const val PROFILE = "profile"
    const val ARTIST = "artist/{artistId}"
    const val ALBUM = "album/{albumId}?name={name}&imageUrl={imageUrl}&artist={artist}"
    const val SEARCH = "search/{query}"
}

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(), 
    onDestinationChanged: ((String) -> Unit)? = null,
    authViewModel: AuthViewModel = koinViewModel()
) {
    val authState by authViewModel.uiState.collectAsState()
    
    // Tạo MusicViewModel ở đây để chia sẻ với tất cả các màn hình
    val musicViewModel: com.example.mymuzic.presentation.screen.MusicViewModel = koinViewModel()
    
    // Kiểm tra authentication state
    LaunchedEffect(authState.isAuthenticated) {
        if (authState.isAuthenticated) {
            navController.navigate(Routes.HOME) {
                popUpTo(0)
            }
        }
    }
    
    NavHost(navController = navController, startDestination = Routes.SPLASH) {
        composable(Routes.SPLASH) {
            SplashScreen(onFinished = {
                // Kiểm tra nếu đã đăng nhập thì chuyển thẳng đến HOME
                if (authState.isAuthenticated) {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                } else {
                    navController.navigate(Routes.WELCOME) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                }
            })
        }
        composable(Routes.WELCOME) {
            WelcomeScreen(onGetStarted = {
                navController.navigate(Routes.LOGIN_OPTIONS)
            })
        }
        composable(Routes.LOGIN_OPTIONS) {
            LoginOptionsScreen(
                navController = navController,
                onPassword = {
                    navController.navigate(Routes.LOGIN_PASSWORD)
                },
                onSpotify = {
                    navController.navigate(Routes.AUTH)
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable(Routes.LOGIN_PASSWORD) {
            LoginWithPasswordScreen(
                onLogin = {navController.navigate(Routes.HOME)},
                onBack = { navController.popBackStack() }
            )
        }
        composable(Routes.AUTH) {
            AuthScreen(
                onAuthSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.AUTH) { inclusive = true }
                    }
                }
            )
        }
        // MainScreen cho từng tab
        composable(Routes.HOME) {
            MainScreen(navController = navController, authViewModel = authViewModel, musicViewModel = musicViewModel, startTab = Routes.HOME)
        }
        composable(Routes.EXPLORE) {
            MainScreen(navController = navController, authViewModel = authViewModel, musicViewModel = musicViewModel, startTab = Routes.EXPLORE)
        }
        composable(Routes.LIBRARY) {
            MainScreen(navController = navController, authViewModel = authViewModel, musicViewModel = musicViewModel, startTab = Routes.LIBRARY)
        }
        composable(Routes.PROFILE) {
            MainScreen(navController = navController, authViewModel = authViewModel, musicViewModel = musicViewModel, startTab = Routes.PROFILE)
        }
        composable(
            route = Routes.SEARCH,
            arguments = listOf(
                navArgument("query") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val query = backStackEntry.arguments?.getString("query") ?: ""
            MainScreen(
                navController = navController,
                authViewModel = authViewModel,
                musicViewModel = musicViewModel,
                startTab = Routes.SEARCH,
                startTabQuery = query // thêm dòng này
            )
        }
        // Các route đặc biệt giữ nguyên
        composable(
            "play_song/{id}?name={name}&imageUrl={imageUrl}&artist={artist}",
            arguments = listOf(
                navArgument("id") { type = NavType.StringType },
                navArgument("name") { type = NavType.StringType; defaultValue = "" },
                navArgument("imageUrl") { type = NavType.StringType; defaultValue = "" },
                navArgument("artist") { type = NavType.StringType; defaultValue = "" }
            )
        ) { backStackEntry ->
            PlaySongScreen(
                navController = navController,
                id = backStackEntry.arguments?.getString("id") ?: "",
                name = backStackEntry.arguments?.getString("name") ?: "",
                imageUrl = backStackEntry.arguments?.getString("imageUrl") ?: "",
                artist = backStackEntry.arguments?.getString("artist") ?: "",
                musicViewModel = musicViewModel
            )
        }
        composable(
            route = Routes.ARTIST,
            arguments = listOf(
                navArgument("artistId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val artistId = backStackEntry.arguments?.getString("artistId") ?: ""
            ArtistScreen(navController, artistId)
        }
        composable(
            route = Routes.ALBUM,
            arguments = listOf(
                navArgument("albumId") { type = NavType.StringType },
                navArgument("name") { type = NavType.StringType; defaultValue = "" },
                navArgument("imageUrl") { type = NavType.StringType; defaultValue = "" },
                navArgument("artist") { type = NavType.StringType; defaultValue = "" }
            )
        ) { backStackEntry ->
            val albumId = backStackEntry.arguments?.getString("albumId") ?: ""
            val albumName = backStackEntry.arguments?.getString("name") ?: ""
            val albumImageUrl = backStackEntry.arguments?.getString("imageUrl") ?: ""
            val albumArtist = backStackEntry.arguments?.getString("artist") ?: ""
            AlbumScreen(navController, albumId, albumName, albumImageUrl, albumArtist)
        }
        composable("category_list") {
            CategoryListScreen(navController = navController)
        }
        composable(
            route = "category_playlists/{categoryId}/{categoryName}",
            arguments = listOf(
                navArgument("categoryId") { type = NavType.StringType },
                navArgument("categoryName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("categoryId") ?: ""
            val categoryName = backStackEntry.arguments?.getString("categoryName") ?: ""
            CategoryPlaylistsScreen(navController = navController, categoryId = categoryId, categoryName = categoryName)
        }
        composable(
            route = "playlist_detail/{playlistId}/{playlistName}",
            arguments = listOf(
                navArgument("playlistId") { type = NavType.StringType },
                navArgument("playlistName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val playlistId = backStackEntry.arguments?.getString("playlistId") ?: ""
            val playlistName = backStackEntry.arguments?.getString("playlistName") ?: ""
            PlaylistDetailScreen(navController = navController, playlistId = playlistId, playlistName = playlistName)
        }
    }
} 