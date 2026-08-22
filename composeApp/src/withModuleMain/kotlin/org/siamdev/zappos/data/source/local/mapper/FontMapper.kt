/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.data.source.local.mapper

import org.siamdev.module.db.sys.schema.M_SYS_FONT
import org.siamdev.zappos.data.source.local.model.FontItem

internal fun M_SYS_FONT.toModel() = FontItem(
    id = I_SF_ID,
    name = I_NAME,
    size = I_SIZE
)