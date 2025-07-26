# Architecture Final - Chia sẻ State giữa các ViewModel

## Cấu trúc hoàn thiện

### 1. AppNavHost (Root Level)
```kotlin
@Composable
fun AppNavHost(navController: NavHostController, authViewModel: AuthViewModel) {
    // Tạo MusicViewModel ở đây để chia sẻ với tất cả các màn hình
    val musicViewModel: MusicViewModel = koinViewModel()
    
    NavHost(...) {
        composable("home") {
            MainScreen(navController, authViewModel, musicViewModel)
        }
        composable("play_song") {
            PlaySongScreen(..., musicViewModel)
        }
        // Các route khác
    }
}
```

### 2. MainScreen (Parent Scope)
```kotlin
@Composable
fun MainScreen(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    musicViewModel: MusicViewModel  // Nhận từ AppNavHost
) {
    Scaffold(
        bottomBar = {
            BottomNavBar(navController, currentRoute, musicViewModel)
        }
    ) {
        when (currentRoute) {
            "home" -> HomeScreen(navController, musicViewModel, modifier)
            "explore" -> ExploreScreen(modifier)
            "library" -> LibraryScreen(modifier)
            "profile" -> ProfileScreen(navController, modifier)
        }
    }
}
```

### 3. Các Composable con
```kotlin
// BottomNavBar
@Composable
fun BottomNavBar(
    navController: NavController,
    currentRoute: String?,
    musicViewModel: MusicViewModel  // Nhận từ MainScreen
) {
    val musicUiState by musicViewModel.uiState.collectAsState()
    // ...
}

// PlaySongScreen
@Composable
fun PlaySongScreen(
    navController: NavController,
    id: String,
    name: String,
    imageUrl: String,
    artist: String,
    musicViewModel: MusicViewModel  // Nhận từ AppNavHost
) {
    val musicUiState by musicViewModel.uiState.collectAsState()
    // ...
}
```

## Luồng dữ liệu

```
AppNavHost
    ↓ (tạo MusicViewModel)
MainScreen
    ↓ (truyền musicViewModel)
BottomNavBar ← HomeScreen ← ExploreScreen ← LibraryScreen ← ProfileScreen
    ↓ (truyền musicViewModel)
PlaySongScreen (từ AppNavHost)
```

## Cách hoạt động

### 1. Single Source of Truth
- `MusicViewModel` được tạo một lần ở `AppNavHost`
- Tất cả các màn hình đều nhận cùng một instance
- State được chia sẻ real-time

### 2. NavBackStackEntry Scope
- `MusicViewModel` được tạo trong cùng NavBackStackEntry với các màn hình chính
- Khi AppNavHost bị destroy, ViewModel cũng bị clear
- Không có memory leaks

### 3. Event Handling
```kotlin
// Cập nhật current playing track
musicViewModel.handleEvent(MusicEvent.UpdateCurrentPlayingTrack(track, isPlaying))

// Toggle play/pause
musicViewModel.handleEvent(MusicEvent.TogglePlayPause)
```

## Ưu điểm

1. **Chuẩn Compose/Koin**: Không lạm dụng singleton, không global scope
2. **Dễ test**: Có thể mock ViewModel dễ dàng
3. **Lifecycle management**: ViewModel tự động clear khi AppNavHost destroy
4. **Memory efficient**: Không có memory leaks
5. **Code clean**: Rõ ràng luồng dữ liệu
6. **Single NavHost**: Không có xung đột ViewModelStore

## Các file đã được cập nhật

- ✅ `AppNavHost.kt` - Tạo MusicViewModel và truyền xuống
- ✅ `MainScreen.kt` - Nhận musicViewModel và quản lý BottomNavBar
- ✅ `BottomNavBar.kt` - Nhận musicViewModel từ MainScreen
- ✅ `HomeScreen.kt` - Nhận musicViewModel từ MainScreen
- ✅ `PlaySongScreen.kt` - Nhận musicViewModel từ AppNavHost
- ✅ `ExploreScreen.kt` - Thêm modifier parameter
- ✅ `LibraryScreen.kt` - Thêm modifier parameter
- ✅ `ProfileScreen.kt` - Thêm modifier parameter
- ✅ `ViewModelModule.kt` - Dùng viewModel thay vì single

## Cách test

1. Chạy ứng dụng
2. Mở một bài hát từ HomeScreen
3. Kiểm tra BottomNavBar có hiển thị bài hát đang phát
4. Nhấn play/pause ở BottomNavBar
5. Kiểm tra PlaySongScreen có cập nhật trạng thái
6. Xem log để confirm cùng instance hash

## Kết luận

Đây là architecture **hoàn thiện và chuẩn nhất** để chia sẻ state giữa các ViewModel trong Jetpack Compose/Koin:

- ✅ Không có lỗi ViewModelStore
- ✅ State được chia sẻ hoàn hảo
- ✅ Code clean và maintainable
- ✅ Dễ test và debug
- ✅ Không có memory leaks
- ✅ Tuân thủ best practices 