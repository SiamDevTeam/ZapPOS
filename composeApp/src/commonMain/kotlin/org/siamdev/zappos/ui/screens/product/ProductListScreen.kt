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
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
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
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.tooling.preview.Preview
import org.siamdev.zappos.LocalSettingVM
import org.siamdev.zappos.ui.components.menu.DefaultProductCategories
import org.siamdev.zappos.ui.components.menu.SearchFilter
import org.siamdev.zappos.ui.components.common.InfoToggleCard
import org.siamdev.zappos.ui.components.common.MaterialButton
import org.siamdev.zappos.ui.components.common.SegmentedTabBar
import org.siamdev.zappos.ui.components.common.TabItem
import org.siamdev.zappos.ui.components.common.WorkspaceHeader
import org.siamdev.zappos.ui.screens.setting.SettingViewModel
import org.siamdev.zappos.utils.formatPrice
import kotlin.math.abs
import kotlin.math.sin


/** Represents a single menu/product item with pricing, stock, and sales metadata. */
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
    val stockQty: Int? = null,
    val stockMax: Int? = null,
    val sku: String = "",
    val soldThisWeek: Int = 0,
    val revenue7d: Double = 0.0
)

private const val SAMPLE_IMAGE = "https://images.pexels.com/photos/350478/pexels-photo-350478.jpeg"

private fun sampleProducts(): List<Product> = listOf(
    Product("1",  "Green Tea Latte",   SAMPLE_IMAGE, 85.0,  "cup",    "beverages", "tea_coffee",
        "Smooth and creamy matcha latte made with premium Japanese matcha and oat milk.",
        true,  true,  50,   200, "ZP-1085", 186, 15820.0),
    Product("2",  "Americano",         SAMPLE_IMAGE, 65.0,  "cup",    "beverages", "tea_coffee",
        "Classic espresso with hot water. Bold and rich flavor.",
        true,  false, null, null, "ZP-1086", 94,  6110.0),
    Product("3",  "Thai Milk Tea",     SAMPLE_IMAGE, 60.0,  "cup",    "beverages", "tea_coffee",
        "Traditional Thai tea with condensed milk and sweet cream.",
        true,  true,  30,   100, "ZP-1087", 120, 7200.0),
    Product("4",  "Red Bull",          SAMPLE_IMAGE, 35.0,  "bottle", "beverages", "energy",
        "Classic energy drink for a quick boost.",
        true,  false, 100,  300, "ZP-1088", 210, 7350.0),
    Product("5",  "Sprite",            SAMPLE_IMAGE, 20.0,  "bottle", "beverages", "soft_drinks",
        "Refreshing lemon-lime soft drink.",
        true,  false, 80,   250, "ZP-1089", 165, 3300.0),
    Product("6",  "Pad Thai",          SAMPLE_IMAGE, 120.0, "plate",  "food",      "noodles",
        "Stir-fried rice noodles with eggs, tofu, and bean sprouts.",
        true,  true,  null, null, "ZP-1090", 78,  9360.0),
    Product("7",  "Khao Man Gai",      SAMPLE_IMAGE, 80.0,  "plate",  "food",      "rice",
        "Thai steamed chicken rice served with a savory broth.",
        true,  false, null, null, "ZP-1091", 55,  4400.0),
    Product("8",  "Tom Yum Soup",      SAMPLE_IMAGE, 150.0, "bowl",   "food",      "soup",
        "Spicy and sour Thai soup with shrimp, mushrooms, and lemongrass.",
        true,  true,  null, null, "ZP-1092", 42,  6300.0),
    Product("9",  "Potato Chips",      SAMPLE_IMAGE, 25.0,  "pack",   "snack",     "chips",
        "Crispy potato chips with original flavor.",
        true,  false, 200,  500, "ZP-1093", 300, 7500.0),
    Product("10", "Mango Sticky Rice", SAMPLE_IMAGE, 95.0,  "plate",  "dessert",   "thai_dessert",
        "Sweet glutinous rice topped with fresh mango and coconut milk.",
        true,  true,  null, null, "ZP-1094", 63,  5985.0),
    Product("11", "Ice Cream Cone",    SAMPLE_IMAGE, 55.0,  "piece",  "dessert",   "ice_cream",
        "Soft serve vanilla ice cream in a waffle cone.",
        false, false, 0,    50,  "ZP-1095", 0,   0.0),
    Product("12", "Electrolyte Water", SAMPLE_IMAGE, 30.0,  "bottle", "beverages", "electrolyte",
        "Mineral-rich electrolyte water for hydration.",
        true,  false, 150,  400, "ZP-1096", 280, 8400.0),
)

