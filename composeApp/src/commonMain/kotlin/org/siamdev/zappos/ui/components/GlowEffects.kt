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

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

enum class GlowShape {
    Circle,
    Square,
    Rounded
}

@Composable
fun GlowEffects(
    modifier: Modifier = Modifier,
    size: Dp = 32.dp,
    glowRadius: Float = 180f,
    color: Color = Color(0xFFFCBE00),
    shape: GlowShape = GlowShape.Circle,
    coreIntensity: Float? = null,
    glowIntensity: Float? = null,
    drawCore: Boolean = true
) {
    Canvas(
        modifier = modifier.size(size * 5)
    ) {
        val center = this.center

        // -------- Glow (Radial falloff) --------
        val glowColors = if (glowIntensity == null) {
            listOf(
                color.copy(alpha = 1f),
                color.copy(alpha = 0.6f),
                color.copy(alpha = 0.25f),
                Color.Transparent
            )
        } else {
            listOf(
                color.copy(alpha = 1f * glowIntensity),
                color.copy(alpha = 0.6f * glowIntensity),
                color.copy(alpha = 0.25f * glowIntensity),
                Color.Transparent
            )
        }

        drawCircle(
            brush = Brush.radialGradient(
                colors = glowColors,
                center = center,
                radius = glowRadius
            ),
            radius = glowRadius,
            center = center,
            blendMode = BlendMode.Plus
        )

        // -------- Core shape (optional) --------
        if (!drawCore) return@Canvas

        val coreSize = size.toPx()
        val topLeft = Offset(
            center.x - coreSize / 2,
            center.y - coreSize / 2
        )

        val coreColor = if (coreIntensity == null) {
            color
        } else {
            color.copy(alpha = coreIntensity.coerceIn(0f, 2f))
        }

        val coreBlend = if (coreIntensity != null) BlendMode.Plus else BlendMode.SrcOver

        when (shape) {
            GlowShape.Circle -> {
                drawCircle(
                    color = coreColor,
                    radius = coreSize / 2,
                    center = center,
                    blendMode = coreBlend
                )
            }

            GlowShape.Square -> {
                drawRect(
                    color = coreColor,
                    topLeft = topLeft,
                    size = Size(coreSize, coreSize),
                    blendMode = coreBlend
                )
            }

            GlowShape.Rounded -> {
                drawRoundRect(
                    color = coreColor,
                    topLeft = topLeft,
                    size = Size(coreSize, coreSize),
                    cornerRadius = CornerRadius(10.dp.toPx()),
                    blendMode = coreBlend
                )
            }
        }
    }
}

@Preview
@Composable
fun GlowEffects1Preview() {
    GlowEffects(shape = GlowShape.Square)
}



@Preview
@Composable
fun GlowEffects2Preview() {
    GlowEffects(shape = GlowShape.Circle)
}


@Preview
@Composable
fun GlowEffects3Preview() {
    GlowEffects(shape = GlowShape.Rounded)
}

@Preview
@Composable
fun GlowEffects4Preview() {
    GlowEffects(
        shape = GlowShape.Square,
        coreIntensity = 1.6f,
        glowIntensity = 0.6f
    )
}

@Preview
@Composable
fun GlowEffects5Preview() {
    GlowEffects(
        shape = GlowShape.Circle,
        coreIntensity = 1.8f,
        glowIntensity = 1.0f
    )
}

@Preview
@Composable
fun GlowEffects6Preview() {
    GlowEffects(
        shape = GlowShape.Rounded,
        coreIntensity = 0.7f,
        glowIntensity = 1.2f
    )
}


@Preview
@Composable
fun GlowEffects7Preview() {
    GlowEffects(
        shape = GlowShape.Square,
        drawCore = false,
        glowIntensity = 1.0f
    )
}


@Preview
@Composable
fun GlowEffects8Preview() {
    GlowEffects(
        shape = GlowShape.Circle,
        drawCore = false,
        glowIntensity = 0.8f
    )
}


@Preview
@Composable
fun GlowEffects9Preview() {
    GlowEffects(
        shape = GlowShape.Rounded,
        drawCore = false,
        glowIntensity = 1.3f
    )
}
