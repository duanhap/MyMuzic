# Data Models Organization - Clean Architecture

## Tổng quan
Các data models được tổ chức theo nguyên tắc **Single Responsibility Principle** và **Separation of Concerns** của Clean Architecture, phân tách rõ ràng giữa các loại model khác nhau.

## Cấu trúc thư mục

```
data/model/
├── auth/              # Authentication related models
│   └── AuthModels.kt
├── music/             # Music/Track related models
│   └── MusicModels.kt
├── response/          # API Response models
│   └── ApiResponseModels.kt
└── README.md          # This file
```

## Chi tiết từng module

### 1. Auth Module (`auth/`)
Chứa các model liên quan đến xác thực và quản lý người dùng:

- `SpotifyTokenResponse` - Response từ Spotify token API
- `SpotifyUserProfile` - Thông tin người dùng Spotify
- `SpotifyImage` - Model cho hình ảnh
- `ExplicitContent` - Cài đặt nội dung nhạy cảm
- `ExternalUrls` - URL bên ngoài
- `Followers` - Thông tin followers
- `AuthState` - Trạng thái xác thực trong app

### 2. Music Module (`music/`)
Chứa các model liên quan đến nhạc và tracks:

- `SpotifyTrack` - Thông tin bài hát
- `SpotifyAlbum` - Thông tin album
- `SpotifyArtist` - Thông tin nghệ sĩ
- `SpotifyImage` - Model cho hình ảnh (shared)
- `ExternalUrls` - URL bên ngoài (shared)
- `Followers` - Thông tin followers (shared)

### 3. Response Module (`response/`)
Chứa các model wrapper cho API responses:

- `RecentlyPlayedResponse` - Response cho recently played tracks
- `RecentlyPlayedItem` - Item trong recently played
- `TopTracksResponse` - Response cho top tracks
- `ArtistsResponse` - Response cho artists

## Lợi ích của cấu trúc mới

### ✅ Tuân thủ Clean Architecture
- **Single Responsibility**: Mỗi file chỉ chứa model cùng chức năng
- **Separation of Concerns**: Tách biệt rõ ràng giữa auth, music, response
- **Dependency Direction**: Domain layer không phụ thuộc vào data layer

### ✅ Dễ bảo trì và mở rộng
- Dễ tìm và sửa model cụ thể
- Có thể thêm model mới mà không ảnh hưởng module khác
- Code review dễ dàng hơn

### ✅ Tái sử dụng tốt hơn
- Shared models (`SpotifyImage`, `ExternalUrls`, `Followers`) có thể dùng chung
- Giảm duplication code
- Consistent structure

### ✅ Testing dễ dàng
- Có thể mock từng nhóm model riêng biệt
- Unit test tập trung vào chức năng cụ thể

## Cách sử dụng

### Import trong Use Cases
```kotlin
// Auth use cases
import com.example.mymuzic.data.model.auth.SpotifyTokenResponse
import com.example.mymuzic.data.model.auth.SpotifyUserProfile

// Music use cases
import com.example.mymuzic.data.model.music.SpotifyTrack
import com.example.mymuzic.data.model.music.SpotifyArtist

// Response models
import com.example.mymuzic.data.model.response.RecentlyPlayedItem
```

### Import trong Repository
```kotlin
import com.example.mymuzic.data.model.auth.AuthState
import com.example.mymuzic.data.model.music.SpotifyTrack
import com.example.mymuzic.data.model.response.RecentlyPlayedItem
```

### Import trong ViewModel
```kotlin
import com.example.mymuzic.data.model.auth.SpotifyUserProfile
import com.example.mymuzic.data.model.music.SpotifyTrack
import com.example.mymuzic.data.model.response.RecentlyPlayedItem
```

## Best Practices

1. **Naming convention**: Tên model phải rõ ràng về chức năng
2. **Package organization**: Nhóm theo chức năng, không theo layer
3. **Shared models**: Đặt ở package phù hợp nhất, import khi cần
4. **Serialization**: Sử dụng `@SerializedName` cho API mapping
5. **Documentation**: Comment cho các field quan trọng

## Migration từ cấu trúc cũ

Cấu trúc cũ có tất cả models trong một file `AuthModels.kt` (13 data classes) đã được tách thành 3 module riêng biệt theo chức năng:

- **Auth models**: 7 classes → `auth/AuthModels.kt`
- **Music models**: 4 classes → `music/MusicModels.kt`  
- **Response models**: 3 classes → `response/ApiResponseModels.kt`

Giúp code dễ đọc, bảo trì và tuân thủ clean architecture tốt hơn. 