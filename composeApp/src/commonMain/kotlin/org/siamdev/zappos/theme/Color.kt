/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.theme

import androidx.compose.ui.graphics.Color

// ─── Brand ───────────────────────────────────────────────────────────────────
val YellowPrimary = Color(0xFFFCBE00)
val YellowLight   = Color(0xFFFFE082)
val YellowSoft    = Color(0xFFFFD54F)
val YellowDeep    = Color(0xFFFFA000)

// ─── Dark Theme ──────────────────────────────────────────────────────────────
val DarkBackground        = Color(0xFF1A1A1A)   // main screen background
val DarkSurface           = Color(0xFF222222)   // elevated cards, top bars
val DarkSurfaceVariant    = Color(0xFF2A2A2A)   // inputs, dropdowns, list rows
val DarkOnBackground      = Color(0xFFFFFFFF)   // primary text
val DarkOnSurface         = Color(0xFFFFFFFF)
val DarkOnSurfaceVariant  = Color(0xFF9E9E9E)   // secondary / hint text
val DarkOutline           = Color(0xFF383838)   // borders, dividers
val DarkOutlineVariant    = Color(0xFF2E2E2E)   // subtler separators
val DarkPrimary           = YellowPrimary
val DarkOnPrimary         = Color(0xFF1A1A1A)   // text/icon on yellow
val DarkPrimaryContainer  = Color(0xFF3A2D00)   // tinted yellow container
val DarkOnPrimaryContainer = YellowLight        // text inside container

// ─── Light Theme ─────────────────────────────────────────────────────────────
val LightBackground        = Color(0xFFF2F2F2)  // main screen background
val LightSurface           = Color(0xFFFFFFFF)  // elevated cards
val LightSurfaceVariant    = Color(0xFFEBECF0)  // inputs, list rows
val LightOnBackground      = Color(0xFF1A1A1A)  // primary text
val LightOnSurface         = Color(0xFF1A1A1A)
val LightOnSurfaceVariant  = Color(0xFF515151)  // secondary / hint text
val LightOutline           = Color(0xFFCCCCCC)  // borders, dividers
val LightOutlineVariant    = Color(0xFFE0E0E0)  // subtler separators
val LightPrimary           = YellowPrimary
val LightOnPrimary         = Color(0xFF1A1A1A)  // text/icon on yellow
val LightPrimaryContainer  = Color(0xFFFFF3CD)  // tinted yellow container
val LightOnPrimaryContainer = Color(0xFF3A2D00) // text inside container

// ─── Map / Category colors ────────────────────────────────────────────────────
val MapLikeColors = listOf(
    // Reds / Pinks
    Color(0xFFEF5350), Color(0xFFE57373), Color(0xFFEC407A), Color(0xFFD81B60),
    // Purples
    Color(0xFFAB47BC), Color(0xFF8E24AA), Color(0xFF7E57C2), Color(0xFF5E35B1),
    // Blues
    Color(0xFF5C6BC0), Color(0xFF3F51B5), Color(0xFF29B6F6), Color(0xFF0288D1),
    // Teal / Cyan
    Color(0xFF26A69A), Color(0xFF009688), Color(0xFF00ACC1), Color(0xFF00838F),
    // Greens
    Color(0xFF66BB6A), Color(0xFF43A047), Color(0xFF9CCC65), Color(0xFF558B2F),
    // Oranges / Yellows
    Color(0xFFFFCA28), Color(0xFFFFA726), Color(0xFFFDD835), Color(0xFFFF8F00),
)