/** A single stock-in event record used in the Monitor & Stock-In history list. */
private data class StockInRecord(
    val date: String,
    val qty: Int,
    val note: String,
    val operator: String
)

/** Generates deterministic mock stock-in history entries seeded by [productId]. */
private fun fakeStockHistory(productId: String): List<StockInRecord> {
    val seed = abs(productId.hashCode()) % 100
    val operators = listOf("Admin", "Manager", "Staff A", "Staff B")
    val notes = listOf("Regular restock", "Emergency order", "Weekly delivery", "Bulk purchase", "Supplier refill")
    return listOf(
        StockInRecord("2026-05-28", 50 + seed % 30, notes[seed % notes.size],          operators[seed % operators.size]),
        StockInRecord("2026-05-21", 30 + seed % 20, notes[(seed + 1) % notes.size],    operators[(seed + 1) % operators.size]),
        StockInRecord("2026-05-14", 40 + seed % 25, notes[(seed + 2) % notes.size],    operators[(seed + 2) % operators.size]),
        StockInRecord("2026-05-07", 25 + seed % 15, notes[(seed + 3) % notes.size],    operators[(seed + 3) % operators.size]),
        StockInRecord("2026-04-30", 60 + seed % 40, notes[(seed + 4) % notes.size],    operators[(seed + 4) % operators.size]),
    )
}

private enum class DetailTab { PRODUCT_DETAIL, MONITOR_STOCK }
private enum class SalesPeriod(val label: String, val days: Int) {
    D7("7d", 7), D30("30d", 30), D90("90d", 90)
}


private fun fakeSparkline(seed: Int, count: Int): List<Float> {
    val base = (seed * 7 % 40 + 30).toFloat()
    return List(count) { i ->
        val wave = sin((i + seed) * 0.8).toFloat() * 18f
        val trend = i * (2.5f + seed % 3)
        (base + wave + trend).coerceAtLeast(10f)
    }
}


