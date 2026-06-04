/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.screens.product.goods

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.siamdev.zappos.LocalSettingVM
import org.siamdev.zappos.ui.components.common.MaterialButton
import org.siamdev.zappos.ui.components.common.SegmentedTabBar
import org.siamdev.zappos.ui.components.common.TabItem
import org.siamdev.zappos.ui.components.menu.DefaultProductCategories
import org.siamdev.zappos.ui.screens.setting.SettingViewModel
import kotlin.math.abs

private enum class StockMoveType { IN, OUT }

private enum class HistoryFilter { ALL, STOCK_IN, STOCK_OUT }

private data class StockRecord(
    val id: String,
    val date: String,
    val time: String,
    val qty: Int,
    val note: String,
    val operator: String,
    val type: StockMoveType,
)

private fun fakeStockHistory(productId: String): List<StockRecord> {
    val seed = abs(productId.hashCode()) % 100
    return listOf(
        StockRecord("SK2026053100001", "31-05-2026", "13:37:05", 18 + seed % 5, "Sold / order", "Staff A", StockMoveType.OUT),
        StockRecord("SK2026053100002", "31-05-2026", "08:09:12", 70 + seed % 10, "Regular restock", "Staff A", StockMoveType.IN),
        StockRecord("SK2026053000045", "30-05-2026", "20:11:45", 4 + seed % 3, "Waste / spoilage", "Staff B", StockMoveType.OUT),
        StockRecord("SK2026052800102", "28-05-2026", "08:00:22", 70 + seed % 15, "Regular restock", "Staff A", StockMoveType.IN),
        StockRecord("SK2026052800156", "28-05-2026", "13:48:10", 33 + seed % 10, "Sold / order", "Staff B", StockMoveType.OUT),
        StockRecord("SK2026052700088", "27-05-2026", "17:05:33", 40 + seed % 20, "Emergency order", "Staff B", StockMoveType.IN),
        StockRecord("SK2026051400012", "14-05-2026", "11:40:00", 40 + seed % 25, "Weekly delivery", "Admin", StockMoveType.IN),
    )
}

private fun todayMovements(history: List<StockRecord>): Pair<Int, Int> {
    val ins = history.filter { it.type == StockMoveType.IN && it.date == "31-05-2026" }.sumOf { it.qty }
    val outs = history.filter { it.type == StockMoveType.OUT && it.date == "31-05-2026" }.sumOf { it.qty }
    return ins to outs
}

private val adjustStockTabs =
    listOf(
        TabItem("Stock In", Icons.Default.MoveToInbox),
        TabItem("Stock Out", Icons.Default.ShoppingCartCheckout),
    )

