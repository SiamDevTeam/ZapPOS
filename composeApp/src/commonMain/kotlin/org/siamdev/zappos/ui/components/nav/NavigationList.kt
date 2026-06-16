/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.components.nav

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
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.automirrored.filled.ViewList
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
import org.siamdev.zappos.LocalNavigationVM
import org.siamdev.zappos.LocalSettingVM
import org.siamdev.zappos.navigation.Route
import org.siamdev.zappos.ui.components.sheet.SlideBottomSheet
import org.siamdev.zappos.ui.screens.setting.SettingSurfaceImpl
import org.siamdev.zappos.ui.screens.setting.SettingViewModel
import zappos.composeapp.generated.resources.Res
import zappos.composeapp.generated.resources.zappos_dark_horizontal_v2
import zappos.composeapp.generated.resources.zappos_white_horizontal_v2

private val DrawerWidth = 290.dp
private val TileSize = 36.dp
private val TileCorner = 10.dp
private val ItemCorner = 12.dp
private val IconSizeDp = 18.dp
private val BarWidth = 3.dp


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
    NavDef("home", "Home", Icons.Default.Home, NavSection.DASHBOARD, Route.Home),
    NavDef("menu", "Main Menu", Icons.Default.Apps, NavSection.SALES, Route.Menu),
    NavDef("counter", "Counter", Icons.Default.Store, NavSection.SALES, Route.Counter),
    NavDef("orders", "Orders", Icons.Default.Receipt, NavSection.SALES),
    NavDef("tx", "Transactions", Icons.Default.SwapHoriz, NavSection.SALES),
    NavDef("cust", "Customers", Icons.Default.People, NavSection.SALES),
    NavDef(
        "prd_ent",
        "Product Entry",
        Icons.Default.AddBox,
        NavSection.PRODUCTS,
        Route.ProductEntryMaster()
    ),
    NavDef(
        "prd_list",
        "Products List",
        Icons.AutoMirrored.Filled.ViewList,
        NavSection.PRODUCTS,
        Route.ProductList
    ),
    NavDef("cat", "Categories", Icons.Default.Category, NavSection.PRODUCTS),
    NavDef("brands", "Brands", Icons.Default.Style, NavSection.PRODUCTS),
    NavDef("units", "Units", Icons.Default.Straighten, NavSection.PRODUCTS),
    NavDef("stk_mv", "Stock Movement", Icons.Default.SwapVert, NavSection.INVENTORY),
    NavDef("stk_ct", "Stock Count", Icons.Default.ContentPaste, NavSection.INVENTORY),
    NavDef("supp", "Suppliers", Icons.Default.LocalShipping, NavSection.INVENTORY),
    NavDef("s_rpt", "Sales Reports", Icons.Default.BarChart, NavSection.REPORTS),
    NavDef("i_rpt", "Inventory Reports", Icons.Default.Assessment, NavSection.REPORTS),
    NavDef("pnl", "Profit & Loss", Icons.AutoMirrored.Filled.TrendingUp, NavSection.REPORTS),
    NavDef("settings", "Settings", Icons.Default.Settings, NavSection.SYSTEM, Route.Setting),
    NavDef("help", "Help & Support", Icons.AutoMirrored.Filled.Help, NavSection.SYSTEM),
)


private data class CatItem(
    val sectionId: String?,
    val label: String,
    val sub: String,
    val icon: ImageVector,
    val color: Color
)

private val CAT_OPTIONS = listOf(
    CatItem(null, "All Sections", "Show every menu", Icons.Default.Apps, Color(0xFFE6B33E)),
    CatItem("DASHBOARD", "Dashboard", "1 screen", Icons.Default.Home, Color(0xFF5C6BC0)),
    CatItem("SALES", "Sales", "4 screens", Icons.Default.Store, Color(0xFF43A047)),
    CatItem(
        "PRODUCTS",
        "Products",
        "5 screens",
        Icons.AutoMirrored.Filled.ViewList,
        Color(0xFFE6B33E)
    ),
    CatItem("INVENTORY", "Inventory", "3 screens", Icons.Default.SwapVert, Color(0xFF7E57C2)),
    CatItem("REPORTS", "Reports", "3 screens", Icons.Default.BarChart, Color(0xFF00897B)),
)

