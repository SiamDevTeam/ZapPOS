/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.module.db

import kotlin.time.Clock

enum class WriteOp { INSERT, UPDATE, DELETE }

data class Actor(
    val userId: String,
    val at: Long = Clock.System.now().toEpochMilliseconds()
)

data class Writable<T>(
    val data: T,
    val mode: WriteOp
)

interface CrudCtx {
    val actor: Actor
    val writeOp: WriteOp
}