@Composable
internal fun MonitorStockTabContent(product: Product) {
    val history = remember(product.id) { fakeStockHistory(product.id) }
    var historyFilter by remember { mutableStateOf(HistoryFilter.ALL) }
    val filteredHistory =
        remember(history, historyFilter) {
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
                    modifier =
                        Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    MonitorProductHeader(product)
                    StockOverviewCard(product, history)
                    AdjustStockSection(product)
                }
                VerticalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                Column(Modifier.weight(1f).fillMaxHeight()) {
                    StockHistoryHeader(
                        filter = historyFilter,
                        onFilter = { historyFilter = it },
                        filteredCount = filteredHistory.size,
                        totalCount = history.size,
                        modifier = Modifier.padding(horizontal = 16.dp).padding(top = 16.dp, bottom = 8.dp),
                    )
                    LazyColumn(
                        modifier =
                            Modifier
                                .weight(1f)
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.surface),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                        verticalArrangement = Arrangement.spacedBy(0.dp),
                    ) {
                        itemsIndexed(filteredHistory, key = { _, it -> "${it.id}-${it.date}-${it.time}" }) { index, record ->
                            StockRecordRow(record = record, unit = product.unit, isEven = index % 2 == 1)
                        }
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        elevation = CardDefaults.cardElevation(0.dp),
                        border = CardDefaults.outlinedCardBorder(),
                    ) {
                        Box(Modifier.padding(16.dp)) { MonitorProductHeader(product) }
                    }
                }
                item { StockOverviewCard(product, history) }
                item { AdjustStockSection(product) }
                item {
                    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        StockHistoryHeader(
                            filter = historyFilter,
                            onFilter = { historyFilter = it },
                            filteredCount = filteredHistory.size,
                            totalCount = history.size,
                        )
                        Column(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .heightIn(max = 320.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(12.dp))
                                    .background(MaterialTheme.colorScheme.surface)
                                    .verticalScroll(rememberScrollState()),
                        ) {
                            filteredHistory.forEachIndexed { index, record ->
                                StockRecordRow(record = record, unit = product.unit, isEven = index % 2 == 1)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MonitorProductHeader(product: Product) {
    val catEntry = DefaultProductCategories.find { it.id == product.category }
    val categoryName = catEntry?.name ?: product.category
    val subName = catEntry?.subCategories?.find { it.id == product.subCategory }?.name
    val catIcon = catEntry?.icon ?: Icons.Default.ShoppingBag
    val breadcrumb = if (subName != null) "$categoryName · $subName" else categoryName

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier =
                    Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    catIcon,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
            Spacer(Modifier.width(12.dp))
            Column {
                Text(
                    breadcrumb,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    product.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
        if (product.sku.isNotBlank()) {
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    "SKU ${product.sku}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    "sold per ${product.unit}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun StockOverviewCard(
    product: Product,
    history: List<StockRecord>,
) {
    val hasStock = product.stockQty != null && product.stockMax != null
    val isLow = hasStock && product.stockQty <= (product.stockMax * 0.20f).toInt()
    val isOut = hasStock && product.stockQty == 0
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
                val (badgeColor, badgeLabel) =
                    when {
                        isOut -> Color(0xFFF44336) to "Out of Stock"
                        isLow -> Color(0xFFFF9800) to "Low Stock"
                        else -> Color(0xFF4CAF50) to "In Stock"
                    }
                Row(
                    modifier =
                        Modifier
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

            if (hasStock) {
                val progress = (product.stockQty.toFloat() / product.stockMax.toFloat()).coerceIn(0f, 1f)
                val barColor =
                    when {
                        isOut -> Color(0xFFF44336)
                        isLow -> Color(0xFFFF9800)
                        else -> MaterialTheme.colorScheme.primary
                    }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Column {
                        Text(
                            product.stockQty.toString(),
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                        Text(
                            "current / ${product.stockMax} ${product.unit}",
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
                                color = Color(0xFF4CAF50),
                            )
                        }
                        if (todayIn > 0 && todayOut > 0) {
                            Text(
                                "·",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                        if (todayOut > 0) {
                            Text(
                                "-$todayOut out",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFFF44336),
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

@Composable
private fun AdjustStockSection(product: Product) {
    var mode by remember { mutableStateOf(0) }
    var qty by remember { mutableStateOf(0) }
    var reason by remember { mutableStateOf("") }

    var isEditing by remember { mutableStateOf(false) }
    var editText by remember { mutableStateOf(TextFieldValue("")) }
    val focusRequester = remember { FocusRequester() }

    fun confirmEdit() {
        qty = editText.text.toIntOrNull() ?: qty
        isEditing = false
    }

    fun adjustQty(delta: Int) {
        if (isEditing) {
            val current = editText.text.toIntOrNull() ?: 0
            val next = (current + delta).coerceAtLeast(0)
            val s = next.toString()
            editText = TextFieldValue(s, selection = TextRange(s.length))
        } else {
            qty = (qty + delta).coerceAtLeast(0)
        }
    }

    val modeLabel = if (mode == 0) "Stock In" else "Stock Out"
    val newBalance =
        product.stockQty?.let {
            if (mode == 0) it + qty else (it - qty).coerceAtLeast(0)
        }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(0.dp),
        border = CardDefaults.outlinedCardBorder(),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                "Adjust stock",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )

            SegmentedTabBar(
                tabs = adjustStockTabs,
                selectedIndex = mode,
                onTabSelect = { mode = it },
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier =
                        Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFFF44336))
                            .clickable { adjustQty(-1) },
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        Icons.Default.Remove,
                        contentDescription = "Decrease",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp),
                    )
                }
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    if (isEditing) {
                        BasicTextField(
                            value = editText,
                            onValueChange = { new ->
                                var filtered = new.text.filter { it.isDigit() }.take(5)
                                if (filtered.length > 1 && filtered.startsWith("0")) {
                                    filtered = filtered.dropWhile { it == '0' }.ifEmpty { "0" }
                                }
                                editText =
                                    new.copy(
                                        text = filtered,
                                        selection = TextRange(minOf(new.selection.start, filtered.length)),
                                    )
                            },
                            textStyle =
                                MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    textAlign = TextAlign.Center,
                                ),
                            singleLine = true,
                            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                            keyboardOptions =
                                KeyboardOptions(
                                    keyboardType = KeyboardType.Number,
                                    imeAction = ImeAction.Done,
                                ),
                            keyboardActions = KeyboardActions(onDone = { confirmEdit() }),
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp))
                                    .padding(vertical = 4.dp)
                                    .focusRequester(focusRequester)
                                    .onKeyEvent { event ->
                                        when (event.key) {
                                            Key.Enter if event.type == KeyEventType.KeyDown -> {
                                                confirmEdit()
                                                true
                                            }

                                            Key.Escape if event.type == KeyEventType.KeyDown -> {
                                                isEditing = false
                                                true
                                            }

                                            else -> {
                                                false
                                            }
                                        }
                                    },
                        )
                        LaunchedEffect(Unit) { focusRequester.requestFocus() }
                    } else {
                        Text(
                            qty.toString(),
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier =
                                Modifier
                                    .clickable {
                                        editText = TextFieldValue(qty.toString(), selection = TextRange(qty.toString().length))
                                        isEditing = true
                                    }.padding(vertical = 4.dp),
                        )
                    }
                }
                Box(
                    modifier =
                        Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFF4CAF50))
                            .clickable { adjustQty(1) },
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Increase",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp),
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                listOf(1, 5, 10, 25).forEach { preset ->
                    OutlinedButton(
                        onClick = { adjustQty(preset) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 10.dp),
                    ) {
                        Text(
                            "+$preset",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Medium,
                        )
                    }
                }
            }

            Text(
                "REASON",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            OutlinedTextField(
                value = reason,
                onValueChange = { reason = it },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyMedium,
            )

            if (newBalance != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        "New balance",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(
                        "$newBalance ${product.unit}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }

            MaterialButton(
                modifier = Modifier.fillMaxWidth().height(48.dp),
                text = "Confirm $modeLabel",
                iconStart = Icons.Default.Check,
                onClick = {},
            )
        }
    }
}

@Composable
private fun StockHistoryHeader(
    filter: HistoryFilter,
    onFilter: (HistoryFilter) -> Unit,
    filteredCount: Int = 0,
    totalCount: Int = 0,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
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
                    "$filteredCount/$totalCount",
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
                    modifier =
                        Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(
                                if (selected) {
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                                } else {
                                    Color.Transparent
                                },
                            ).border(
                                1.dp,
                                if (selected) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.outlineVariant
                                },
                                RoundedCornerShape(6.dp),
                            ).clickable { onFilter(f) }
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                ) {
                    Text(
                        label,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
                        color =
                            if (selected) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            },
                    )
                }
            }
        }
    }
}

