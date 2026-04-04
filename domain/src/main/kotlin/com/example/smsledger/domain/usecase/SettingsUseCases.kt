package com.example.smsledger.domain.usecase

import com.example.smsledger.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow

class GetGeminiApiKeyUseCase(private val repository: SettingsRepository) {
    operator fun invoke(): Flow<String> = repository.getGeminiApiKey()
}

class SaveGeminiApiKeyUseCase(private val repository: SettingsRepository) {
    suspend operator fun invoke(key: String) = repository.saveGeminiApiKey(key)
}
