/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.screens.setting.appearance

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.abs
import kotlin.math.roundToInt
import org.siamdev.zappos.LocalSettingVM
import org.siamdev.zappos.data.source.local.FontItem
import org.siamdev.zappos.data.source.local.ThemeItem
import org.siamdev.zappos.theme.MapLikeColors
import org.siamdev.zappos.theme.YellowPrimary
import org.siamdev.zappos.theme.colorFromHex
import org.siamdev.zappos.theme.toHex
import org.siamdev.zappos.ui.components.common.WorkspaceHeader

private val AllAccentColors: List<Color> = listOf(YellowPrimary) + MapLikeColors

@Composable
fun AppearanceSettingScreen(onNavigateBack: () -> Unit = {}) {
    val viewModel = LocalSettingVM.current
    val themes by viewModel.themes.collectAsState()
    val activeTheme by viewModel.activeTheme.collectAsState()
    val fonts by viewModel.fonts.collectAsState()
    val activeFont by viewModel.activeFont.collectAsState()
    val accentColorHex by viewModel.accentColor.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val activeAccentColor = accentColorHex?.let { colorFromHex(it) } ?: YellowPrimary

    Column(
        modifier =
            Modifier.fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .windowInsetsPadding(WindowInsets.systemBars)
    ) {
        WorkspaceHeader(title = "Appearance", subtitle = "Settings · appearance", onNavigateBack = onNavigateBack)

        if (isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
            return@Column
        }

        BoxWithConstraints(modifier = Modifier.weight(1f)) {
            if (maxWidth >= 600.dp) {
                DesktopSettingsLayout(
                    themes = themes, activeTheme = activeTheme,
                    fonts = fonts, activeFont = activeFont,
                    activeAccentColor = activeAccentColor,
                    onSelectTheme = { viewModel.selectTheme(it) },
                    onSelectFont = { viewModel.selectFont(it) },
                    onSelectColor = { viewModel.selectAccentColor(it.toHex()) }
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item {
                        SettingsCard(
                            themes = themes, activeTheme = activeTheme,
                            fonts = fonts, activeFont = activeFont,
                            activeAccentColor = activeAccentColor,
                            onSelectTheme = { viewModel.selectTheme(it) },
                            onSelectFont = { viewModel.selectFont(it) },
                            onSelectColor = { viewModel.selectAccentColor(it.toHex()) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingsCard(
    themes: List<ThemeItem>,
    activeTheme: ThemeItem?,
    fonts: List<FontItem>,
    activeFont: FontItem?,
    activeAccentColor: Color,
    onSelectTheme: (String) -> Unit,
    onSelectFont: (String) -> Unit,
    onSelectColor: (Color) -> Unit
) {
    Column(
        modifier =
            Modifier.fillMaxWidth()
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
            FontSizeSlider(fonts = fonts, activeFont = activeFont, onSelect = onSelectFont)
        }
    }
}

@Composable
private fun DesktopSettingsLayout(
    themes: List<ThemeItem>, activeTheme: ThemeItem?,
    fonts: List<FontItem>, activeFont: FontItem?,
    activeAccentColor: Color,
    onSelectTheme: (String) -> Unit,
    onSelectFont: (String) -> Unit,
    onSelectColor: (Color) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Left panel — Accent Color (wider, needs more room for swatches + hue band)
        Column(
            modifier = Modifier
                .weight(1.5f)
                .fillMaxHeight()
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surface)
                .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp))
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                "ACCENT COLOR",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                letterSpacing = 2.sp
            )
            AccentColorPicker(activeColor = activeAccentColor, onSelect = onSelectColor)
        }

        // Right panel — Theme + Font stacked
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surface)
                .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp))
        ) {
            SettingSection(label = "THEME") {
                ThemeSegmentedControl(themes = themes, activeTheme = activeTheme, onSelect = onSelectTheme)
            }
            SectionDivider()
            SettingSection(label = "FONT SIZE") {
                FontSizeSlider(fonts = fonts, activeFont = activeFont, onSelect = onSelectFont)
            }
        }
    }
}