@Composable
private fun StockRecordRow(
    record: StockRecord,
    unit: String,
    isEven: Boolean = false,
) {
    val isIn = record.type == StockMoveType.IN
    val amountColor = if (isIn) Color(0xFF4CAF50) else Color(0xFFF44336)
    val amountText = if (isIn) "+${record.qty} $unit" else "-${record.qty} $unit"
    val icon = if (isIn) Icons.Default.MoveToInbox else Icons.Default.ShoppingCartCheckout
    val iconColor = if (isIn) Color(0xFF4CAF50) else Color(0xFFF44336)

    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(
                    if (isEven) {
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.04f)
                    } else {
                        Color.Transparent
                    },
                ).padding(horizontal = 12.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier =
                Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(iconColor.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = iconColor,
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

@Preview(showBackground = true, widthDp = 411, heightDp = 891, name = "MonitorStockPanel · In stock")
@Composable
private fun MonitorStockInStockPreview() {
    CompositionLocalProvider(LocalSettingVM provides SettingViewModel()) {
        MonitorStockTabContent(product = sampleProducts().first())
    }
}

@Preview(showBackground = true, widthDp = 411, heightDp = 891, name = "MonitorStockPanel · Out of stock")
@Composable
private fun MonitorStockOutOfStockPreview() {
    CompositionLocalProvider(LocalSettingVM provides SettingViewModel()) {
        MonitorStockTabContent(product = sampleProducts().first { it.stockQty == 0 })
    }
}

@Preview(showBackground = true, widthDp = 411, heightDp = 891, name = "MonitorStockPanel · No stock tracking")
@Composable
private fun MonitorStockNoTrackingPreview() {
    CompositionLocalProvider(LocalSettingVM provides SettingViewModel()) {
        MonitorStockTabContent(product = sampleProducts().first { it.stockQty == null })
    }
}

@Preview(showBackground = true, widthDp = 900, heightDp = 600, name = "MonitorStockPanel · Wide layout")
@Composable
private fun MonitorStockWidePreview() {
    CompositionLocalProvider(LocalSettingVM provides SettingViewModel()) {
        MonitorStockTabContent(product = sampleProducts().first())
    }
}
