package com.example.smsledger

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.util.Log
import com.example.smsledger.domain.model.Transaction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class SmsReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
            val app = context.applicationContext as SmsLedgerApp
            
            for (message in messages) {
                val body = message.displayMessageBody
                val sender = message.originatingAddress
                Log.d("SmsReceiver", "Received SMS from $sender: $body")
                
                CoroutineScope(Dispatchers.IO).launch {
                    val rules = app.getParsingRulesUseCase().first().filter { it.isActive }
                    
                    for (rule in rules) {
                        // Check sender if specified
                        if (!rule.senderNumber.isNullOrBlank() && sender != null) {
                            if (!sender.contains(rule.senderNumber)) continue
                        }

                        try {
                            val amountPattern = Pattern.compile(rule.amountPattern)
                            val storePattern = Pattern.compile(rule.storePattern)
                            val amountMatcher = amountPattern.matcher(body)
                            val storeMatcher = storePattern.matcher(body)

                            if (amountMatcher.find()) {
                                val amount = amountMatcher.group(1).replace(",", "").toLongOrNull() ?: 0L
                                val store = if (storeMatcher.find()) storeMatcher.group(1).trim() else "알 수 없음"
                                
                                app.addTransactionUseCase(Transaction(
                                    amount = amount, 
                                    storeName = store, 
                                    originalMessage = body,
                                    type = rule.type
                                ))
                                Log.d("SmsReceiver", "Successfully parsed and saved transaction: $amount from $store")
                                break // Match found for this message
                            }
                        } catch (e: Exception) {
                            Log.e("SmsReceiver", "Error parsing with rule ${rule.name}", e)
                        }
                    }
                }
            }
        }
    }
}
