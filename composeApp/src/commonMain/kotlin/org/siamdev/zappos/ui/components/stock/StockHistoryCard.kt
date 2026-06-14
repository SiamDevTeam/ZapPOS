/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.components.stock

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoveToInbox
import androidx.compose.material.icons.filled.ShoppingCartCheckout
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun StockHistoryCard(
    filteredHistory: List<StockRecord>,
    totalCount: Int,
    filter: HistoryFilter,
    onFilter: (HistoryFilter) -> Unit,
    unit: String,
    modifier: Modifier = Modifier,
    compact: Boolean = false,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(0.dp),
        border = CardDefaults.outlinedCardBorder(),
    ) {
        // Header
        Column(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .padding(top = 12.dp, bottom = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    "STOCK HISTORY",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                if (totalCount > 0) {
                    Text(
                        "${filteredHistory.size}/$totalCount",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                listOf(
                    "All" to HistoryFilter.ALL,
                    "Stock In" to HistoryFilter.STOCK_IN,
                    "Stock Out" to HistoryFilter.STOCK_OUT,
                ).forEach { (label, f) ->
                    val selected = filter == f
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(
                                if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                                else Color.Transparent
                            )
                            .border(
                                1.dp,
                                if (selected) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.outlineVariant,
                                RoundedCornerShape(6.dp),
                            )
                            .clickable { onFilter(f) }
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                    ) {
                        Text(
                            label,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
                            color = if (selected) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

        if (compact) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 300.dp)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 8.dp, vertical = 4.dp),
            ) {
                filteredHistory.forEachIndexed { index, record ->
                    StockRecordRow(record = record, unit = unit, isEven = index % 2 == 1)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                contentPadding = PaddingValues(vertical = 4.dp),
            ) {
                itemsIndexed(
                    filteredHistory,
                    key = { _, it -> "${it.id}-${it.date}-${it.time}" },
                ) { index, record ->
                    StockRecordRow(record = record, unit = unit, isEven = index % 2 == 1)
                }
            }
        }
    }
}

@Composable
private fun StockRecordRow(record: StockRecord, unit: String, isEven: Boolean = false) {
    val isIn = record.type == StockMoveType.IN
    val amountColor = if (isIn) Color(0xFF4CAF50) else Color(0xFFF44336)
    val amountText = if (isIn) "+${record.qty} $unit" else "-${record.qty} $unit"
    val icon = if (isIn) Icons.Default.MoveToInbox else Icons.Default.ShoppingCartCheckout

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(
                if (isEven) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.04f)
                else Color.Transparent
            )
            .padding(horizontal = 12.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(amountColor.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = amountColor,
            )
        }
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                record.id,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                "${record.date} ${record.time} · ${record.operator}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            if (record.note.isNotBlank()) {
                Text(
                    record.note,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    maxLines = 1,
                )
            }
        }
        Text(
            amountText,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = amountColor,
        )
    }
}

@Preview
@Composable
private fun StockHistoryCardPreview() {
    MaterialTheme {
        Box(Modifier.padding(16.dp)) {
            StockHistoryCard(
                filteredHistory = listOf(
                    StockRecord(
                        "1001",
                        "2024-05-20",
                        "14:30",
                        50,
                        "Restock",
                        "Admin",
                        StockMoveType.IN
                    ),
                    StockRecord(
                        "1002",
                        "2024-05-20",
                        "15:00",
                        2,
                        "Sale",
                        "Cashier",
                        StockMoveType.OUT
                    ),
                    StockRecord(
                        "1003",
                        "2024-05-21",
                        "09:15",
                        100,
                        "Initial Stock",
                        "System",
                        StockMoveType.IN
                    ),
                ),
                totalCount = 3,
                filter = HistoryFilter.ALL,
                onFilter = {},
                unit = "pcs"
            )
        }
    }
}
