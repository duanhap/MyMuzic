package com.example.mymuzic.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mymuzic.R
import kotlinx.coroutines.delay
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import android.app.Activity
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.draw.shadow

@Composable
fun SplashScreen(modifier: Modifier = Modifier, onFinished: () -> Unit = {}) {
    val view = LocalView.current
    SideEffect {
        val window = (view.context as? Activity)?.window
        window?.let {
            WindowCompat.getInsetsController(it, view).isAppearanceLightStatusBars = false
        }
    }
    val scale = remember { Animatable(3.5f) }
    val alpha = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        scale.animateTo(1f, animationSpec = tween(durationMillis = 900))
        alpha.animateTo(1f, animationSpec = tween(durationMillis = 900))
        delay(1100)
        onFinished()
    }
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.musiumlogo),
                contentDescription = "App Logo",
                modifier = Modifier
                    .padding(bottom = 24.dp)
                    .scale(scale.value)
            )
            Text(
                text = "My Muzic",
                color = Color(0xFF1DE9FF),
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .background(Color.Black)
                    .padding(top = 8.dp)
                    .alpha(alpha.value)

            )
        }
    }
} 