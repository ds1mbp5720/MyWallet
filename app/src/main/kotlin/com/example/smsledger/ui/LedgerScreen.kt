package com.example.smsledger.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smsledger.data.Transaction
import com.example.smsledger.mvi.LedgerIntent
import com.example.smsledger.mvi.LedgerViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LedgerScreen(viewModel: LedgerViewModel) {
    val state by viewModel.state.collectAsState()
    val numberFormat = NumberFormat.getCurrencyInstance(Locale.KOREA)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("SMS 가계부", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { 
                // Manual add dialog could go here
            }) {
                Icon(Icons.Default.Add, contentDescription = "추가")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            // Summary Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text("이번 달 총 지출", style = MaterialTheme.typography.labelMedium)
                    Text(
                        text = numberFormat.format(state.totalAmount),
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Text(
                "최근 내역",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            if (state.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(state.transactions.reversed()) { transaction ->
                        TransactionItem(
                            transaction = transaction,
                            onDelete = { viewModel.handleIntent(LedgerIntent.DeleteTransaction(transaction)) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TransactionItem(transaction: Transaction, onDelete: () -> Unit) {
    val dateFormat = SimpleDateFormat("MM월 dd일 HH:mm", Locale.KOREA)
    val numberFormat = NumberFormat.getNumberInstance(Locale.KOREA)

    ListItem(
        headlineContent = { Text(transaction.storeName, fontWeight = FontWeight.SemiBold) },
        supportingContent = { Text(dateFormat.format(Date(transaction.date))) },
        trailingContent = {
            Row(verticalAlignment = Alignment.CenterCenter) {
                Text(
                    "${numberFormat.format(transaction.amount)}원",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFD32F2F),
                    fontSize = 16.sp
                )
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "삭제", tint = Color.Gray)
                }
            }
        },
        overlineContent = { Text(transaction.category, color = MaterialTheme.colorScheme.secondary) }
    )
    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = Color.LightGray)
}
