package com.example.smsledger.data.repository

import com.example.smsledger.data.local.ParsingRuleDao
import com.example.smsledger.data.local.toDomain
import com.example.smsledger.data.local.toEntity
import com.example.smsledger.domain.model.ParsingRule
import com.example.smsledger.domain.repository.ParsingRuleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ParsingRuleRepositoryImpl(private val dao: ParsingRuleDao) : ParsingRuleRepository {
    override fun getParsingRules(): Flow<List<ParsingRule>> = 
        dao.getParsingRules().map { list -> list.map { it.toDomain() } }

    override suspend fun addParsingRule(rule: ParsingRule) = dao.insert(rule.toEntity())

    override suspend fun updateParsingRule(rule: ParsingRule) = dao.update(rule.toEntity())

    override suspend fun deleteParsingRule(rule: ParsingRule) = dao.delete(rule.toEntity())
}
