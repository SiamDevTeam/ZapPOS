/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.screens.product.goods

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.siamdev.zappos.LocalSettingVM
import org.siamdev.zappos.ui.components.common.WorkspaceHeader
import org.siamdev.zappos.ui.screens.setting.SettingViewModel

/** Root screen for the product catalogue. Switches between [DesktopLayout] and [MobileLayout] based on window width. */
@Composable
fun ProductListScreen(
    onOpenDrawer: () -> Unit = {},
    onEditProduct: (String) -> Unit = {},
    onDeleteProduct: (String) -> Unit = {},
    initialTab: DetailTab = DetailTab.PRODUCT_DETAIL,
) {
    val products = remember { sampleProducts() }
    var selectedId by remember { mutableStateOf<String?>(null) }
    val selected = products.find { it.id == selectedId }

    BoxWithConstraints(
        modifier =
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .windowInsetsPadding(WindowInsets.systemBars),
    ) {
        val isDesktop = maxWidth >= 750.dp && maxHeight >= 500.dp
        if (isDesktop) {
            DesktopLayout(
                products = products,
                selectedId = selectedId,
                selected = selected,
                onSelect = { selectedId = it },
                onOpenDrawer = onOpenDrawer,
                onEdit = onEditProduct,
                onDelete = onDeleteProduct,
                initialTab = initialTab,
            )
        } else {
            MobileLayout(
                products = products,
                selectedId = selectedId,
                selected = selected,
                onSelect = { selectedId = it },
                onBack = { selectedId = null },
                onOpenDrawer = onOpenDrawer,
                onEdit = onEditProduct,
                onDelete = onDeleteProduct,
                initialTab = initialTab,
            )
        }
    }
}

@Composable
private fun DesktopLayout(
    products: List<Product>,
    selectedId: String?,
    selected: Product?,
    onSelect: (String) -> Unit,
    onOpenDrawer: () -> Unit,
    onEdit: (String) -> Unit,
    onDelete: (String) -> Unit = {},
    initialTab: DetailTab = DetailTab.PRODUCT_DETAIL,
) {
    var splitRatio by remember { mutableStateOf(0.30f) }

    Column(Modifier.fillMaxSize()) {
        WorkspaceHeader(title = "Products List", subtitle = "Inventory · catalog", onSegmentClick = onOpenDrawer)

        BoxWithConstraints(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
        ) {
            val totalWidth = maxWidth

            Row(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier =
                        Modifier
                            .width(totalWidth * splitRatio)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.surface)
                            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp)),
                ) {
                    ProductListPane(
                        products = products,
                        selectedId = selectedId,
                        onSelect = onSelect,
                        modifier = Modifier.fillMaxSize(),
                    )
                }

                // Draggable divider
                Box(
                    modifier =
                        Modifier
                            .width(16.dp)
                            .fillMaxHeight()
                            .pointerHoverIcon(PointerIcon.Hand)
                            .pointerInput(totalWidth) {
                                val totalPx = totalWidth.toPx()
                                detectHorizontalDragGestures { change, dragAmount ->
                                    change.consume()
                                    splitRatio = (splitRatio + dragAmount / totalPx).coerceIn(0.20f, 0.55f)
                                }
                            },
                    contentAlignment = Alignment.Center,
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(5.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        repeat(5) {
                            Box(
                                Modifier
                                    .size(3.dp)
                                    .clip(RoundedCornerShape(50))
                                    .background(MaterialTheme.colorScheme.outlineVariant),
                            )
                        }
                    }
                }

                Box(
                    modifier =
                        Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.surface)
                            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp)),
                ) {
                    if (selected != null) {
                        ProductDetailPanel(
                            product = selected,
                            onEdit = { onEdit(selected.id) },
                            onDelete = onDelete,
                            initialTab = initialTab,
                        )
                    } else {
                        EmptyDetailState()
                    }
                }
            }
        }
    }
}

