/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.screens.setting.appearance

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.siamdev.zappos.data.source.local.FontItem
import org.siamdev.zappos.data.source.local.ThemeItem
import org.siamdev.zappos.theme.YellowPrimary
import org.siamdev.zappos.ui.screens.setting.SettingViewModel

@Composable
fun AppearanceSettingScreen(
    onNavigateBack: () -> Unit = {},
    viewModel: SettingViewModel
) {
    val themes by viewModel.themes.collectAsState()
    val activeTheme by viewModel.activeTheme.collectAsState()
    val fonts by viewModel.fonts.collectAsState()
    val activeFont by viewModel.activeFont.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(WindowInsets.systemBars)
    ){
        AppearanceHeader(onNavigateBack)

        if (isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = YellowPrimary)
            }
            return@Column
        }

        LazyColumn(
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                SectionLabel("Theme")
            }
            items(themes) { theme ->
                ThemeOptionCard(
                    theme = theme,
                    isActive = theme.id == activeTheme?.id,
                    onClick = { viewModel.selectTheme(theme.id) }
                )
            }

            item { Spacer(Modifier.height(8.dp)) }

            item {
                SectionLabel("Font Size")
            }
            items(fonts) { font ->
                FontOptionCard(
                    font = font,
                    isActive = font.id == activeFont?.id,
                    onClick = { viewModel.selectFont(font.id) }
                )
            }
        }
    }
}

@Composable
private fun AppearanceHeader(onBack: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBack,
            modifier = Modifier.clip(RoundedCornerShape(10.dp)).background(YellowPrimary.copy(alpha = 0.15f))
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = YellowPrimary, modifier = Modifier.size(18.dp))
        }
        Spacer(Modifier.width(12.dp))
        Text("Appearance", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text.uppercase(),
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(vertical = 4.dp)
    )
}

@Composable
private fun ThemeOptionCard(theme: ThemeItem, isActive: Boolean, onClick: () -> Unit) {
    val icon: ImageVector = when (theme.mode) {
        "LIGHT" -> Icons.Default.LightMode
        "DARK" -> Icons.Default.DarkMode
        else -> Icons.Default.SettingsBrightness
    }

    OptionCard(
        label = theme.name,
        subtitle = theme.mode.lowercase().replaceFirstChar { it.uppercase() },
        icon = icon,
        isActive = isActive,
        onClick = onClick
    )
}

@Composable
private fun FontOptionCard(font: FontItem, isActive: Boolean, onClick: () -> Unit) {
    OptionCard(
        label = font.name,
        subtitle = "${font.size.toInt()} sp",
        icon = Icons.Default.FormatSize,
        isActive = isActive,
        onClick = onClick
    )
}

@Composable
private fun OptionCard(
    label: String,
    subtitle: String,
    icon: ImageVector,
    isActive: Boolean,
    onClick: () -> Unit
) {
    val bg = if (isActive) YellowPrimary.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surface
    val iconBg = if (isActive) YellowPrimary else YellowPrimary.copy(alpha = 0.15f)
    val iconTint = if (isActive) Color.White else YellowPrimary

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(bg)
            .clickable(onClick = onClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(40.dp).clip(RoundedCornerShape(10.dp)).background(iconBg),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(20.dp))
        }
        Spacer(Modifier.width(16.dp))
        Column(Modifier.weight(1f)) {
            Text(label, style = MaterialTheme.typography.bodyLarge, fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Normal, color = MaterialTheme.colorScheme.onSurface)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        RadioButton(
            selected = isActive,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(selectedColor = YellowPrimary)
        )
    }
}