/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.components.product

import org.siamdev.zappos.ui.components.menu.MenuItemsContent
import org.siamdev.zappos.ui.components.common.ViewModeToggle
import org.siamdev.zappos.ui.components.menu.MenuViewMode
import org.siamdev.zappos.ui.components.menu.SearchFilter

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ViewList
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.siamdev.zappos.LocalProductBrowserVM
import org.siamdev.zappos.ui.screens.sale.MenuItem

interface ProductBrowser {
    val browserState: StateFlow<BrowserState>
    fun addItem(id: Int)
    fun reduceItem(id: Int)
    fun reload()

    data class BrowserState(
        val items: List<MenuItem> = emptyList(),
        val isLoading: Boolean = false
    )
}

@Composable
fun ProductPanel(modifier: Modifier = Modifier) {
    val vm = LocalProductBrowserVM.current
    val browserState by vm.browserState.collectAsState()

    var viewMode by remember { mutableStateOf(MenuViewMode.LIST) }
    var searchQuery by remember { mutableStateOf("") }
    var categoryFilter by remember { mutableStateOf<String?>(null) }

    val categories by remember {
        derivedStateOf {
            browserState.items.map { it.category }.filter { it.isNotBlank() }.distinct().sorted()
        }
    }
    val filteredItems by remember {
        derivedStateOf {
            browserState.items.filter { item ->
                (categoryFilter == null || item.category == categoryFilter) &&
                        (searchQuery.isBlank() || item.name.contains(
                            searchQuery,
                            ignoreCase = true
                        ))
            }
        }
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, end = 16.dp, top = 16.dp)
        ) {
            SearchFilter(
                searchQuery = searchQuery,
                onSearchChange = { searchQuery = it },
                categories = categories,
                selectedCategory = categoryFilter,
                onCategorySelect = { categoryFilter = it },
                trailingContent = {
                    ViewModeToggle(
                        options = listOf(Icons.AutoMirrored.Filled.ViewList, Icons.Default.GridView),
                        selectedIndex = viewMode.ordinal,
                        onSelect = { viewMode = MenuViewMode.entries[it] }
                    )
                }
            )

            Box(modifier = Modifier.fillMaxSize()) {
                MenuItemsContent(
                    items = filteredItems,
                    viewMode = viewMode,
                    isLoading = browserState.isLoading,
                    onRefresh = { vm.reload() },
                    onAddItem = { vm.addItem(it) },
                    onReduceItem = { vm.reduceItem(it) },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

private val mockBrowserWithItems = object : ProductBrowser {
    override val browserState = MutableStateFlow(
        ProductBrowser.BrowserState(
            items = listOf(
                MenuItem(
                    1,
                    "",
                    "Mocha",
                    "70.00",
                    "17,500",
                    "coffee",
                    isRecommended = true,
                    count = 2u
                ),
                MenuItem(2, "", "Latte", "70.00", "17,500", "coffee"),
                MenuItem(
                    3,
                    "",
                    "Matcha Latte",
                    "100.00",
                    "26,000",
                    "matcha",
                    isRecommended = true,
                    count = 1u
                ),
                MenuItem(4, "", "Thai Tea", "60.00", "15,000", "tea"),
                MenuItem(5, "", "Espresso", "50.00", "12,500", "coffee"),
                MenuItem(6, "", "Americano", "60.00", "15,000", "coffee")
            ),
            isLoading = false
        )
    )

    override fun addItem(id: Int) {}
    override fun reduceItem(id: Int) {}
    override fun reload() {}
}

private val mockBrowserLoading = object : ProductBrowser {
    override val browserState = MutableStateFlow(ProductBrowser.BrowserState(isLoading = true))
    override fun addItem(id: Int) {}
    override fun reduceItem(id: Int) {}
    override fun reload() {}
}

@Preview(showBackground = true, widthDp = 700, heightDp = 500)
@Composable
fun ProductPanelListPreview() {
    MaterialTheme {
        CompositionLocalProvider(LocalProductBrowserVM provides mockBrowserWithItems) {
            ProductPanel(modifier = Modifier.fillMaxSize().padding(16.dp))
        }
    }
}

@Preview(showBackground = true, widthDp = 900, heightDp = 500)
@Composable
fun ProductPanelGridPreview() {
    MaterialTheme {
        CompositionLocalProvider(LocalProductBrowserVM provides mockBrowserWithItems) {
            ProductPanel(modifier = Modifier.fillMaxSize().padding(16.dp))
        }
    }
}

@Preview(showBackground = true, widthDp = 700, heightDp = 500)
@Composable
fun ProductPanelLoadingPreview() {
    MaterialTheme {
        CompositionLocalProvider(LocalProductBrowserVM provides mockBrowserLoading) {
            ProductPanel(modifier = Modifier.fillMaxSize().padding(16.dp))
        }
    }
}