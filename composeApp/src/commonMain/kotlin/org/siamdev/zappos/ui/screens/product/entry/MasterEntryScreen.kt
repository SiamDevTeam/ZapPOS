/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.screens.product.entry

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.siamdev.zappos.viewModelOf
import org.siamdev.zappos.ui.components.common.SegmentedTabBar
import org.siamdev.zappos.ui.components.common.WorkspaceHeader
import org.siamdev.zappos.ui.components.menu.DefaultProductCategories
import org.siamdev.zappos.ui.components.product.ProductHeader
import org.siamdev.zappos.ui.screens.product.entry.sections.AdvancedSection
import org.siamdev.zappos.ui.screens.product.entry.sections.EntryActionBar
import org.siamdev.zappos.ui.screens.product.entry.sections.InventorySection
import org.siamdev.zappos.ui.screens.product.entry.sections.OptionsSection
import org.siamdev.zappos.ui.screens.product.entry.sections.PricingSection
import org.siamdev.zappos.ui.screens.product.entry.sections.ProductDetailsSection
import org.siamdev.zappos.ui.screens.product.entry.sections.ResourcesBookingSection
import org.siamdev.zappos.ui.screens.product.entry.sections.ScheduleCapacitySection

@Composable
fun MasterEntryScreen(
    productId: String? = null,
    onNavigateBack: () -> Unit = {},
    onOpenDrawer: () -> Unit = {},
    onSave: () -> Unit = {},
    showBackButton: Boolean = false,
) {
    val vm = viewModelOf { MasterEntryViewModel() }
    val surface = remember(vm) { MasterEntrySurfaceImpl(vm) }

    LaunchedEffect(productId) { vm.init(productId) }

    LaunchedEffect(vm) {
        vm.effect.collect { effect ->
            when (effect) {
                is MasterEntryViewModel.SideEffect.SaveSuccess -> onSave()
                is MasterEntryViewModel.SideEffect.NavigateBack -> onNavigateBack()
                is MasterEntryViewModel.SideEffect.SaveError -> Unit
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(WindowInsets.systemBars),
    ) {
        WorkspaceHeader(
            title = "Product Master",
            subtitle = if (surface.isEditMode) "Edit product · master data" else "New product · master data",
            onSegmentClick = onOpenDrawer,
            onNavigateBack = if (showBackButton) onNavigateBack else null,
        )

        if (!surface.isEditMode) {
            SegmentedTabBar(
                tabs = entryTabs,
                selectedIndex = surface.entryType.ordinal,
                onTabSelect = { surface.entryType = EntryType.entries[it] },
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 8.dp, bottom = 8.dp),
            )
        }

        BoxWithConstraints(modifier = Modifier.weight(1f)) {
            if (maxWidth >= 750.dp) {
                EntryDesktopLayout(surface)
            } else {
                EntryMobileLayout(surface)
            }
        }

        EntryActionBar(
            isFormValid = surface.isFormValid,
            isEditMode = surface.isEditMode,
            onSave = { surface.save() },
            onDiscard = { surface.discard() },
        )
    }
}

@Composable
private fun EntryMobileLayout(surface: MasterEntrySurface) {
    val tab = entryTabs[surface.entryType.ordinal]
    val catEntry =
        remember(surface.category) { DefaultProductCategories.find { it.id == surface.category } }
    val categoryName = catEntry?.name ?: surface.category.ifBlank { tab.label }
    val subName = catEntry?.subCategories?.find { it.id == surface.subCategory }?.name
    val catIcon = catEntry?.icon ?: tab.icon ?: Icons.Default.ShoppingBag

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        if (surface.name.isNotBlank()) {
            item {
                ProductHeader(
                    name = surface.name,
                    categoryName = categoryName,
                    subName = subName,
                    catIcon = catIcon,
                )
            }
        }

        item { ProductDetailsSection(surface) }
        item { PricingSection(surface) }

        when (surface.entryType) {
            EntryType.GOODS -> {
                item { InventorySection(surface) }
                item { OptionsSection(surface) }
            }

            EntryType.SERVICE -> {
                item { ScheduleCapacitySection(surface) }
                item { OptionsSection(surface) }
            }

            EntryType.RENTAL -> item { ResourcesBookingSection(surface) }
        }

        item { AdvancedSection(surface) }
    }
}

@Composable
private fun EntryDesktopLayout(surface: MasterEntrySurface) {
    val tab = entryTabs[surface.entryType.ordinal]
    val catEntry =
        remember(surface.category) { DefaultProductCategories.find { it.id == surface.category } }
    val categoryName = catEntry?.name ?: surface.category.ifBlank { tab.label }
    val subName = catEntry?.subCategories?.find { it.id == surface.subCategory }?.name
    val catIcon = catEntry?.icon ?: tab.icon ?: Icons.Default.ShoppingBag

    Column(modifier = Modifier.fillMaxSize()) {
        if (surface.name.isNotBlank()) {
            ProductHeader(
                name = surface.name,
                categoryName = categoryName,
                subName = subName,
                catIcon = catIcon,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
            )
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

        Row(modifier = Modifier.weight(1f)) {
            LazyColumn(
                modifier = Modifier.weight(1f).fillMaxHeight(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                item { ProductDetailsSection(surface) }
                item { PricingSection(surface) }
            }

            VerticalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            LazyColumn(
                modifier = Modifier.weight(1f).fillMaxHeight(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                when (surface.entryType) {
                    EntryType.GOODS -> {
                        item { InventorySection(surface) }
                        item { OptionsSection(surface) }
                    }

                    EntryType.SERVICE -> {
                        item { ScheduleCapacitySection(surface) }
                        item { OptionsSection(surface) }
                    }

                    EntryType.RENTAL -> {
                        item { ResourcesBookingSection(surface) }
                    }
                }
                item { AdvancedSection(surface) }
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