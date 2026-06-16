/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.components.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import org.siamdev.zappos.LocalSettingVM
import org.siamdev.zappos.navigation.Route
import org.siamdev.zappos.ui.components.sheet.SlideBottomSheet
import org.siamdev.zappos.ui.screens.setting.SettingSurfaceImpl
import org.siamdev.zappos.ui.screens.setting.SettingViewModel
import zappos.composeapp.generated.resources.Res
import zappos.composeapp.generated.resources.zappos_dark_horizontal_v2
import zappos.composeapp.generated.resources.zappos_white_horizontal_v2

// ── Layout constants (change here to retheme) ─────────────────────────────────
private val DrawerWidth = 290.dp
private val TileSize    = 36.dp
private val TileCorner  = 10.dp
private val ItemCorner  = 12.dp
private val IconSizeDp  = 18.dp
private val BarWidth    = 3.dp

// ── Domain ────────────────────────────────────────────────────────────────────

private enum class NavSection(val label: String) {
    DASHBOARD("DASHBOARD"),
    SALES("SALES"),
    PRODUCTS("PRODUCTS"),
    INVENTORY("INVENTORY"),
    REPORTS("REPORTS"),
    SYSTEM("SYSTEM")
}

private data class NavDef(
    val id: String,
    val label: String,
    val icon: ImageVector,
    val section: NavSection,
    val route: Route? = null,
    val chevron: Boolean = false
)

private val NAV_ITEMS = listOf(
    NavDef("home",     "Home",             Icons.Default.Home,          NavSection.DASHBOARD, Route.Home),
    NavDef("counter",  "Counter",          Icons.Default.Store,         NavSection.SALES,     Route.Counter),
    NavDef("orders",   "Orders",           Icons.Default.Receipt,       NavSection.SALES),
    NavDef("tx",       "Transactions",     Icons.Default.SwapHoriz,     NavSection.SALES),
    NavDef("cust",     "Customers",        Icons.Default.People,        NavSection.SALES),
    NavDef("prd_ent",  "Product Entry",    Icons.Default.AddBox,        NavSection.PRODUCTS,  Route.ProductEntryMaster()),
    NavDef("prd_list", "Products List",    Icons.Default.ViewList,      NavSection.PRODUCTS,  Route.ProductList),
    NavDef("cat",      "Categories",       Icons.Default.Category,      NavSection.PRODUCTS),
    NavDef("brands",   "Brands",           Icons.Default.Style,         NavSection.PRODUCTS),
    NavDef("units",    "Units",            Icons.Default.Straighten,    NavSection.PRODUCTS),
    NavDef("stk_mv",   "Stock Movement",   Icons.Default.SwapVert,      NavSection.INVENTORY),
    NavDef("stk_ct",   "Stock Count",      Icons.Default.ContentPaste,  NavSection.INVENTORY),
    NavDef("supp",     "Suppliers",        Icons.Default.LocalShipping, NavSection.INVENTORY),
    NavDef("s_rpt",    "Sales Reports",    Icons.Default.BarChart,      NavSection.REPORTS),
    NavDef("i_rpt",    "Inventory Reports",Icons.Default.Assessment,    NavSection.REPORTS),
    NavDef("pnl",      "Profit & Loss",    Icons.Default.TrendingUp,    NavSection.REPORTS),
    NavDef("settings", "Settings",         Icons.Default.Settings,      NavSection.SYSTEM,    Route.Setting),
    NavDef("fn_cat",   "Function Category",Icons.Default.GridView,      NavSection.SYSTEM,    chevron = true),
    NavDef("help",     "Help & Support",   Icons.Default.Help,          NavSection.SYSTEM),
)

private data class StoreItem(val id: String, val name: String, val sub: String, val color: Color)

