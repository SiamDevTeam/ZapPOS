/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.screens.product.entry

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.siamdev.zappos.ui.components.common.SegmentedTabBar
import org.siamdev.zappos.ui.components.common.WorkspaceHeader
import org.siamdev.zappos.ui.screens.product.entry.sections.EntryActionBar
import org.siamdev.zappos.ui.screens.product.entry.sections.InventorySection
import org.siamdev.zappos.ui.screens.product.entry.sections.OptionsSection
import org.siamdev.zappos.ui.screens.product.entry.sections.PricingSection
import org.siamdev.zappos.ui.screens.product.entry.sections.ProductDetailsSection
import org.siamdev.zappos.ui.screens.product.entry.sections.ResourcesBookingSection
import org.siamdev.zappos.ui.screens.product.entry.sections.ScheduleCapacitySection
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
        val event = remember(productId) { sampleProducts().find { it.id == productId } }
        LaunchedEffect(productId) { event?.let { state.loadFrom(it) } }
    }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .windowInsetsPadding(WindowInsets.systemBars),
    ) {
        WorkspaceHeader(
            title = "Product Master",
            subtitle = if (isEditMode) "Edit product · master data" else "New product · master data",
            onSegmentClick = onOpenDrawer,
            onNavigateBack = if (showBackButton) onNavigateBack else null,
        )

        if (isEditMode) {
            ProductSummary(state)
        } else {
            SegmentedTabBar(
                tabs = entryTabs,
                selectedIndex = state.entryType.ordinal,
                onTabSelect = { state.entryType = EntryType.entries[it] },
                modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp, bottom = 8.dp),
            )
        }

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
    LazyColumn(
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
            EntryType.RENTAL -> item { ResourcesBookingSection(state) }
        }

        
    }
}

// Desktop: two equal columns separated by a VerticalDivider — mirrors MonitorStockPanel's wide layout.
@Composable
private fun EntryDesktopLayout(state: EntryFormState) {
    Row(modifier = Modifier.fillMaxSize()) {
        // Left column — product details + pricing
        LazyColumn(
            modifier = Modifier.weight(1f).fillMaxHeight(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item { ProductDetailsSection(state) }
            item { PricingSection(state) }
        }

        VerticalDivider(color = MaterialTheme.colorScheme.outlineVariant)

        // Right column — type-specific operational sections + advanced
        LazyColumn(
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
            
        }
    }
}

/** Compact read-only product identity shown in place of the SegmentedTabBar when editing an existing item.
 *  Mirrors ProductHeader's structure: leading icon box → ID breadcrumb + type chip → product name. */
@Composable
private fun ProductSummary(state: EntryFormState) {
    val tab = entryTabs[state.entryType.ordinal]
    val formattedId = state.productId?.padStart(5, '0') ?: "—"

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 12.dp, bottom = 8.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Leading icon box — same dimensions and colours as ProductHeader
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center,
            ) {
                if (tab.icon != null) {
                    Icon(
                        imageVector = tab.icon,
                        contentDescription = null,
                        modifier = Modifier.size(22.dp),
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }
            }

            Spacer(Modifier.width(12.dp))

            Column {
                // Breadcrumb row: Product ID · type chip
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Text(
                        formattedId,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                            .padding(horizontal = 7.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Text(
                            tab.label,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                }
                // Product name — same style as ProductHeader
                Text(
                    state.name.ifBlank { "—" },
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }

    }
}

@Preview(showBackground = true, widthDp = 411, heightDp = 891, name = "Mobile · Create")
@Composable
private fun MasterEntryMobilePreview() {
    MaterialTheme { MasterEntryScreen() }
}

@Preview(showBackground = true, widthDp = 411, heightDp = 891, name = "Mobile · Edit")
@Composable
private fun MasterEntryMobileEditPreview() {
    MaterialTheme { MasterEntryScreen(productId = "1") }
}

@Preview(showBackground = true, widthDp = 1280, heightDp = 800, name = "Desktop · Create")
@Composable
private fun MasterEntryDesktopPreview() {
    MaterialTheme { MasterEntryScreen() }
}

@Preview(showBackground = true, widthDp = 1280, heightDp = 800, name = "Desktop · Edit")
@Composable
private fun MasterEntryDesktopEditPreview() {
    MaterialTheme { MasterEntryScreen(productId = "1") }
}