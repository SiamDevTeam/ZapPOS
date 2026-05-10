/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.components.menu

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ViewList
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.siamdev.zappos.ui.screens.sale.MenuItem

@Composable
internal fun SearchFilter(
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    categories: List<String> = emptyList(),
    selectedCategory: String? = null,
    onCategorySelect: (String?) -> Unit = {},
    modifier: Modifier = Modifier,
    trailingContent: (@Composable () -> Unit)? = null
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchChange,
            placeholder = { Text("Search...") },
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
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                cursorColor = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        if (categories.isNotEmpty() || trailingContent != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
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
                            selectedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.18f),
                            selectedLabelColor = MaterialTheme.colorScheme.onSurface
                        ),
                        border = if (allSelected) BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary)
                                 else BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                    )
                    categories.forEach { cat ->
                        val isSelected = selectedCategory == cat
                        FilterChip(
                            selected = isSelected,
                            onClick = { onCategorySelect(if (isSelected) null else cat) },
                            label = { Text(cat.replaceFirstChar { it.uppercase() }) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.18f),
                                selectedLabelColor = MaterialTheme.colorScheme.onSurface
                            ),
                            border = if (isSelected) BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary)
                                     else BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                        )
                    }
                }

                trailingContent?.invoke()
            }
        }
    }
}

@Composable
internal fun MenuViewToggle(
    viewMode: MenuViewMode,
    onViewModeChange: (MenuViewMode) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
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
                    .background(if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MenuItemsContent(
    items: List<MenuItem>,
    viewMode: MenuViewMode,
    isLoading: Boolean,
    onRefresh: () -> Unit,
    onAddItem: (Int) -> Unit,
    onReduceItem: (Int) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(bottom = 24.dp)
) {
    PullToRefreshBox(
        isRefreshing = isLoading,
        onRefresh = onRefresh,
        modifier = modifier
    ) {
        when {
            isLoading -> when (viewMode) {
                MenuViewMode.LIST -> MenuListSkeleton()
                MenuViewMode.GRID -> MenuGridSkeleton()
            }

            items.isEmpty() -> Box(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                contentAlignment = Alignment.Center
            ) {
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

            viewMode == MenuViewMode.LIST -> LazyColumn(
                modifier = Modifier.fillMaxSize(),
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
                        category = item.category.replaceFirstChar { it.uppercase() },
                        isRecommended = item.isRecommended,
                        isAvailable = item.isAvailable,
                        count = item.count,
                        viewMode = MenuViewMode.LIST,
                        onAddClick = { onAddItem(item.id) },
                        onReduceClick = { onReduceItem(item.id) }
                    )
                }
            }

            else -> LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 250.dp),
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
                        category = item.category.replaceFirstChar { it.uppercase() },
                        isRecommended = item.isRecommended,
                        isAvailable = item.isAvailable,
                        count = item.count,
                        viewMode = MenuViewMode.GRID,
                        onAddClick = { onAddItem(item.id) },
                        onReduceClick = { onReduceItem(item.id) }
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true, widthDp = 411)
@Composable
private fun SearchFilterPreview() {
    var query by remember { mutableStateOf("") }
    var selected by remember { mutableStateOf<String?>(null) }
    var mode by remember { mutableStateOf(MenuViewMode.LIST) }
    val categories = listOf("coffee", "matcha", "tea", "other")

    SearchFilter(
        searchQuery = query,
        onSearchChange = { query = it },
        categories = categories,
        selectedCategory = selected,
        onCategorySelect = { selected = it },
        trailingContent = { MenuViewToggle(viewMode = mode, onViewModeChange = { mode = it }) },
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
    )
}


@Preview(showBackground = true, widthDp = 411)
@Composable
private fun SearchFilterWithQueryPreview() {
    var mode by remember { mutableStateOf(MenuViewMode.GRID) }
    val categories = listOf("coffee", "matcha", "tea")

    SearchFilter(
        searchQuery = "latte",
        onSearchChange = {},
        categories = categories,
        selectedCategory = "coffee",
        onCategorySelect = {},
        trailingContent = { MenuViewToggle(viewMode = mode, onViewModeChange = { mode = it }) },
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
    )
}


@Preview(showBackground = true, widthDp = 411)
@Composable
private fun SearchFilterNoTrailingPreview() {
    var query by remember { mutableStateOf("") }
    var selected by remember { mutableStateOf<String?>(null) }
    val categories = listOf("food", "drinks", "dessert")

    SearchFilter(
        searchQuery = query,
        onSearchChange = { query = it },
        categories = categories,
        selectedCategory = selected,
        onCategorySelect = { selected = it },
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
    )
}
