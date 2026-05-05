/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.screens.sale

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ViewList
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.CurrencyLira
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.siamdev.zappos.LocalMenuVM
import org.siamdev.zappos.theme.YellowPrimary
import org.siamdev.zappos.ui.components.*
import org.siamdev.zappos.ui.components.WorkspaceHeader
import zappos.composeapp.generated.resources.Res
import zappos.composeapp.generated.resources.sat_unit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainMenuScreen(
    onOpenDrawer: () -> Unit = {},
    onCheckout: () -> Unit = {}
) {
    val viewModel = LocalMenuVM.current
    val items = viewModel.items

    LaunchedEffect(Unit) {
        viewModel.loadProductsData()
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isDesktop = maxWidth >= 750.dp && maxHeight >= 500.dp

        if (isDesktop) {
            DesktopMenuLayout(
                viewModel = viewModel,
                items = items,
                onOpenDrawer = onOpenDrawer,
                onCheckout = onCheckout
            )
        } else {
            MobileMenuLayout(
                viewModel = viewModel,
                items = items,
                onOpenDrawer = onOpenDrawer,
                onCheckout = onCheckout
            )
        }
    }
}


@Composable
private fun MenuSearchFilter(
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    categories: List<String>,
    selectedCategory: String?,
    onCategorySelect: (String?) -> Unit,
    viewMode: MenuViewMode,
    onViewModeChange: (MenuViewMode) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchChange,
            placeholder = { Text("Search menus...") },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(18.dp))
            },
            trailingIcon = if (searchQuery.isNotEmpty()) {
                {
                    IconButton(onClick = { onSearchChange("") }) {
                        Icon(Icons.Default.Clear, contentDescription = null, modifier = Modifier.size(16.dp))
                    }
                }
            } else null,
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = YellowPrimary,
                cursorColor = YellowPrimary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Category chips — scrollable, takes remaining width
            Row(
                modifier = Modifier
                    .weight(1f)
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val allSelected = selectedCategory == null
                FilterChip(
                    selected = allSelected,
                    onClick = { onCategorySelect(null) },
                    label = { Text("All") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = YellowPrimary.copy(alpha = 0.18f),
                        selectedLabelColor = MaterialTheme.colorScheme.onSurface
                    ),
                    border = if (allSelected) BorderStroke(1.5.dp, YellowPrimary)
                             else BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                )
                categories.forEach { cat ->
                    val isSelected = selectedCategory == cat
                    FilterChip(
                        selected = isSelected,
                        onClick = { onCategorySelect(if (isSelected) null else cat) },
                        label = { Text(cat.replaceFirstChar { it.uppercase() }) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = YellowPrimary.copy(alpha = 0.18f),
                            selectedLabelColor = MaterialTheme.colorScheme.onSurface
                        ),
                        border = if (isSelected) BorderStroke(1.5.dp, YellowPrimary)
                                 else BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                    )
                }
            }

            // View mode toggle
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                MenuViewMode.entries.forEach { mode ->
                    val isSelected = viewMode == mode
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) YellowPrimary else Color.Transparent)
                            .clickable { onViewModeChange(mode) }
                            .padding(6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (mode == MenuViewMode.LIST)
                                Icons.AutoMirrored.Filled.ViewList
                            else
                                Icons.Default.GridView,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = if (isSelected) MaterialTheme.colorScheme.onSurface
                                   else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                        )
                    }
                }
            }
        }
    }
}


@Composable
private fun MenuItemsContent(
    items: List<MenuItem>,
    viewMode: MenuViewMode,
    viewModel: MainMenuViewModel,
    isLoading: Boolean = false,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(bottom = 24.dp)
) {
    if (isLoading) {
        when (viewMode) {
            MenuViewMode.LIST -> MenuListSkeleton()
            MenuViewMode.GRID -> MenuGridSkeleton()
        }
        return
    }

    if (items.isEmpty()) {
        Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    Icons.Default.SearchOff,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    "No menus found",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        return
    }

    if (viewMode == MenuViewMode.LIST) {
        LazyColumn(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = contentPadding
        ) {
            items(items.size) { index ->
                val item = items[index]
                MenuItemCard(
                    imageUrl = item.imageUrl,
                    name = item.name,
                    priceBaht = item.priceBaht,
                    priceSat = item.priceSat,
                    count = item.count,
                    viewMode = MenuViewMode.LIST,
                    onAddClick = { viewModel.addItem(item.id) },
                    onReduceClick = { viewModel.reduceItem(item.id) }
                )
            }
        }
    } else {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 160.dp),
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            contentPadding = contentPadding
        ) {
            items(items) { item ->
                MenuItemCard(
                    imageUrl = item.imageUrl,
                    name = item.name,
                    priceBaht = item.priceBaht,
                    priceSat = item.priceSat,
                    count = item.count,
                    viewMode = MenuViewMode.GRID,
                    onAddClick = { viewModel.addItem(item.id) },
                    onReduceClick = { viewModel.reduceItem(item.id) }
                )
            }
        }
    }
}

