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
import androidx.compose.material.icons.automirrored.filled.Segment
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Notifications
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
import org.siamdev.zappos.theme.MapLikeColors
import kotlin.math.abs

@Composable
fun WorkspaceHeader(
    modifier: Modifier = Modifier,
    title: String = "Untitled",
    onClick: (expanded: Boolean) -> Unit = {},
    onSegmentClick: () -> Unit = {}
) {
    val brandText = brandTextFromTitle(title)
    val brandColor = colorFromTitle(title)

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
            // ----- Left group -----
            Box(contentAlignment = Alignment.CenterStart) {
                GlowEffects(
                    modifier = Modifier
                        .size(32.dp)
                        .align(Alignment.CenterStart),
                    shape = GlowShape.Square,
                    drawCore = false,
                    glowIntensity = 0.5f,
                    glowRadius = 220f,
                    color = brandColor
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable {
                            expanded = !expanded
                            onClick(expanded)
                        }
                        .padding(horizontal = 6.dp, vertical = 2.dp)

                ) {
                    // Brand
                    Box(
                        modifier = Modifier.size(28.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(color = brandColor, shape = RoundedCornerShape(6.dp)),
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
                        imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Toggle menu",
                        tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // ----- Right group -----
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { println("Notifications clicked") },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifications",
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(Modifier.width(12.dp))

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onSegmentClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Segment,
                        contentDescription = "Segment",
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(24.dp)
                    )
                }
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

fun colorFromTitle(title: String): Color {
    val trimmed = title.trim()

    if (trimmed.equals("ZapPOS", ignoreCase = true)) {
        return Color(0xFFFCBE00)
    }

    if (trimmed.isEmpty()) return MapLikeColors.first()

    val hash = trimmed.uppercase().hashCode()
    val index = abs(hash) % MapLikeColors.size
    return MapLikeColors[index]
}



@Preview(showBackground = true, widthDp = 411, heightDp = 891)
@Composable
private fun WorkspaceHeaderPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        WorkspaceHeader()
        WorkspaceHeader(title = "ZapPOS")
        WorkspaceHeader(title = "Demo")
        WorkspaceHeader(title = "Zap POS")
        WorkspaceHeader(title = "rushmi0")
        WorkspaceHeader(title = "lnwza007")
        WorkspaceHeader(title = "minseo")
        WorkspaceHeader(title = "Vaz")
        WorkspaceHeader(title = "Emperor13")
    }
}