@Composable
fun NavigationList(
    isOpen: Boolean,
    currentRoute: Route? = null,
    onDismiss: () -> Unit,
    onNavigate: (Route) -> Unit = {}
) {
    val setting = LocalSettingVM.current
    val navVM = LocalNavigationVM.current
    val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f
    val accent = MaterialTheme.colorScheme.primary

    // Drawer-specific palette (the drawer has its own surface, darker than app bg)
    val drawerBg = if (isDark) Color(0xFF101013) else Color(0xFFFFFFFF)
    val surface = if (isDark) Color(0xFF1B1B20) else Color(0xFFF5F5F7)
    val text = if (isDark) Color.White else Color(0xFF1A1A1A)
    val muted = if (isDark) Color(0xFF8B8B93) else Color(0xFF8B8B99)
    val divider =
        if (isDark) Color.White.copy(alpha = 0.07f) else Color(0xFF1A1A1A).copy(alpha = 0.07f)

    val activeId = navVM.activeNavId
    val filter: NavSection? = remember(navVM.activeSectionId) {
        navVM.activeSectionId?.let { id -> NavSection.entries.find { it.name == id } }
    }
    var showCatSheet by remember { mutableStateOf(false) }

    // Sync highlighted item with the currently displayed screen
    LaunchedEffect(currentRoute, isOpen) {
        if (!isOpen) return@LaunchedEffect
        NAV_ITEMS
            .firstOrNull { it.route != null && currentRoute != null && it.route::class == currentRoute::class }
            ?.let { navVM.setActiveNav(it.id) }
    }

    val sections: List<NavSection> = remember(filter) {
        when (filter) {
            null -> NavSection.entries.filter { it != NavSection.SYSTEM }
            NavSection.DASHBOARD -> listOf(NavSection.DASHBOARD)
            else -> listOf(NavSection.DASHBOARD, filter)
        }
    }

    Box(Modifier.fillMaxSize()) {

        // Scrim
        AnimatedVisibility(
            visible = isOpen,
            enter = fadeIn(tween(200)),
            exit = fadeOut(tween(200))
        ) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .clickable { onDismiss() }
            )
        }

        // Drawer panel (slides in from the right)
        AnimatedVisibility(
            visible = isOpen,
            enter = slideInHorizontally(tween(280, easing = FastOutSlowInEasing)) { it },
            exit = slideOutHorizontally(tween(240, easing = FastOutSlowInEasing)) { it }
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

                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(bottom = 2.dp)
                    ) {
                        for (sec in sections) {
                            val secItems = NAV_ITEMS.filter { it.section == sec }
                            if (secItems.isEmpty()) continue

                            item(key = "hd_${sec.name}") { SecHeader(sec.label, muted) }

                            items(secItems, key = { it.id }) { nav ->
                                NavRow(
                                    nav = nav,
                                    isActive = nav.id == activeId,
                                    accent = accent,
                                    text = text,
                                    onClick = {
                                        navVM.setActiveNav(nav.id)
                                        if (nav.route != null) {
                                            onNavigate(nav.route)
                                            onDismiss()
                                        }
                                    }
                                )
                            }
                        }
                    }

                    HorizontalDivider(color = divider, thickness = 1.dp)

                    FooterSection(
                        isDark = isDark,
                        accent = accent,
                        surface = surface,
                        text = text,
                        muted = muted,
                        activeFilter = filter,
                        onToggle = { on ->
                            if (on) setting.selectTheme("theme-dark")
                            else setting.selectTheme("theme-light")
                        },
                        onOpenCatSheet = { showCatSheet = true },
                        onNavigate = onNavigate,
                        onDismiss = onDismiss
                    )
                }
            }
        }

        // Function Category bottom sheet
        SlideBottomSheet(
            show = showCatSheet,
            onDismiss = { showCatSheet = false },
            topContent = {
                Text(
                    text = "FUNCTION CATEGORY",
                    color = muted,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 1.5.sp,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                CAT_OPTIONS.forEach { opt ->
                    val selected = if (opt.sectionId == null) filter == null
                    else filter?.name == opt.sectionId
                    CatRow(
                        opt = opt,
                        isSelected = selected,
                        accent = accent,
                        text = text,
                        muted = muted,
                        onClick = {
                            navVM.setFilter(opt.sectionId)
                            showCatSheet = false
                        }
                    )
                }
            },
            bottomContent = {}
        )

    }
}

