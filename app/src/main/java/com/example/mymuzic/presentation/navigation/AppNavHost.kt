package com.example.mymuzic.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mymuzic.presentation.screen.SplashScreen
import com.example.mymuzic.presentation.screen.WelcomeScreen
import com.example.mymuzic.presentation.screen.LoginOptionsScreen
import com.example.mymuzic.presentation.screen.LoginWithPasswordScreen
import com.example.mymuzic.presentation.screen.AuthScreen
import com.example.mymuzic.presentation.screen.AuthViewModel
import com.example.mymuzic.presentation.screen.home.HomeScreen
import com.example.mymuzic.presentation.screen.ProfileScreen
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
}

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(), 
    onDestinationChanged: ((String) -> Unit)? = null,
    authViewModel: AuthViewModel = koinViewModel()
) {
    val authState by authViewModel.uiState.collectAsState()
    
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
        composable(Routes.HOME) {
            HomeScreen(navController)
        }
        composable(Routes.EXPLORE) {
            com.example.mymuzic.presentation.screen.ExploreScreen()
        }
        composable(Routes.LIBRARY) {
            com.example.mymuzic.presentation.screen.LibraryScreen()
        }
        composable(Routes.PROFILE) {
            ProfileScreen(navController)
        }
    }
} 