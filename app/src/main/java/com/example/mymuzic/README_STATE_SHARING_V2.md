# Chia sẻ State giữa các ViewModel - Cách chuẩn nhất

## Vấn đề ban đầu

Trước đây, chúng ta đã thử 2 cách:
1. **Singleton ViewModel**: Dùng `single { }` trong Koin - không chuẩn
2. **get() function**: Dùng `get()` để lấy singleton - không chuẩn

## Giải pháp chuẩn nhất: Parent Scope ViewModel

### 1. Cấu trúc mới

```kotlin
@Composable
fun MainScreen(navController: NavHostController, authViewModel: AuthViewModel) {
    // Tạo MusicViewModel ở đây - sẽ được chia sẻ với tất cả các màn hình con
    val musicViewModel: MusicViewModel = koinViewModel()
    
    Scaffold(
        bottomBar = {
            BottomNavBar(navController, currentRoute, musicViewModel)
        }
    ) {
        NavHost(...) {
            composable("home") {
                HomeScreen(navController, musicViewModel)
            }
            composable("play_song") {
                PlaySongScreen(navController, ..., musicViewModel)
            }
        }
    }
}
```

### 2. Truyền ViewModel qua parameters

```kotlin
// BottomNavBar
@Composable
fun BottomNavBar(
    navController: NavController, 
    currentRoute: String?,
    musicViewModel: MusicViewModel  // Nhận từ parent
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
    musicViewModel: MusicViewModel  // Nhận từ parent
) {
    val musicUiState by musicViewModel.uiState.collectAsState()
    // ...
}
```

### 3. Các file đã được cập nhật

- ✅ `MainScreen.kt` - Tạo và quản lý MusicViewModel
- ✅ `AppNavHost.kt` - Sử dụng MainScreen
- ✅ `BottomNavBar.kt` - Nhận musicViewModel từ parameter
- ✅ `HomeScreen.kt` - Nhận musicViewModel từ parameter
- ✅ `PlaySongScreen.kt` - Nhận musicViewModel từ parameter
- ✅ `MainActivity.kt` - Đơn giản hóa, chỉ gọi AppNavHost
- ✅ `ViewModelModule.kt` - Khôi phục về viewModel thay vì single

## Cách hoạt động

### 1. NavBackStackEntry Scope
- `MusicViewModel` được tạo trong `MainScreen` (cùng NavBackStackEntry)
- Tất cả các composable con trong cùng NavBackStackEntry sẽ dùng chung instance
- Khi MainScreen bị destroy, ViewModel cũng bị clear

### 2. State Flow
- `MusicViewModel` sử dụng `StateFlow` để quản lý state
- Khi state thay đổi, tất cả các Composable đang observe sẽ được notify
- State được chia sẻ real-time giữa các màn hình

### 3. Event Handling
```kotlin
// Cập nhật current playing track
musicViewModel.handleEvent(MusicEvent.UpdateCurrentPlayingTrack(track, isPlaying))

// Toggle play/pause
musicViewModel.handleEvent(MusicEvent.TogglePlayPause)
```

## Ưu điểm của cách này

1. **Chuẩn Compose/Koin**: Không lạm dụng singleton, không phụ thuộc global scope
2. **Dễ test**: Có thể mock ViewModel dễ dàng
3. **Lifecycle management**: ViewModel tự động bị clear khi MainScreen bị destroy
4. **Dễ đọc code**: Rõ ràng luồng dữ liệu, không bị bất ngờ
5. **Memory efficient**: Không có memory leaks

## So sánh các cách

| Cách | Ưu điểm | Nhược điểm | Khuyến nghị |
|------|---------|------------|-------------|
| Singleton | Đơn giản | Không chuẩn, khó test | ❌ |
| get() function | Đơn giản | Không chuẩn, global scope | ❌ |
| Parent Scope | Chuẩn, dễ test | Phức tạp hơn một chút | ✅ |

## Cách test

1. Chạy ứng dụng
2. Mở một bài hát từ HomeScreen
3. Kiểm tra BottomNavBar có hiển thị bài hát đang phát
4. Nhấn play/pause ở BottomNavBar
5. Kiểm tra PlaySongScreen có cập nhật trạng thái
6. Xem log để confirm cùng instance hash

## Kết luận

Đây là cách **chuẩn nhất và được khuyến nghị** trong Jetpack Compose/Koin để chia sẻ state giữa các ViewModel. Nó đảm bảo:

- ✅ Code clean và maintainable
- ✅ Dễ test và debug
- ✅ Không có memory leaks
- ✅ Tuân thủ best practices của Compose/Koin 