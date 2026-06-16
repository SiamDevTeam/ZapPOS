/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.screens.sale

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import org.siamdev.zappos.LocalMenuVM
import org.siamdev.zappos.LocalProductBrowserVM
import org.siamdev.zappos.LocalSettingVM
import org.siamdev.zappos.ui.components.common.CurrencyCodeIcon
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.siamdev.zappos.ui.components.common.MaterialButton
import org.siamdev.zappos.ui.components.common.WorkspaceHeader
import org.siamdev.zappos.ui.components.menu.MenuItemsContent
import org.siamdev.zappos.ui.components.menu.MenuViewMode
import org.siamdev.zappos.ui.components.menu.MenuViewToggle
import org.siamdev.zappos.ui.components.menu.SearchFilter
import org.siamdev.zappos.ui.components.order.OrderItemCard
import org.siamdev.zappos.ui.components.order.OrderPanel
import org.siamdev.zappos.ui.components.product.ProductPanel
import org.siamdev.zappos.ui.components.sheet.SlideBottomSheet
import org.siamdev.zappos.ui.screens.setting.SettingFacadeImpl
import org.siamdev.zappos.ui.screens.setting.SettingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainMenuScreen(
    onOpenDrawer: () -> Unit = {},
    onCheckout: () -> Unit = {}
) {
    val menu = LocalMenuVM.current

    LaunchedEffect(Unit) {
        menu.ensureLoaded()
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        val isDesktop = maxWidth >= 750.dp && maxHeight >= 500.dp

        if (isDesktop) {
            DesktopMenuLayout(
                menu = menu,
                onOpenDrawer = onOpenDrawer,
                onCheckout = onCheckout
            )
        } else {
            MobileMenuLayout(
                menu = menu,
                onOpenDrawer = onOpenDrawer,
                onCheckout = onCheckout
            )
        }
    }
}


