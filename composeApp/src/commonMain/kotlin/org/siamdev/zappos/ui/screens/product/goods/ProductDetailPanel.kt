/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.screens.product.goods

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
import org.siamdev.zappos.ui.components.common.AppDialog
import org.siamdev.zappos.ui.components.common.InfoToggleCard
import org.siamdev.zappos.ui.components.common.MaterialButton
import org.siamdev.zappos.ui.components.common.SegmentedTabBar
import org.siamdev.zappos.ui.components.common.TabItem
import org.siamdev.zappos.ui.components.menu.DefaultProductCategories
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
    product: Product,
    onEdit: () -> Unit,
    onDelete: (String) -> Unit = {},
    initialTab: DetailTab = DetailTab.PRODUCT_DETAIL,
) {
    var selectedTab by remember(product.id) { mutableStateOf(initialTab) }

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
                    product = product,
                    onEdit = onEdit,
                    onDelete = { onDelete(product.id) },
                )
            }

            DetailTab.MONITOR_STOCK -> {
                MonitorStockTabContent(product = product)
            }
        }
    }
}

/** Scrollable detail view: header, sales chart, stock bar, item info, and status toggles. */
@Composable
private fun ProductDetailTabContent(
    product: Product,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
) {
    val catEntry = DefaultProductCategories.find { it.id == product.category }
    val categoryName = catEntry?.name ?: product.category
    val subName = catEntry?.subCategories?.find { it.id == product.subCategory }?.name
    val catIcon = catEntry?.icon
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        AppDialog(
            icon = Icons.Default.Delete,
            iconTint = MaterialTheme.colorScheme.error,
            title = "Delete Product",
            message = "Are you sure you want to delete \"${product.name}\"? This action cannot be undone.",
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
                    product = product,
                    categoryName = categoryName,
                    subName = subName,
                    catIcon = catIcon ?: Icons.Default.ShoppingBag,
                )
            }
            item { SalesChartCard(product = product) }
            if (product.stockQty != null && product.stockMax != null) {
                item {
                    StockCard(
                        stockQty = product.stockQty,
                        stockMax = product.stockMax,
                        unit = product.unit,
                    )
                }
            }
            item { ItemInfoCard(product = product) }
            item {
                InfoToggleCard(
                    icon = Icons.Default.Visibility,
                    label = "Available",
                    subtitle = "Shown on the menu right now",
                    checked = product.isAvailable,
                )
            }
            item {
                InfoToggleCard(
                    icon = Icons.Default.Star,
                    label = "Recommended",
                    subtitle = "Featured with a ★ on the menu",
                    checked = product.isRecommended,
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
    product: Product,
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
                    product.name,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
        if (showMeta && product.sku.isNotBlank()) {
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

/** Card showing revenue total and a sparkline chart for the selected period (7 d / 30 d / 90 d). */
@Composable
private fun SalesChartCard(product: Product) {
    var period by remember { mutableStateOf(SalesPeriod.D7) }
    val points = remember(product.id, period) { fakeSparkline(product.id.hashCode(), period.days) }
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
                            "฿${product.revenue7d.formatPrice()}",
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

/** Key-value card listing price, unit, SKU, stock count, and 7-day revenue. */
@Composable
private fun ItemInfoCard(product: Product) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(0.dp),
        border = CardDefaults.outlinedCardBorder(),
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
            Text(
                "ITEM INFO",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(8.dp))

            val rows =
                buildList {
                    add("Price" to "฿ ${product.price.formatPrice()}")
                    add("Unit" to product.unit)
                    if (product.sku.isNotBlank()) add("SKU" to product.sku)
                    if (product.stockQty != null) add("In stock" to "${product.stockQty} ${product.unit}")
                    if (product.soldThisWeek > 0) add("Sold this week" to "${product.soldThisWeek}")
                    if (product.revenue7d > 0) add("Revenue (7d)" to "฿ ${product.revenue7d.formatPrice()}")
                }

            rows.forEachIndexed { i, (label, value) ->
                if (i > 0) {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = MaterialTheme.colorScheme.outlineVariant,
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        label,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Text(
                        value,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 411, heightDp = 700, name = "ProductDetailPanel · Product Detail tab")
@Composable
private fun ProductDetailPanelPreview() {
    CompositionLocalProvider(LocalSettingVM provides SettingViewModel()) {
        ProductDetailPanel(
            product = sampleProducts().first(),
            onEdit = {},
        )
    }
}

@Preview(showBackground = true, widthDp = 411, heightDp = 700, name = "ProductDetailPanel · Monitor & Stock tab")
@Composable
private fun ProductDetailPanelMonitorPreview() {
    CompositionLocalProvider(LocalSettingVM provides SettingViewModel()) {
        ProductDetailPanel(
            product = sampleProducts().first(),
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
            product = sampleProducts().first { it.stockQty == null },
            onEdit = {},
        )
    }
}
