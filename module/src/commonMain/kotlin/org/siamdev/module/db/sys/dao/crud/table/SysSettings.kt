/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.module.db.sys.dao.crud.table

import org.siamdev.module.db.TxStepBuilder
import org.siamdev.module.db.sys.dao.crud.SysTxScope

// T_SYS_SETTINGS  (singleton row — always UPDATE via COALESCE)
fun SysTxScope.SysSettings(block: SysSettingsBuilder.() -> Unit): TxStepBuilder<Unit> {
    val b = SysSettingsBuilder().apply(block)
    val a = ctx.actor
    return TxStepBuilder({
        db.sETTINGS_CRUDQueries.updateAll(
            b.I_THEME_ID,
            b.I_FONT_ID,
            b.I_PRIMARY_CURRENCY_ID,
            b.I_SECONDARY_CURRENCY_ID,
            a.at,
            a.userId
        )
    }, this)
}

class SysSettingsBuilder {
    var I_THEME_ID: String?              = null
    var I_FONT_ID: String?               = null
    var I_PRIMARY_CURRENCY_ID: String?   = null
    var I_SECONDARY_CURRENCY_ID: String? = null
}