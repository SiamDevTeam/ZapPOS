/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.screens.product

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import androidx.compose.ui.tooling.preview.Preview
import org.siamdev.zappos.ui.components.menu.DefaultProductCategories
import org.siamdev.zappos.ui.components.menu.MenuItemCard
import org.siamdev.zappos.ui.components.menu.MenuViewMode
import org.siamdev.zappos.ui.components.menu.SearchFilter
import org.siamdev.zappos.ui.components.common.WorkspaceHeader
import org.siamdev.zappos.utils.formatPrice

data class Product(
    val id: String,
    val name: String,
    val imageUrl: String = "",
    val price: Double,
    val unit: String,
    val category: String,
    val subCategory: String? = null,
    val description: String = "",
    val isAvailable: Boolean = true,
    val isRecommended: Boolean = false,
    val stockQty: Int? = null
)

private fun sampleProducts(): List<Product> = listOf(
    Product("1", "Green Tea Latte", "", 85.0, "cup", "beverages", "tea_coffee",
        "Smooth and creamy matcha latte made with premium Japanese matcha and oat milk.", true, true, 50),
    Product("2", "Americano", "", 65.0, "cup", "beverages", "tea_coffee",
        "Classic espresso with hot water. Bold and rich flavor.", true, false, null),
    Product("3", "Thai Milk Tea", "", 60.0, "cup", "beverages", "tea_coffee",
        "Traditional Thai tea with condensed milk and sweet cream.", true, true, 30),
    Product("4", "Red Bull", "", 35.0, "bottle", "beverages", "energy",
        "Classic energy drink for a quick boost.", true, false, 100),
    Product("5", "Sprite", "", 20.0, "bottle", "beverages", "soft_drinks",
        "Refreshing lemon-lime soft drink.", true, false, 80),
    Product("6", "Pad Thai", "", 120.0, "plate", "food", "noodles",
        "Stir-fried rice noodles with eggs, tofu, and bean sprouts.", true, true, null),
    Product("7", "Khao Man Gai", "", 80.0, "plate", "food", "rice",
        "Thai steamed chicken rice served with a savory broth.", true, false, null),
    Product("8", "Tom Yum Soup", "", 150.0, "bowl", "food", "soup",
        "Spicy and sour Thai soup with shrimp, mushrooms, and lemongrass.", true, true, null),
    Product("9", "Potato Chips", "", 25.0, "pack", "snack", "chips",
        "Crispy potato chips with original flavor.", true, false, 200),
    Product("10", "Mango Sticky Rice", "", 95.0, "plate", "dessert", "thai_dessert",
        "Sweet glutinous rice topped with fresh mango and coconut milk.", true, true, null),
    Product("11", "Ice Cream Cone", "", 55.0, "piece", "dessert", "ice_cream",
        "Soft serve vanilla ice cream in a waffle cone.", false, false, 0),
    Product("12", "Electrolyte Water", "", 30.0, "bottle", "beverages", "electrolyte",
        "Mineral-rich electrolyte water for hydration.", true, false, 150),
)


@Composable
fun ProductListDetailScreen(
    onOpenDrawer: () -> Unit = {},
    onEditProduct: (String) -> Unit = {}
) {
    val products = remember { sampleProducts() }
    var selectedId by remember { mutableStateOf<String?>(null) }
    val selected = products.find { it.id == selectedId }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(WindowInsets.systemBars)
    ) {
        if (maxWidth >= 750.dp) {
            ExpandedLayout(
                products = products,
                selectedId = selectedId,
                selected = selected,
                onSelect = { selectedId = it },
                onOpenDrawer = onOpenDrawer,
                onEdit = onEditProduct
            )
        } else {
            CompactLayout(
                products = products,
                selectedId = selectedId,
                selected = selected,
                onSelect = { selectedId = it },
                onBack = { selectedId = null },
                onOpenDrawer = onOpenDrawer,
                onEdit = onEditProduct
            )
        }
    }
}


@Composable
private fun ExpandedLayout(
    products: List<Product>,
    selectedId: String?,
    selected: Product?,
    onSelect: (String) -> Unit,
    onOpenDrawer: () -> Unit,
    onEdit: (String) -> Unit
) {
    var splitRatio by remember { mutableStateOf(0.30f) }
    val minRatio = 0.20f
    val maxRatio = 0.55f

    Column(Modifier.fillMaxSize()) {
        WorkspaceHeader(title = "Products List", onSegmentClick = onOpenDrawer)

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
                // List pane
                Box(
                    modifier = Modifier
                        .width(totalWidth * splitRatio)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp))
                ) {
                    ProductListPane(
                        products = products,
                        onSelect = onSelect,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                // Draggable divider handle
                Box(
                    modifier = Modifier
                        .width(18.dp)
                        .fillMaxHeight()
                        .pointerHoverIcon(PointerIcon.Hand)
                        .pointerInput(totalWidth) {
                            val totalPx = totalWidth.toPx()
                            detectHorizontalDragGestures { change, dragAmount ->
                                change.consume()
                                splitRatio = (splitRatio + dragAmount / totalPx)
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

                // Detail pane
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp))
                ) {
                    if (selected != null) {
                        ProductDetailPane(
                            product = selected,
                            showBackButton = false,
                            onBack = {},
                            onEdit = { onEdit(selected.id) }
                        )
                    } else {
                        EmptyDetailState()
                    }
                }
            }
        }
    }
}


