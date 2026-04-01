package com.example.smsledger.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.smsledger.domain.model.ParsingRule

@Entity(tableName = "parsing_rules")
data class ParsingRuleEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val senderNumber: String? = null,
    val amountPattern: String,
    val storePattern: String,
    val isActive: Boolean = true
)

fun ParsingRuleEntity.toDomain() = ParsingRule(
    id = id,
    name = name,
    senderNumber = senderNumber,
    amountPattern = amountPattern,
    storePattern = storePattern,
    isActive = isActive
)

fun ParsingRule.toEntity() = ParsingRuleEntity(
    id = id,
    name = name,
    senderNumber = senderNumber,
    amountPattern = amountPattern,
    storePattern = storePattern,
    isActive = isActive
)
