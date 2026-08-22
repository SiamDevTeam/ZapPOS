/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.data.source.local

import org.siamdev.zappos.data.source.local.contract.ProductSource
import org.siamdev.zappos.data.source.local.contract.SettingSource

object LocalSources {
    var setting: SettingSource? = null
    var product: ProductSource? = null
}