@Composable
private fun SettingSection(label: String, content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
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
private fun AccentColorPicker(activeColor: Color, onSelect: (Color) -> Unit) {
    var hexInput by remember { mutableStateOf(activeColor.toHex().drop(2).uppercase()) }

    LaunchedEffect(activeColor) { hexInput = activeColor.toHex().drop(2).uppercase() }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        // Hue gradient band
        HueBand(
            selectedHue = activeColor.toHsv()[0],
            onHueSelected = { hue -> onSelect(hsvToColor(hue, 1f, 1f)) }
        )

        // Preset swatch grid (6 per row, full-width distributed)
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            AllAccentColors.chunked(6).forEach { rowColors ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
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

        // Hex input + Apply
        Row(
            modifier =
                Modifier.fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.background)
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.outlineVariant,
                        RoundedCornerShape(10.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier =
                    Modifier.size(24.dp)
                        .clip(CircleShape)
                        .background(activeColor)
                        .border(
                            1.5.dp,
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f),
                            CircleShape
                        )
            )
            Text(
                "#",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.35f)
            )
            BasicTextField(
                value = hexInput,
                onValueChange = { raw ->
                    hexInput = raw.uppercase().filter { it in "0123456789ABCDEF" }.take(6)
                },
                singleLine = true,
                textStyle =
                    MaterialTheme.typography.labelLarge.copy(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.SemiBold
                    ),
                modifier = Modifier.weight(1f)
            )
            Button(
                onClick = { if (hexInput.length == 6) onSelect(colorFromHex("FF$hexInput")) },
                enabled = hexInput.length == 6,
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 0.dp),
                modifier = Modifier.height(30.dp)
            ) {
                Text("Apply", style = MaterialTheme.typography.labelMedium)
            }
        }
    }
}

