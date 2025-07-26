package com.example.mymuzic.domain.usecase.music

import com.example.mymuzic.domain.repository.MusicRepository

class GetNewReleasesUseCase(private val musicRepository: MusicRepository) {
    suspend operator fun invoke(limit: Int = 10, offset: Int = 0, country: String = "VN") =
        musicRepository.getNewReleases(limit, offset, country)
} 