/** Single-column layout that slides between the list and detail panel. Used on screens < 750 dp wide. */
@Composable
private fun MobileLayout(
    products: List<Product>,
    selectedId: String?,
    selected: Product?,
    onSelect: (String) -> Unit,
    onBack: () -> Unit,
    onOpenDrawer: () -> Unit,
    onEdit: (String) -> Unit,
    onDelete: (String) -> Unit = {},
    initialTab: DetailTab = DetailTab.PRODUCT_DETAIL,
) {
    AnimatedContent(
        targetState = selected,
        transitionSpec = {
            if (targetState != null) {
                slideInHorizontally { it } + fadeIn() togetherWith
                    slideOutHorizontally { -it / 3 } + fadeOut()
            } else {
                slideInHorizontally { -it / 3 } + fadeIn() togetherWith
                    slideOutHorizontally { it } + fadeOut()
            }
        },
    ) { current ->
        Column(Modifier.fillMaxSize()) {
            if (current != null) {
                WorkspaceHeader(
                    title = "Information",
                    subtitle = "Product · detail",
                    onSegmentClick = onOpenDrawer,
                    onNavigateBack = onBack,
                )
                ProductDetailPanel(
                    product = current,
                    onEdit = { onEdit(current.id) },
                    onDelete = onDelete,
                    initialTab = initialTab,
                )
            } else {
                WorkspaceHeader(
                    title = "Products List",
                    onSegmentClick = onOpenDrawer,
                )
                ProductListPane(
                    products = products,
                    selectedId = selectedId,
                    onSelect = onSelect,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}

@Composable
private fun EmptyDetailState() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                Icons.Default.TouchApp,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f),
            )
            Spacer(Modifier.height(16.dp))
            Text(
                "Select a product to view details",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.35f),
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 411, heightDp = 891, name = "Mobile · List")
@Composable
private fun MobileListPreview() {
    CompositionLocalProvider(LocalSettingVM provides SettingViewModel()) {
        val products = sampleProducts()
        MobileLayout(
            products = products,
            selectedId = null,
            selected = null,
            onSelect = {},
            onBack = {},
            onOpenDrawer = {},
            onEdit = {},
        )
    }
}

@Preview(showBackground = true, widthDp = 411, heightDp = 891, name = "Mobile · Detail – Product Detail")
@Composable
private fun MobileDetailProductPreview() {
    CompositionLocalProvider(LocalSettingVM provides SettingViewModel()) {
        val products = sampleProducts()
        val selected = products.first()
        MobileLayout(
            products = products,
            selectedId = selected.id,
            selected = selected,
            onSelect = {},
            onBack = {},
            onOpenDrawer = {},
            onEdit = {},
        )
    }
}

@Preview(showBackground = true, widthDp = 411, heightDp = 891, name = "Mobile · Detail – Monitor & Stock")
@Composable
private fun MobileDetailMonitorPreview() {
    CompositionLocalProvider(LocalSettingVM provides SettingViewModel()) {
        val products = sampleProducts()
        val selected = products.first()
        Column(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
        ) {
            WorkspaceHeader(title = "Information", onNavigateBack = {})
            MonitorStockTabContent(product = selected)
        }
    }
}

@Preview(showBackground = true, widthDp = 411, heightDp = 891, name = "Mobile · Detail – Out of Stock")
@Composable
private fun MobileDetailOutOfStockPreview() {
    CompositionLocalProvider(LocalSettingVM provides SettingViewModel()) {
        val products = sampleProducts()
        val selected = products.first { it.stockQty == 0 }
        Column(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
        ) {
            WorkspaceHeader(title = "Information", onNavigateBack = {})
            MonitorStockTabContent(product = selected)
        }
    }
}

@Preview(showBackground = true, widthDp = 1280, heightDp = 800, name = "Desktop · No Selection")
@Composable
private fun DesktopNoSelectionPreview() {
    CompositionLocalProvider(LocalSettingVM provides SettingViewModel()) {
        ProductListScreen()
    }
}

@Preview(showBackground = true, widthDp = 1280, heightDp = 800, name = "Desktop · With Product Detail Selection")
@Composable
private fun DesktopWithProductDetailSelectionPreview() {
    CompositionLocalProvider(LocalSettingVM provides SettingViewModel()) {
        val products = sampleProducts()
        val selected = products.first()
        DesktopLayout(
            products = products,
            selectedId = selected.id,
            selected = selected,
            onSelect = {},
            onOpenDrawer = {},
            onEdit = {},
        )
    }
}

@Preview(showBackground = true, widthDp = 1280, heightDp = 800, name = "Desktop · With Monitor & Stock Selection")
@Composable
private fun DesktopWithMonitorAndStockSelectionPreview() {
    CompositionLocalProvider(LocalSettingVM provides SettingViewModel()) {
        val products = sampleProducts()
        val selected = products.first()
        DesktopLayout(
            products = products,
            selectedId = selected.id,
            selected = selected,
            onSelect = {},
            onOpenDrawer = {},
            onEdit = {},
            initialTab = DetailTab.MONITOR_STOCK,
        )
    }
}
