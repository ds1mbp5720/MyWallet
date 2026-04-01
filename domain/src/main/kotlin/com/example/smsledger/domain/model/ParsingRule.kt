package com.example.smsledger.domain.model

data class ParsingRule(
    val id: Long = 0,
    val name: String,
    val senderNumber: String? = null,
    val amountPattern: String,
    val storePattern: String,
    val isActive: Boolean = true,
    val type: TransactionType = TransactionType.EXPENSE
)
