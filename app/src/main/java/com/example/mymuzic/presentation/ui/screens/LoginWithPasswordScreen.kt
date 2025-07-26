package com.example.mymuzic.presentation.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mymuzic.R
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import android.app.Activity
import androidx.compose.runtime.SideEffect
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff

import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource

@Composable
fun LoginWithPasswordScreen(
    onLogin: () -> Unit = {},
    onForgotPassword: () -> Unit = {},
    onGoogle: () -> Unit = {},
    onFacebook: () -> Unit = {},
    onApple: () -> Unit = {},
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
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
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
                    .size(180.dp)
                    .background(Color.Black, shape = CircleShape)
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "Login to your account",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 24.dp)
            )
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black),
                textStyle = androidx.compose.ui.text.TextStyle(color = Color.White)
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black),
                textStyle = androidx.compose.ui.text.TextStyle(color = Color.White),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = if (passwordVisible) "Hide password" else "Show password",
                            tint = Color(0xFF1DE9FF)
                        )
                    }
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(
                    checked = rememberMe,
                    onCheckedChange = { rememberMe = it },
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = "Remember me",
                    color = Color.White,
                    fontSize = 16.sp
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onLogin,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1DE9FF)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .shadow(16.dp, shape = RoundedCornerShape(12.dp), ambientColor = Color(0xFF1DE9FF), spotColor = Color(0xFF1DE9FF))
            ) {
                Text("Log in", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Forgot the password?",
                color = Color(0xFF1DE9FF),
                fontSize = 16.sp,
                modifier = Modifier
                    .align(Alignment.End)
                    .clickable { onForgotPassword() }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Divider(modifier = Modifier.weight(1f), color = Color.Gray)
                Text(
                    text = "  or continue with  ",
                    color = Color.Gray,
                    fontSize = 16.sp
                )
                Divider(modifier = Modifier.weight(1f), color = Color.Gray)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                SocialIconButton(iconRes = R.drawable.google, onClick = onGoogle)
                SocialIconButton(iconRes = R.drawable.fb, onClick = onFacebook)
                SocialIconButton(iconRes = R.drawable.spotify, onClick = onApple)
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
private fun SocialIconButton(iconRes: Int, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = CircleShape,
        modifier = Modifier.size(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = Color.Unspecified
        ),
        contentPadding = PaddingValues(0.dp),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.5f)), // 🌟 viền trắng nè

    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
    }
}

