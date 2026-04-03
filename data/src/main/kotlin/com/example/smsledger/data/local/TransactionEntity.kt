package com.example.smsledger.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val amount: Long,
    val storeName: String,
    val date: Long,
    val category: String,
    val originalMessage: String,
    val type: String = "EXPENSE"
)
