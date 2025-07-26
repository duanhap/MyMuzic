package com.example.mymuzic.presentation.screen

import android.net.Uri
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.mymuzic.presentation.navigation.Routes
import com.example.mymuzic.presentation.ui.screens.ExploreScreen
import com.example.mymuzic.presentation.ui.screens.SearchScreen

@Composable
fun MainScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    musicViewModel: MusicViewModel,
    startTab: String = Routes.HOME,
    startTabQuery: String = ""
) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route
    // State để lưu tab hiện tại
    var selectedTab by rememberSaveable { mutableStateOf(startTab) }

    // Khi route thay đổi (do navigate), cập nhật selectedTab
    LaunchedEffect(currentRoute) {
        when (currentRoute) {
            Routes.HOME -> selectedTab = Routes.HOME
            Routes.EXPLORE -> selectedTab = Routes.EXPLORE
            Routes.LIBRARY -> selectedTab = Routes.LIBRARY
            Routes.PROFILE -> selectedTab = Routes.PROFILE
            Routes.SEARCH -> selectedTab = Routes.SEARCH
        }
    }

    val showBottomNav = selectedTab in listOf(
        Routes.HOME, Routes.EXPLORE, Routes.LIBRARY, Routes.PROFILE, Routes.SEARCH
    )
    
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (showBottomNav) {
                BottomNavBar(
                    navController = navController, 
                    currentRoute = selectedTab,
                    musicViewModel = musicViewModel
                )
            }
        }
    ) { innerPadding ->
        // Render content theo selectedTab
        when (selectedTab) {
            Routes.HOME -> {
                HomeScreen(
                    navController = navController,
                    musicViewModel = musicViewModel,
                )
            }
            Routes.EXPLORE -> {
                ExploreScreen(
                    navController = navController,
                )
            }
            Routes.LIBRARY -> {
                LibraryScreen(modifier = Modifier.padding(innerPadding))
            }
            Routes.PROFILE -> {
                ProfileScreen(navController)
            }
            Routes.SEARCH -> {
                SearchScreen(
                    navController = navController,
                    query = Uri.decode(startTabQuery).replace("+", " "),
                )
            }
            else -> {
                // Các route khác sẽ được xử lý bởi AppNavHost
            }
        }
    }
} 