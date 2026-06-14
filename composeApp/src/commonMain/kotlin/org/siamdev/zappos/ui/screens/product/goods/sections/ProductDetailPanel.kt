/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.screens.product.goods.sections

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.siamdev.zappos.LocalSettingVM
import org.siamdev.zappos.data.source.MasterEvent
import org.siamdev.zappos.ui.components.common.InfoToggleCard
import org.siamdev.zappos.ui.components.common.MaterialButton
import org.siamdev.zappos.ui.components.common.SegmentedTabBar
import org.siamdev.zappos.ui.components.common.TabItem
import org.siamdev.zappos.ui.components.dialog.AppDialog
import org.siamdev.zappos.ui.components.menu.DefaultProductCategories
import org.siamdev.zappos.ui.components.product.*
import org.siamdev.zappos.ui.screens.product.entry.EntryFormState
import org.siamdev.zappos.ui.screens.product.entry.loadFrom
import org.siamdev.zappos.ui.screens.product.goods.DetailTab
import org.siamdev.zappos.ui.screens.product.goods.sampleProducts
import org.siamdev.zappos.ui.screens.setting.SettingViewModel

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

            DetailTab.MONITOR_STOCK -> MonitorStockTabContent(product = event)
        }
    }
}

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
                    name = event.name,
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
                item { OptionsInfoCard(optionGroups = entryState.optionGroups) }
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
            modifier = Modifier
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
                modifier = Modifier
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

@Preview(
    showBackground = true,
    widthDp = 411,
    heightDp = 700,
    name = "ProductDetailPanel · Product Detail tab"
)
@Composable
private fun ProductDetailPanelPreview() {
    CompositionLocalProvider(LocalSettingVM provides SettingViewModel()) {
        ProductDetailPanel(
            event = sampleProducts().first(),
            onEdit = {},
        )
    }
}

@Preview(
    showBackground = true,
    widthDp = 411,
    heightDp = 700,
    name = "ProductDetailPanel · Monitor & Stock tab"
)
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

@Preview(
    showBackground = true,
    widthDp = 411,
    heightDp = 700,
    name = "ProductDetailPanel · No stock product"
)
@Composable
private fun ProductDetailPanelNoStockPreview() {
    CompositionLocalProvider(LocalSettingVM provides SettingViewModel()) {
        ProductDetailPanel(
            event = sampleProducts().first { it.stockQty == null },
            onEdit = {},
        )
    }
}