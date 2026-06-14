/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.screens.product.goods.sections

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.siamdev.zappos.LocalSettingVM
import org.siamdev.zappos.data.source.MasterEvent
import org.siamdev.zappos.ui.components.menu.DefaultProductCategories
import org.siamdev.zappos.ui.components.product.ProductHeader
import org.siamdev.zappos.ui.components.stock.*
import org.siamdev.zappos.ui.screens.product.goods.sampleProducts
import org.siamdev.zappos.ui.screens.setting.SettingViewModel
import kotlin.math.abs

private fun fakeStockHistory(productId: String): List<StockRecord> {
    val seed = abs(productId.hashCode()) % 100
    return listOf(
        StockRecord(
            "SK2026053100001",
            "31-05-2026",
            "13:37:05",
            18 + seed % 5,
            "Sold / order",
            "Staff A",
            StockMoveType.OUT
        ),
        StockRecord(
            "SK2026053100002",
            "31-05-2026",
            "08:09:12",
            70 + seed % 10,
            "Regular restock",
            "Staff A",
            StockMoveType.IN
        ),
        StockRecord(
            "SK2026053000045",
            "30-05-2026",
            "20:11:45",
            4 + seed % 3,
            "Waste / spoilage",
            "Staff B",
            StockMoveType.OUT
        ),
        StockRecord(
            "SK2026052800102",
            "28-05-2026",
            "08:00:22",
            70 + seed % 15,
            "Regular restock",
            "Staff A",
            StockMoveType.IN
        ),
        StockRecord(
            "SK2026052800156",
            "28-05-2026",
            "13:48:10",
            33 + seed % 10,
            "Sold / order",
            "Staff B",
            StockMoveType.OUT
        ),
        StockRecord(
            "SK2026052700088",
            "27-05-2026",
            "17:05:33",
            40 + seed % 20,
            "Emergency order",
            "Staff B",
            StockMoveType.IN
        ),
        StockRecord(
            "SK2026051400012",
            "14-05-2026",
            "11:40:00",
            40 + seed % 25,
            "Weekly delivery",
            "Admin",
            StockMoveType.IN
        ),
    )
}

@Composable
internal fun MonitorStockTabContent(product: MasterEvent) {
    val catEntry =
        remember(product.category) { DefaultProductCategories.find { it.id == product.category } }
    val categoryName = catEntry?.name ?: product.category
    val subName = catEntry?.subCategories?.find { it.id == product.subCategory }?.name
    val catIcon = catEntry?.icon ?: Icons.Default.ShoppingBag

    val history = remember(product.id) { fakeStockHistory(product.id) }
    var historyFilter by remember { mutableStateOf(HistoryFilter.ALL) }
    val filteredHistory = remember(history, historyFilter) {
        when (historyFilter) {
            HistoryFilter.ALL -> history
            HistoryFilter.STOCK_IN -> history.filter { it.type == StockMoveType.IN }
            HistoryFilter.STOCK_OUT -> history.filter { it.type == StockMoveType.OUT }
        }
    }

    BoxWithConstraints(Modifier.fillMaxSize()) {
        val isWide = maxWidth >= 580.dp
        if (isWide) {
            Row(Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    ProductHeader(
                        event = product,
                        categoryName = categoryName,
                        subName = subName,
                        catIcon = catIcon
                    )
                    StockOverviewCard(product, history)
                    AdjustStockCard(product)
                }
                VerticalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                StockHistoryCard(
                    filteredHistory = filteredHistory,
                    totalCount = history.size,
                    filter = historyFilter,
                    onFilter = { historyFilter = it },
                    unit = product.unit,
                    compact = false,
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                item {
                    ProductHeader(
                        event = product,
                        categoryName = categoryName,
                        subName = subName,
                        catIcon = catIcon
                    )
                }
                item { StockOverviewCard(product, history) }
                item { AdjustStockCard(product) }
                item {
                    StockHistoryCard(
                        filteredHistory = filteredHistory,
                        totalCount = history.size,
                        filter = historyFilter,
                        onFilter = { historyFilter = it },
                        unit = product.unit,
                        compact = true,
                    )
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    widthDp = 411,
    heightDp = 891,
    name = "MonitorStockPanel · In stock"
)
@Composable
private fun MonitorStockInStockPreview() {
    CompositionLocalProvider(LocalSettingVM provides SettingViewModel()) {
        MonitorStockTabContent(product = sampleProducts().first())
    }
}

@Preview(
    showBackground = true,
    widthDp = 411,
    heightDp = 891,
    name = "MonitorStockPanel · Out of stock"
)
@Composable
private fun MonitorStockOutOfStockPreview() {
    CompositionLocalProvider(LocalSettingVM provides SettingViewModel()) {
        MonitorStockTabContent(product = sampleProducts().first { it.stockQty == 0 })
    }
}

@Preview(
    showBackground = true,
    widthDp = 411,
    heightDp = 891,
    name = "MonitorStockPanel · No stock tracking"
)
@Composable
private fun MonitorStockNoTrackingPreview() {
    CompositionLocalProvider(LocalSettingVM provides SettingViewModel()) {
        MonitorStockTabContent(product = sampleProducts().first { it.stockQty == null })
    }
}

@Preview(
    showBackground = true,
    widthDp = 900,
    heightDp = 600,
    name = "MonitorStockPanel · Wide layout"
)
@Composable
private fun MonitorStockWidePreview() {
    CompositionLocalProvider(LocalSettingVM provides SettingViewModel()) {
        MonitorStockTabContent(product = sampleProducts().first())
    }
}