/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.screens.product.entry

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.siamdev.zappos.ui.components.common.SegmentedTabBar
import org.siamdev.zappos.ui.components.common.WorkspaceHeader
import org.siamdev.zappos.ui.screens.product.goods.sampleProducts

@Composable
fun MasterEntryScreen(
    productId: String? = null,
    onNavigateBack: () -> Unit = {},
    onOpenDrawer: () -> Unit = {},
    onSave: () -> Unit = {},
    showBackButton: Boolean = false,
) {
    val state = rememberEntryFormState()
    val isEditMode = productId != null

    if (isEditMode) {
        val product = remember(productId) { sampleProducts().find { it.id == productId } }
        LaunchedEffect(productId) { product?.let { state.loadFrom(it) } }
    }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .windowInsetsPadding(WindowInsets.systemBars),
    ) {
        WorkspaceHeader(
            title = "Product Entry",
            subtitle = if (isEditMode) "Edit product · master data" else "New product · master data",
            onSegmentClick = onOpenDrawer,
            onNavigateBack = if (showBackButton) onNavigateBack else null,
        )

        SegmentedTabBar(
            tabs = entryTabs,
            selectedIndex = state.entryType.ordinal,
            onTabSelect = { state.entryType = EntryType.entries[it] },
            modifier =
                Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 8.dp, bottom = 8.dp),
        )

        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

        BoxWithConstraints(modifier = Modifier.weight(1f)) {
            if (maxWidth >= 750.dp) {
                EntryDesktopLayout(state)
            } else {
                EntryMobileLayout(state)
            }
        }

        EntryActionBar(
            isFormValid = state.isFormValid,
            isEditMode = isEditMode,
            onSave = onSave,
            onDiscard = onNavigateBack,
        )
    }
}

// Mobile: single scrolling column — padding and spacing match MonitorStockPanel.
@Composable
private fun EntryMobileLayout(state: EntryFormState) {
    androidx.compose.foundation.lazy.LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item { ProductDetailsSection(state) }
        item { PricingSection(state) }

        when (state.entryType) {
            EntryType.GOODS -> {
                item { InventorySection(state) }
                item { OptionsSection(state) }
            }
            EntryType.SERVICE -> {
                item { ScheduleCapacitySection(state) }
                item { OptionsSection(state) }
            }
            EntryType.RENTAL -> {
                item { ResourcesBookingSection(state) }
            }
        }

        item { AdvancedSection(state) }
    }
}

// Desktop: two equal columns separated by a VerticalDivider — mirrors MonitorStockPanel's wide layout.
@Composable
private fun EntryDesktopLayout(state: EntryFormState) {
    Row(modifier = Modifier.fillMaxSize()) {
        // Left column — product details + pricing
        androidx.compose.foundation.lazy.LazyColumn(
            modifier = Modifier.weight(1f).fillMaxHeight(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item { ProductDetailsSection(state) }
            item { PricingSection(state) }
        }

        VerticalDivider(color = MaterialTheme.colorScheme.outlineVariant)

        // Right column — type-specific operational sections + advanced
        androidx.compose.foundation.lazy.LazyColumn(
            modifier = Modifier.weight(1f).fillMaxHeight(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            when (state.entryType) {
                EntryType.GOODS -> {
                    item { InventorySection(state) }
                    item { OptionsSection(state) }
                }
                EntryType.SERVICE -> {
                    item { ScheduleCapacitySection(state) }
                    item { OptionsSection(state) }
                }
                EntryType.RENTAL -> {
                    item { ResourcesBookingSection(state) }
                }
            }
            item { AdvancedSection(state) }
        }
    }
}

@Preview(showBackground = true, widthDp = 411, heightDp = 891)
@Composable
private fun MasterEntryMobilePreview() {
    MaterialTheme { MasterEntryScreen() }
}

@Preview(showBackground = true, widthDp = 1280, heightDp = 800)
@Composable
private fun MasterEntryDesktopPreview() {
    MaterialTheme { MasterEntryScreen() }
}