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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
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
        true,  false, 195,  300, "ZP-1086", 94,  6110.0),
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
        true,
        isRecommended = false,
        stockQty = 150,
        stockMax = 400,
        sku = "ZP-1096",
        soldThisWeek = 280,
        revenue7d = 8400.0
    ),
)

private enum class StockMoveType { IN, OUT }
private enum class HistoryFilter  { ALL, STOCK_IN, STOCK_OUT }

private data class StockRecord(
    val id: String,
    val date: String,
    val time: String,
    val qty: Int,
    val note: String,
    val operator: String,
    val type: StockMoveType
)

private fun fakeStockHistory(productId: String): List<StockRecord> {
    val seed = abs(productId.hashCode()) % 100
    return listOf(
        StockRecord("SK2026053100001", "31-05-2026", "13:37:05", 18 + seed % 5,  "Sold / order",     "Staff A", StockMoveType.OUT),
        StockRecord("SK2026053100002", "31-05-2026", "08:09:12", 70 + seed % 10, "Regular restock",  "Staff A", StockMoveType.IN),
        StockRecord("SK2026053000045", "30-05-2026", "20:11:45", 4  + seed % 3,  "Waste / spoilage", "Staff B", StockMoveType.OUT),
        StockRecord("SK2026052800102", "28-05-2026", "08:00:22", 70 + seed % 15, "Regular restock",  "Staff A", StockMoveType.IN),
        StockRecord("SK2026052800156", "28-05-2026", "13:48:10", 33 + seed % 10, "Sold / order",     "Staff B", StockMoveType.OUT),
        StockRecord("SK2026052700088", "27-05-2026", "17:05:33", 40 + seed % 20, "Emergency order",  "Staff B", StockMoveType.IN),
        StockRecord("SK2026051400012", "14-05-2026", "11:40:00", 40 + seed % 25, "Weekly delivery",  "Admin",   StockMoveType.IN),
    )
}

private fun todayMovements(history: List<StockRecord>): Pair<Int, Int> {
    val ins  = history.filter { it.type == StockMoveType.IN  && it.date == "31-05-2026" }.sumOf { it.qty }
    val outs = history.filter { it.type == StockMoveType.OUT && it.date == "31-05-2026" }.sumOf { it.qty }
    return ins to outs
}

enum class DetailTab { PRODUCT_DETAIL, MONITOR_STOCK }
private enum class SalesPeriod(val label: String, val days: Int) {
    D7("7d", 7),
    D30("30d", 30),
    D90("90d", 90)
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
    onEditProduct: (String) -> Unit = {},
    initialTab: DetailTab = DetailTab.PRODUCT_DETAIL
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
                onEdit = onEditProduct,
                initialTab = initialTab
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
                initialTab = initialTab
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
    initialTab: DetailTab = DetailTab.PRODUCT_DETAIL
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
                            onEdit = { onEdit(selected.id) },
                            initialTab = initialTab
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
    initialTab: DetailTab = DetailTab.PRODUCT_DETAIL
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
                    onEdit = { onEdit(current.id) },
                    initialTab = initialTab
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
    onEdit: () -> Unit,
    initialTab: DetailTab = DetailTab.PRODUCT_DETAIL
) {
    var selectedTab by remember(product.id) { mutableStateOf(initialTab) }

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
    TabItem("Monitor & Stock", Icons.Default.Inventory)
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
    var historyFilter by remember { mutableStateOf(HistoryFilter.ALL) }
    val filteredHistory = remember(history, historyFilter) {
        when (historyFilter) {
            HistoryFilter.ALL       -> history
            HistoryFilter.STOCK_IN  -> history.filter { it.type == StockMoveType.IN }
            HistoryFilter.STOCK_OUT -> history.filter { it.type == StockMoveType.OUT }
        }
    }

    BoxWithConstraints(Modifier.fillMaxSize()) {
        val isWide = maxWidth >= 580.dp
        if (isWide) {
            Row(Modifier.fillMaxSize()) {
                LazyColumn(
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item { MonitorProductHeader(product) }
                    item { StockOverviewCard(product, history) }
                    item { AdjustStockSection(product) }
                }
                VerticalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                Column(Modifier.weight(1f).fillMaxHeight()) {
                    StockHistoryHeader(
                        filter = historyFilter,
                        onFilter = { historyFilter = it },
                        filteredCount = filteredHistory.size,
                        totalCount = history.size,
                        modifier = Modifier.padding(horizontal = 16.dp).padding(top = 16.dp, bottom = 8.dp)
                    )
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surface),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                        verticalArrangement = Arrangement.spacedBy(0.0.dp)
                    ) {
                        itemsIndexed(filteredHistory, key = { _, it -> "${it.id}-${it.date}-${it.time}" }) { index, record ->
                            StockRecordRow(record = record, unit = product.unit, isEven = index % 2 == 1)
                        }
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Top part: Product Header, Overview, and Adjustment
                // We wrap these in a scrollable Column that takes at most half the screen
                // if history is long, ensuring the history list is always accessible.
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, fill = false)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        elevation = CardDefaults.cardElevation(0.dp),
                        border = CardDefaults.outlinedCardBorder()
                    ) {
                        Box(Modifier.padding(16.dp)) {
                            MonitorProductHeader(product)
                        }
                    }
                    StockOverviewCard(product, history)
                    AdjustStockSection(product)
                }

                StockHistoryHeader(
                    filter = historyFilter,
                    onFilter = { historyFilter = it },
                    filteredCount = filteredHistory.size,
                    totalCount = history.size,
                    modifier = Modifier.padding(top = 8.dp)
                )

                // History List: Takes remaining space and scrolls independently
                LazyColumn(
                    modifier = Modifier
                        .weight(1.5f)
                        .clip(RoundedCornerShape(12.dp))
                        .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surface),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(0.dp)
                ) {
                    itemsIndexed(filteredHistory, key = { _, it -> "${it.id}-${it.date}-${it.time}" }) { index, record ->
                        StockRecordRow(record = record, unit = product.unit, isEven = index % 2 == 1)
                    }
                }
            }
        }
    }
}