private val STORES = listOf(
    StoreItem("downtown",  "Downtown Flagship", "Sana · Cashier", Color(0xFF4CAF50)),
    StoreItem("riverside", "Riverside Kiosk",   "Cashier 2",      Color(0xFF2196F3)),
    StoreItem("warehouse", "Warehouse Outlet",  "Rick & Moroe",   Color(0xFFFF9800)),
)

private data class CatItem(
    val sectionId: String?,
    val label: String,
    val sub: String,
    val icon: ImageVector,
    val color: Color
)

private val CAT_OPTIONS = listOf(
    CatItem(null,        "All Sections", "Show every menu", Icons.Default.Apps,     Color(0xFFE6B33E)),
    CatItem("DASHBOARD", "Dashboard",   "1 screen",        Icons.Default.Home,     Color(0xFF5C6BC0)),
    CatItem("SALES",     "Sales",       "4 screens",       Icons.Default.Store,    Color(0xFF43A047)),
    CatItem("PRODUCTS",  "Products",    "5 screens",       Icons.Default.ViewList, Color(0xFFE6B33E)),
    CatItem("INVENTORY", "Inventory",   "3 screens",       Icons.Default.SwapVert, Color(0xFF7E57C2)),
    CatItem("REPORTS",   "Reports",     "3 screens",       Icons.Default.BarChart, Color(0xFF00897B)),
)

// ── Public API ────────────────────────────────────────────────────────────────

