package com.example.mymuzic

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.mymuzic.ui.theme.MyMuzicTheme
import com.example.mymuzic.presentation.navigation.AppNavHost
import androidx.navigation.compose.rememberNavController
import com.example.mymuzic.presentation.screen.BottomNavBar
import com.example.mymuzic.presentation.navigation.Routes
import com.example.mymuzic.presentation.screen.AuthViewModel
import com.example.mymuzic.presentation.screen.AuthEvent
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    
    private val authViewModel: AuthViewModel by viewModel()
    
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Xử lý deep link từ intent
        handleIntent(intent)
        
        setContent {
            MyMuzicTheme {
                val navController = rememberNavController()
                val navBackStackEntry = navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry.value?.destination?.route
                val showBottomNav = currentRoute in listOf(
                    Routes.HOME, Routes.EXPLORE, Routes.LIBRARY
                )
                
                // Xử lý deep link callback
                LaunchedEffect(Unit) {
                    // Đây sẽ được xử lý khi có deep link
                }
                
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (showBottomNav) {
                            BottomNavBar(navController = navController, currentRoute = currentRoute)
                        }
                    }
                ) { innerPadding ->
                    AppNavHost(navController = navController, authViewModel = authViewModel)
                }
            }
        }
    }
    
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }
    
    private fun handleIntent(intent: Intent?) {
        intent?.data?.let { uri ->
            if (uri.scheme == "com.example.mymuzic" && uri.host == "callback") {
                authViewModel.handleEvent(AuthEvent.HandleAuthCallback(uri.toString()))
            }
        }
    }
}