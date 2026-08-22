/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.module.db.sys.dao.crud.table

import org.siamdev.module.db.TxStepBuilder
import org.siamdev.module.db.sys.dao.crud.SysTxScope

// T_SYS_SETTINGS  (singleton row — always UPDATE via COALESCE)
fun SysTxScope.SysSettings(block: SysSettingsBuilder.() -> Unit): TxStepBuilder<Unit> {
    val builder = SysSettingsBuilder().apply(block)
    val actor   = ctx.actor
    return TxStepBuilder({
        db.sETTINGS_CRUDQueries.updateAll(
            builder.I_THEME_ID,
            builder.I_FONT_ID,
            builder.I_PRIMARY_CURRENCY_ID,
            builder.I_SECONDARY_CURRENCY_ID,
            actor.at,
            actor.userId
        )
    }, this)
}

class SysSettingsBuilder {
    var I_THEME_ID: String?              = null
    var I_FONT_ID: String?               = null
    var I_PRIMARY_CURRENCY_ID: String?   = null
    var I_SECONDARY_CURRENCY_ID: String? = null
}