/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.screens.setting.appearance

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.abs
import kotlin.math.roundToInt
import org.siamdev.zappos.data.source.local.FontItem
import org.siamdev.zappos.data.source.local.ThemeItem
import org.siamdev.zappos.theme.MapLikeColors
import org.siamdev.zappos.theme.YellowPrimary
import org.siamdev.zappos.theme.colorFromHex
import org.siamdev.zappos.theme.toHex
import org.siamdev.zappos.ui.components.common.WorkspaceHeader
import org.siamdev.zappos.ui.screens.setting.SettingViewModel

private val AllAccentColors: List<Color> = listOf(YellowPrimary) + MapLikeColors

@Composable
fun AppearanceSettingScreen(
    onNavigateBack: () -> Unit = {},
    viewModel: SettingViewModel
) {
    val themes by viewModel.themes.collectAsState()
    val activeTheme by viewModel.activeTheme.collectAsState()
    val fonts by viewModel.fonts.collectAsState()
    val activeFont by viewModel.activeFont.collectAsState()
    val accentColorHex by viewModel.accentColor.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val activeAccentColor = accentColorHex?.let { colorFromHex(it) } ?: YellowPrimary

    // Tracks the font being previewed while the slider is dragged
    var previewFontOverride by remember { mutableStateOf<FontItem?>(null) }

    // Clear the preview override once the actual setting catches up from the ViewModel
    LaunchedEffect(activeFont) {
        if (activeFont?.id == previewFontOverride?.id) {
            previewFontOverride = null
        }
    }

    val previewFont = previewFontOverride ?: activeFont

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(WindowInsets.systemBars)
    ) {
        WorkspaceHeader(title = "Appearance", onNavigateBack = onNavigateBack)

        if (isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
            return@Column
        }

        BoxWithConstraints(modifier = Modifier.weight(1f)) {
            if (maxWidth >= 750.dp) {
                DesktopLayout(
                    themes = themes, activeTheme = activeTheme,
                    fonts = fonts, activeFont = activeFont, previewFont = previewFont,
                    activeAccentColor = activeAccentColor,
                    onSelectTheme = { viewModel.selectTheme(it) },
                    onSelectFont = { viewModel.selectFont(it) },
                    onSelectColor = { viewModel.selectAccentColor(it.toHex()) },
                    onPreviewFont = { previewFontOverride = it }
                )
            } else {
                MobileLayout(
                    themes = themes, activeTheme = activeTheme,
                    fonts = fonts, activeFont = activeFont, previewFont = previewFont,
                    activeAccentColor = activeAccentColor,
                    onSelectTheme = { viewModel.selectTheme(it) },
                    onSelectFont = { viewModel.selectFont(it) },
                    onSelectColor = { viewModel.selectAccentColor(it.toHex()) },
                    onPreviewFont = { previewFontOverride = it }
                )
            }
        }
    }
}


@Composable
private fun MobileLayout(
    themes: List<ThemeItem>, activeTheme: ThemeItem?,
    fonts: List<FontItem>, activeFont: FontItem?, previewFont: FontItem?,
    activeAccentColor: Color,
    onSelectTheme: (String) -> Unit, onSelectFont: (String) -> Unit,
    onSelectColor: (Color) -> Unit, onPreviewFont: (FontItem?) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            PreviewCard(
                activeAccentColor = activeAccentColor,
                previewFont = previewFont,
                compact = true
            )
        }

        item { SettingsCard(themes, activeTheme, fonts, activeFont, activeAccentColor,
            onSelectTheme, onSelectFont, onSelectColor, onPreviewFont) }
    }
}

