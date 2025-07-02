package com.example.mymuzic.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mymuzic.R
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.pointer.pointerInput


@Composable
fun WelcomeScreen(onGetStarted: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF41C3D6))
    ) {


        // Ảnh nhân vật (placeholder)
        Image(
            painter = painterResource(id = R.drawable.img_girl),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 80.dp)
                .size(500.dp)
        )
        Box(
            modifier = Modifier
                .size(140.dp)
                .offset(x = 24.dp, y = 24.dp)
                .clip(CircleShape)
                .background(Color(0xFF81F1FF).copy(alpha = 0.3f))
        )
        Box(
            modifier = Modifier
                .size(80.dp)
                .offset(x = 260.dp, y = 60.dp)
                .clip(CircleShape)
                .background(Color(0xFF81F1FF).copy(alpha = 0.3f))
        )
        Box(
            modifier = Modifier
                .size(100.dp)
                .offset(x = 200.dp, y = 180.dp)
                .clip(CircleShape)
                .background(Color(0xFF81F1FF).copy(alpha = 0.3f))
        )
        // Card chứa slogan, progress, nút
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .align(Alignment.BottomCenter)
                .background(
                    color = Color.Black.copy(alpha = 1f),
                    shape = RoundedCornerShape(32.dp, 32.dp, 0.dp, 0.dp)
                )
                .padding(horizontal = 24.dp, vertical = 32.dp)
        ) {
            var page by remember { mutableStateOf(0) }
            val coroutineScope = rememberCoroutineScope()
            val sloganList = listOf(
                buildAnnotatedString {
                    append("From the ")
                    withStyle(SpanStyle(color = Color(0xFF1DE9FF), fontWeight = FontWeight.Bold)) { append("latest") }
                    append(" to the ")
                    withStyle(SpanStyle(color = Color(0xFF1DE9FF), fontWeight = FontWeight.Bold)) { append("greatest") }
                    append(" hits, play your favorite tracks on ")
                    withStyle(SpanStyle(color = Color(0xFF1DE9FF), fontWeight = FontWeight.Bold)) { append("musium") }
                    append(" now!")
                },
                buildAnnotatedString {
                    append("Discover ")
                    withStyle(SpanStyle(color = Color(0xFF1DE9FF), fontWeight = FontWeight.Bold)) { append("new vibes") }
                    append(" and ")
                    withStyle(SpanStyle(color = Color(0xFF1DE9FF), fontWeight = FontWeight.Bold)) { append("timeless classics") }
                    append(". Let ")
                    withStyle(SpanStyle(color = Color(0xFF1DE9FF), fontWeight = FontWeight.Bold)) { append("music") }
                    append(" inspire your day!")
                }
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxSize()
            ) {
                Spacer(modifier = Modifier.height(12.dp))
                // Chỉ phần text là swipe được
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .pointerInput(page) {
                            detectHorizontalDragGestures { _, dragAmount ->
                                if (dragAmount < -30 && page < sloganList.lastIndex) {
                                    coroutineScope.launch { page++ }
                                } else if (dragAmount > 30 && page > 0) {
                                    coroutineScope.launch { page-- }
                                }
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        sloganList[page],
                        color = Color.White,
                        fontSize = 20.sp,
                        lineHeight = 28.sp,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                // Progress bar dạng 2 chấm tròn
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    repeat(sloganList.size) { i ->
                        Box(
                            modifier = Modifier
                                .size(if (i == page) 16.dp else 10.dp)
                                .background(
                                    if (i == page) Color.White else Color(0xFF1DE9FF).copy(alpha = 0.5f),
                                    shape = CircleShape
                                )
                        )
                        if (i < sloganList.lastIndex) Spacer(modifier = Modifier.width(12.dp))
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                // Button luôn ở dưới
                Button(
                    onClick = onGetStarted,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1DE9FF)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .shadow(16.dp, shape = RoundedCornerShape(12.dp), ambientColor = Color(0xFF1DE9FF), spotColor = Color(0xFF1DE9FF))
                ) {
                    Text("Get Started", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
} 