@Composable
private fun MonitorProductHeader(product: Product) {
    val catEntry = DefaultProductCategories.find { it.id == product.category }
    val categoryName = catEntry?.name ?: product.category
    val subName = catEntry?.subCategories?.find { it.id == product.subCategory }?.name
    val catIcon = catEntry?.icon ?: Icons.Default.ShoppingBag
    val breadcrumb = if (subName != null) "$categoryName · $subName" else categoryName

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(catIcon, contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.primary)
            }
            Spacer(Modifier.width(12.dp))
            Column {
                Text(breadcrumb,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(product.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface)
            }
        }
        if (product.sku.isNotBlank()) {
            Column(horizontalAlignment = Alignment.End) {
                Text("SKU ${product.sku}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("sold per ${product.unit}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun StockOverviewCard(product: Product, history: List<StockRecord>) {
    val hasStock = product.stockQty != null && product.stockMax != null
    val isLow = hasStock && product.stockQty <= (product.stockMax * 0.20f).toInt()
    val isOut = hasStock && product.stockQty == 0
    val (todayIn, todayOut) = remember(history) { todayMovements(history) }

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
                val (badgeColor, badgeLabel) = when {
                    isOut -> Color(0xFFF44336) to "Out of Stock"
                    isLow -> Color(0xFFFF9800) to "Low Stock"
                    else  -> Color(0xFF4CAF50) to "In Stock"
                }
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(badgeColor.copy(alpha = 0.12f))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(Modifier.size(6.dp).clip(RoundedCornerShape(50)).background(badgeColor))
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
                if (todayIn > 0 || todayOut > 0) {
                    Spacer(Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("Today",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                        if (todayIn > 0)
                            Text("+$todayIn in",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF4CAF50))
                        if (todayIn > 0 && todayOut > 0)
                            Text("·",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant)
                        if (todayOut > 0)
                            Text("-$todayOut out",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFFF44336))
                    }
                }
            } else {
                Text("No stock tracking for this product",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

private val adjustStockTabs = listOf(
    TabItem("Stock In",  Icons.Default.MoveToInbox),
    TabItem("Stock Out", Icons.Default.ShoppingCartCheckout)
)

@Composable
private fun AdjustStockSection(product: Product) {
    var mode   by remember { mutableStateOf(0) }
    var qty    by remember { mutableStateOf(0) }
    var reason by remember { mutableStateOf("") }

    var isEditing by remember { mutableStateOf(false) }
    var editText by remember { mutableStateOf(TextFieldValue("")) }
    val focusRequester = remember { FocusRequester() }

    fun confirmEdit() {
        qty = editText.text.toIntOrNull() ?: qty
        isEditing = false
    }

    fun adjustQty(delta: Int) {
        if (isEditing) {
            val current = editText.text.toIntOrNull() ?: 0
            val next = (current + delta).coerceAtLeast(0)
            val s = next.toString()
            editText = TextFieldValue(s, selection = TextRange(s.length))
        } else {
            qty = (qty + delta).coerceAtLeast(0)
        }
    }

    val modeLabel  = if (mode == 0) "Stock In" else "Stock Out"
    val newBalance = product.stockQty?.let {
        if (mode == 0) it + qty else (it - qty).coerceAtLeast(0)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(0.dp),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Adjust stock",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface)

            SegmentedTabBar(
                tabs = adjustStockTabs,
                selectedIndex = mode,
                onTabSelect = { mode = it }
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFF44336))
                        .clickable { adjustQty(-1) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Remove, contentDescription = "Decrease",
                        tint = Color.White, modifier = Modifier.size(22.dp))
                }
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (isEditing) {
                        BasicTextField(
                            value = editText,
                            onValueChange = { new ->
                                var filtered = new.text.filter { it.isDigit() }.take(5)
                                if (filtered.length > 1 && filtered.startsWith("0")) {
                                    filtered = filtered.dropWhile { it == '0' }.ifEmpty { "0" }
                                }
                                editText = new.copy(
                                    text = filtered,
                                    selection = TextRange(minOf(new.selection.start, filtered.length))
                                )
                            },
                            textStyle = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                textAlign = TextAlign.Center
                            ),
                            singleLine = true,
                            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(onDone = { confirmEdit() }),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp))
                                .padding(vertical = 4.dp)
                                .focusRequester(focusRequester)
                                .onKeyEvent { event ->
                                    when (event.key) {
                                        Key.Enter if event.type == KeyEventType.KeyDown -> {
                                            confirmEdit(); true
                                        }
                                        Key.Escape if event.type == KeyEventType.KeyDown -> {
                                            isEditing = false; true
                                        }
                                        else -> false
                                    }
                                }
                        )
                        LaunchedEffect(Unit) { focusRequester.requestFocus() }
                    } else {
                        Text(qty.toString(),
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .clickable {
                                    editText = TextFieldValue(qty.toString(), selection = TextRange(qty.toString().length))
                                    isEditing = true
                                }
                                .padding(vertical = 4.dp)
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF4CAF50))
                        .clickable { adjustQty(1) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Increase",
                        tint = Color.White, modifier = Modifier.size(22.dp))
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(1, 5, 10, 25).forEach { preset ->
                    OutlinedButton(
                        onClick = { adjustQty(preset) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 10.dp)
                    ) {
                        Text("+$preset",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Medium)
                    }
                }
            }

            Text("REASON",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
            OutlinedTextField(
                value = reason,
                onValueChange = { reason = it },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyMedium
            )

            if (newBalance != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("New balance",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("$newBalance ${product.unit}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface)
                }
            }

            MaterialButton(
                modifier = Modifier.fillMaxWidth().height(48.dp),
                text = "Confirm $modeLabel",
                iconStart = Icons.Default.Check,
                onClick = {}
            )
        }
    }
}

