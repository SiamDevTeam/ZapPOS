/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.module.db.sys.dao.crud

import org.siamdev.module.db.AppDatabase
import org.siamdev.module.db.CrudCtx

suspend fun <R> AppDatabase.sysTx(
    ctx: CrudCtx,
    block: SysTxScope.() -> R
): Result<R> = runCatching {
    sys {
        val scope = SysTxScope(this, ctx)
        val value = scope.block()
        transactionWithResult {
            scope.runAll()
            value
        }
    }
}
