package com.example.smsledger.mvi

import com.example.smsledger.data.Transaction

data class LedgerState(
    val transactions: List<Transaction> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val totalAmount: Long = 0
)

sealed class LedgerIntent {
    object LoadTransactions : LedgerIntent()
    data class AddTransaction(val transaction: Transaction) : LedgerIntent()
    data class DeleteTransaction(val transaction: Transaction) : LedgerIntent()
    data class ParseSms(val message: String) : LedgerIntent()
}

sealed class LedgerEffect {
    data class ShowToast(val message: String) : LedgerEffect()
}
