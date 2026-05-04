/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.screens.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.siamdev.zappos.theme.YellowPrimary

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
    var search by remember { mutableStateOf("") }
    val allItems = remember { getSettingItems() }

    val filteredItems = remember(search) {
        if (search.isBlank()) allItems
        else allItems.filter { it.destination.title.contains(search, true) }
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (maxWidth >= 600.dp) {
            DesktopLayout(
                search,
                { search = it },
                allItems,
                filteredItems,
                onNavigateBack,
                onNavigateTo,
                onLogout
            )
        } else {
            MobileLayout(
                search,
                { search = it },
                allItems,
                filteredItems,
                onNavigateBack,
                onNavigateTo,
                onLogout
            )
        }
    }
}


@Composable
private fun MobileLayout(
    search: String, onSearchChange: (String) -> Unit,
    allItems: List<SettingItemData>, filteredItems: List<SettingItemData>,
    onBack: () -> Unit,
    onNavigate: (SettingInfo) -> Unit,
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.systemBars)
    ) {
        SettingHeader(onBack)
        SettingSearchBox(search, onSearchChange, Modifier.padding(horizontal = 20.dp))

        SettingList(
            search,
            allItems,
            filteredItems,
            onNavigate = {
                if (it == SettingInfo.SIGN_OUT) onLogout() else onNavigate(it)
            },
            contentPadding = PaddingValues(20.dp),
            showLogout = true
        )
    }
}

@Composable
private fun DesktopLayout(
    search: String, onSearchChange: (String) -> Unit,
    allItems: List<SettingItemData>, filteredItems: List<SettingItemData>,
    onBack: () -> Unit,
    onNavigate: (SettingInfo) -> Unit,
    onLogout: () -> Unit
) {
    val desktopFilteredItems = remember(filteredItems) {
        filteredItems.filter { it.destination != SettingInfo.SIGN_OUT }
    }
    val desktopAllItems = remember(allItems) {
        allItems.filter { it.destination != SettingInfo.SIGN_OUT }
    }

    Row(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .width(320.dp)
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.surface)
                .padding(24.dp)
        ) {
            SettingHeader(onBack)

            Spacer(Modifier.height(32.dp))

            Text("CURRENT ACCOUNT", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
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

            Spacer(Modifier.height(32.dp))

            Text("SYSTEM STATUS", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(16.dp))
            StatusItem("Version", "1.0")
            StatusItem("Database", "12.5 MB")
            StatusItem("Relays", "3 Connected")

            Spacer(Modifier.weight(1f))

            SettingItemRow(
                item = SettingItemData(
                    SettingInfo.SIGN_OUT,
                    SettingGroup.ACCOUNT,
                    Icons.AutoMirrored.Filled.Logout
                )
            ) {
                onLogout()
            }
        }

        Column(modifier = Modifier.weight(1f).padding(top = 20.dp)) {
            SettingSearchBox(search, onSearchChange, Modifier.padding(horizontal = 24.dp))

            SettingList(
                search,
                desktopAllItems,
                desktopFilteredItems,
                onNavigate = {
                    if (it == SettingInfo.SIGN_OUT) onLogout() else onNavigate(it)
                },
                contentPadding = PaddingValues(24.dp),
                showLogout = false
            )
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
private fun SettingHeader(onBack: () -> Unit, isCompact: Boolean = true) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBack, modifier = Modifier.clip(RoundedCornerShape(10.dp)).background(YellowPrimary.copy(alpha = 0.15f))) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = YellowPrimary, modifier = Modifier.size(18.dp))
        }
        Spacer(Modifier.width(12.dp))
        Text("Settings", color = MaterialTheme.colorScheme.onSurface, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
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
    LazyColumn(modifier = Modifier.fillMaxSize().padding(top = 10.dp), contentPadding = contentPadding, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        if (search.isBlank()) {
            SettingGroup.entries.forEach { group ->
                val groupItems = allItems.filter {
                    it.group == group && (showLogout || it.destination != SettingInfo.SIGN_OUT)
                }

                if (groupItems.isNotEmpty()) {
                    item {
                        Text(group.title.uppercase(), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(vertical = 8.dp))
                    }
                    items(groupItems) { item ->
                        SettingItemRow(item) { onNavigate(item.destination) }
                    }
                }
            }
        } else {
            items(filteredItems) { item ->
                SettingItemRow(item) { onNavigate(item.destination) }
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
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface).clickable(onClick = onClick).padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon Box - ตามรูปแบบ NavigationList ที่คุณต้องการ
        Box(
            modifier = Modifier.size(40.dp).clip(RoundedCornerShape(10.dp)).background(bgColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(item.icon, contentDescription = null, tint = tintColor, modifier = Modifier.size(20.dp))
        }

        Spacer(Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(item.destination.title, style = MaterialTheme.typography.bodyLarge, color = if (isLogout) tintColor else MaterialTheme.colorScheme.onSurface)
            item.destination.subtitle?.let {
                Text(it, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }

        if (!isLogout) {
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.outline)
        }
    }
}

@Composable
private fun SettingSearchBox(query: String, onQueryChange: (String) -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface).padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.Search, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(8.dp))
        BasicTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier.weight(1f),
            decorationBox = { inner ->
                if (query.isEmpty()) Text("Search settings...", color = MaterialTheme.colorScheme.onSurfaceVariant)
                inner()
            }
        )
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
        SettingScreen()
    }
}

@Preview(showBackground = true, widthDp = 1280, heightDp = 800)
@Composable
private fun SettingScreenDesktopPreview() {
    MaterialTheme {
        SettingScreen()
    }
}