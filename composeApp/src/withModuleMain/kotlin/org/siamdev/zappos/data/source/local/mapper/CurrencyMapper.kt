/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.data.source.local.mapper

import org.siamdev.module.db.sys.schema.M_SYS_CURRENCY
import org.siamdev.zappos.data.source.local.model.CurrencyItem

internal fun M_SYS_CURRENCY.toModel() = CurrencyItem(
    id = I_CURRENCY_CODE,
    code = I_CURRENCY_CODE,
    name = I_NAME,
    symbol = I_SYMBOL
)