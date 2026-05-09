/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.module.db.sys.dao.crud

import org.siamdev.module.db.CrudCtx
import org.siamdev.module.db.TxScope
import org.siamdev.module.db.sys.ZapPOSSys

class SysTxScope internal constructor(
    internal val db: ZapPOSSys,
    val ctx: CrudCtx
) : TxScope {
    override val steps = mutableListOf<suspend () -> Unit>()

    internal suspend fun runAll() = steps.forEach { it() }
}