// Sub-composables
@Composable
private fun BrandHeader(muted: Color) {
    val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f
    val logoRes = if (isDark) Res.drawable.zappos_white_horizontal_v2
    else Res.drawable.zappos_dark_horizontal_v2

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Image(
            painter = painterResource(logoRes),
            contentDescription = "ZapPOS",
            modifier = Modifier.height(36.dp).wrapContentWidth(),
            contentScale = ContentScale.Fit
        )
        Spacer(Modifier.height(10.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Box(
                Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF4CAF50))
            )
            Text(
                text = "Point of Sale System",
                color = muted,
                fontSize = 10.sp,
                letterSpacing = 0.3.sp
            )
        }
    }
}

@Composable
private fun SecHeader(label: String, muted: Color) {
    Text(
        text = label,
        color = muted,
        fontSize = 10.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = 1.5.sp,
        modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 2.dp, end = 12.dp)
    )
}

@Composable
private fun NavRow(
    nav: NavDef,
    isActive: Boolean,
    accent: Color,
    text: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 12.dp, top = 2.dp, bottom = 2.dp)
            .clip(
                RoundedCornerShape(
                    topStart = ItemCorner,
                    topEnd = ItemCorner,
                    bottomStart = ItemCorner,
                    bottomEnd = ItemCorner
                )
            )
            .background(if (isActive) accent.copy(alpha = 0.13f) else Color.Transparent)
            .clickable { onClick() }
            .padding(vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

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
                imageVector = nav.icon,
                contentDescription = nav.label,
                tint = if (isActive) Color(0xFF1A1A1A) else accent,
                modifier = Modifier.size(IconSizeDp)
            )
        }
        Spacer(Modifier.width(12.dp))

        Text(
            text = nav.label,
            color = if (isActive) text else text.copy(alpha = 0.80f),
            fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Normal,
            fontSize = 14.sp,
            modifier = Modifier.weight(1f)
        )

        if (nav.chevron) {
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = text.copy(alpha = 0.35f),
                modifier = Modifier.size(16.dp)
            )
            Spacer(Modifier.width(2.dp))
        }
    }
}

@Composable
private fun FooterSection(
    isDark: Boolean,
    accent: Color,
    surface: Color,
    text: Color,
    muted: Color,
    activeFilter: NavSection?,
    onToggle: (Boolean) -> Unit,
    onOpenCatSheet: () -> Unit,
    onNavigate: (Route) -> Unit,
    onDismiss: () -> Unit
) {
    Column(Modifier.padding(horizontal = 12.dp, vertical = 6.dp)) {

        // Dark mode toggle
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 3.dp)
                .heightIn(min = 36.dp),
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
                    imageVector = if (isDark) Icons.Default.DarkMode else Icons.Default.LightMode,
                    contentDescription = "Theme",
                    tint = accent,
                    modifier = Modifier.size(IconSizeDp)
                )
            }
            Spacer(Modifier.width(12.dp))
            Text("Dark Mode", color = text, fontSize = 14.sp, modifier = Modifier.weight(1f))
            Switch(
                checked = isDark,
                onCheckedChange = onToggle,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = accent
                )
            )
        }

        // Settings
        FooterRow(
            icon = Icons.Default.Settings,
            iconBg = accent.copy(alpha = 0.12f),
            iconTint = accent,
            surface = Color.Transparent,
            onClick = { onNavigate(Route.Setting); onDismiss() }
        ) {
            Text("Settings", color = text, fontSize = 14.sp, modifier = Modifier.weight(1f))
        }

        // Help & Support
        FooterRow(
            icon = Icons.AutoMirrored.Filled.Help,
            iconBg = accent.copy(alpha = 0.12f),
            iconTint = accent,
            surface = Color.Transparent,
            onClick = {}
        ) {
            Text("Help & Support", color = text, fontSize = 14.sp, modifier = Modifier.weight(1f))
        }

        // Function Category
        FooterRow(
            icon = Icons.Default.GridView,
            iconBg = accent.copy(alpha = 0.12f),
            iconTint = accent,
            surface = surface,
            onClick = onOpenCatSheet
        ) {
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    "Function Category",
                    color = text,
                    fontWeight = FontWeight.Medium,
                    fontSize = 13.sp
                )
                Text(
                    text = activeFilter?.label ?: "All Sections",
                    color = accent,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Icon(Icons.Default.ChevronRight, null, tint = muted, modifier = Modifier.size(16.dp))
        }

        Spacer(Modifier.height(4.dp))
    }
}

