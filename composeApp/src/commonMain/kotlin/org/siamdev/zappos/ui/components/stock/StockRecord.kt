/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.components.stock

enum class StockMoveType { IN, OUT }

enum class HistoryFilter { ALL, STOCK_IN, STOCK_OUT }

data class StockRecord(
    val id: String,
    val date: String,
    val time: String,
    val qty: Int,
    val note: String,
    val operator: String,
    val type: StockMoveType,
)