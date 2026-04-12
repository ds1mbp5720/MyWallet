package com.example.smsledger

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.smsledger.feature.ledger.LedgerViewModel
import com.example.smsledger.feature.ledger.LedgerScreen
import com.example.smsledger.feature.ledger.LedgerIntent
import com.example.smsledger.ui.theme.SmsLedgerTheme
import com.google.android.gms.ads.MobileAds

class MainActivity : ComponentActivity() {
    
    private val viewModel: LedgerViewModel by viewModels {
        val app = application as SmsLedgerApp
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return LedgerViewModel(
                    app.getTransactionsUseCase,
                    app.addTransactionUseCase,
                    app.updateTransactionUseCase,
                    app.deleteTransactionUseCase,
                    app.getParsingRulesUseCase,
                    app.addParsingRuleUseCase,
                    app.updateParsingRuleUseCase,
                    app.deleteParsingRuleUseCase,
                    app.getCategoriesUseCase,
                    app.addCategoryUseCase,
                    app.updateCategoryUseCase,
                    app.deleteCategoryUseCase,
                    app.getGeminiApiKeyUseCase,
                    app.saveGeminiApiKeyUseCase,
                    app.getUseSmartAiUseCase,
                    app.setUseSmartAiUseCase,
                    app.getRecurringTransactionsUseCase,
                    app.addRecurringTransactionUseCase,
                    app.updateRecurringTransactionUseCase,
                    app.deleteRecurringTransactionUseCase
                ) as T
            }
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions.entries.all { it.value }
        if (granted) {
            // Permission granted
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        MobileAds.initialize(this) {}
        checkPermissions()

        setContent {
            SmsLedgerTheme {
                var showBatteryDialog by remember { mutableStateOf(false) }
                
                LaunchedEffect(Unit) {
                    if (!isIgnoringBatteryOptimizations(this@MainActivity)) {
                        showBatteryDialog = true
                    }
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LedgerScreen(viewModel = viewModel)
                    
                    if (showBatteryDialog) {
                        AlertDialog(
                            onDismissRequest = { showBatteryDialog = false },
                            title = { Text("백그라운드 실행 권한", fontWeight = FontWeight.Bold) },
                            text = { Text("앱이 꺼져 있을 때도 문자를 자동으로 읽어오려면 배터리 최적화 '제한 없음' 설정이 필요합니다.\n\n설정 화면에서 '제한 없음'을 선택해 주세요.") },
                            confirmButton = {
                                Button(
                                    onClick = {
                                        showBatteryDialog = false
                                        requestIgnoreBatteryOptimizations(this@MainActivity)
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB)),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text("설정하러 가기", fontWeight = FontWeight.Bold)
                                }
                            },
                            dismissButton = {
                                TextButton(onClick = { showBatteryDialog = false }) {
                                    Text("나중에", color = Color(0xFF64748B))
                                }
                            },
                            shape = RoundedCornerShape(24.dp),
                            containerColor = Color.White
                        )
                    }
                }
            }
        }
    }

    private fun isIgnoringBatteryOptimizations(context: Context): Boolean {
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            powerManager.isIgnoringBatteryOptimizations(context.packageName)
        } else {
            true
        }
    }

    private fun requestIgnoreBatteryOptimizations(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
                    data = Uri.parse("package:${context.packageName}")
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(intent)
            } catch (e: Exception) {
                // Fallback to app details if the direct request fails
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", context.packageName, null)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(intent)
            }
        }
    }

    private fun checkPermissions() {
        val permissions = arrayOf(
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_SMS
        )
        val missingPermissions = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }
        if (missingPermissions.isNotEmpty()) {
            requestPermissionLauncher.launch(missingPermissions.toTypedArray())
        }
    }
}