@Composable
fun NavigationList(
    isOpen: Boolean,
    currentRoute: Route? = null,
    onDismiss: () -> Unit,
    onNavigate: (Route) -> Unit = {}
) {
    val setting = LocalSettingVM.current
    val isDark  = MaterialTheme.colorScheme.background.luminance() < 0.5f
    val accent  = MaterialTheme.colorScheme.primary

    // Drawer-specific palette (the drawer has its own surface, darker than app bg)
    val drawerBg  = if (isDark) Color(0xFF101013) else Color(0xFFFFFFFF)
    val surface   = if (isDark) Color(0xFF1B1B20) else Color(0xFFF5F5F7)
    val text      = if (isDark) Color.White       else Color(0xFF1A1A1A)
    val muted     = if (isDark) Color(0xFF8B8B93) else Color(0xFF8B8B99)
    val divider   = if (isDark) Color.White.copy(alpha = 0.07f) else Color(0xFF1A1A1A).copy(alpha = 0.07f)

    var activeId       by remember { mutableStateOf("home") }
    var filter         by remember { mutableStateOf<NavSection?>(null) }
    var showCatSheet   by remember { mutableStateOf(false) }
    var showStoreSheet by remember { mutableStateOf(false) }
    var activeStore    by remember { mutableStateOf(STORES[0]) }

    // Sync highlighted item with the currently displayed screen
    LaunchedEffect(currentRoute, isOpen) {
        if (!isOpen) return@LaunchedEffect
        NAV_ITEMS
            .firstOrNull { it.route != null && currentRoute != null && it.route::class == currentRoute::class }
            ?.let { activeId = it.id }
    }

    val sections: List<NavSection> = remember(filter) {
        if (filter == null) NavSection.entries else listOf(filter!!, NavSection.SYSTEM)
    }

    Box(Modifier.fillMaxSize()) {

        // ── Scrim ──────────────────────────────────────────────────────────────
        AnimatedVisibility(
            visible = isOpen,
            enter = fadeIn(tween(200)),
            exit  = fadeOut(tween(200))
        ) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .clickable { onDismiss() }
            )
        }

        // ── Drawer panel (slides in from the right) ────────────────────────────
        AnimatedVisibility(
            visible = isOpen,
            enter = slideInHorizontally(tween(280, easing = FastOutSlowInEasing)) { it },
            exit  = slideOutHorizontally(tween(240, easing = FastOutSlowInEasing)) { it }
        ) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.CenterEnd) {
                Column(
                    Modifier
                        .fillMaxHeight()
                        .width(DrawerWidth)
                        .background(drawerBg)
                        .windowInsetsPadding(WindowInsets.statusBars)
                        .windowInsetsPadding(WindowInsets.navigationBars)
                ) {
                    BrandHeader(muted)
                    HorizontalDivider(color = divider, thickness = 1.dp)

                    if (filter != null) {
                        FilterRow(
                            sectionName = filter!!.label,
                            accent      = accent,
                            surface     = surface,
                            text        = text,
                            onClear     = { filter = null }
                        )
                    }

                    LazyColumn(
                        modifier        = Modifier.weight(1f),
                        contentPadding  = PaddingValues(bottom = 8.dp)
                    ) {
                        for (sec in sections) {
                            val secItems = NAV_ITEMS.filter { it.section == sec }
                            if (secItems.isEmpty()) continue

                            item(key = "hd_${sec.name}") { SecHeader(sec.label, muted) }

                            items(secItems, key = { it.id }) { nav ->
                                NavRow(
                                    nav      = nav,
                                    isActive = nav.id == activeId,
                                    accent   = accent,
                                    text     = text,
                                    onClick  = {
                                        when {
                                            nav.id == "fn_cat"  -> showCatSheet = true
                                            nav.route != null   -> {
                                                activeId = nav.id
                                                onNavigate(nav.route)
                                                onDismiss()
                                            }
                                            else                -> activeId = nav.id
                                        }
                                    }
                                )
                            }
                        }
                    }

                    HorizontalDivider(color = divider, thickness = 1.dp)

                    FooterSection(
                        isDark      = isDark,
                        accent      = accent,
                        surface     = surface,
                        text        = text,
                        muted       = muted,
                        store       = activeStore,
                        onToggle    = { on ->
                            if (on) setting.selectTheme("theme-dark")
                            else    setting.selectTheme("theme-light")
                        },
                        onOpenStore = { showStoreSheet = true }
                    )
                }
            }
        }

        // ── Function Category bottom sheet ─────────────────────────────────────
        SlideBottomSheet(
            show      = showCatSheet,
            onDismiss = { showCatSheet = false },
            topContent = {
                Text(
                    text         = "FUNCTION CATEGORY",
                    color        = muted,
                    fontSize     = 10.sp,
                    fontWeight   = FontWeight.Medium,
                    letterSpacing = 1.5.sp,
                    modifier     = Modifier.padding(bottom = 12.dp)
                )
                CAT_OPTIONS.forEach { opt ->
                    val selected = if (opt.sectionId == null) filter == null
                                   else filter?.name == opt.sectionId
                    CatRow(
                        opt        = opt,
                        isSelected = selected,
                        accent     = accent,
                        text       = text,
                        muted      = muted,
                        onClick    = {
                            filter = if (opt.sectionId == null) null
                                     else NavSection.entries.find { it.name == opt.sectionId }
                            showCatSheet = false
                        }
                    )
                }
            },
            bottomContent = {}
        )

        // ── Store Switcher bottom sheet ────────────────────────────────────────
        SlideBottomSheet(
            show      = showStoreSheet,
            onDismiss = { showStoreSheet = false },
            topContent = {
                Text(
                    text          = "SWITCH STORE",
                    color         = muted,
                    fontSize      = 10.sp,
                    fontWeight    = FontWeight.Medium,
                    letterSpacing = 1.5.sp,
                    modifier      = Modifier.padding(bottom = 12.dp)
                )
                STORES.forEach { store ->
                    StoreRow(
                        store      = store,
                        isSelected = store.id == activeStore.id,
                        accent     = accent,
                        text       = text,
                        muted      = muted,
                        onClick    = {
                            activeStore    = store
                            showStoreSheet = false
                        }
                    )
                }
            },
            bottomContent = {}
        )
    }
}

// ── Sub-composables ────────────────────────────────────────────────────────────

