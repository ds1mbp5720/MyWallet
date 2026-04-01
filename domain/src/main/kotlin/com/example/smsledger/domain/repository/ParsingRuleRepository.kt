package com.example.smsledger.domain.repository

import com.example.smsledger.domain.model.ParsingRule
import kotlinx.coroutines.flow.Flow

interface ParsingRuleRepository {
    fun getParsingRules(): Flow<List<ParsingRule>>
    suspend fun addParsingRule(rule: ParsingRule)
    suspend fun updateParsingRule(rule: ParsingRule)
    suspend fun deleteParsingRule(rule: ParsingRule)
}