@Composable
private fun DesktopLayout(
    themes: List<ThemeItem>, activeTheme: ThemeItem?,
    fonts: List<FontItem>, activeFont: FontItem?, previewFont: FontItem?,
    activeAccentColor: Color,
    onSelectTheme: (String) -> Unit, onSelectFont: (String) -> Unit,
    onSelectColor: (Color) -> Unit, onPreviewFont: (FontItem?) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Left: all settings in a single scrollable card
        LazyColumn(
            modifier = Modifier
                .weight(0.55f)
                .fillMaxHeight(),
            contentPadding = PaddingValues(bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            item {
                SettingsCard(themes, activeTheme, fonts, activeFont, activeAccentColor,
                    onSelectTheme, onSelectFont, onSelectColor, onPreviewFont)
            }
        }

        // Right: sticky live preview
        Column(
            modifier = Modifier
                .weight(0.45f)
                .fillMaxHeight()
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surface)
                .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp))
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                "LIVE PREVIEW",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.35f),
                letterSpacing = 2.sp
            )
            PreviewCard(activeAccentColor = activeAccentColor, previewFont = previewFont)
        }
    }
}


@Composable
private fun SettingsCard(
    themes: List<ThemeItem>, activeTheme: ThemeItem?,
    fonts: List<FontItem>, activeFont: FontItem?,
    activeAccentColor: Color,
    onSelectTheme: (String) -> Unit, onSelectFont: (String) -> Unit,
    onSelectColor: (Color) -> Unit, onPreviewFont: (FontItem?) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp))
    ) {
        SettingSection(label = "ACCENT COLOR") {
            AccentColorPicker(activeColor = activeAccentColor, onSelect = onSelectColor)
        }

        SectionDivider()

        SettingSection(label = "THEME") {
            ThemeSegmentedControl(
                themes = themes,
                activeTheme = activeTheme,
                onSelect = onSelectTheme
            )
        }

        SectionDivider()

        SettingSection(label = "FONT SIZE") {
            FontSizeSlider(
                fonts = fonts,
                activeFont = activeFont,
                onPreviewFont = onPreviewFont,
                onSelect = { id ->
                    // Set override immediately to maintain visual position during save transition
                    onPreviewFont(fonts.find { it.id == id })
                    onSelectFont(id)
                }
            )
        }
    }
}

@Composable
private fun SettingSection(label: String, content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
            letterSpacing = 2.sp
        )
        content()
    }
}

@Composable
private fun SectionDivider() {
    HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f))
}


