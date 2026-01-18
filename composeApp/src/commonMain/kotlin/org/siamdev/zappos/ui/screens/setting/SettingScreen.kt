/*
 * MIT License
 *
 * Copyright (c) 2025 SiamDevTeam
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.siamdev.zappos.ui.screens.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.siamdev.zappos.theme.MapLikeColors

enum class SettingGroup(val title: String) {
    GENERAL("General"),
    APPEARANCE("Appearance & Personalization"),
    NETWORK("Network & Data"),
    ACCOUNT("Account")
}

enum class SettingInfo(
    val title: String,
    val subtitle: String? = null
) {
    ABOUT("About", "App information"),
    CURRENCY("Preferred Currency"),
    LANGUAGE("Display Language", "English"),

    APPEARANCE("Appearance"),
    LOCK("Lock Screen"),
    NOTIFICATION("Notifications"),

    DATA("Data and Storage"),
    RELAY("Relay"),
    NWC("NWC", "Nostr Wallet Connect"),

    SIGN_OUT("Sign out")
}

data class SettingItemData(
    val destination: SettingInfo,
    val group: SettingGroup,
    val icon: ImageVector,
    val iconBg: Color
)

/* ---------------------------------------------------
 * Screen
 * --------------------------------------------------- */

@Composable
fun SettingScreen(
    onNavigateBack: () -> Unit = {},
    onNavigateTo: (SettingInfo) -> Unit = {}
) {
    var search by remember { mutableStateOf("") }
    val items = remember { settingItems() }

    val filteredItems = remember(search) {
        if (search.isBlank()) items
        else items.filter {
            it.destination.title.contains(search, true) ||
                    it.destination.subtitle?.contains(search, true) == true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp)
    ) {

        Spacer(Modifier.height(48.dp))

        Header(onNavigateBack)

        Spacer(Modifier.height(16.dp))

        SearchBox(
            query = search,
            onQueryChange = { search = it }
        )

        Spacer(Modifier.height(24.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            if (search.isBlank()) {
                SettingGroup.entries.forEach { group ->
                    val groupItems = items.filter { it.group == group }

                    if (groupItems.isNotEmpty()) {
                        item { GroupHeader(group.title) }

                        items(
                            items = groupItems,
                            key = { it.destination }
                        ) { item ->
                            SettingItem(
                                item = item,
                                onClick = { onNavigateTo(item.destination) }
                            )
                        }

                        item { Spacer(Modifier.height(16.dp)) }
                    }
                }
            } else {
                items(
                    items = filteredItems,
                    key = { it.destination }
                ) { item ->
                    SettingItem(
                        item = item,
                        onClick = { onNavigateTo(item.destination) }
                    )
                }
            }
        }
    }
}

/* ---------------------------------------------------
 * Data source
 * --------------------------------------------------- */

private fun settingItems(): List<SettingItemData> = listOf(
    // General
    SettingItemData(
        SettingInfo.ABOUT,
        SettingGroup.GENERAL,
        Icons.Default.Info,
        MapLikeColors[0]
    ),
    SettingItemData(
        SettingInfo.CURRENCY,
        SettingGroup.GENERAL,
        Icons.Default.CurrencyExchange,
        MapLikeColors[20]
    ),
    SettingItemData(
        SettingInfo.LANGUAGE,
        SettingGroup.GENERAL,
        Icons.Default.Language,
        MapLikeColors[11]
    ),

    // Appearance
    SettingItemData(
        SettingInfo.APPEARANCE,
        SettingGroup.APPEARANCE,
        Icons.Default.Palette,
        MapLikeColors[10]
    ),
    SettingItemData(
        SettingInfo.LOCK,
        SettingGroup.APPEARANCE,
        Icons.Default.Lock,
        MapLikeColors[3]
    ),
    SettingItemData(
        SettingInfo.NOTIFICATION,
        SettingGroup.APPEARANCE,
        Icons.Default.Notifications,
        MapLikeColors[6]
    ),

    // Network
    SettingItemData(
        SettingInfo.DATA,
        SettingGroup.NETWORK,
        Icons.Default.Storage,
        MapLikeColors[14]
    ),
    SettingItemData(
        SettingInfo.RELAY,
        SettingGroup.NETWORK,
        Icons.Default.Cloud,
        MapLikeColors[18]
    ),
    SettingItemData(
        SettingInfo.NWC,
        SettingGroup.NETWORK,
        Icons.Default.Link,
        MapLikeColors[8]
    ),

    // Account
    SettingItemData(
        SettingInfo.SIGN_OUT,
        SettingGroup.ACCOUNT,
        Icons.AutoMirrored.Filled.Logout,
        MapLikeColors[2]
    )
)

/* ---------------------------------------------------
 * UI Components
 * --------------------------------------------------- */

@Composable
private fun Header(onNavigateBack: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = onNavigateBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
        }
        Spacer(Modifier.width(8.dp))
        Text(
            text = "Settings",
            fontSize = 28.sp,
            style = MaterialTheme.typography.headlineLarge
        )
    }
}

@Composable
private fun GroupHeader(title: String) {
    Text(
        text = title,
        modifier = Modifier.padding(vertical = 8.dp),
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
private fun SettingItem(
    item: SettingItemData,
    onClick: () -> Unit
) {
    val destination = item.destination

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(item.iconBg),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = destination.title,
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(Modifier.width(16.dp))

        Column(Modifier.weight(1f)) {
            Text(destination.title, style = MaterialTheme.typography.bodyLarge)
            destination.subtitle?.let {
                Text(
                    it,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun SearchBox(
    query: String,
    onQueryChange: (String) -> Unit
) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text("Search settings") },
        leadingIcon = { Icon(Icons.Default.Search, null) },
        singleLine = true,
        shape = RoundedCornerShape(10.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

/* ---------------------------------------------------
 * Preview
 * --------------------------------------------------- */

@Preview(showBackground = true, widthDp = 411, heightDp = 891)
@Composable
private fun SettingScreenPreview() {
    MaterialTheme {
        SettingScreen()
    }
}
