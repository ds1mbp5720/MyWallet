package com.example.smsledger.domain.usecase

import com.example.smsledger.domain.model.Transaction
import com.example.smsledger.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow

class GetTransactionsUseCase(private val repository: TransactionRepository) {
    operator fun invoke(): Flow<List<Transaction>> = repository.getTransactions()
}

class AddTransactionUseCase(private val repository: TransactionRepository) {
    suspend operator fun invoke(transaction: Transaction) = repository.insertTransaction(transaction)
}

class UpdateTransactionUseCase(private val repository: TransactionRepository) {
    suspend operator fun invoke(transaction: Transaction) = repository.updateTransaction(transaction)
}

class DeleteTransactionUseCase(private val repository: TransactionRepository) {
    suspend operator fun invoke(transaction: Transaction) = repository.deleteTransaction(transaction)
}