@Composable
private fun FooterRow(
    icon: ImageVector,
    iconBg: Color,
    iconTint: Color,
    surface: Color,
    onClick: () -> Unit,
    content: @Composable RowScope.() -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(ItemCorner))
            .background(surface)
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .heightIn(min = 30.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(TileSize)
                .clip(RoundedCornerShape(TileCorner))
                .background(iconBg),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = iconTint, modifier = Modifier.size(IconSizeDp))
        }
        content()
    }
}

@Composable
private fun CatRow(
    opt: CatItem,
    isSelected: Boolean,
    accent: Color,
    text: Color,
    muted: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(if (isSelected) accent.copy(alpha = 0.10f) else Color.Transparent)
            .clickable { onClick() }
            .padding(horizontal = 4.dp, vertical = 8.dp)
            .heightIn(min = 44.dp),
        verticalAlignment = Alignment.CenterVertically,
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
            Text(
                opt.label,
                color = text,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                fontSize = 14.sp
            )
            Text(opt.sub, color = muted, fontSize = 11.sp)
        }
        if (isSelected) {
            Icon(Icons.Default.Check, null, tint = accent, modifier = Modifier.size(18.dp))
        }
    }
}



@Preview(name = "Drawer – Dark", showBackground = true, widthDp = 411, heightDp = 891)
@Composable
private fun DrawerDarkPreview() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        CompositionLocalProvider(
            LocalSettingVM provides SettingSurfaceImpl(SettingViewModel())
        ) {
            NavigationList(isOpen = true, onDismiss = {})
        }
    }
}

@Preview(name = "Drawer – Light", showBackground = true, widthDp = 411, heightDp = 891)
@Composable
private fun DrawerLightPreview() {
    MaterialTheme(colorScheme = lightColorScheme()) {
        CompositionLocalProvider(
            LocalSettingVM provides SettingSurfaceImpl(SettingViewModel())
        ) {
            NavigationList(isOpen = true, onDismiss = {})
        }
    }
}

@Preview(
    name = "Drawer – Active Item (Counter)",
    showBackground = true,
    widthDp = 411,
    heightDp = 891
)
@Composable
private fun DrawerActiveItemPreview() {
    MaterialTheme {
        CompositionLocalProvider(
            LocalSettingVM provides SettingSurfaceImpl(SettingViewModel())
        ) {
            NavigationList(
                isOpen = true,
                currentRoute = Route.Counter,
                onDismiss = {}
            )
        }
    }
}

@Preview(
    name = "Drawer – Active Item (Settings)",
    showBackground = true,
    widthDp = 411,
    heightDp = 891
)
@Composable
private fun DrawerActiveSettingsPreview() {
    MaterialTheme {
        CompositionLocalProvider(
            LocalSettingVM provides SettingSurfaceImpl(SettingViewModel())
        ) {
            NavigationList(
                isOpen = true,
                currentRoute = Route.Setting,
                onDismiss = {}
            )
        }
    }
}

@Preview(name = "Sub: Brand Header", showBackground = true, widthDp = 290)
@Composable
private fun BrandHeaderPreview() {
    MaterialTheme {
        Surface(color = Color(0xFF101013)) {
            BrandHeader(muted = Color(0xFF8B8B93))
        }
    }
}


@Preview(name = "Sub: Nav Row", showBackground = true, widthDp = 290)
@Composable
private fun NavRowPreview() {
    MaterialTheme {
        Surface(color = Color(0xFF101013)) {
            Column {
                NavRow(
                    nav = NAV_ITEMS[0],
                    isActive = true,
                    accent = Color(0xFF4CAF50),
                    text = Color.White,
                    onClick = {}
                )
                NavRow(
                    nav = NAV_ITEMS[1],
                    isActive = false,
                    accent = Color(0xFF2196F3),
                    text = Color.White,
                    onClick = {}
                )
            }
        }
    }
}

@Preview(name = "Sub: Footer Section", showBackground = true, widthDp = 290)
@Composable
private fun FooterSectionPreview() {
    MaterialTheme {
        Surface(color = Color(0xFF101013)) {
            FooterSection(
                isDark = true,
                accent = Color(0xFF4CAF50),
                surface = Color(0xFF1B1B20),
                text = Color.White,
                muted = Color(0xFF8B8B93),
                activeFilter = NavSection.SALES,
                onToggle = {},
                onOpenCatSheet = {},
                onNavigate = {},
                onDismiss = {}
            )
        }
    }
}

