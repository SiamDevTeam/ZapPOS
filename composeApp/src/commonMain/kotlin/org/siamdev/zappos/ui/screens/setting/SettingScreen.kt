/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.screens.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import org.siamdev.zappos.LocalSettingVM
import androidx.compose.runtime.CompositionLocalProvider
import io.ktor.client.request.invoke
import org.siamdev.zappos.theme.YellowPrimary
import org.siamdev.zappos.ui.components.SearchFilter
import org.siamdev.zappos.ui.components.WorkspaceHeader

enum class SettingGroup(val title: String) {
    GENERAL("General"),
    APPEARANCE("Appearance"),
    NETWORK("Network & Data"),
    ACCOUNT("Account")
}

enum class SettingInfo(val title: String, val subtitle: String? = null) {
    ABOUT("About", "App information"), CURRENCY("Preferred Currency"),
    LANGUAGE("Display Language", "English"), APPEARANCE("Appearance"),
    LOCK("Lock Screen"), NOTIFICATION("Notifications"),
    DATA("Data and Storage"), RELAY("Relay"),
    NWC("NWC", "Nostr Wallet Connect"),
    ACCOUNT_KEYS("Account Keys", "Private key & mnemonic"),
    SIGN_OUT("Sign out")
}

data class SettingItemData(val destination: SettingInfo, val group: SettingGroup, val icon: ImageVector)

@Composable
fun SettingScreen(
    onNavigateBack: () -> Unit = {},
    onNavigateTo: (SettingInfo) -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val vm = LocalSettingVM.current
    val writeError by vm.writeError.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(writeError) {
        val err = writeError ?: return@LaunchedEffect
        snackbarHostState.showSnackbar(err.message ?: "Operation failed")
        vm.clearError()
    }

    var search by remember { mutableStateOf("") }
    val allItems = remember { getSettingItems() }
    val filteredItems = remember(search) {
        if (search.isBlank()) allItems
        else allItems.filter { it.destination.title.contains(search, true) }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { _ ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .windowInsetsPadding(WindowInsets.systemBars)
        ) {
            WorkspaceHeader(title = "Settings", onNavigateBack = onNavigateBack)

            BoxWithConstraints(modifier = Modifier.weight(1f)) {
                if (maxWidth >= 600.dp) {
                    DesktopLayout(search, { search = it }, allItems, filteredItems, onNavigateTo, onLogout)
                } else {
                    MobileLayout(search, { search = it }, allItems, filteredItems, onNavigateTo, onLogout)
                }
            }
        }
    }
}


@Composable
private fun MobileLayout(
    search: String, onSearchChange: (String) -> Unit,
    allItems: List<SettingItemData>, filteredItems: List<SettingItemData>,
    onNavigate: (SettingInfo) -> Unit,
    onLogout: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        SearchFilter(
            searchQuery = search,
            onSearchChange = onSearchChange,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
        )
        SettingList(
            search = search,
            allItems = allItems,
            filteredItems = filteredItems,
            onNavigate = { if (it == SettingInfo.SIGN_OUT) onLogout() else onNavigate(it) },
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 20.dp),
            showLogout = true
        )
    }
}

@Composable
private fun DesktopLayout(
    search: String, onSearchChange: (String) -> Unit,
    allItems: List<SettingItemData>, filteredItems: List<SettingItemData>,
    onNavigate: (SettingInfo) -> Unit,
    onLogout: () -> Unit
) {
    val desktopFilteredItems = remember(filteredItems) {
        filteredItems.filter { it.destination != SettingInfo.SIGN_OUT }
    }
    val desktopAllItems = remember(allItems) {
        allItems.filter { it.destination != SettingInfo.SIGN_OUT }
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Left sidebar panel
        Box(
            modifier = Modifier
                .width(280.dp)
                .fillMaxHeight()
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surface)
                .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                Text(
                    "CURRENT ACCOUNT",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(12.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(YellowPrimary.copy(alpha = 0.05f))
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(YellowPrimary)
                    )
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text("User Name", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                        Text("nostr:npub1...", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                    }
                }

                Spacer(Modifier.height(28.dp))

                Text(
                    "SYSTEM STATUS",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(12.dp))
                StatusItem("Version", "1.0")
                StatusItem("Database", "12.5 MB")
                StatusItem("Relays", "3 Connected")

                Spacer(Modifier.weight(1f))

                SettingItemRow(
                    item = SettingItemData(SettingInfo.SIGN_OUT, SettingGroup.ACCOUNT, Icons.AutoMirrored.Filled.Logout),
                    onClick = onLogout
                )
            }
        }

        // Right content panel
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surface)
                .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp))
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                SearchFilter(
                    searchQuery = search,
                    onSearchChange = onSearchChange,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                )
                SettingList(
                    search = search,
                    allItems = desktopAllItems,
                    filteredItems = desktopFilteredItems,
                    onNavigate = { if (it == SettingInfo.SIGN_OUT) onLogout() else onNavigate(it) },
                    contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 20.dp),
                    showLogout = false
                )
            }
        }
    }
}