@Composable
private fun DesktopMenuLayout(
    menu: MainMenuFacade,
    onOpenDrawer: () -> Unit,
    onCheckout: () -> Unit
) {
    var splitRatio by remember { mutableStateOf(0.25f) }
    val minRatio = 0.15f
    val maxRatio = 0.45f

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(WindowInsets.statusBars)
    ) {
        WorkspaceHeader(
            title = "Main Menu",
            subtitle = "Sales · point of sale",
            onSegmentClick = onOpenDrawer
        )

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            val totalWidth = maxWidth

            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ProductPanel(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                )

                Box(
                    modifier = Modifier
                        .width(18.dp)
                        .fillMaxHeight()
                        .pointerHoverIcon(PointerIcon.Hand)
                        .pointerInput(totalWidth) {
                            val totalPx = totalWidth.toPx()
                            detectHorizontalDragGestures { change, dragAmount ->
                                change.consume()
                                splitRatio = (splitRatio - dragAmount / totalPx)
                                    .coerceIn(minRatio, maxRatio)
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(5.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        repeat(5) {
                            Box(
                                modifier = Modifier
                                    .size(3.dp)
                                    .clip(RoundedCornerShape(50))
                                    .background(MaterialTheme.colorScheme.outlineVariant)
                            )
                        }
                    }
                }

                OrderPanel(
                    selectedKeys = menu.selectedKeys,
                    items = menu.items,
                    totalFiat = menu.totalFiat,
                    totalSat = menu.totalSat,
                    onAddItem = { menu.addItem(it) },
                    onReduceItem = { menu.reduceItem(it) },
                    onCountChange = { id, count -> menu.setItemCount(id, count) },
                    onCheckout = onCheckout,
                    onClearCart = { menu.clearAllItems() },
                    modifier = Modifier
                        .width(totalWidth * splitRatio)
                        .fillMaxHeight()
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MobileMenuLayout(
    menu: MainMenuFacade,
    onOpenDrawer: () -> Unit,
    onCheckout: () -> Unit
) {
    val setting = LocalSettingVM.current
    val primaryCode = setting.primaryCurrency?.code ?: "THB"
    val secondaryCode = setting.secondaryCurrency?.code ?: "SATS"
    val showSecondary = setting.showSecondaryCurrency

    val selectedKeys = menu.selectedKeys
    val isLoading = menu.isLoading
    val items = menu.items

    var viewMode by remember { mutableStateOf(MenuViewMode.LIST) }
    var searchQuery by remember { mutableStateOf("") }
    var categoryFilter by remember { mutableStateOf<String?>(null) }

    val categories by remember(items) {
        derivedStateOf {
            items.map { it.category }.filter { it.isNotBlank() }.distinct().sorted()
        }
    }
    val filteredItems by remember(items) {
        derivedStateOf {
            items.filter { item ->
                (categoryFilter == null || item.category == categoryFilter) &&
                        (searchQuery.isBlank() || item.name.contains(
                            searchQuery,
                            ignoreCase = true
                        ))
            }
        }
    }

    val sheetState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.PartiallyExpanded,
            skipHiddenState = true
        )
    )

    Box(modifier = Modifier.fillMaxSize()) {
        SlideBottomSheet(
            sheetState = sheetState,
            sheetMaxHeight = 420.dp,
            topContent = {
                selectedKeys.forEach { key ->
                    val item = items.first { it.id == key }
                    OrderItemCard(
                        item = item,
                        onAddClick = { menu.addItem(item.id) },
                        onReduceClick = { menu.reduceItem(item.id) },
                        onCountChange = { menu.setItemCount(item.id, it) }
                    )
                }
            },
            bottomContent = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Total Payment",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(bottom = 30.dp)
                    )
                    Column(horizontalAlignment = Alignment.End) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            CurrencyCodeIcon(
                                code = primaryCode,
                                modifier = Modifier.size(18.dp),
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                menu.totalFiat,
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        if (showSecondary) {
                            Spacer(Modifier.height(4.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                CurrencyCodeIcon(
                                    code = secondaryCode,
                                    modifier = Modifier.size(16.dp),
                                    tint = Color(0xFFFFB700)
                                )
                                Text(
                                    menu.totalSat,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    MaterialButton(
                        modifier = Modifier.weight(1f),
                        text = "Clear Cart",
                        buttonColor = Color.Transparent,
                        showBorder = true,
                        onClick = { menu.clearAllItems() }
                    )
                    Spacer(Modifier.width(12.dp))
                    MaterialButton(
                        modifier = Modifier.weight(1f),
                        text = "Checkout",
                        iconStart = Icons.Default.ShoppingCart,
                        enabled = selectedKeys.isNotEmpty(),
                        onClick = { onCheckout() }
                    )
                }
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
            ) {
                WorkspaceHeader(
                    title = "Main Menu",
                    subtitle = "Sales · point of sale",
                    onSegmentClick = onOpenDrawer
                )

                SearchFilter(
                    searchQuery = searchQuery,
                    onSearchChange = { searchQuery = it },
                    categories = categories,
                    selectedCategory = categoryFilter,
                    onCategorySelect = { categoryFilter = it },
                    trailingContent = {
                        MenuViewToggle(
                            viewMode = viewMode,
                            onViewModeChange = { viewMode = it })
                    },
                    modifier = Modifier.padding(horizontal = 20.dp).padding(top = 10.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 17.dp, bottom = 80.dp)
                ) {
                    MenuItemsContent(
                        items = filteredItems,
                        viewMode = viewMode,
                        isLoading = isLoading,
                        onRefresh = { menu.reloadProductsData() },
                        onAddItem = { menu.addItem(it) },
                        onReduceItem = { menu.reduceItem(it) },
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}


private val previewItems = listOf(
    MenuItem(1, "", "Mocha", "70.00", "17,500", "coffee", isRecommended = true, count = 2u),
    MenuItem(2, "", "Latte", "70.00", "17,500", "coffee"),
    MenuItem(3, "", "Matcha Latte", "100.00", "26,000", "matcha", isRecommended = true, count = 1u),
    MenuItem(4, "", "Thai Tea", "60.00", "15,000", "tea"),
    MenuItem(5, "", "Espresso", "50.00", "12,500", "coffee"),
    MenuItem(6, "", "Americano", "60.00", "15,000", "coffee"),
    MenuItem(7, "", "Cappuccino", "75.00", "18,750", "coffee", isRecommended = true),
    MenuItem(8, "", "Flat White", "80.00", "20,000", "coffee")
)

private fun previewVM() = MainMenuViewModel(autoLoad = false).also {
    it.loadItemsForPreview(previewItems)
}

@Preview(showBackground = true, widthDp = 411, heightDp = 891)
@Composable
fun MainMenuScreenPreview() {
    val vm = remember { previewVM() }
    val settingVM = remember { SettingViewModel() }

    CompositionLocalProvider(
        LocalMenuVM provides MainMenuFacadeImpl(vm),
        LocalProductBrowserVM provides vm,
        LocalSettingVM provides SettingFacadeImpl(settingVM)
    ) {
        MainMenuScreen()
    }
}

@Preview(showBackground = true, widthDp = 1280, heightDp = 800)
@Composable
fun MainMenuScreenDesktopPreview() {
    val vm = remember { previewVM() }
    val settingVM = remember { SettingViewModel() }

    CompositionLocalProvider(
        LocalMenuVM provides MainMenuFacadeImpl(vm),
        LocalProductBrowserVM provides vm,
        LocalSettingVM provides SettingFacadeImpl(settingVM)
    ) {
        MainMenuScreen()
    }
}