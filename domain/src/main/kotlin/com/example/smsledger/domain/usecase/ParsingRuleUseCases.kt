package com.example.smsledger.domain.usecase

import com.example.smsledger.domain.model.ParsingRule
import com.example.smsledger.domain.repository.ParsingRuleRepository
import kotlinx.coroutines.flow.Flow

class GetParsingRulesUseCase(private val repository: ParsingRuleRepository) {
    operator fun invoke(): Flow<List<ParsingRule>> = repository.getParsingRules()
}

class AddParsingRuleUseCase(private val repository: ParsingRuleRepository) {
    suspend operator fun invoke(rule: ParsingRule) = repository.addParsingRule(rule)
}

class UpdateParsingRuleUseCase(private val repository: ParsingRuleRepository) {
    suspend operator fun invoke(rule: ParsingRule) = repository.updateParsingRule(rule)
}

class DeleteParsingRuleUseCase(private val repository: ParsingRuleRepository) {
    suspend operator fun invoke(rule: ParsingRule) = repository.deleteParsingRule(rule)
}
