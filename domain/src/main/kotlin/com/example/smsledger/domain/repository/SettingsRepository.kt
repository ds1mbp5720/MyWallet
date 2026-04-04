package com.example.smsledger.domain.repository

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun getGeminiApiKey(): Flow<String>
    suspend fun saveGeminiApiKey(key: String)
    fun getUseSmartAi(): Flow<Boolean>
    suspend fun setUseSmartAi(use: Boolean)
}