@Composable
private fun BrandHeader(muted: Color) {
    val isDark  = MaterialTheme.colorScheme.background.luminance() < 0.5f
    val logoRes = if (isDark) Res.drawable.zappos_white_horizontal_v2
                  else        Res.drawable.zappos_dark_horizontal_v2

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Image(
            painter            = painterResource(logoRes),
            contentDescription = "ZapPOS",
            modifier           = Modifier.height(36.dp).wrapContentWidth(),
            contentScale       = ContentScale.Fit
        )
        Spacer(Modifier.height(10.dp))
        Row(
            verticalAlignment     = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Box(
                Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF4CAF50))
            )
            Text(
                text          = "Point of Sale System",
                color         = muted,
                fontSize      = 10.sp,
                letterSpacing = 0.3.sp
            )
        }
    }
}

@Composable
private fun FilterRow(
    sectionName : String,
    accent      : Color,
    surface     : Color,
    text        : Color,
    onClear     : () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(surface)
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text("VIEWING", color = accent, fontSize = 10.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 1.sp)
        Box(
            Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(accent.copy(alpha = 0.15f))
                .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
            Text(sectionName, color = accent, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
        }
        Spacer(Modifier.weight(1f))
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .clickable { onClear() }
                .padding(horizontal = 6.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text("Show all", color = text.copy(alpha = 0.55f), fontSize = 10.sp)
            Icon(Icons.Default.Close, contentDescription = null, tint = text.copy(alpha = 0.55f), modifier = Modifier.size(12.dp))
        }
    }
}

@Composable
private fun SecHeader(label: String, muted: Color) {
    Text(
        text          = label,
        color         = muted,
        fontSize      = 10.sp,
        fontWeight    = FontWeight.Medium,
        letterSpacing = 1.5.sp,
        modifier      = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 2.dp, end = 12.dp)
    )
}

@Composable
private fun NavRow(
    nav      : NavDef,
    isActive : Boolean,
    accent   : Color,
    text     : Color,
    onClick  : () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 12.dp, top = 2.dp, bottom = 2.dp)
            .clip(RoundedCornerShape(topEnd = ItemCorner, bottomEnd = ItemCorner))
            .background(if (isActive) accent.copy(alpha = 0.13f) else Color.Transparent)
            .clickable { onClick() }
            .padding(vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Accent bar flush with the drawer's inner (left) edge
        Box(
            Modifier
                .width(BarWidth)
                .height(26.dp)
                .clip(RoundedCornerShape(topEnd = 4.dp, bottomEnd = 4.dp))
                .background(if (isActive) accent else Color.Transparent)
        )
        Spacer(Modifier.width(10.dp))

        // Icon tile
        Box(
            modifier = Modifier
                .size(TileSize)
                .clip(RoundedCornerShape(TileCorner))
                .background(if (isActive) accent else accent.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector     = nav.icon,
                contentDescription = nav.label,
                tint            = if (isActive) Color(0xFF1A1A1A) else accent,
                modifier        = Modifier.size(IconSizeDp)
            )
        }
        Spacer(Modifier.width(12.dp))

        Text(
            text       = nav.label,
            color      = if (isActive) text else text.copy(alpha = 0.80f),
            fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Normal,
            fontSize   = 14.sp,
            modifier   = Modifier.weight(1f)
        )

        if (nav.chevron) {
            Icon(
                imageVector     = Icons.Default.ChevronRight,
                contentDescription = null,
                tint            = text.copy(alpha = 0.35f),
                modifier        = Modifier.size(16.dp)
            )
            Spacer(Modifier.width(2.dp))
        }
    }
}

