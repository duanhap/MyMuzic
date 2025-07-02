# Spotify PKCE Authentication Implementation

## Tổng quan
Đây là implementation của luồng đăng nhập Spotify sử dụng PKCE (Proof Key for Code Exchange) cho ứng dụng Android MyMuzic.

## Cấu trúc Files

### 1. Utils
- `PKCEUtils.kt`: Chứa các utility functions để tạo code_verifier, code_challenge và authorization URL

### 2. Data Layer
- `AuthModels.kt`: Data models cho authentication response
- `SpotifyAuthApi.kt`: Retrofit interface cho Spotify API
- `AuthLocalDataSource.kt`: Local storage sử dụng SharedPreferences
- `AuthRepositoryImpl.kt`: Implementation của repository pattern

### 3. Domain Layer
- `AuthRepository.kt`: Interface cho repository
- `AuthUseCases.kt`: Use cases cho business logic

### 4. Presentation Layer
- `AuthViewModel.kt`: ViewModel quản lý state và logic
- `AuthScreen.kt`: UI screen cho authentication

### 5. Dependency Injection
- `AppModule.kt`: Koin module cho DI

## Luồng hoạt động

### 1. Khởi tạo Authentication
```kotlin
// User nhấn nút "Sign in with Spotify"
viewModel.handleEvent(AuthEvent.SignInWithSpotify)
```

### 2. Tạo PKCE Parameters
```kotlin
// Tạo code_verifier ngẫu nhiên
val codeVerifier = PKCEUtils.generateCodeVerifier()

// Tạo code_challenge từ code_verifier
val codeChallenge = PKCEUtils.generateCodeChallenge(codeVerifier)

// Tạo authorization URL
val authUrl = PKCEUtils.createAuthorizationUrl(
    clientId = "your_client_id",
    redirectUri = "com.example.mymuzic://callback",
    codeChallenge = codeChallenge
)
```

### 3. Mở Spotify Authorization Page
```kotlin
// Mở browser với authorization URL
val intent = Intent(Intent.ACTION_VIEW, Uri.parse(authUrl))
startActivity(intent)
```

### 4. Xử lý Callback
```kotlin
// Spotify redirect về app với authorization code
// URL format: com.example.mymuzic://callback?code=xxx&state=xxx

// Trong MainActivity
override fun onNewIntent(intent: Intent?) {
    super.onNewIntent(intent)
    intent?.data?.let { uri ->
        if (uri.scheme == "com.example.mymuzic" && uri.host == "callback") {
            authViewModel.handleDeepLink(uri.toString())
        }
    }
}
```

### 5. Exchange Code for Token
```kotlin
// Gọi API để đổi authorization code lấy access token
val tokenResponse = authApi.exchangeCodeForToken(
    clientId = clientId,
    code = extractedCode,
    redirectUri = redirectUri,
    codeVerifier = savedCodeVerifier
)
```

### 6. Lưu Token và User Profile
```kotlin
// Lưu token vào local storage
localDataSource.saveAccessToken(tokenResponse.accessToken)
localDataSource.saveRefreshToken(tokenResponse.refreshToken)

// Lấy user profile
val userProfile = authApi.getUserProfile("Bearer $accessToken")
localDataSource.saveUserProfile(userProfile)
```

## Continue Listening (Recently Played)

### API sử dụng
- `GET https://api.spotify.com/v1/me/player/recently-played`
- Header: `Authorization: Bearer {access_token}`

### Flow thực hiện
1. **Model**: Tạo các data class cho response recently played (RecentlyPlayedResponse, RecentlyPlayedItem, SpotifyTrack, SpotifyAlbum, SpotifyArtist)
2. **API**: Thêm hàm getRecentlyPlayed trong SpotifyAuthApi
3. **Repository**: Thêm hàm getRecentlyPlayedTracks trong AuthRepository và AuthRepositoryImpl
4. **UseCase**: Thêm GetRecentlyPlayedTracksUseCase
5. **ViewModel**: Thêm state và logic fetch recently played vào AuthViewModel
6. **UI**: HomeScreen gọi fetch khi mở, hiển thị danh sách bài hát vừa nghe ở section "Continue Listening"

### Hiển thị UI
- Ảnh album, tên bài hát, nghệ sĩ
- Dữ liệu lấy trực tiếp từ Spotify API, luôn cập nhật mới nhất

