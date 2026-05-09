/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.module.db.biz.dao.crud

import org.siamdev.module.db.CrudCtx
import org.siamdev.module.db.TxScope
import org.siamdev.module.db.Writable
import org.siamdev.module.db.WriteOp
import org.siamdev.module.db.biz.ZapPOSBiz

class BizTxScope internal constructor(
    internal val db: ZapPOSBiz,
    val ctx: CrudCtx
) : TxScope {
    override val steps = mutableListOf<suspend () -> Unit>()

    // Set by eachLine before each iteration; read by detail table extension functions.
    internal var currentDetailMode: WriteOp? = null

    internal suspend fun runAll() = steps.forEach { it() }

    fun <T> eachLine(items: List<Writable<T>>, block: BizTxScope.(T) -> Unit) {
        items.forEach { item ->
            currentDetailMode = item.mode
            block(item.data)
        }
        currentDetailMode = null
    }
}