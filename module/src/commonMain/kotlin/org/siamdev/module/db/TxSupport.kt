/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.module.db

// Shared contract for both BizTxScope and SysTxScope.
interface TxScope {
    val steps: MutableList<suspend () -> Unit>
}

class TxCallback<T> {
    var onSuccess: (T) -> Unit         = {}
    var onFailure: (Throwable) -> Unit = {}
}

class TxStepBuilder<T> internal constructor(
    private val op: suspend () -> T,
    scope: TxScope
) {
    private val callback = TxCallback<T>()

    init {
        scope.steps += {
            try   { callback.onSuccess(op()) }
            catch (e: Throwable) { callback.onFailure(e); throw e }
        }
    }

    infix fun onResult(block: TxCallback<T>.() -> Unit): TxStepBuilder<T> {
        callback.apply(block)
        return this
    }
}