## Your Top Mix (Top Tracks)

### API sử dụng
- `GET https://api.spotify.com/v1/me/top/tracks`
- Header: `Authorization: Bearer {access_token}`
- Tham số:
  - `time_range`: "short_term" (4 tuần), "medium_term" (6 tháng), "long_term" (nhiều năm)
  - `limit`: số lượng bài hát trả về (tối đa 50)

### Flow thực hiện
1. **Model**: Tạo các data class cho response top tracks (TopTracksResponse, SpotifyTrack, SpotifyAlbum, SpotifyArtist)
2. **API**: Thêm hàm getTopTracks trong SpotifyAuthApi
3. **Repository**: Thêm hàm getTopTracks trong AuthRepository và AuthRepositoryImpl
4. **UseCase**: Thêm GetTopTracksUseCase
5. **ViewModel**: Thêm state và logic fetch top tracks vào AuthViewModel
6. **UI**: HomeScreen gọi fetch khi mở, hiển thị danh sách bài hát ở section "Your Top Mix"

### Hiển thị UI
- Ảnh album, tên bài hát, nghệ sĩ
- Dữ liệu lấy trực tiếp từ Spotify API, luôn cập nhật mới nhất

### Ví dụ gọi API
```kotlin
@GET("me/top/tracks")
suspend fun getTopTracks(
    @Header("Authorization") authHeader: String,
    @Query("time_range") timeRange: String = "short_term",
    @Query("limit") limit: Int = 10
): TopTracksResponse
```

### Gợi ý UI/UX
- Có thể cho phép user chọn time_range (4 tuần, 6 tháng, all time)
- Hiển thị danh sách bài hát với ảnh album, tên bài, nghệ sĩ
- Nhấn vào bài hát có thể mở Spotify hoặc phát preview nếu muốn

## Cấu hình

### 1. AndroidManifest.xml
```xml
<intent-filter android:autoVerify="true">
    <action android:name="android.intent.action.VIEW" />
    <category android:name="android.intent.category.DEFAULT" />
    <category android:name="android.intent.category.BROWSABLE" />
    <data android:scheme="com.example.mymuzic" android:host="callback" />
</intent-filter>
```

### 2. Spotify App Settings
- Client ID: `c227a0aaa5d54c4881c1ad98e8dad3ec`
- Redirect URI: `com.example.mymuzic://callback`
- Scopes: 
  - user-read-private
  - user-read-email
  - user-read-recently-played
  - user-top-read
  - user-read-playback-state
  - user-modify-playback-state
  - playlist-read-private
  - playlist-read-collaborative
  - user-library-read

## Sử dụng

### 1. Trong Navigation
```kotlin
// Thêm route cho AuthScreen
composable(Routes.AUTH) {
    AuthScreen(
        onAuthSuccess = {
            navController.navigate(Routes.HOME) {
                popUpTo(Routes.AUTH) { inclusive = true }
            }
        }
    )
}
```

### 2. Kiểm tra Authentication State
```kotlin
val authViewModel: AuthViewModel by viewModel()
val authState by authViewModel.uiState.collectAsState()

if (authState.isAuthenticated) {
    // User đã đăng nhập
    // Chuyển đến Home screen
} else {
    // User chưa đăng nhập
    // Hiển thị AuthScreen
}
```

### 3. Sử dụng Access Token
```kotlin
val accessToken = authViewModel.getValidAccessToken()
if (accessToken != null) {
    // Sử dụng token để gọi Spotify API
    val headers = mapOf("Authorization" to "Bearer $accessToken")
}
```

## Lưu ý

1. **Security**: Code verifier được lưu trong SharedPreferences, trong production nên sử dụng EncryptedSharedPreferences
2. **Token Refresh**: Access token có thời hạn 1 giờ, cần refresh tự động
3. **Error Handling**: Cần xử lý các trường hợp lỗi network, token expired, user denied access
4. **Testing**: Sử dụng AuthTest.kt để test PKCE flow

## Troubleshooting

### Lỗi thường gặp:
1. **Invalid redirect URI**: Kiểm tra redirect URI trong Spotify Dashboard
2. **Code verifier mismatch**: Đảm bảo code_verifier được lưu và sử dụng đúng
3. **Network errors**: Kiểm tra internet connection và API endpoints
4. **Deep link không hoạt động**: Kiểm tra intent filter trong AndroidManifest.xml 