@Composable
private fun DesktopMenuLayout(
    viewModel: MainMenuViewModel,
    items: List<MenuItem>,
    onOpenDrawer: () -> Unit,
    onCheckout: () -> Unit
) {
    var viewMode by remember { mutableStateOf(MenuViewMode.LIST) }
    var searchQuery by remember { mutableStateOf("") }
    var categoryFilter by remember { mutableStateOf<String?>(null) }
    var splitRatio by remember { mutableStateOf(0.25f) }
    val minRatio = 0.15f
    val maxRatio = 0.45f
    val panelColor = MaterialTheme.colorScheme.surfaceContainer

    val categories by remember {
        derivedStateOf {
            items.map { it.category }.filter { it.isNotBlank() }.distinct().sorted()
        }
    }
    val filteredItems by remember {
        derivedStateOf {
            items.filter { item ->
                (categoryFilter == null || item.category == categoryFilter) &&
                (searchQuery.isBlank() || item.name.contains(searchQuery, ignoreCase = true))
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(WindowInsets.statusBars)
    ) {
        WorkspaceHeader(title = "Main Menu", onSegmentClick = onOpenDrawer)

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
                // Left: Menu pane
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    shape = RoundedCornerShape(16.dp),
                    color = panelColor,
                    shadowElevation = 0.dp,
                    tonalElevation = 0.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 16.dp, end = 16.dp, top = 16.dp)
                    ) {
                        MenuSearchFilter(
                            searchQuery = searchQuery,
                            onSearchChange = { searchQuery = it },
                            categories = categories,
                            selectedCategory = categoryFilter,
                            onCategorySelect = { categoryFilter = it },
                            viewMode = viewMode,
                            onViewModeChange = { viewMode = it }
                        )

                        Box(modifier = Modifier.fillMaxSize()) {
                            MenuItemsContent(
                                items = filteredItems,
                                viewMode = viewMode,
                                viewModel = viewModel,
                                isLoading = viewModel.isLoading,
                                modifier = Modifier.fillMaxSize()
                            )

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(32.dp)
                                    .align(Alignment.BottomCenter)
                                    .background(
                                        Brush.verticalGradient(
                                            colors = listOf(Color.Transparent, panelColor)
                                        )
                                    )
                            )
                        }
                    }
                }

                // Draggable divider handle
                Box(
                    modifier = Modifier
                        .width(18.dp)
                        .fillMaxHeight()
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

                // Right: Order Panel
                Surface(
                    modifier = Modifier
                        .width(totalWidth * splitRatio)
                        .fillMaxHeight(),
                    shape = RoundedCornerShape(16.dp),
                    color = panelColor,
                    shadowElevation = 0.dp,
                    tonalElevation = 0.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 20.dp, vertical = 16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = 16.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ShoppingCart,
                                contentDescription = null,
                                tint = YellowPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = "Order",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))
                        Spacer(Modifier.height(8.dp))

                        LazyColumn(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            if (viewModel.selectedKeys.isEmpty()) {
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 32.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "No items yet",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                                        )
                                    }
                                }
                            } else {
                                items(viewModel.selectedKeys.size) { index ->
                                    val key = viewModel.selectedKeys[index]
                                    val item = items.first { it.id == key }
                                    OrderItemCard(
                                        item = item,
                                        onAddClick = { viewModel.addItem(item.id) },
                                        onReduceClick = { viewModel.reduceItem(item.id) },
                                        onCountChange = { viewModel.setItemCount(item.id, it) },
                                        isDesktop = true
                                    )
                                }
                            }
                        }

                        Spacer(Modifier.height(12.dp))
                        HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))
                        Spacer(Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Total",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                            Column(horizontalAlignment = Alignment.End) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.CurrencyLira,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(Modifier.width(4.dp))
                                    Text(
                                        viewModel.totalFiat,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Image(
                                        painterResource(Res.drawable.sat_unit),
                                        contentDescription = null,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(Modifier.width(4.dp))
                                    Text(
                                        viewModel.totalSat,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                    )
                                }
                            }
                        }

                        Spacer(Modifier.height(16.dp))

                        MaterialButton(
                            modifier = Modifier.fillMaxWidth(),
                            text = "Checkout",
                            iconStart = Icons.Default.ShoppingCart,
                            enabled = viewModel.selectedKeys.isNotEmpty(),
                            onClick = { onCheckout() }
                        )
                        Spacer(Modifier.height(8.dp))
                        MaterialButton(
                            modifier = Modifier.fillMaxWidth(),
                            text = "Clear Cart",
                            buttonColor = Color.Transparent,
                            showBorder = true,
                            onClick = { viewModel.clearAllItems() }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MobileMenuLayout(
    viewModel: MainMenuViewModel,
    items: List<MenuItem>,
    onOpenDrawer: () -> Unit,
    onCheckout: () -> Unit
) {
    var viewMode by remember { mutableStateOf(MenuViewMode.LIST) }
    var searchQuery by remember { mutableStateOf("") }
    var categoryFilter by remember { mutableStateOf<String?>(null) }

    val categories by remember {
        derivedStateOf {
            items.map { it.category }.filter { it.isNotBlank() }.distinct().sorted()
        }
    }
    val filteredItems by remember {
        derivedStateOf {
            items.filter { item ->
                (categoryFilter == null || item.category == categoryFilter) &&
                (searchQuery.isBlank() || item.name.contains(searchQuery, ignoreCase = true))
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
                viewModel.selectedKeys.forEach { key ->
                    val item = items.first { it.id == key }
                    OrderItemCard(
                        item = item,
                        onAddClick = { viewModel.addItem(item.id) },
                        onReduceClick = { viewModel.reduceItem(item.id) },
                        onCountChange = { viewModel.setItemCount(item.id, it) }
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
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CurrencyLira,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(viewModel.totalFiat, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onSurface)
                        }
                        Spacer(Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painterResource(Res.drawable.sat_unit),
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(viewModel.totalSat, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
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
                        onClick = { viewModel.clearAllItems() }
                    )
                    Spacer(Modifier.width(12.dp))
                    MaterialButton(
                        modifier = Modifier.weight(1f),
                        text = "Checkout",
                        iconStart = Icons.Default.ShoppingCart,
                        enabled = viewModel.selectedKeys.isNotEmpty(),
                        onClick = { onCheckout() }
                    )
                }
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.statusBars)
            ) {
                WorkspaceHeader(title = "Main Menu", onSegmentClick = onOpenDrawer)

                MenuSearchFilter(
                    searchQuery = searchQuery,
                    onSearchChange = { searchQuery = it },
                    categories = categories,
                    selectedCategory = categoryFilter,
                    onCategorySelect = { categoryFilter = it },
                    viewMode = viewMode,
                    onViewModeChange = { viewMode = it },
                    modifier = Modifier.padding(horizontal = 20.dp).padding(top = 10.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background.copy(alpha = 0.9f))
                        .padding(top = 5.dp, bottom = 80.dp)
                ) {
                    MenuItemsContent(
                        items = filteredItems,
                        viewMode = viewMode,
                        viewModel = viewModel,
                        isLoading = viewModel.isLoading,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp)
                    )

                    // Fade ด้านบน
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .align(Alignment.TopCenter)
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        MaterialTheme.colorScheme.background,
                                        Color.Transparent
                                    )
                                )
                            )
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 411, heightDp = 891)
@Composable
fun MainMenuScreenPreview() {
    val vm = remember { MainMenuViewModel() }
    CompositionLocalProvider(LocalMenuVM provides vm) {
        MainMenuScreen()
    }
}

@Preview(showBackground = true, widthDp = 1280, heightDp = 800)
@Composable
fun MainMenuScreenDesktopPreview() {
    val vm = remember { MainMenuViewModel() }
    CompositionLocalProvider(LocalMenuVM provides vm) {
        MainMenuScreen()
    }
}