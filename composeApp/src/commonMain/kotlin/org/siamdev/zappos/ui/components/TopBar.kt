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
package org.siamdev.zappos.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Reorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    title: String = "Untitled",
    onClick: (expanded: Boolean) -> Unit = {}
) {
    val brandText = brandTextFromTitle(title)
    val brandColor = brandColorFromTitle(title)

    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 15.dp)
            .height(50.dp)
            .padding(horizontal = 14.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.6f))
                .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable {
                    expanded = !expanded
                    onClick(expanded)
                }
            ) {
                // Left group
                Box(
                    modifier = Modifier.size(32.dp),
                    contentAlignment = Alignment.Center
                ) {

                    // Brand + Title + Arrow
                    GlowEffects(
                        modifier = Modifier.matchParentSize(),
                        shape = GlowShape.Square,
                        drawCore = false,
                        glowIntensity = 0.5f,
                        glowRadius = 220f,
                        color = brandColor
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = brandColor, shape = RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = brandText,
                            color = Color.White,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }

                Spacer(Modifier.width(10.dp))

                Text(
                    text = title,
                    fontSize = 15.sp,
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(Modifier.width(4.dp))

                Icon(
                    imageVector = if (expanded)
                        Icons.Default.KeyboardArrowUp
                    else
                        Icons.Default.KeyboardArrowDown,
                    contentDescription = "Toggle menu",
                    tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    modifier = Modifier.size(20.dp)
                )
            }

            // ----- Right group -----
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notifications",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            println("Notifications clicked")
                        }
                )

                Spacer(Modifier.width(12.dp))

                Icon(
                    imageVector = Icons.Default.Reorder,
                    contentDescription = "Reorder",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            println("Reorder clicked")
                        }
                )
            }
        }
    }

}



private fun brandTextFromTitle(title: String): String {
    val trimmed = title.trim()
    if (trimmed.isEmpty()) return ""

    val uppercaseChars = trimmed.filter { it.isUpperCase() }

    return when {
        uppercaseChars.length >= 2 ->
            uppercaseChars.take(2)

        uppercaseChars.length == 1 ->
            uppercaseChars.first().toString()

        else -> trimmed.first().uppercase()
    }
}


private val MapLikeColors = listOf(
    // Reds / Pinks
    Color(0xFFEF5350), // Red
    Color(0xFFE57373), // Light Red
    Color(0xFFEC407A), // Pink
    Color(0xFFD81B60), // Deep Pink

    // Purples
    Color(0xFFAB47BC), // Purple
    Color(0xFF8E24AA), // Deep Purple
    Color(0xFF7E57C2), // Soft Violet
    Color(0xFF5E35B1), // Indigo Purple

    // Blues
    Color(0xFF5C6BC0), // Indigo
    Color(0xFF3F51B5), // Deep Indigo
    Color(0xFF29B6F6), // Light Blue
    Color(0xFF0288D1), // Blue

    // Teal / Cyan
    Color(0xFF26A69A), // Teal
    Color(0xFF009688), // Deep Teal
    Color(0xFF00ACC1), // Cyan
    Color(0xFF00838F), // Dark Cyan

    // Greens
    Color(0xFF66BB6A), // Green
    Color(0xFF43A047), // Deep Green
    Color(0xFF9CCC65), // Light Green
    Color(0xFF558B2F), // Olive Green

    // Oranges / Yellows
    Color(0xFFFFCA28), // Amber
    Color(0xFFFFA726), // Orange
    Color(0xFFFDD835), // Yellow
    Color(0xFFFF8F00)  // Deep Orange
)


private fun brandColorFromTitle(title: String): Color {
    val trimmed = title.trim()

    if (trimmed.equals("ZapPOS", ignoreCase = true)) {
        return Color(0xFFFCBE00)
    }

    if (trimmed.isEmpty()) return MapLikeColors.first()

    val hash = trimmed.uppercase().hashCode()
    val index = kotlin.math.abs(hash) % MapLikeColors.size
    return MapLikeColors[index]
}



@Preview
@Composable
private fun UtilityHeaderPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        TopBar()
        TopBar(title = "ZapPOS")
        TopBar(title = "Demo")
        TopBar(title = "Zap POS")
        TopBar(title = "rushmi0")
    }
}
