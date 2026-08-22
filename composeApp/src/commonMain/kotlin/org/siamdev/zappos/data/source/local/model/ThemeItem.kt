/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.data.source.local.model

data class ThemeItem(
    val id: String,
    val name: String,
    val mode: String,       // "SYSTEM" | "LIGHT" | "DARK"
    val isDefault: Boolean
)
