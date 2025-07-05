package com.example.mymuzic.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.mymuzic.data.model.auth.SpotifyUserProfile
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(

    navController: NavController,
    viewModel: AuthViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val userProfile = uiState.userProfile

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF18181A))
            .verticalScroll(rememberScrollState())
    ) {
        // Header với nút back
        ProfileHeader(
            onBackClick = { navController.popBackStack() }
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Profile Info
        userProfile?.let { profile ->
            ProfileInfo(profile = profile)
            Spacer(modifier = Modifier.height(32.dp))
            
            // Account Settings
            AccountSettings()
            Spacer(modifier = Modifier.height(32.dp))
            
            // Profile Details
            ProfileDetails(profile = profile)
            Spacer(modifier = Modifier.height(32.dp))
            
            // App Settings
            AppSettings()
            Spacer(modifier = Modifier.height(32.dp))
            
            // Logout Button
            LogoutButton(
                onLogout = {
                    viewModel.handleEvent(AuthEvent.Logout)
                    navController.popBackStack()
                }
            )
        } ?: run {
            // Loading hoặc error state
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF1DB954))
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileHeader(onBackClick: () -> Unit) {
    TopAppBar(
        title = {
            Text(
                text = "Hồ sơ",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF18181A)
        )
    )
}

@Composable
fun ProfileInfo(profile: SpotifyUserProfile) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Avatar
        val avatarUrl = profile.images?.firstOrNull()?.url ?: ""
        Image(
            painter = rememberAsyncImagePainter(avatarUrl),
            contentDescription = "Profile Avatar",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Display Name
        Text(
            text = profile.displayName,
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Email
        profile.email?.let { email ->
            Text(
                text = email,
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        
        // Country
        profile.country?.let { country ->
            Text(
                text = "Quốc gia: $country",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        
        // Product type
        profile.product?.let { product ->
            val productText = when (product.lowercase()) {
                "premium" -> "Spotify Premium"
                "free" -> "Spotify Free"
                else -> product
            }
            Text(
                text = productText,
                color = Color(0xFF1DB954),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
        }
        
        // Followers count
        profile.followers?.let { followers ->
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${followers.total} người theo dõi",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
        }
        
        // Spotify profile link
        profile.externalUrls?.spotify?.let { spotifyUrl ->
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Xem trên Spotify",
                color = Color(0xFF1DB954),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ProfileDetails(profile: SpotifyUserProfile) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Thông tin chi tiết",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        DetailItem(
            icon = Icons.Default.Person,
            title = "ID người dùng",
            value = profile.id
        )
        
        profile.href?.let { href ->
            DetailItem(
                icon = Icons.Default.Link,
                title = "API URL",
                value = href
            )
        }
        
        profile.uri?.let { uri ->
            DetailItem(
                icon = Icons.Default.MusicNote,
                title = "Spotify URI",
                value = uri
            )
        }
        
        profile.type?.let { type ->
            DetailItem(
                icon = Icons.Default.Info,
                title = "Loại tài khoản",
                value = type
            )
        }
        
        profile.explicitContent?.let { explicit ->
            DetailItem(
                icon = Icons.Default.Shield,
                title = "Nội dung rõ ràng",
                value = if (explicit.filterEnabled) "Bật" else "Tắt"
            )
        }
    }
}

@Composable
fun DetailItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    value: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF232326)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = value,
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun AccountSettings() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Tài khoản",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        SettingItem(
            icon = Icons.Default.Person,
            title = "Chỉnh sửa hồ sơ",
            subtitle = "Cập nhật thông tin cá nhân",
            onClick = { /* TODO: Navigate to edit profile */ }
        )
        
        SettingItem(
            icon = Icons.Default.Notifications,
            title = "Thông báo",
            subtitle = "Quản lý thông báo",
            onClick = { /* TODO: Navigate to notifications */ }
        )
        
        SettingItem(
            icon = Icons.Default.Security,
            title = "Bảo mật",
            subtitle = "Cài đặt bảo mật tài khoản",
            onClick = { /* TODO: Navigate to security */ }
        )
    }
}

@Composable
fun AppSettings() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Ứng dụng",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        SettingItem(
            icon = Icons.Default.Settings,
            title = "Cài đặt chung",
            subtitle = "Tùy chỉnh ứng dụng",
            onClick = { /* TODO: Navigate to general settings */ }
        )
        
        SettingItem(
            icon = Icons.Default.Palette,
            title = "Giao diện",
            subtitle = "Tùy chỉnh giao diện",
            onClick = { /* TODO: Navigate to theme settings */ }
        )
        
        SettingItem(
            icon = Icons.Default.Info,
            title = "Về ứng dụng",
            subtitle = "Thông tin phiên bản",
            onClick = { /* TODO: Show app info */ }
        )
    }
}

@Composable
fun SettingItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF232326)
        ),
        shape = RoundedCornerShape(12.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = subtitle,
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 14.sp
                )
            }
            
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.5f)
            )
        }
    }
}

@Composable
fun LogoutButton(onLogout: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Button(
            onClick = onLogout,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFE74C3C)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Logout,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Đăng xuất",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
} 