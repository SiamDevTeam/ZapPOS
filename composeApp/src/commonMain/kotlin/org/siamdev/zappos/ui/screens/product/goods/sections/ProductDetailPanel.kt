/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.screens.product.goods.sections

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.siamdev.zappos.LocalSettingVM
import org.siamdev.zappos.data.source.MasterEvent
import org.siamdev.zappos.ui.components.common.AppDialog
import org.siamdev.zappos.ui.components.common.InfoToggleCard
import org.siamdev.zappos.ui.components.common.MaterialButton
import org.siamdev.zappos.ui.components.common.SegmentedTabBar
import org.siamdev.zappos.ui.components.common.TabItem
import org.siamdev.zappos.ui.components.menu.DefaultProductCategories
import org.siamdev.zappos.ui.screens.product.entry.EntryFormState
import org.siamdev.zappos.ui.screens.product.entry.EntryType
import org.siamdev.zappos.ui.screens.product.entry.PickMode
import org.siamdev.zappos.ui.screens.product.entry.loadFrom
import org.siamdev.zappos.ui.screens.product.goods.DetailTab
import org.siamdev.zappos.ui.screens.product.goods.sampleProducts
import org.siamdev.zappos.ui.screens.setting.SettingViewModel
import org.siamdev.zappos.utils.formatPrice
import kotlin.math.sin

private enum class SalesPeriod(
    val label: String,
    val days: Int,
) {
    D7("7d", 7),
    D30("30d", 30),
    D90("90d", 90),
}

private fun fakeSparkline(
    seed: Int,
    count: Int,
): List<Float> {
    val base = (seed * 7 % 40 + 30).toFloat()
    return List(count) { i ->
        val wave = sin((i + seed) * 0.8).toFloat() * 18f
        val trend = i * (2.5f + seed % 3)
        (base + wave + trend).coerceAtLeast(10f)
    }
}

private val detailTabs =
    listOf(
        TabItem("Product Detail", Icons.Default.Info),
        TabItem("Monitor & Stock", Icons.Default.Inventory),
    )

/** Tabbed detail view for a selected product. Hosts [ProductDetailTabContent] and [MonitorStockTabContent]. */
@Composable
internal fun ProductDetailPanel(
    event: MasterEvent,
    onEdit: () -> Unit,
    onDelete: (String) -> Unit = {},
    initialTab: DetailTab = DetailTab.PRODUCT_DETAIL,
) {
    var selectedTab by remember(event.id) { mutableStateOf(initialTab) }

    Column(modifier = Modifier.fillMaxSize()) {
        SegmentedTabBar(
            tabs = detailTabs,
            selectedIndex = selectedTab.ordinal,
            onTabSelect = { selectedTab = DetailTab.entries[it] },
            modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp, bottom = 8.dp),
        )

        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

        when (selectedTab) {
            DetailTab.PRODUCT_DETAIL -> {
                ProductDetailTabContent(
                    event = event,
                    onEdit = onEdit,
                    onDelete = { onDelete(event.id) },
                )
            }

            DetailTab.MONITOR_STOCK -> {
                MonitorStockTabContent(product = event)
            }
        }
    }
}

/** Scrollable detail view: header, sales chart, stock bar, item info, and status toggles. */
@Composable
private fun ProductDetailTabContent(
    event: MasterEvent,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
) {
    val catEntry = DefaultProductCategories.find { it.id == event.category }
    val categoryName = catEntry?.name ?: event.category
    val subName = catEntry?.subCategories?.find { it.id == event.subCategory }?.name
    val catIcon = catEntry?.icon
    val entryState = remember(event.id) { EntryFormState().also { it.loadFrom(event) } }
    val stockQty = event.stockQty
    val stockMax = event.stockMax
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        AppDialog(
            icon = Icons.Default.Delete,
            iconTint = MaterialTheme.colorScheme.error,
            title = "Delete Product",
            message = "Are you sure you want to delete \"${event.name}\"? This action cannot be undone.",
            confirmText = "Delete",
            dismissText = "Cancel",
            confirmButtonColor = MaterialTheme.colorScheme.error,
            onConfirm = {
                showDeleteDialog = false
                onDelete()
            },
            onDismiss = { showDeleteDialog = false },
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item {
                ProductHeader(
                    event = event,
                    categoryName = categoryName,
                    subName = subName,
                    catIcon = catIcon ?: Icons.Default.ShoppingBag,
                )
            }
            item { SalesChartCard(event = event) }
            if (stockQty != null && stockMax != null) {
                item {
                    StockCard(
                        stockQty = stockQty,
                        stockMax = stockMax,
                        unit = event.unit,
                    )
                }
            }
            item { ItemInfoCard(state = entryState) }
            if (entryState.optionGroups.isNotEmpty()) {
                item { OptionsInfoCard(state = entryState) }
            }
            item {
                InfoToggleCard(
                    icon = Icons.Default.Visibility,
                    label = "Available",
                    subtitle = "Shown on the menu right now",
                    checked = event.isAvailable,
                )
            }
            item {
                InfoToggleCard(
                    icon = Icons.Default.Star,
                    label = "Recommended",
                    subtitle = "Featured with a ★ on the menu",
                    checked = event.isRecommended,
                )
            }
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            MaterialButton(
                modifier = Modifier.weight(1f).height(48.dp),
                text = "Edit Product",
                iconStart = Icons.Default.Edit,
                onClick = onEdit,
            )
            IconButton(
                onClick = { showDeleteDialog = true },
                modifier =
                    Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.errorContainer),
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(20.dp),
                )
            }
        }
    }
}