@Composable
private fun CompactLayout(
    products: List<Product>,
    selectedId: String?,
    selected: Product?,
    onSelect: (String) -> Unit,
    onBack: () -> Unit,
    onOpenDrawer: () -> Unit,
    onEdit: (String) -> Unit
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
        }
    ) { currentSelected ->
        if (currentSelected != null) {
            ProductDetailPane(
                product = currentSelected,
                showBackButton = true,
                onBack = onBack,
                onEdit = { onEdit(currentSelected.id) }
            )
        } else {
            Column(Modifier.fillMaxSize()) {
                WorkspaceHeader(title = "Products List", onSegmentClick = onOpenDrawer)
                ProductListPane(
                    products = products,
                    onSelect = onSelect,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}


@Composable
private fun ProductListPane(
    products: List<Product>,
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
                    Icon(
                        Icons.Default.SearchOff,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp),
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "No products found",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
            ) {
                items(filtered, key = { it.id }) { product ->
                    val categoryName = DefaultProductCategories
                        .find { it.id == product.category }?.name ?: product.category
                    val subName = DefaultProductCategories
                        .find { it.id == product.category }
                        ?.subCategories?.find { it.id == product.subCategory }?.name
                    MenuItemCard(
                        imageUrl = product.imageUrl,
                        name = product.name,
                        priceBaht = "฿${product.price.formatPrice()} / ${product.unit}",
                        category = if (subName != null) "$categoryName · $subName" else categoryName,
                        isRecommended = product.isRecommended,
                        isAvailable = product.isAvailable,
                        viewMode = MenuViewMode.LIST,
                        showChevron = true,
                        onClick = { onSelect(product.id) }
                    )
                }
            }
        }
    }
}


@Composable
private fun ProductDetailPane(
    product: Product,
    showBackButton: Boolean,
    onBack: () -> Unit,
    onEdit: () -> Unit
) {
    val categoryName = DefaultProductCategories.find { it.id == product.category }?.name ?: product.category
    val subName = DefaultProductCategories
        .find { it.id == product.category }
        ?.subCategories?.find { it.id == product.subCategory }?.name

    Column(modifier = Modifier.fillMaxSize()) {
        if (showBackButton) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f))
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                }
                Spacer(Modifier.width(12.dp))
                Text("Product Details",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f))
                IconButton(
                    onClick = onEdit,
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f))
                ) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit",
                        tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                }
            }
        }

        LazyColumn(contentPadding = PaddingValues(bottom = 32.dp)) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    if (product.imageUrl.isNotBlank()) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalPlatformContext.current)
                                .data(product.imageUrl)
                                .crossfade(true)
                                .build(),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(Icons.Default.BrokenImage, contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
                    }
                }
            }

            item {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CategoryChip(categoryName)
                        if (subName != null) {
                            Icon(Icons.Default.ChevronRight, contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant)
                            CategoryChip(subName)
                        }
                    }

                    Spacer(Modifier.height(12.dp))

                    Row(verticalAlignment = Alignment.Top) {
                        Text(
                            text = product.name,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.weight(1f)
                        )
                        if (product.isRecommended) {
                            Spacer(Modifier.width(8.dp))
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f))
                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Star, contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(12.dp))
                                Text("Featured",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }

                    Spacer(Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "฿${product.price.formatPrice()}",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = " / ${product.unit}",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }

                    if (product.description.isNotBlank()) {
                        Spacer(Modifier.height(16.dp))
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                        Spacer(Modifier.height(16.dp))
                        Text("DESCRIPTION",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(Modifier.height(6.dp))
                        Text(product.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface)
                    }

                    Spacer(Modifier.height(16.dp))
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    Spacer(Modifier.height(16.dp))

                    Text("STATUS",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(Modifier.height(10.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        StatusBadge(
                            label = if (product.isAvailable) "Available" else "Unavailable",
                            icon = if (product.isAvailable) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            active = product.isAvailable,
                            activeColor = Color(0xFF4CAF50)
                        )
                        if (product.stockQty != null) {
                            StatusBadge(
                                label = "Stock: ${product.stockQty}",
                                icon = Icons.Default.Inventory,
                                active = product.stockQty > 0,
                                activeColor = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    if (!showBackButton) {
                        Spacer(Modifier.height(28.dp))
                        Button(
                            onClick = onEdit,
                            modifier = Modifier.fillMaxWidth().height(48.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = Color.Black
                            )
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("Edit Product", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun EmptyDetailState() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.TouchApp, contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f))
            Spacer(Modifier.height(16.dp))
            Text("Select a product to view details",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.35f))
        }
    }
}


@Composable
private fun CategoryChip(text: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(text, style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun StatusBadge(
    label: String,
    icon: ImageVector,
    active: Boolean,
    activeColor: Color
) {
    val bg = if (active) activeColor.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surfaceVariant
    val tint = if (active) activeColor else MaterialTheme.colorScheme.onSurfaceVariant
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(bg)
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(14.dp))
        Text(label, style = MaterialTheme.typography.labelMedium, color = tint)
    }
}


@Preview(widthDp = 411, heightDp = 891)
@Composable
private fun MobilePreview() {
    ProductListDetailScreen()
}

@Preview(widthDp = 1280, heightDp = 800)
@Composable
private fun DesktopPreview() {
    ProductListDetailScreen()
}