@Composable
private fun ColorSwatch(color: Color, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier =
            Modifier.size(if (isSelected) 38.dp else 34.dp)
                .clip(CircleShape)
                .background(color)
                .then(
                    if (isSelected)
                        Modifier.border(2.5.dp, MaterialTheme.colorScheme.onSurface, CircleShape)
                    else
                        Modifier.border(
                            0.5.dp,
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
                            CircleShape
                        )
                )
                .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (isSelected) {
            Icon(
                Icons.Default.Check,
                null,
                tint = if (color.luminance() > 0.35f) Color(0xFF1A1A1A) else Color.White,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
private fun HueBand(selectedHue: Float, onHueSelected: (Float) -> Unit) {
    val hueColors = remember { (0..24).map { i -> hsvToColor(i * 15f, 1f, 1f) } }
    var bandWidth by remember { mutableStateOf(0) }

    Box(
        modifier =
            Modifier.fillMaxWidth()
                .height(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .onSizeChanged { bandWidth = it.width }
                .pointerInput(bandWidth) {
                    detectTapGestures { offset ->
                        if (bandWidth > 0)
                            onHueSelected((offset.x / bandWidth * 360f).coerceIn(0f, 360f))
                    }
                }
                .pointerInput(bandWidth) {
                    detectDragGestures { change, _ ->
                        if (bandWidth > 0)
                            onHueSelected((change.position.x / bandWidth * 360f).coerceIn(0f, 360f))
                    }
                }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(brush = Brush.horizontalGradient(hueColors))
            val thumbX = (selectedHue / 360f) * size.width
            val cy = size.height / 2f
            drawCircle(color = Color.White, radius = 14f, center = Offset(thumbX, cy))
            drawCircle(
                color = Color.Black.copy(alpha = 0.25f),
                radius = 14f,
                center = Offset(thumbX, cy),
                style = Stroke(width = 2f)
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
        modifier =
            Modifier.fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.background)
                .padding(3.dp),
        horizontalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        themes.forEach { theme ->
            val isActive = theme.id == activeTheme?.id
            val icon: ImageVector =
                when (theme.mode) {
                    "LIGHT" -> Icons.Default.LightMode
                    "DARK" -> Icons.Default.DarkMode
                    else -> Icons.Default.SettingsBrightness
                }
            Box(
                modifier =
                    Modifier.weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            if (isActive) MaterialTheme.colorScheme.surface else Color.Transparent
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
                        icon,
                        null,
                        tint =
                            if (isActive) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.35f),
                        modifier = Modifier.size(15.dp)
                    )
                    Text(
                        theme.name,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Normal,
                        color =
                            if (isActive) MaterialTheme.colorScheme.onSurface
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
    onSelect: (String) -> Unit
) {
    if (fonts.isEmpty()) return

    val sortedFonts = remember(fonts) { fonts.sortedBy { it.size } }
    val minSp = sortedFonts.first().size.toFloat()
    val maxSp = sortedFonts.last().size.toFloat()

    var sliderValue by
    remember(activeFont?.id) { mutableFloatStateOf(activeFont?.size?.toFloat() ?: minSp) }

    val nearestFont by
    remember(sliderValue, sortedFonts) {
        derivedStateOf { sortedFonts.minByOrNull { abs(it.size.toFloat() - sliderValue) } }
    }

    val displaySp = (sliderValue * 2).roundToInt() / 2f
    val displayText =
        if (displaySp == displaySp.toInt().toFloat()) "${displaySp.toInt()} sp" else "$displaySp sp"

    Column(verticalArrangement = Arrangement.spacedBy(0.dp)) {
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
                modifier =
                    Modifier.clip(RoundedCornerShape(8.dp))
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

        Slider(
            value = sliderValue,
            onValueChange = { sliderValue = it },
            onValueChangeFinished = {
                val nearest = sortedFonts.minByOrNull { abs(it.size.toFloat() - sliderValue) }
                if (nearest != null) {
                    sliderValue = nearest.size.toFloat()
                    onSelect(nearest.id)
                }
            },
            steps = (maxSp - minSp).toInt() - 1,
            valueRange = minSp..maxSp,
            colors =
                SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.primary,
                    activeTrackColor = MaterialTheme.colorScheme.primary,
                    inactiveTrackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
                ),
            modifier = Modifier.fillMaxWidth()
        )

        // Labels at min, mid, and max
        val anchorFonts =
            remember(sortedFonts) {
                listOfNotNull(
                    sortedFonts.firstOrNull(),
                    sortedFonts.getOrNull(sortedFonts.size / 2),
                    sortedFonts.lastOrNull()
                )
                    .distinctBy { it.id }
            }
        BoxWithConstraints(modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp)) {
            anchorFonts.forEach { font ->
                val fraction =
                    if (maxSp > minSp) (font.size.toFloat() - minSp) / (maxSp - minSp) else 0.5f
                val isNearest = font.id == nearestFont?.id
                Text(
                    text = font.name,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = if (isNearest) FontWeight.SemiBold else FontWeight.Normal,
                    color =
                        if (isNearest) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.35f),
                    textAlign = TextAlign.Center,
                    modifier =
                        Modifier.align(
                            BiasAlignment(horizontalBias = fraction * 2 - 1, verticalBias = 0f)
                        )
                            .clickable { onSelect(font.id) }
                )
            }
        }
        Spacer(Modifier.height(16.dp))
    }
}

private fun hsvToColor(hue: Float, saturation: Float, value: Float): Color {
    val h = (hue % 360f) / 60f
    val i = h.toInt()
    val f = h - i
    val p = value * (1f - saturation)
    val q = value * (1f - saturation * f)
    val t = value * (1f - saturation * (1f - f))
    return when (i % 6) {
        0 -> Color(value, t, p)
        1 -> Color(q, value, p)
        2 -> Color(p, value, t)
        3 -> Color(p, q, value)
        4 -> Color(t, p, value)
        else -> Color(value, p, q)
    }
}

private fun Color.toHsv(): FloatArray {
    val r = red
    val g = green
    val b = blue
    val cMax = maxOf(r, g, b)
    val cMin = minOf(r, g, b)
    val delta = cMax - cMin
    val s = if (cMax == 0f) 0f else delta / cMax
    var h =
        when {
            delta == 0f -> 0f
            cMax == r -> 60f * ((g - b) / delta % 6f)
            cMax == g -> 60f * ((b - r) / delta + 2f)
            else -> 60f * ((r - g) / delta + 4f)
        }
    if (h < 0f) h += 360f
    return floatArrayOf(h, s, cMax)
}

private val previewThemes =
    listOf(
        ThemeItem("theme-system", "System", "SYSTEM", true),
        ThemeItem("theme-light", "Light", "LIGHT", false),
        ThemeItem("theme-dark", "Dark", "DARK", false)
    )

private val previewFonts =
    listOf(
        FontItem("font-size-12", "Small", 12.0),
        FontItem("font-size-14", "Normal", 14.0),
        FontItem("font-size-16", "Large", 16.0)
    )

@Preview(showBackground = true, name = "Mobile", widthDp = 390, heightDp = 844)
@Composable
private fun AppearanceSettingMobilePreview() {
    SettingsCard(
        themes = previewThemes,
        activeTheme = previewThemes.first(),
        fonts = previewFonts,
        activeFont = previewFonts[1],
        activeAccentColor = YellowPrimary,
        onSelectTheme = {},
        onSelectFont = {},
        onSelectColor = {}
    )
}

@Preview(showBackground = true, name = "Desktop", widthDp = 1200, heightDp = 800)
@Composable
private fun AppearanceSettingDesktopPreview() {
    DesktopSettingsLayout(
        themes = previewThemes,
        activeTheme = previewThemes[1],
        fonts = previewFonts,
        activeFont = previewFonts[2],
        activeAccentColor = YellowPrimary,
        onSelectTheme = {},
        onSelectFont = {},
        onSelectColor = {}
    )
}