@Composable
private fun StatusItem(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
    }
}

@Composable
private fun SettingList(
    search: String,
    allItems: List<SettingItemData>,
    filteredItems: List<SettingItemData>,
    onNavigate: (SettingInfo) -> Unit,
    contentPadding: PaddingValues,
    showLogout: Boolean
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (search.isBlank()) {
            SettingGroup.entries.forEach { group ->
                val groupItems = allItems.filter {
                    it.group == group && (showLogout || it.destination != SettingInfo.SIGN_OUT)
                }
                if (groupItems.isNotEmpty()) {
                    item {
                        Text(
                            group.title.uppercase(),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                        )
                    }
                    items(groupItems) { item ->
                        SettingItemRow(item, onClick = { onNavigate(item.destination) })
                    }
                }
            }
        } else {
            items(filteredItems) { item ->
                SettingItemRow(item, onClick = { onNavigate(item.destination) })
            }
        }
    }
}

@Composable
private fun SettingItemRow(item: SettingItemData, onClick: () -> Unit) {
    val isLogout = item.destination == SettingInfo.SIGN_OUT
    val tintColor = if (isLogout) Color(0xFFE53935) else YellowPrimary
    val bgColor = if (isLogout) Color(0xFFFFEBEE) else YellowPrimary.copy(alpha = 0.15f)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable(onClick = onClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(40.dp).clip(RoundedCornerShape(10.dp)).background(bgColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(item.icon, contentDescription = null, tint = tintColor, modifier = Modifier.size(20.dp))
        }

        Spacer(Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                item.destination.title,
                style = MaterialTheme.typography.bodyLarge,
                color = if (isLogout) tintColor else MaterialTheme.colorScheme.onSurface
            )
            item.destination.subtitle?.let {
                Text(it, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }

        if (!isLogout) {
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.outline)
        }
    }
}

private fun getSettingItems() = listOf(
    SettingItemData(SettingInfo.ABOUT, SettingGroup.GENERAL, Icons.Default.Info),
    SettingItemData(SettingInfo.CURRENCY, SettingGroup.GENERAL, Icons.Default.CurrencyExchange),
    SettingItemData(SettingInfo.LANGUAGE, SettingGroup.GENERAL, Icons.Default.Language),
    SettingItemData(SettingInfo.APPEARANCE, SettingGroup.APPEARANCE, Icons.Default.Palette),
    SettingItemData(SettingInfo.LOCK, SettingGroup.APPEARANCE, Icons.Default.Lock),
    SettingItemData(SettingInfo.NOTIFICATION, SettingGroup.APPEARANCE, Icons.Default.Notifications),
    SettingItemData(SettingInfo.DATA, SettingGroup.NETWORK, Icons.Default.Storage),
    SettingItemData(SettingInfo.RELAY, SettingGroup.NETWORK, Icons.Default.Cloud),
    SettingItemData(SettingInfo.NWC, SettingGroup.NETWORK, Icons.Default.Link),
    SettingItemData(SettingInfo.ACCOUNT_KEYS, SettingGroup.ACCOUNT, Icons.Default.Key),
    SettingItemData(SettingInfo.SIGN_OUT, SettingGroup.ACCOUNT, Icons.AutoMirrored.Filled.Logout)
)


@Preview(showBackground = true, widthDp = 411, heightDp = 891)
@Composable
private fun SettingScreenMobilePreview() {
    MaterialTheme {
        CompositionLocalProvider(LocalSettingVM provides SettingViewModel()) {
            SettingScreen()
        }
    }
}

@Preview(showBackground = true, widthDp = 1280, heightDp = 800)
@Composable
private fun SettingScreenDesktopPreview() {
    MaterialTheme {
        CompositionLocalProvider(LocalSettingVM provides SettingViewModel()) {
            SettingScreen()
        }
    }
}