/** Root screen for the product catalogue. Switches between [DesktopLayout] and [MobileLayout] based on window width. */
@Composable
fun ProductListScreen(
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
        val isDesktop = maxWidth >= 750.dp && maxHeight >= 500.dp
        if (isDesktop) {
            DesktopLayout(
                products = products,
                selectedId = selectedId,
                selected = selected,
                onSelect = { selectedId = it },
                onOpenDrawer = onOpenDrawer,
                onEdit = onEditProduct
            )
        } else {
            MobileLayout(
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
private fun DesktopLayout(
    products: List<Product>,
    selectedId: String?,
    selected: Product?,
    onSelect: (String) -> Unit,
    onOpenDrawer: () -> Unit,
    onEdit: (String) -> Unit
) {
    var splitRatio by remember { mutableStateOf(0.30f) }

    Column(Modifier.fillMaxSize()) {
        WorkspaceHeader(title = "Products List", onSegmentClick = onOpenDrawer)

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            val totalWidth = maxWidth

            Row(modifier = Modifier.fillMaxSize()) {
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
                        selectedId = selectedId,
                        onSelect = onSelect,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                // Draggable divider
                Box(
                    modifier = Modifier
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
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(5.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        repeat(5) {
                            Box(
                                Modifier
                                    .size(3.dp)
                                    .clip(RoundedCornerShape(50))
                                    .background(MaterialTheme.colorScheme.outlineVariant)
                            )
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp))
                ) {
                    if (selected != null) {
                        ProductDetailPanel(
                            product = selected,
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


/** Single-column layout that slides between the list and detail panel. Used on screens < 750 dp wide. */
@Composable
private fun MobileLayout(
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
    ) { current ->
        Column(Modifier.fillMaxSize()) {
            if (current != null) {
                WorkspaceHeader(
                    title = "Information",
                    onSegmentClick = onOpenDrawer,
                    onNavigateBack = onBack
                )
                ProductDetailPanel(
                    product = current,
                    onEdit = { onEdit(current.id) }
                )
            } else {
                WorkspaceHeader(
                    title = "Products List",
                    onSegmentClick = onOpenDrawer
                )
                ProductListPane(
                    products = products,
                    selectedId = selectedId,
                    onSelect = onSelect,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}


/** Scrollable list of products with search and category filter controls. */
@Composable
private fun ProductListPane(
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
        // Selected indicator bar
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
            // Thumbnail
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

/** Tabbed detail view for a selected product. Hosts [ProductDetailTabContent] and [MonitorStockTabContent]. */
@Composable
private fun ProductDetailPanel(
    product: Product,
    onEdit: () -> Unit
) {
    var selectedTab by remember(product.id) { mutableStateOf(DetailTab.PRODUCT_DETAIL) }

    Column(modifier = Modifier.fillMaxSize()) {
        DetailTabBar(
            selected = selectedTab,
            onSelect = { selectedTab = it },
            modifier = Modifier.padding(horizontal = 16.dp).padding(top = 8.dp, bottom = 8.dp)
        )

        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

        when (selectedTab) {
            DetailTab.PRODUCT_DETAIL -> ProductDetailTabContent(product = product, onEdit = onEdit)
            DetailTab.MONITOR_STOCK  -> MonitorStockTabContent(product = product)
        }
    }
}

/** Tab definitions for the product detail panel, mapped to [DetailTab] by ordinal. */
private val detailTabs = listOf(
    TabItem("Product Detail", Icons.Default.Info),
    TabItem("Monitor & Stock-In", Icons.Default.Inventory)
)

@Composable
private fun DetailTabBar(selected: DetailTab, onSelect: (DetailTab) -> Unit, modifier: Modifier = Modifier) {
    SegmentedTabBar(
        tabs = detailTabs,
        selectedIndex = selected.ordinal,
        onTabSelect = { onSelect(DetailTab.entries[it]) },
        modifier = modifier
    )
}

/** Scrollable detail view: header, sales chart, stock bar, item info, and status toggles. */
@Composable
private fun ProductDetailTabContent(product: Product, onEdit: () -> Unit) {
    val catEntry = DefaultProductCategories.find { it.id == product.category }
    val categoryName = catEntry?.name ?: product.category
    val subName = catEntry?.subCategories?.find { it.id == product.subCategory }?.name
    val catIcon = catEntry?.icon

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                ProductHeader(
                    product = product,
                    categoryName = categoryName,
                    subName = subName,
                    catIcon = catIcon ?: Icons.Default.ShoppingBag
                )
            }
            item { SalesChartCard(product = product) }
            if (product.stockQty != null && product.stockMax != null) {
                item {
                    StockCard(
                        stockQty = product.stockQty,
                        stockMax = product.stockMax,
                        unit = product.unit
                    )
                }
            }
            item { ItemInfoCard(product = product) }
            item {
                InfoToggleCard(
                    icon = Icons.Default.Visibility,
                    label = "Available",
                    subtitle = "Shown on the menu right now",
                    checked = product.isAvailable
                )
            }
            item {
                InfoToggleCard(
                    icon = Icons.Default.Star,
                    label = "Recommended",
                    subtitle = "Featured with a ★ on the menu",
                    checked = product.isRecommended
                )
            }
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            MaterialButton(
                modifier = Modifier.weight(1f).height(48.dp),
                text = "Edit Product",
                iconStart = Icons.Default.Edit,
                onClick = onEdit
            )
            IconButton(
                onClick = {},
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.errorContainer)
            ) {
                Icon(Icons.Default.Delete, contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(20.dp))
            }
        }
    }
}

/** Category icon + breadcrumb + product name displayed at the top of the detail tab. */
@Composable
private fun ProductHeader(
    product: Product,
    categoryName: String,
    subName: String?,
    catIcon: ImageVector
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(catIcon, contentDescription = null,
                modifier = Modifier.size(22.dp),
                tint = MaterialTheme.colorScheme.primary)
        }
        Spacer(Modifier.width(12.dp))
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(categoryName,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                if (subName != null) {
                    Icon(Icons.Default.ChevronRight, contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(subName,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            Text(product.name,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface)
        }
    }
}

/** Card showing revenue total and a sparkline chart for the selected period (7 d / 30 d / 90 d). */
@Composable
private fun SalesChartCard(product: Product) {
    var period by remember { mutableStateOf(SalesPeriod.D7) }
    val points = remember(product.id, period) { fakeSparkline(product.id.hashCode(), period.days) }
    val primaryColor = MaterialTheme.colorScheme.primary

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(0.dp),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text("SALES · LAST ${period.days} DAYS",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(Modifier.height(2.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("฿${product.revenue7d.formatPrice()}",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface)
                        Spacer(Modifier.width(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.AutoMirrored.Filled.TrendingUp, contentDescription = null,
                                tint = Color(0xFF4CAF50), modifier = Modifier.size(14.dp))
                            Text(" 9%",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF4CAF50),
                                fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
                // Period selector pills
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    SalesPeriod.entries.forEach { p ->
                        val active = p == period
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (active) primaryColor else Color.Transparent)
                                .clickable { period = p }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(p.label,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = if (active) FontWeight.Bold else FontWeight.Normal,
                                color = if (active) Color.Black else MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            Canvas(modifier = Modifier.fillMaxWidth().height(80.dp)) {
                drawSparkline(points, primaryColor)
            }
        }
    }
}

/** Draws a smooth cubic-bezier sparkline with a gradient fill onto the [DrawScope]. */
private fun DrawScope.drawSparkline(points: List<Float>, color: Color) {
    if (points.size < 2) return
    val minVal = points.min()
    val maxVal = points.max()
    val range  = (maxVal - minVal).coerceAtLeast(1f)
    val w = size.width
    val h = size.height
    val step = w / (points.size - 1)

    fun px(i: Int)  = i * step
    fun py(v: Float) = h - ((v - minVal) / range) * h * 0.85f - h * 0.05f

    val line = Path()
    val fill = Path()

    line.moveTo(px(0), py(points[0]))
    fill.moveTo(px(0), h)
    fill.lineTo(px(0), py(points[0]))

    for (i in 1 until points.size) {
        val cx = (px(i - 1) + px(i)) / 2f
        line.cubicTo(cx, py(points[i - 1]), cx, py(points[i]), px(i), py(points[i]))
        fill.cubicTo(cx, py(points[i - 1]), cx, py(points[i]), px(i), py(points[i]))
    }
    fill.lineTo(px(points.size - 1), h)
    fill.close()

    drawPath(fill, brush = Brush.verticalGradient(
        colors = listOf(color.copy(alpha = 0.28f), color.copy(alpha = 0.03f)),
        startY = 0f, endY = h
    ))
    drawPath(line, color = color, style = Stroke(width = 2.5f, cap = StrokeCap.Round))
}


/** Progress bar card showing current stock quantity vs. maximum capacity. */
@Composable
private fun StockCard(stockQty: Int, stockMax: Int, unit: String) {
    val progress = (stockQty.toFloat() / stockMax.toFloat()).coerceIn(0f, 1f)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(0.dp),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("STOCK",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("$stockQty / $stockMax $unit",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface)
            }
            Spacer(Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.outlineVariant
            )
        }
    }
}


/** Key-value card listing price, unit, SKU, stock count, and 7-day revenue. */
@Composable
private fun ItemInfoCard(product: Product) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(0.dp),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
            Text("ITEM INFO",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(8.dp))

            val rows = buildList {
                add("Price"            to "฿ ${product.price.formatPrice()}")
                add("Unit"             to product.unit)
                if (product.sku.isNotBlank())     add("SKU"            to product.sku)
                if (product.stockQty != null)     add("In stock"       to "${product.stockQty} ${product.unit}")
                if (product.soldThisWeek > 0)     add("Sold this week" to "${product.soldThisWeek}")
                if (product.revenue7d > 0)        add("Revenue (7d)"   to "฿ ${product.revenue7d.formatPrice()}")
            }

            rows.forEachIndexed { i, (label, value) ->
                if (i > 0) HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = MaterialTheme.colorScheme.outlineVariant
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(label,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface)
                    Text(value,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface)
                }
            }
        }
    }
}

@Composable
private fun MonitorStockTabContent(product: Product) {
    val history = remember(product.id) { fakeStockHistory(product.id) }
    val hasStock = product.stockQty != null && product.stockMax != null
    val isLow = hasStock && product.stockQty <= (product.stockMax * 0.20f).toInt()
    val isOut = hasStock && product.stockQty == 0

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Stock overview
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                elevation = CardDefaults.cardElevation(0.dp),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("STOCK OVERVIEW",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                        // status badge
                        val (badgeColor, badgeLabel) = when {
                            isOut  -> Color(0xFFF44336) to "Out of Stock"
                            isLow  -> Color(0xFFFF9800) to "Low Stock"
                            else   -> Color(0xFF4CAF50) to "In Stock"
                        }
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(badgeColor.copy(alpha = 0.12f))
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(RoundedCornerShape(50))
                                    .background(badgeColor)
                            )
                            Text(badgeLabel,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = badgeColor)
                        }
                    }

                    Spacer(Modifier.height(12.dp))

                    if (hasStock) {
                        val progress = (product.stockQty.toFloat() / product.stockMax.toFloat()).coerceIn(0f, 1f)
                        val barColor = when {
                            isOut -> Color(0xFFF44336)
                            isLow -> Color(0xFFFF9800)
                            else  -> MaterialTheme.colorScheme.primary
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(product.stockQty.toString(),
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface)
                                Text("current / ${product.stockMax} ${product.unit}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("${(progress * 100).toInt()}%",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = barColor)
                                Text("capacity",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                        Spacer(Modifier.height(10.dp))
                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier.fillMaxWidth().height(10.dp).clip(RoundedCornerShape(5.dp)),
                            color = barColor,
                            trackColor = MaterialTheme.colorScheme.outlineVariant
                        )
                    } else {
                        Text("No stock tracking for this product",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }

        // Low stock warning
        if (isLow || isOut) {
            item {
                val warnColor = if (isOut) Color(0xFFF44336) else Color(0xFFFF9800)
                val warnText  = if (isOut) "This product is out of stock. Please restock immediately."
                                else       "Stock is below 20% capacity. Consider restocking soon."
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(warnColor.copy(alpha = 0.10f))
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(Icons.Default.Warning, contentDescription = null,
                        tint = warnColor, modifier = Modifier.size(18.dp))
                    Text(warnText,
                        style = MaterialTheme.typography.bodySmall,
                        color = warnColor)
                }
            }
        }

        item {
            MaterialButton(
                modifier = Modifier.fillMaxWidth().height(48.dp),
                text = "Stock-In",
                iconStart = Icons.Default.AddBox,
                onClick = {}
            )
        }

        // Stock in/out history
        item {
            Text("STOCK HISTORY",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
        }

        items(history) { record ->
            StockInRecordRow(record = record, unit = product.unit)
        }
    }
}

@Composable
private fun StockInRecordRow(record: StockInRecord, unit: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.MoveToInbox, contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = MaterialTheme.colorScheme.primary)
        }
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(record.note,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface)
                Text("+${record.qty} $unit",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary)
            }
            Spacer(Modifier.height(2.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(record.operator,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(record.date,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
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
            onEdit = {}
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
            onEdit = {}
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
                .background(MaterialTheme.colorScheme.background)
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
                .background(MaterialTheme.colorScheme.background)
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

@Preview(showBackground = true, widthDp = 1280, heightDp = 800, name = "Desktop · With Selection")
@Composable
private fun DesktopWithSelectionPreview() {
    CompositionLocalProvider(LocalSettingVM provides SettingViewModel()) {
        val products = sampleProducts()
        val selected = products.first()
        DesktopLayout(
            products = products,
            selectedId = selected.id,
            selected = selected,
            onSelect = {},
            onOpenDrawer = {},
            onEdit = {}
        )
    }
}
