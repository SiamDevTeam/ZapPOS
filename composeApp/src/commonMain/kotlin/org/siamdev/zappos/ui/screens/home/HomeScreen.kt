/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CurrencyLira
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import org.siamdev.zappos.ui.components.WorkspaceHeader

@Composable
fun HomeScreen(
    onOpenDrawer: () -> Unit = {},
    onNavigateToMenu: () -> Unit = {}
) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        val isDesktop = maxWidth >= 750.dp

        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars)
        ) {
            WorkspaceHeader(title = "ZapPOS", onSegmentClick = onOpenDrawer)

            if (isDesktop) {
                DesktopHomeContent(onNavigateToMenu = onNavigateToMenu)
            } else {
                MobileHomeContent(onNavigateToMenu = onNavigateToMenu)
            }
        }
    }
}

@Composable
private fun MobileHomeContent(onNavigateToMenu: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        GreetingSection()

        // Stats row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.CurrencyLira,
                label = "Today Sales",
                value = "0.00",
                iconTint = YellowPrimary
            )
            StatCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.Receipt,
                label = "Orders",
                value = "0",
                iconTint = Color(0xFF4CAF50)
            )
        }

        QuickActionCard(onNavigateToMenu = onNavigateToMenu)

        Spacer(Modifier.height(8.dp))
    }
}

@Composable
private fun DesktopHomeContent(onNavigateToMenu: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Left column
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            GreetingSection()
            QuickActionCard(onNavigateToMenu = onNavigateToMenu)
        }

        // Right column — stats
        Column(
            modifier = Modifier
                .width(280.dp)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                modifier = Modifier.fillMaxWidth(),
                icon = Icons.Default.CurrencyLira,
                label = "Today Sales",
                value = "0.00",
                iconTint = YellowPrimary,
                large = true
            )
            StatCard(
                modifier = Modifier.fillMaxWidth(),
                icon = Icons.Default.Receipt,
                label = "Orders Today",
                value = "0",
                iconTint = Color(0xFF4CAF50),
                large = true
            )
        }
    }
}


@Composable
private fun GreetingSection() {
    Column {
        Text(
            text = "Good morning 👋",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = "Ready to take orders?",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun StatCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    label: String,
    value: String,
    iconTint: Color,
    large: Boolean = false
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(if (large) 20.dp else 16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(iconTint.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(Modifier.height(12.dp))

        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f),
            letterSpacing = 0.5.sp
        )

        Spacer(Modifier.height(4.dp))

        Text(
            text = value,
            style = if (large) MaterialTheme.typography.headlineMedium
            else MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun QuickActionCard(onNavigateToMenu: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(YellowPrimary)
            .clickable { onNavigateToMenu() }
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Restaurant,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(22.dp)
                )
            }
            Column {
                Text(
                    text = "New Order",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Open the menu to start",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Preview(showBackground = true, widthDp = 411, heightDp = 891)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}

@Preview(showBackground = true, widthDp = 1280, heightDp = 800)
@Composable
fun HomeScreenDesktopPreview() {
    HomeScreen()
}