/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.module.db.biz.dao.crud

import org.siamdev.module.db.AppDatabase
import org.siamdev.module.db.CrudCtx

suspend fun <R> AppDatabase.bizTx(
    ctx: CrudCtx,
    block: BizTxScope.() -> R
): Result<R> = runCatching {
    biz {
        val scope = BizTxScope(this, ctx)
        val value = scope.block()
        transactionWithResult {
            scope.runAll()
            value
        }
    }
}