/** Category icon + breadcrumb + product name. Pass [showMeta] to also display SKU and unit on the trailing edge. */
@Composable
internal fun ProductHeader(
    event: MasterEvent,
    categoryName: String,
    subName: String?,
    catIcon: ImageVector,
    showMeta: Boolean = false,
) {
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
                        .size(44.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    catIcon,
                    contentDescription = null,
                    modifier = Modifier.size(22.dp),
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
            Spacer(Modifier.width(12.dp))
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        categoryName,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    if (subName != null) {
                        Icon(
                            Icons.Default.ChevronRight,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Text(
                            subName,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
                Text(
                    event.name,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
        if (showMeta && event.sku.isNotBlank()) {
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    "SKU ${event.sku}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    "sold per ${event.unit}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

/** Card showing revenue total and a sparkline chart for the selected period (7 d / 30 d / 90 d). */
@Composable
private fun SalesChartCard(event: MasterEvent) {
    var period by remember { mutableStateOf(SalesPeriod.D7) }
    val points = remember(event.id, period) { fakeSparkline(event.id.hashCode(), period.days) }
    val primaryColor = MaterialTheme.colorScheme.primary

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
                verticalAlignment = Alignment.Top,
            ) {
                Column {
                    Text(
                        "SALES · LAST ${period.days} DAYS",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Spacer(Modifier.height(2.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            "฿${event.revenue7d.formatPrice()}",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                        Spacer(Modifier.width(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.AutoMirrored.Filled.TrendingUp,
                                contentDescription = null,
                                tint = Color(0xFF4CAF50),
                                modifier = Modifier.size(14.dp),
                            )
                            Text(
                                " 9%",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF4CAF50),
                                fontWeight = FontWeight.SemiBold,
                            )
                        }
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    SalesPeriod.entries.forEach { p ->
                        val active = p == period
                        Box(
                            modifier =
                                Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (active) primaryColor else Color.Transparent)
                                    .clickable { period = p }
                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                        ) {
                            Text(
                                p.label,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = if (active) FontWeight.Bold else FontWeight.Normal,
                                color = if (active) Color.Black else MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            Canvas(modifier = Modifier.fillMaxWidth().height(80.dp)) {
                drawSparkline(points, primaryColor)
            }
        }
    }
}

/** Draws a smooth cubic-bezier sparkline with a gradient fill onto the [DrawScope]. */
private fun DrawScope.drawSparkline(
    points: List<Float>,
    color: Color,
) {
    if (points.size < 2) return
    val minVal = points.min()
    val maxVal = points.max()
    val range = (maxVal - minVal).coerceAtLeast(1f)
    val w = size.width
    val h = size.height
    val step = w / (points.size - 1)

    fun px(i: Int) = i * step

    fun py(v: Float) = h - ((v - minVal) / range) * h * 0.85f - h * 0.05f

    val line = Path()
    val fill = Path()

    line.moveTo(px(0), py(points[0]))
    fill.moveTo(px(0), h)
    fill.lineTo(px(0), py(points[0]))

    for (i in 1 until points.size) {
        val cx = (px(i - 1) + px(i)) / 2f
        line.cubicTo(cx, py(points[i - 1]), cx, py(points[i]), px(i), py(points[i]))
        fill.cubicTo(cx, py(points[i - 1]), cx, py(points[i]), px(i), py(points[i]))
    }
    fill.lineTo(px(points.size - 1), h)
    fill.close()

    drawPath(
        fill,
        brush =
            Brush.verticalGradient(
                colors = listOf(color.copy(alpha = 0.28f), color.copy(alpha = 0.03f)),
                startY = 0f,
                endY = h,
            ),
    )
    drawPath(line, color = color, style = Stroke(width = 2.5f, cap = StrokeCap.Round))
}

/** Progress bar card showing current stock quantity vs. maximum capacity. */
@Composable
private fun StockCard(
    stockQty: Int,
    stockMax: Int,
    unit: String,
) {
    val progress = (stockQty.toFloat() / stockMax.toFloat()).coerceIn(0f, 1f)

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
                    "STOCK",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    "$stockQty / $stockMax $unit",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
            Spacer(Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.outlineVariant,
            )
        }
    }
}

/** Read-only master-data summary built from [EntryFormState]. Adapts to Goods / Service / Rental
 *  and skips any field that is empty, zero, or still at its default value. */
@Composable
private fun ItemInfoCard(state: EntryFormState) {
    val catEntry = DefaultProductCategories.find { it.id == state.category }
    val categoryDisplay = run {
        val cat = catEntry?.name ?: state.category.ifBlank { null } ?: return@run null
        val sub = catEntry?.subCategories?.find { it.id == state.subCategory }?.name
        if (sub != null) "$cat · $sub" else cat
    }
    val priceValue = state.price.toDoubleOrNull() ?: 0.0

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(0.dp),
        border = CardDefaults.outlinedCardBorder(),
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp).padding(10.dp)) {
            Text(
                "ITEM INFO",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(8.dp))

            val pairs = buildList<Pair<String, String>> {
                if (state.productId != null) add("Product ID" to state.productId!!)
                if (categoryDisplay != null) add("Category" to categoryDisplay)
                add("Price" to "฿ ${priceValue.formatPrice()} / ${state.unit}")
                if (state.openPrice && state.entryType == EntryType.GOODS) add("Pricing" to "Open / variable price")
                if (!state.chargeVat) add("VAT" to "Not charged")
                if (state.showCostPrice) {
                    val cost = state.costPrice.toDoubleOrNull() ?: 0.0
                    if (cost > 0) add("Cost price" to "฿ ${cost.formatPrice()} / ${state.unit}")
                }

                when (state.entryType) {
                    EntryType.GOODS -> {
                        if (state.trackStock) {
                            val qty = state.openingStock.toIntOrNull() ?: 0
                            val cap = state.maxCapacity.toIntOrNull() ?: 0
                            if (qty > 0 || cap > 0) add("Stock" to "$qty / $cap ${state.unit}")
                            val alert = state.lowStockAlert.toIntOrNull() ?: 0
                            if (alert > 0) add("Low stock alert" to "$alert ${state.unit}")
                        }
                        if (state.supplier.isNotBlank()) add("Supplier" to state.supplier)
                    }

                    EntryType.SERVICE -> {
                        val chargedByLabel = listOf("person", "session", "hour")
                            .getOrElse(state.chargedBy) { "hour" }
                        add("Charged per" to chargedByLabel)
                        val cap = state.serviceCapacity.toIntOrNull() ?: 0
                        if (cap > 0) add("Capacity" to "$cap people")
                        val dur = state.serviceDuration.toIntOrNull() ?: 0
                        if (dur > 0) add("Duration" to "$dur min / session")
                        add("Hours" to "${state.serviceOpens} – ${state.serviceCloses}")
                        if (state.activeDays.isNotEmpty()) {
                            val dayLabels = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
                            add("Active days" to state.activeDays.sorted()
                                .joinToString(", ") { dayLabels.getOrElse(it) { "?" } })
                        }
                        if (state.instructor.isNotBlank()) add("Instructor" to state.instructor)
                        if (state.serviceRequiresBooking) add("Booking" to "Required")
                    }

                    EntryType.RENTAL -> {
                        val units = state.rentalUnitsCount.toIntOrNull() ?: 0
                        if (units > 0) add("Units available" to "$units")
                        val dur = state.bookingDuration.toIntOrNull() ?: 0
                        if (dur > 0) add("Slot duration" to "$dur min")
                        val minBook = state.minBooking.toIntOrNull() ?: 0
                        if (minBook > 1) add("Min. booking" to "$minBook slot(s)")
                        add("Hours" to "${state.rentalOpens} – ${state.rentalCloses}")
                        val buf = state.rentalBuffer.toIntOrNull() ?: 0
                        if (buf > 0) add("Buffer" to "$buf min")
                        val dep = state.depositAmount.toDoubleOrNull() ?: 0.0
                        if (dep > 0) add("Deposit" to "฿ ${dep.formatPrice()}")
                        if (state.rentalRequiresBooking) add("Booking" to "Required")
                    }
                }
            }

            pairs.forEachIndexed { i, (label, value) ->
                if (i > 0) HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = MaterialTheme.colorScheme.outlineVariant,
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
                    Text(value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
                }
            }

            if (state.description.isNotBlank()) {
                if (pairs.isNotEmpty()) HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = MaterialTheme.colorScheme.outlineVariant,
                )
                Text("Description", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
                Spacer(Modifier.height(4.dp))
                Text(
                    state.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

        }
    }
}

/** Dedicated card for Options & Add-ons groups. Each group shows its name, pick mode, and
 *  a row per item with the price modifier. Shown only when the product has option groups. */
@Composable
private fun OptionsInfoCard(state: EntryFormState) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(0.dp),
        border = CardDefaults.outlinedCardBorder(),
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp).padding(10.dp)) {
            Text(
                "OPTIONS & ADD-ONS",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            state.optionGroups.forEach { group ->
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 10.dp),
                    color = MaterialTheme.colorScheme.outlineVariant,
                )

                // Group header: name | badges
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        group.name,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        if (group.pickMode == PickMode.MANY) {
                            GroupBadge("multi-select", MaterialTheme.colorScheme.primary)
                        }
                        GroupBadge(
                            label = if (group.required) "required" else "optional",
                            color = if (group.required) MaterialTheme.colorScheme.error
                                    else MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }

                Spacer(Modifier.height(8.dp))

                // Item rows
                group.items.forEachIndexed { ii, item ->
                    if (ii > 0) Spacer(Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            HorizontalDivider(
                                modifier = Modifier.width(12.dp),
                                color = MaterialTheme.colorScheme.outlineVariant,
                            )
                            Text(
                                item.name,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                        }
                        val (modLabel, modColor) = when {
                            item.priceModifier > 0 ->
                                "+฿${item.priceModifier}" to MaterialTheme.colorScheme.primary
                            item.priceModifier < 0 ->
                                "−฿${-item.priceModifier}" to MaterialTheme.colorScheme.error
                            else ->
                                "included" to MaterialTheme.colorScheme.onSurfaceVariant
                        }
                        Text(
                            modLabel,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Medium,
                            color = modColor,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun GroupBadge(label: String, color: Color) {
    Text(
        label,
        style = MaterialTheme.typography.labelSmall,
        color = color,
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(color.copy(alpha = 0.08f))
            .padding(horizontal = 6.dp, vertical = 2.dp),
    )
}

@Preview(showBackground = true, widthDp = 411, heightDp = 700, name = "ProductDetailPanel · Product Detail tab")
@Composable
private fun ProductDetailPanelPreview() {
    CompositionLocalProvider(LocalSettingVM provides SettingViewModel()) {
        ProductDetailPanel(
            event = sampleProducts().first(),
            onEdit = {},
        )
    }
}

@Preview(showBackground = true, widthDp = 411, heightDp = 700, name = "ProductDetailPanel · Monitor & Stock tab")
@Composable
private fun ProductDetailPanelMonitorPreview() {
    CompositionLocalProvider(LocalSettingVM provides SettingViewModel()) {
        ProductDetailPanel(
            event = sampleProducts().first(),
            onEdit = {},
            initialTab = DetailTab.MONITOR_STOCK,
        )
    }
}

@Preview(showBackground = true, widthDp = 411, heightDp = 700, name = "ProductDetailPanel · No stock product")
@Composable
private fun ProductDetailPanelNoStockPreview() {
    CompositionLocalProvider(LocalSettingVM provides SettingViewModel()) {
        ProductDetailPanel(
            event = sampleProducts().first { it.stockQty == null },
            onEdit = {},
        )
    }
}

@Preview(showBackground = true, widthDp = 411, name = "ItemInfoCard · Goods (fully populated)")
@Composable
private fun ItemInfoCardGoodsFullPreview() {
    val state = EntryFormState().apply {
            productId = "PRD20260001"
            entryType = EntryType.GOODS
            name = "Green Tea Latte"
            category = "beverages"
            subCategory = "tea_coffee"
            price = "85"
            unit = "cup"
            showCostPrice = true
            costPrice = "35"
            trackStock = true
            openingStock = "50"
            maxCapacity = "200"
            lowStockAlert = "15"
            supplier = "Thai Matcha Co., Ltd."
            description = "Smooth and creamy matcha latte made with premium Japanese matcha and oat milk."
        }
    MaterialTheme { ItemInfoCard(state = state) }
}
