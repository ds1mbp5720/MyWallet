package com.example.smsledger.domain.model

data class Transaction(
    val id: Long = 0,
    val amount: Long,
    val storeName: String,
    val date: Long = System.currentTimeMillis(),
    val category: String = "기타",
    val originalMessage: String = "",
    val type: TransactionType = TransactionType.EXPENSE
)

enum class TransactionType {
    INCOME, EXPENSE
}
