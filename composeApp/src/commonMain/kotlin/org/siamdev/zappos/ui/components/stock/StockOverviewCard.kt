/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.components.stock

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.siamdev.zappos.data.source.MasterEvent
import org.siamdev.zappos.ui.screens.product.goods.sampleEvents

private val colorRed = Color(0xFFF44336)
private val colorOrange = Color(0xFFFF9800)
private val colorGreen = Color(0xFF4CAF50)

private fun todayMovements(history: List<StockRecord>): Pair<Int, Int> {
    val ins =
        history.filter { it.type == StockMoveType.IN && it.date == "31-05-2026" }.sumOf { it.qty }
    val outs =
        history.filter { it.type == StockMoveType.OUT && it.date == "31-05-2026" }.sumOf { it.qty }
    return ins to outs
}

@Composable
fun StockOverviewCard(product: MasterEvent, history: List<StockRecord>) {
    val stockQty = product.stockQty
    val stockMax = product.stockMax
    val hasStock = stockQty != null && stockMax != null
    val isLow = stockQty != null && stockMax != null && stockQty <= (stockMax * 0.20f).toInt()
    val isOut = stockQty == 0
    val (todayIn, todayOut) = remember(history) { todayMovements(history) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(0.dp),
        border = CardDefaults.outlinedCardBorder(),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    "STOCK OVERVIEW",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                val (badgeColor, badgeLabel) = when {
                    isOut -> colorRed to "Out of Stock"
                    isLow -> colorOrange to "Low Stock"
                    else -> colorGreen to "In Stock"
                }
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(badgeColor.copy(alpha = 0.12f))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Box(Modifier.size(6.dp).clip(RoundedCornerShape(50)).background(badgeColor))
                    Text(
                        badgeLabel,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = badgeColor,
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            if (hasStock && stockQty != null && stockMax != null) {
                val progress = (stockQty.toFloat() / stockMax.toFloat()).coerceIn(0f, 1f)
                val barColor = when {
                    isOut -> colorRed
                    isLow -> colorOrange
                    else -> MaterialTheme.colorScheme.primary
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Column {
                        Text(
                            stockQty.toString(),
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                        Text(
                            "current / $stockMax ${product.unit}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            "${(progress * 100).toInt()}%",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = barColor,
                        )
                        Text(
                            "capacity",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
                Spacer(Modifier.height(10.dp))
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth().height(10.dp).clip(RoundedCornerShape(5.dp)),
                    color = barColor,
                    trackColor = MaterialTheme.colorScheme.outlineVariant,
                )
                if (todayIn > 0 || todayOut > 0) {
                    Spacer(Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            "Today",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        if (todayIn > 0) {
                            Text(
                                "+$todayIn in",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.SemiBold,
                                color = colorGreen,
                            )
                        }
                        if (todayIn > 0 && todayOut > 0) {
                            Text(
                                "·",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        if (todayOut > 0) {
                            Text(
                                "-$todayOut out",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.SemiBold,
                                color = colorRed,
                            )
                        }
                    }
                }
            } else {
                Text(
                    "No stock tracking for this product",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Preview
@Composable
private fun StockOverviewCardPreview() {
    MaterialTheme {
        Box(Modifier.padding(16.dp)) {
            StockOverviewCard(
                product = sampleEvents().first(),
                history = listOf(
                    StockRecord(
                        "1",
                        "31-05-2026",
                        "10:00",
                        20,
                        "Refill",
                        "Admin",
                        StockMoveType.IN
                    ),
                    StockRecord("2", "31-05-2026", "11:00", 5, "Sale", "Cashier", StockMoveType.OUT)
                )
            )
        }
    }
}
