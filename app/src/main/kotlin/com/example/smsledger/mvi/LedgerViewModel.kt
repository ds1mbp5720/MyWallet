package com.example.smsledger.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smsledger.data.Transaction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class LedgerViewModel : ViewModel() {

    private val _state = MutableStateFlow(LedgerState())
    val state: StateFlow<LedgerState> = _state.asStateFlow()

    init {
        handleIntent(LedgerIntent.LoadTransactions)
    }

    fun handleIntent(intent: LedgerIntent) {
        when (intent) {
            is LedgerIntent.LoadTransactions -> loadTransactions()
            is LedgerIntent.AddTransaction -> addTransaction(intent.transaction)
            is LedgerIntent.DeleteTransaction -> deleteTransaction(intent.transaction)
            is LedgerIntent.ParseSms -> parseSms(intent.message)
        }
    }

    private fun loadTransactions() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            // In a real app, this would come from a Repository
            // Mocking some data for now
            val mockData = listOf(
                Transaction(1, 15000, "스타벅스", System.currentTimeMillis(), "카페", "카드승인 15,000원"),
                Transaction(2, 45000, "이마트", System.currentTimeMillis() - 86400000, "식비", "카드승인 45,000원")
            )
            _state.update { 
                it.copy(
                    transactions = mockData, 
                    isLoading = false,
                    totalAmount = mockData.sumOf { t -> t.amount }
                ) 
            }
        }
    }

    private fun addTransaction(transaction: Transaction) {
        viewModelScope.launch {
            val newList = _state.value.transactions + transaction
            _state.update { 
                it.copy(
                    transactions = newList,
                    totalAmount = newList.sumOf { t -> t.amount }
                ) 
            }
        }
    }

    private fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch {
            val newList = _state.value.transactions.filter { it.id != transaction.id }
            _state.update { 
                it.copy(
                    transactions = newList,
                    totalAmount = newList.sumOf { t -> t.amount }
                ) 
            }
        }
    }

    private fun parseSms(message: String) {
        // Simple regex for parsing SMS like "카드승인 15,000원 스타벅스"
        val amountPattern = Pattern.compile("([0-9,]+)원")
        val storePattern = Pattern.compile("원\\s+(.+)")

        val amountMatcher = amountPattern.matcher(message)
        val storeMatcher = storePattern.matcher(message)

        if (amountMatcher.find()) {
            val amountStr = amountMatcher.group(1).replace(",", "")
            val amount = amountStr.toLongOrNull() ?: 0L
            
            var storeName = "알 수 없음"
            if (storeMatcher.find()) {
                storeName = storeMatcher.group(1).trim()
            }

            val transaction = Transaction(
                amount = amount,
                storeName = storeName,
                originalMessage = message
            )
            addTransaction(transaction)
        }
    }
}
