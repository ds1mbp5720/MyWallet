package com.example.smsledger.feature.ledger

import java.text.SimpleDateFormat
import java.util.*

fun Long.toKoreanCurrency(): String {
    return "₩${String.format("%,d", Math.abs(this))}"
}

fun Long.toFormattedDate(): String {
    val sdf = SimpleDateFormat("MM월 dd일 HH:mm", Locale.KOREA)
    return sdf.format(Date(this))
}
