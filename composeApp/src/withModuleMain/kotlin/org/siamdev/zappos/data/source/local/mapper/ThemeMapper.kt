/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.data.source.local.mapper

import org.siamdev.module.db.sys.schema.M_SYS_THEME
import org.siamdev.zappos.data.source.local.model.ThemeItem

internal fun M_SYS_THEME.toModel() = ThemeItem(
    id = I_STH_ID,
    name = I_NAME,
    mode = I_MODE,
    isDefault = I_IS_DEFAULT == 1L
)