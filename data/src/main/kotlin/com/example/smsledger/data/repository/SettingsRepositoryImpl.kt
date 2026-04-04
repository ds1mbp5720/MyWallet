package com.example.smsledger.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.smsledger.domain.repository.SettingsRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.onStart

class SettingsRepositoryImpl(context: Context) : SettingsRepository {
    private val prefs: SharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)

    override fun getGeminiApiKey(): Flow<String> = callbackFlow {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
            if (key == "gemini_api_key") {
                trySend(sharedPreferences.getString("gemini_api_key", "") ?: "")
            }
        }
        prefs.registerOnSharedPreferenceChangeListener(listener)
        trySend(prefs.getString("gemini_api_key", "") ?: "")
        awaitClose { prefs.unregisterOnSharedPreferenceChangeListener(listener) }
    }

    override suspend fun saveGeminiApiKey(key: String) {
        prefs.edit().putString("gemini_api_key", key).apply()
    }
}
