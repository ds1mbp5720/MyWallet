package com.example.smsledger.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val amount: Long,
    val storeName: String,
    val date: Long = System.currentTimeMillis(),
    val category: String = "기타",
    val originalMessage: String
)
