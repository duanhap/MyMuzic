package com.example.mymuzic.domain.usecase.music

import com.example.mymuzic.data.model.response.CategoriesContent
import com.example.mymuzic.domain.repository.MusicRepository

class GetCategoriesUseCase(private val musicRepository: MusicRepository) {
    suspend operator fun invoke(limit: Int = 20, offset: Int = 0, country: String = "US"): Result<CategoriesContent> {
        return musicRepository.getCategories(limit, offset, country)
    }
} 