package com.example.mymuzic.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mymuzic.R
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import android.app.Activity
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavController
import com.example.mymuzic.presentation.navigation.Routes
import org.koin.androidx.compose.koinViewModel
import com.example.mymuzic.presentation.screen.AuthViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState

@Composable
fun LoginOptionsScreen(
    navController: NavController,
    onGoogle: () -> Unit = {},
    onFacebook: () -> Unit = {},
    onSpotify: () -> Unit = {},
    onPassword: () -> Unit = {},
    onSignUp: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    val view = LocalView.current
    SideEffect {
        val window = (view.context as? Activity)?.window
        window?.let {
            WindowCompat.getInsetsController(it, view).isAppearanceLightStatusBars = false
        }
    }
    val authViewModel: AuthViewModel = koinViewModel()
    val uiState by authViewModel.uiState.collectAsState()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF000000))
    ) {
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id= R.drawable.vector),
                contentDescription = "Back",
                tint = Color.White
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 32.dp)
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            Image(
                painter = painterResource(id = R.drawable.musiumlogo),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(180   .dp)
                    .background(Color.Black, shape = CircleShape)
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "Let's get you in",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 32.dp)
            )
            SocialButton(
                iconRes = R.drawable.google,
                text = "Continue with Google",
                onClick = onGoogle
            )
            Spacer(modifier = Modifier.height(12.dp))
            SocialButton(
                iconRes = R.drawable.fb,
                text = "Continue with Facebook",
                onClick = onFacebook
            )
            Spacer(modifier = Modifier.height(12.dp))
            SocialButton(
                iconRes = R.drawable.spotify,
                text = "Continue with Spotify",
                onClick = onSpotify
            )
            Spacer(modifier = Modifier.height(24.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Divider(modifier = Modifier.weight(1f), color = Color.Gray)
                Text(
                    text = "  or  ",
                    color = Color.Gray,
                    fontSize = 16.sp
                )
                Divider(modifier = Modifier.weight(1f), color = Color.Gray)
            }
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onPassword,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1DE9FF)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .shadow(16.dp, shape = RoundedCornerShape(12.dp), ambientColor = Color(0xFF1DE9FF), spotColor = Color(0xFF1DE9FF))
            ) {
                Text("Log in with a password", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Don't have an account? ",
                    color = Color.White,
                    fontSize = 16.sp
                )
                Text(
                    text = "Sign Up",
                    color = Color(0xFF1DE9FF),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onSignUp() }
                )
            }
        }
    }
}

@Composable
private fun SocialButton(iconRes: Int, text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(Color(0xFF1E1E1E), shape = RoundedCornerShape(12.dp))
            .border(1.dp, Color.Gray, shape = RoundedCornerShape(12.dp))
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = text,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
        }
    }
} 