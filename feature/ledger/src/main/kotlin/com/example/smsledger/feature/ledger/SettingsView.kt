package com.example.smsledger.feature.ledger

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smsledger.domain.model.Category
import com.example.smsledger.domain.model.ParsingRule
import com.example.smsledger.domain.model.TransactionType

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SettingsView(
    state: LedgerState, 
    viewModel: LedgerViewModel,
    onShowAddCategory: (Category?) -> Unit,
    onShowAddRule: (ParsingRule?) -> Unit
) {
    var categoryToDelete by remember { mutableStateOf<Category?>(null) }
    var testSms by remember { mutableStateOf("") }
    var testSender by remember { mutableStateOf("") }
    var apiKeyInput by remember(state.geminiApiKey) { mutableStateOf(state.geminiApiKey) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Gemini API Key Section
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    "AI 스마트 인식 설정",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
                    color = Color(0xFF1E293B)
                )
                Text(
                    "영수증 사진 분석을 위한 Gemini API 키를 입력하세요.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF64748B)
                )
                OutlinedTextField(
                    value = apiKeyInput,
                    onValueChange = { apiKeyInput = it },
                    label = { Text("Gemini API Key") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    placeholder = { Text("AI Studio에서 발급받은 키를 입력하세요") },
                    trailingIcon = {
                        if (apiKeyInput != state.geminiApiKey) {
                            TextButton(onClick = { 
                                viewModel.handleIntent(LedgerIntent.SaveApiKey(apiKeyInput))
                            }) {
                                Text("저장", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                )
            }
        }

        item { HorizontalDivider(color = Color(0xFFF1F5F9)) }

        // Category Management Section
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "카테고리 관리",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
                        color = Color(0xFF1E293B)
                    )
                    IconButton(onClick = { 
                        onShowAddCategory(null)
                    }) {
                        Icon(Icons.Default.AddCircle, contentDescription = "카테고리 추가", tint = Color(0xFF2563EB))
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    state.categories.forEach { category ->
                        Surface(
                            onClick = {
                                onShowAddCategory(category)
                            },
                            color = Color.White,
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(category.name, style = MaterialTheme.typography.bodyMedium, color = Color(0xFF1E293B))
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "삭제",
                                    modifier = Modifier.size(16.dp).clickable {
                                        categoryToDelete = category
                                    },
                                    tint = Color(0xFF94A3B8)
                                )
                            }
                        }
                    }
                }
            }
        }

        item { HorizontalDivider(color = Color(0xFFF1F5F9)) }

        // SMS Parsing Rules Section
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "SMS 파싱 규칙",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
                        color = Color(0xFF1E293B)
                    )
                    IconButton(onClick = { 
                        onShowAddRule(null)
                    }) {
                        Icon(Icons.Default.AddCircle, contentDescription = "규칙 추가", tint = Color(0xFF2563EB))
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                
                if (state.parsingRules.isEmpty()) {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFFF8FAFC),
                        border = BorderStroke(1.dp, Color(0xFFF1F5F9))
                    ) {
                        Text(
                            "등록된 파싱 규칙이 없습니다.\n우측 상단의 + 버튼을 눌러 추가하세요.",
                            modifier = Modifier.padding(20.dp),
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF94A3B8),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }
        }

        items(state.parsingRules) { rule ->
            ParsingRuleItem(
                rule = rule,
                onEdit = { 
                    onShowAddRule(rule)
                },
                onDelete = { viewModel.handleIntent(LedgerIntent.DeleteParsingRule(rule)) },
                onToggle = { viewModel.handleIntent(LedgerIntent.UpdateParsingRule(rule.copy(isActive = !rule.isActive))) }
            )
        }

        item { HorizontalDivider(color = Color(0xFFF1F5F9)) }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    "파싱 테스트",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
                    color = Color(0xFF1E293B)
                )
                
                OutlinedTextField(
                    value = testSender,
                    onValueChange = { testSender = it },
                    label = { Text("발신 번호 (선택사항)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    placeholder = { Text("예: 1588-8100") }
                )

                OutlinedTextField(
                    value = testSms,
                    onValueChange = { testSms = it },
                    label = { Text("테스트 문자 내용") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    minLines = 3
                )

                Button(
                    onClick = { viewModel.handleIntent(LedgerIntent.ParseSms(testSms, testSender.ifBlank { null })) },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("테스트 실행 (DB 저장됨)", fontWeight = FontWeight.Bold)
                }
            }
        }
        
        item { Spacer(modifier = Modifier.height(80.dp)) }
    }

    if (categoryToDelete != null) {
        AlertDialog(
            onDismissRequest = { categoryToDelete = null },
            title = { Text("카테고리 삭제") },
            text = { Text("'${categoryToDelete?.name}' 카테고리를 삭제하시겠습니까?\n해당 카테고리의 내역은 '미분류'로 이동됩니다.") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.handleIntent(LedgerIntent.DeleteCategory(categoryToDelete!!))
                        categoryToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("삭제")
                }
            },
            dismissButton = {
                TextButton(onClick = { categoryToDelete = null }) { Text("취소") }
            }
        )
    }
}

@Composable
fun ParsingRuleItem(
    rule: ParsingRule,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onToggle: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = if (rule.isActive) Color.White else Color(0xFFF8FAFC),
        border = BorderStroke(1.dp, if (rule.isActive) Color(0xFFF1F5F9) else Color(0xFFE2E8F0))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            rule.name,
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                            color = Color(0xFF1E293B)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            color = if (rule.type == TransactionType.INCOME) Color(0xFFEFF6FF) else Color(0xFFFEF2F2),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = if (rule.type == TransactionType.INCOME) "수입" else "지출",
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = if (rule.type == TransactionType.INCOME) Color(0xFF2563EB) else Color(0xFFEF4444)
                            )
                        }
                    }
                    if (!rule.senderNumber.isNullOrBlank()) {
                        Text("발신번호: ${rule.senderNumber}", style = MaterialTheme.typography.labelMedium, color = Color(0xFF64748B))
                    }
                }
                Switch(
                    checked = rule.isActive, 
                    onCheckedChange = { onToggle() },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Color(0xFF2563EB),
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = Color(0xFFE2E8F0),
                        uncheckedBorderColor = Color.Transparent
                    )
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            Text("금액 패턴: ${rule.amountPattern}", style = MaterialTheme.typography.labelSmall, color = Color(0xFF94A3B8))
            Text("상점 패턴: ${rule.storePattern}", style = MaterialTheme.typography.labelSmall, color = Color(0xFF94A3B8))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onEdit) { Text("수정", color = Color(0xFF2563EB), fontWeight = FontWeight.Bold) }
                TextButton(onClick = onDelete) { Text("삭제", color = Color(0xFFEF4444), fontWeight = FontWeight.Bold) }
            }
        }
    }
}
