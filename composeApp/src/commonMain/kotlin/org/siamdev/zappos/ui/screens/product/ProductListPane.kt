/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.screens.product

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.tooling.preview.Preview
import org.siamdev.zappos.LocalSettingVM
import org.siamdev.zappos.ui.components.menu.DefaultProductCategories
import org.siamdev.zappos.ui.components.menu.SearchFilter
import org.siamdev.zappos.ui.screens.setting.SettingViewModel
import org.siamdev.zappos.utils.formatPrice

/** Scrollable list of products with search and category filter controls. */
@Composable
internal fun ProductListPane(
    products: List<Product>,
    selectedId: String?,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var categoryFilter by remember { mutableStateOf<String?>(null) }

    val categoryIds = remember(products) {
        DefaultProductCategories.map { it.id }
            .filter { id -> products.any { p -> p.category == id } }
    }

    val filtered = remember(products, searchQuery, categoryFilter) {
        products.filter { p ->
            (categoryFilter == null || p.category == categoryFilter) &&
            (searchQuery.isBlank() || p.name.contains(searchQuery, ignoreCase = true))
        }
    }

    Column(modifier = modifier) {
        SearchFilter(
            searchQuery = searchQuery,
            onSearchChange = { searchQuery = it },
            categories = categoryIds,
            selectedCategory = categoryFilter,
            onCategorySelect = { categoryFilter = it },
            modifier = Modifier.padding(horizontal = 16.dp).padding(top = 12.dp)
        )

        if (filtered.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.SearchOff, contentDescription = null,
                        modifier = Modifier.size(40.dp),
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
                    Spacer(Modifier.height(8.dp))
                    Text("No products found",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        } else {
            LazyColumn(contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)) {
                items(filtered, key = { it.id }) { product ->
                    ProductListItem(
                        product = product,
                        isSelected = product.id == selectedId,
                        onClick = { onSelect(product.id) }
                    )
                }
            }
        }
    }
}

/** Single row in the product list showing thumbnail, name, category, and price. */
@Composable
private fun ProductListItem(product: Product, isSelected: Boolean, onClick: () -> Unit) {
    val catEntry = DefaultProductCategories.find { it.id == product.category }
    val categoryName = catEntry?.name ?: product.category
    val subName = catEntry?.subCategories?.find { it.id == product.subCategory }?.name
    val catLabel = if (subName != null) "$categoryName · $subName" else categoryName
    val catIcon = catEntry?.icon ?: Icons.Default.ShoppingBag
    val primary = MaterialTheme.colorScheme.primary

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(
                if (isSelected) primary.copy(alpha = 0.07f)
                else MaterialTheme.colorScheme.surfaceVariant
            )
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .width(3.dp)
                .height(72.dp)
                .background(
                    if (isSelected) primary else Color.Transparent,
                    RoundedCornerShape(topStart = 10.dp, bottomStart = 10.dp)
                )
        )

        Row(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 10.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (product.imageUrl.isNotBlank()) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalPlatformContext.current)
                        .data(product.imageUrl)
                        .crossfade(true)
                        .memoryCachePolicy(CachePolicy.ENABLED)
                        .build(),
                    contentDescription = null,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(primary.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(catIcon, contentDescription = null,
                        modifier = Modifier.size(24.dp), tint = primary)
                }
            }

            Spacer(Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(product.name,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1)
                    if (product.isRecommended) {
                        Spacer(Modifier.width(4.dp))
                        Icon(Icons.Default.Star, contentDescription = null,
                            tint = primary, modifier = Modifier.size(13.dp))
                    }
                }
                Text(catLabel,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1)
                Text("฿ ${product.price.formatPrice()} / ${product.unit}",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1)
            }

            Icon(Icons.Default.ChevronRight, contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                modifier = Modifier.size(16.dp))
        }
    }
}


@Preview(showBackground = true, widthDp = 360, heightDp = 700, name = "ProductListPane · Empty selection")
@Composable
private fun ProductListPanePreview() {
    CompositionLocalProvider(LocalSettingVM provides SettingViewModel()) {
        ProductListPane(
            products = sampleProducts(),
            selectedId = null,
            onSelect = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 700, name = "ProductListPane · With selection")
@Composable
private fun ProductListPaneSelectedPreview() {
    CompositionLocalProvider(LocalSettingVM provides SettingViewModel()) {
        val products = sampleProducts()
        ProductListPane(
            products = products,
            selectedId = products.first().id,
            onSelect = {}
        )
    }
}