@Composable
private fun StockHistoryHeader(
    filter: HistoryFilter,
    onFilter: (HistoryFilter) -> Unit,
    filteredCount: Int = 0,
    totalCount: Int = 0,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("STOCK HISTORY",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
            if (totalCount > 0) {
                Text("$filteredCount/$totalCount",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            listOf(
                "All"       to HistoryFilter.ALL,
                "Stock In"  to HistoryFilter.STOCK_IN,
                "Stock Out" to HistoryFilter.STOCK_OUT
            ).forEach { (label, f) ->
                val selected = filter == f
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(
                            if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                            else Color.Transparent
                        )
                        .border(
                            1.dp,
                            if (selected) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.outlineVariant,
                            RoundedCornerShape(6.dp)
                        )
                        .clickable { onFilter(f) }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(label,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
                        color = if (selected) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

        }
    }
}

@Composable
private fun StockRecordRow(record: StockRecord, unit: String, isEven: Boolean = false) {
    val isIn       = record.type == StockMoveType.IN
    val amountColor = if (isIn) Color(0xFF4CAF50) else Color(0xFFF44336)
    val amountText  = if (isIn) "+${record.qty} $unit" else "-${record.qty} $unit"
    
    val icon = if (isIn) Icons.Default.MoveToInbox else Icons.Default.ShoppingCartCheckout
    val iconColor = if (isIn) Color(0xFF4CAF50) else Color(0xFFF44336)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(
                if (isEven) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.04f)
                else Color.Transparent
            )
            .padding(horizontal = 12.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(iconColor.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = iconColor
            )
        }
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(record.id,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface)
            
            Text("${record.date} ${record.time} · ${record.operator}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
            
            if (record.note.isNotBlank()) {
                Text(record.note,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    maxLines = 1)
            }
        }
        Text(amountText,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = amountColor)
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
            onEdit = {}
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
            initialTab = DetailTab.MONITOR_STOCK
        )
    }
}

