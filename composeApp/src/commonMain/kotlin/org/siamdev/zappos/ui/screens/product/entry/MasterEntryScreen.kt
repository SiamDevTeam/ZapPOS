/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.screens.product.entry

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.siamdev.zappos.ui.components.common.SegmentedTabBar
import org.siamdev.zappos.ui.components.common.WorkspaceHeader

@Composable
fun MasterEntryScreen(
    onNavigateBack: () -> Unit = {},
    onOpenDrawer: () -> Unit = {},
    onSave: () -> Unit = {},
    showBackButton: Boolean = false,
) {
    val state = rememberEntryFormState()

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .windowInsetsPadding(WindowInsets.systemBars),
    ) {
        WorkspaceHeader(
            title = "Product Entry",
            subtitle = "New product · master data",
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
            onSave = onSave,
            onDiscard = onNavigateBack,
        )
    }
}

@Composable
private fun EntryMobileLayout(state: EntryFormState) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
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
        item { Spacer(Modifier.height(8.dp)) }
    }
}

@Composable
private fun EntryDesktopLayout(state: EntryFormState) {
    Row(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        // Left column — product details + pricing
        LazyColumn(
            modifier = Modifier.weight(0.46f).fillMaxHeight(),
            contentPadding = PaddingValues(bottom = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            item { ProductDetailsSection(state) }
            item { PricingSection(state) }
        }

        // Right column — tab-specific sections + advanced
        LazyColumn(
            modifier = Modifier.weight(0.54f).fillMaxHeight(),
            contentPadding = PaddingValues(bottom = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
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
