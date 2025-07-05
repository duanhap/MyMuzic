# Use Case Organization - Clean Architecture

## Tổng quan
Các use case được tổ chức theo nguyên tắc **Single Responsibility Principle** và **Separation of Concerns** của Clean Architecture.

## Cấu trúc thư mục

```
domain/usecase/
├── auth/           # Authentication related use cases
│   └── AuthUseCases.kt
├── music/          # Music/Track related use cases  
│   └── MusicUseCases.kt
├── player/         # Player/Playback related use cases
│   └── PlayerUseCases.kt
└── README.md       # This file
```

## Chi tiết từng module

### 1. Auth Module (`auth/`)
Chứa các use case liên quan đến xác thực và quản lý người dùng:

- `GenerateAuthUrlUseCase` - Tạo URL xác thực Spotify
- `HandleAuthCallbackUseCase` - Xử lý callback từ Spotify
- `RefreshTokenUseCase` - Làm mới access token
- `GetUserProfileUseCase` - Lấy thông tin người dùng
- `GetValidAccessTokenUseCase` - Lấy access token hợp lệ
- `IsAuthenticatedUseCase` - Kiểm tra trạng thái đăng nhập
- `LogoutUseCase` - Đăng xuất
- `GetAuthStateUseCase` - Lấy trạng thái xác thực

### 2. Music Module (`music/`)
Chứa các use case liên quan đến quản lý nhạc và tracks:

- `GetRecentlyPlayedTracksUseCase` - Lấy danh sách bài hát vừa nghe
- `GetTopTracksUseCase` - Lấy top tracks của người dùng
- `GetArtistsByIdsUseCase` - Lấy thông tin nghệ sĩ theo IDs
- `GetTrackDetailUseCase` - Lấy chi tiết bài hát

### 3. Player Module (`player/`)
Chứa các use case liên quan đến phát nhạc:

- `PlaySpotifyTrackUseCase` - Phát bài hát qua Spotify

## Lợi ích của cấu trúc mới

### ✅ Tuân thủ Clean Architecture
- **Single Responsibility**: Mỗi file chỉ chứa use case cùng chức năng
- **Separation of Concerns**: Tách biệt rõ ràng giữa auth, music, player
- **Dependency Inversion**: Use case chỉ phụ thuộc vào repository interface

### ✅ Dễ bảo trì và mở rộng
- Dễ tìm và sửa use case cụ thể
- Có thể thêm use case mới mà không ảnh hưởng module khác
- Code review dễ dàng hơn

### ✅ Tái sử dụng tốt hơn
- Có thể inject use case riêng lẻ thay vì toàn bộ file
- Giảm coupling giữa các module

### ✅ Testing dễ dàng
- Có thể mock từng nhóm use case riêng biệt
- Unit test tập trung vào chức năng cụ thể

## Cách sử dụng

### Trong DI Module
```kotlin
val UsecaseModule = module {
    // Auth use cases
    single { GenerateAuthUrlUseCase(get()) }
    single { HandleAuthCallbackUseCase(get()) }
    // ...
    
    // Music use cases  
    single { GetRecentlyPlayedTracksUseCase(get()) }
    single { GetTopTracksUseCase(get()) }
    // ...
    
    // Player use cases
    single { PlaySpotifyTrackUseCase(get()) }
}
```

### Trong ViewModel
```kotlin
class AuthViewModel(
    private val generateAuthUrlUseCase: GenerateAuthUrlUseCase,
    private val getRecentlyPlayedTracksUseCase: GetRecentlyPlayedTracksUseCase,
    // ...
) : ViewModel()
```

## Best Practices

1. **Mỗi use case chỉ làm một việc**: Không nên có use case làm nhiều chức năng khác nhau
2. **Naming convention**: Tên use case phải rõ ràng về chức năng
3. **Dependency injection**: Sử dụng constructor injection cho repository
4. **Error handling**: Trả về `Result<T>` để xử lý lỗi
5. **Testing**: Viết unit test cho từng use case

## Migration từ cấu trúc cũ

Cấu trúc cũ có tất cả use case trong một file `AuthUseCases.kt` (11 use cases) đã được tách thành 3 module riêng biệt theo chức năng, giúp code dễ đọc và bảo trì hơn. 