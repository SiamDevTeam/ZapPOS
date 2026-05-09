/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.module.db.biz.dao.crud

import org.siamdev.module.db.AppDatabase
import org.siamdev.module.db.CrudCtx

// ── Entry point ──────────────────────────────────────────────────────────────
//
// The block runs first (non-suspend) to register steps and compute the return
// value. All registered steps then execute together inside a single SQLite
// transaction. Any step failure fires onFailure, re-throws, and rolls back
// every write in the block — header and all detail lines together.
//
// Suspend work (e.g. numbering.generate) must be done before calling bizTx
// and captured in a val that the block closes over.

suspend fun <R> AppDatabase.bizTx(
    ctx: CrudCtx,
    block: BizTxScope.() -> R
): Result<R> = runCatching {
    biz {
        val scope = BizTxScope(this, ctx)
        val value = scope.block()            // register steps, compute return value
        transactionWithResult {
            scope.runAll()                   // execute all steps atomically
            value
        }
    }
}