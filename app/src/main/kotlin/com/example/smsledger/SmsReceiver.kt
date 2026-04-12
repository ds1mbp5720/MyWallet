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

import android.widget.Toast
import android.os.Handler
import android.os.Looper

class SmsReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            Log.d("SmsReceiver", "SMS_RECEIVED_ACTION triggered")
            val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
            val app = context.applicationContext as SmsLedgerApp
            
            // Combine multi-part messages
            val fullBody = messages.joinToString("") { it.displayMessageBody ?: "" }
            val sender = messages.firstOrNull()?.originatingAddress
            
            if (fullBody.isBlank() || sender == null) {
                Log.d("SmsReceiver", "Empty body or sender")
                return
            }

            Log.d("SmsReceiver", "Received SMS from $sender: $fullBody")
            
            // Show toast for debugging
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(context, "문자 수신: $sender", Toast.LENGTH_SHORT).show()
            }
            
            val pendingResult = goAsync()
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val rules = app.getParsingRulesUseCase().first().filter { it.isActive }
                    Log.d("SmsReceiver", "Checking ${rules.size} active rules")
                    
                    for (rule in rules) {
                        // Check sender if specified
                        val ruleSender = rule.senderNumber
                        if (!ruleSender.isNullOrBlank()) {
                            // Normalize numbers: remove all non-digit characters
                            val cleanSender = sender.filter { it.isDigit() }
                            val cleanRuleSender = ruleSender.filter { it.isDigit() }
                            
                            if (!cleanSender.endsWith(cleanRuleSender) && !cleanRuleSender.endsWith(cleanSender)) {
                                Log.d("SmsReceiver", "Sender mismatch: $cleanSender vs $cleanRuleSender")
                                continue
                            }
                        }

                        try {
                            val amountPattern = Pattern.compile(rule.amountPattern)
                            val storePattern = Pattern.compile(rule.storePattern)
                            val amountMatcher = amountPattern.matcher(fullBody)
                            val storeMatcher = storePattern.matcher(fullBody)

                            if (amountMatcher.find()) {
                                val amountStr = try { amountMatcher.group(1) } catch (e: Exception) { null }
                                if (amountStr == null) continue

                                val amount = amountStr.replace(",", "").toLongOrNull() ?: 0L
                                val store = if (storeMatcher.find()) {
                                    try { storeMatcher.group(1).trim() } catch (e: Exception) { "알 수 없음" }
                                } else {
                                    "알 수 없음"
                                }
                                
                                app.addTransactionUseCase(Transaction(
                                    amount = amount, 
                                    storeName = store, 
                                    originalMessage = fullBody,
                                    type = rule.type
                                ))
                                Log.d("SmsReceiver", "Successfully parsed and saved transaction: $amount from $store")
                                break // Match found for this message
                            }
                        } catch (e: Exception) {
                            Log.e("SmsReceiver", "Error parsing with rule ${rule.name}", e)
                        }
                    }
                } catch (e: Exception) {
                    Log.e("SmsReceiver", "Error in CoroutineScope", e)
                } finally {
                    pendingResult.finish()
                }
            }
        }
    }
}