@Composable
private fun PreviewCard(
    activeAccentColor: Color,
    previewFont: FontItem?,
    modifier: Modifier = Modifier,
    compact: Boolean = false
) {
    val onAccent = if (activeAccentColor.luminance() > 0.35f) Color(0xFF1A1A1A) else Color.White
    val fontLabel = previewFont?.name ?: "Default"
    val fontSp = previewFont?.size?.toInt()?.let { "$it sp" } ?: ""

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(14.dp))
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Simulated app bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(activeAccentColor.copy(alpha = 0.1f))
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(26.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(activeAccentColor),
                contentAlignment = Alignment.Center
            ) {
                Text("ZP", color = onAccent, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
            }
            Text(
                "ZapPOS",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.weight(1f))
            Icon(
                Icons.Default.Notifications, null,
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                modifier = Modifier.size(18.dp)
            )
        }

        Column(
            modifier = Modifier.padding(
                horizontal = if (compact) 12.dp else 14.dp,
                vertical = if (compact) 10.dp else 14.dp
            ),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                "Point of Sale",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                "Ready to take orders and process payments.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.55f)
            )

            // Accent highlight row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Box(Modifier.size(5.dp).clip(CircleShape).background(activeAccentColor))
                Text(
                    "ACTIVE SESSION",
                    style = MaterialTheme.typography.labelSmall,
                    color = activeAccentColor,
                    letterSpacing = 0.8.sp
                )
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.07f))

            // Button previews
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(9.dp))
                        .background(activeAccentColor)
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Text("Checkout", style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold, color = onAccent)
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(9.dp))
                        .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.16f), RoundedCornerShape(9.dp))
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Text("Cancel", style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                }
            }

            // Font scale bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
                    .padding(horizontal = 10.dp, vertical = 7.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                listOf(10, 13, 16, 20).forEachIndexed { i, size ->
                    Text(
                        "Aa",
                        fontSize = size.sp,
                        color = if (i == 2) activeAccentColor
                                else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f + i * 0.1f),
                        fontWeight = if (i == 2) FontWeight.SemiBold else FontWeight.Normal
                    )
                }
                Spacer(Modifier.weight(1f))
                Column(horizontalAlignment = Alignment.End) {
                    Text(fontLabel, style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f))
                    if (fontSp.isNotEmpty()) {
                        Text(fontSp, style = MaterialTheme.typography.labelSmall,
                            color = activeAccentColor.copy(alpha = 0.7f),
                            fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}


@Composable
private fun AccentColorPicker(activeColor: Color, onSelect: (Color) -> Unit) {
    // Selected color header — shows which color is active
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier
                .size(26.dp)
                .clip(CircleShape)
                .background(activeColor)
                .border(1.5.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f), CircleShape)
        )
        Text(
            text = "#${activeColor.toHex().drop(2).uppercase()}",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.55f)
        )
    }

    Spacer(Modifier.height(6.dp))

    // Compact 6-per-row swatch grid
    val rows = AllAccentColors.chunked(6)
    Column(verticalArrangement = Arrangement.spacedBy(7.dp)) {
        rows.forEach { rowColors ->
            Row(horizontalArrangement = Arrangement.spacedBy(7.dp)) {
                rowColors.forEach { color ->
                    ColorSwatch(
                        color = color,
                        isSelected = color.toHex() == activeColor.toHex(),
                        onClick = { onSelect(color) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ColorSwatch(color: Color, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(if (isSelected) 38.dp else 34.dp)
            .clip(CircleShape)
            .background(color)
            .then(
                if (isSelected) Modifier.border(2.5.dp, MaterialTheme.colorScheme.onSurface, CircleShape)
                else Modifier.border(0.5.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f), CircleShape)
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (isSelected) {
            Icon(
                Icons.Default.Check, null,
                tint = if (color.luminance() > 0.35f) Color(0xFF1A1A1A) else Color.White,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
private fun ThemeSegmentedControl(
    themes: List<ThemeItem>,
    activeTheme: ThemeItem?,
    onSelect: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.background)
            .padding(3.dp),
        horizontalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        themes.forEach { theme ->
            val isActive = theme.id == activeTheme?.id
            val icon: ImageVector = when (theme.mode) {
                "LIGHT" -> Icons.Default.LightMode
                "DARK" -> Icons.Default.DarkMode
                else -> Icons.Default.SettingsBrightness
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        if (isActive) MaterialTheme.colorScheme.surface
                        else Color.Transparent
                    )
                    .clickable { onSelect(theme.id) }
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        icon, null,
                        tint = if (isActive) MaterialTheme.colorScheme.primary
                               else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.35f),
                        modifier = Modifier.size(15.dp)
                    )
                    Text(
                        theme.name,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Normal,
                        color = if (isActive) MaterialTheme.colorScheme.onSurface
                                else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.35f)
                    )
                }
            }
        }
    }
}


@Composable
private fun FontSizeSlider(
    fonts: List<FontItem>,
    activeFont: FontItem?,
    onPreviewFont: (FontItem?) -> Unit,
    onSelect: (String) -> Unit
) {
    if (fonts.isEmpty()) return

    val sortedFonts = remember(fonts) { fonts.sortedBy { it.size } }
    val minSp = sortedFonts.first().size.toFloat()
    val maxSp = sortedFonts.last().size.toFloat()

    // Use the active font ID as a key to reset position when the source of truth changes
    var sliderValue by remember(activeFont?.id) {
        mutableFloatStateOf(activeFont?.size?.toFloat() ?: minSp)
    }

    // Nearest predefined font to the current drag position
    val nearestFont by remember(sliderValue, sortedFonts) {
        derivedStateOf {
            sortedFonts.minByOrNull { abs(it.size.toFloat() - sliderValue) }
        }
    }

    // Round to nearest 0.5 sp for display
    val displaySp = (sliderValue * 2).roundToInt() / 2f
    val displayText = if (displaySp == displaySp.toInt().toFloat())
        "${displaySp.toInt()} sp" else "$displaySp sp"

    Column(verticalArrangement = Arrangement.spacedBy(0.dp)) {
        // Header: name + live sp badge
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = nearestFont?.name ?: "",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    displayText,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        // Continuous slider — no forced steps; snaps to nearest on release
        Slider(
            value = sliderValue,
            onValueChange = { v ->
                sliderValue = v
                val nearest = sortedFonts.minByOrNull { abs(it.size.toFloat() - v) }
                if (nearest != null) {
                    onPreviewFont(nearest)
                }
            },
            onValueChangeFinished = {
                val nearest = sortedFonts.minByOrNull { abs(it.size.toFloat() - sliderValue) }
                if (nearest != null) {
                    sliderValue = nearest.size.toFloat()
                    onSelect(nearest.id)
                    // If we've snapped back to the already active font, clear override
                    if (nearest.id == activeFont?.id) {
                        onPreviewFont(null)
                    }
                }
            },
            valueRange = minSp..maxSp,
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.primary,
                activeTrackColor = MaterialTheme.colorScheme.primary,
                inactiveTrackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
            ),
            modifier = Modifier.fillMaxWidth()
        )

        // Reference labels positioned at each predefined size
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
        ) {
            sortedFonts.forEach { font ->
                val fraction = if (maxSp > minSp)
                    (font.size.toFloat() - minSp) / (maxSp - minSp) else 0.5f
                val isNearest = font.id == nearestFont?.id

                Text(
                    text = font.name,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = if (isNearest) FontWeight.SemiBold else FontWeight.Normal,
                    color = if (isNearest) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.35f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(
                            BiasAlignment(
                                horizontalBias = fraction * 2 - 1,
                                verticalBias = 0f
                            )
                        )
                        .clickable { onSelect(font.id) }
                )
            }
        }
        Spacer(Modifier.height(16.dp))
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
private fun AppearanceSettingMobilePreview() {
    val sampleThemes = listOf(
        ThemeItem("1", "Light", "LIGHT", true),
        ThemeItem("2", "Dark", "DARK", false),
        ThemeItem("3", "System", "SYSTEM", false)
    )
    val sampleFonts = listOf(
        FontItem("1", "Small", 12.0),
        FontItem("2", "Medium", 16.0),
        FontItem("3", "Large", 20.0)
    )

    MaterialTheme {
        Surface {
            MobileLayout(
                themes = sampleThemes,
                activeTheme = sampleThemes[0],
                fonts = sampleFonts,
                activeFont = sampleFonts[1],
                previewFont = null,
                activeAccentColor = YellowPrimary,
                onSelectTheme = {},
                onSelectFont = {},
                onSelectColor = {},
                onPreviewFont = {}
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 1024, heightDp = 768)
@Composable
private fun AppearanceSettingDesktopPreview() {
    val sampleThemes = listOf(
        ThemeItem("1", "Light", "LIGHT", true),
        ThemeItem("2", "Dark", "DARK", false),
        ThemeItem("3", "System", "SYSTEM", false)
    )
    val sampleFonts = listOf(
        FontItem("1", "Small", 12.0),
        FontItem("2", "Medium", 16.0),
        FontItem("3", "Large", 20.0)
    )

    MaterialTheme {
        Surface {
            DesktopLayout(
                themes = sampleThemes,
                activeTheme = sampleThemes[0],
                fonts = sampleFonts,
                activeFont = sampleFonts[1],
                previewFont = null,
                activeAccentColor = YellowPrimary,
                onSelectTheme = {},
                onSelectFont = {},
                onSelectColor = {},
                onPreviewFont = {}
            )
        }
    }
}
