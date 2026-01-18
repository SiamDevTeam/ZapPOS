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
package org.siamdev.zappos.theme

import androidx.compose.ui.graphics.Color

// Base brand colors
val YellowPrimary = Color(0xFFFCBE00)
val GrayText = Color(0xFF515151)

// Light Theme
val LightPrimary = YellowPrimary
val LightOnPrimary = GrayText
val LightBackground = Color(0xFFEBECF0)

// Dark Theme
val DarkPrimary = YellowPrimary
val DarkOnPrimary = Color.White
val DarkBackground = Color(0xFF1B1B1B)

val MapLikeColors = listOf(
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