@Composable
private fun FooterSection(
    isDark      : Boolean,
    accent      : Color,
    surface     : Color,
    text        : Color,
    muted       : Color,
    store       : StoreItem,
    onToggle    : (Boolean) -> Unit,
    onOpenStore : () -> Unit
) {
    Column(Modifier.padding(horizontal = 12.dp, vertical = 10.dp)) {
        // Dark mode toggle row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 2.dp)
                .heightIn(min = 44.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(TileSize)
                    .clip(RoundedCornerShape(TileCorner))
                    .background(accent.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector     = if (isDark) Icons.Default.DarkMode else Icons.Default.LightMode,
                    contentDescription = "Theme",
                    tint            = accent,
                    modifier        = Modifier.size(IconSizeDp)
                )
            }
            Spacer(Modifier.width(12.dp))
            Text("Dark Mode", color = text, fontSize = 14.sp, modifier = Modifier.weight(1f))
            Switch(
                checked        = isDark,
                onCheckedChange = onToggle,
                colors         = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = accent
                )
            )
        }

        Spacer(Modifier.height(8.dp))

        // Store card
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(ItemCorner))
                .background(surface)
                .clickable { onOpenStore() }
                .padding(horizontal = 12.dp, vertical = 10.dp)
                .heightIn(min = 44.dp),
            verticalAlignment     = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(TileSize)
                    .clip(RoundedCornerShape(TileCorner))
                    .background(store.color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector     = Icons.Default.Storefront,
                    contentDescription = null,
                    tint            = store.color,
                    modifier        = Modifier.size(IconSizeDp)
                )
            }
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(store.name, color = text, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                Text(store.sub,  color = muted, fontSize = 11.sp)
            }
            Icon(
                imageVector     = Icons.Default.ChevronRight,
                contentDescription = "Switch store",
                tint            = muted,
                modifier        = Modifier.size(16.dp)
            )
        }

        Spacer(Modifier.height(6.dp))
    }
}

@Composable
private fun ColumnScope.CatRow(
    opt        : CatItem,
    isSelected : Boolean,
    accent     : Color,
    text       : Color,
    muted      : Color,
    onClick    : () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(if (isSelected) accent.copy(alpha = 0.10f) else Color.Transparent)
            .clickable { onClick() }
            .padding(horizontal = 4.dp, vertical = 8.dp)
            .heightIn(min = 44.dp),
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(TileSize)
                .clip(RoundedCornerShape(TileCorner))
                .background(opt.color.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(opt.icon, null, tint = opt.color, modifier = Modifier.size(IconSizeDp))
        }
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(opt.label, color = text, fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal, fontSize = 14.sp)
            Text(opt.sub,   color = muted, fontSize = 11.sp)
        }
        if (isSelected) {
            Icon(Icons.Default.Check, null, tint = accent, modifier = Modifier.size(18.dp))
        }
    }
}

@Composable
private fun ColumnScope.StoreRow(
    store      : StoreItem,
    isSelected : Boolean,
    accent     : Color,
    text       : Color,
    muted      : Color,
    onClick    : () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(if (isSelected) accent.copy(alpha = 0.10f) else Color.Transparent)
            .clickable { onClick() }
            .padding(horizontal = 4.dp, vertical = 8.dp)
            .heightIn(min = 44.dp),
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(TileSize)
                .clip(RoundedCornerShape(TileCorner))
                .background(store.color.copy(alpha = if (isSelected) 0.22f else 0.14f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Storefront, null, tint = store.color, modifier = Modifier.size(IconSizeDp))
        }
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(store.name, color = text, fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal, fontSize = 14.sp)
            Text(store.sub,  color = muted, fontSize = 11.sp)
        }
        if (isSelected) {
            Icon(Icons.Default.Check, null, tint = accent, modifier = Modifier.size(18.dp))
        }
    }
}

// ── Preview ────────────────────────────────────────────────────────────────────

@Preview(name = "Drawer – Dark", showBackground = true, widthDp = 411, heightDp = 891)
@Composable
private fun DrawerDarkPreview() {
    MaterialTheme {
        androidx.compose.runtime.CompositionLocalProvider(
            LocalSettingVM provides SettingSurfaceImpl(SettingViewModel())
        ) {
            NavigationList(isOpen = true, onDismiss = {})
        }
    }
}