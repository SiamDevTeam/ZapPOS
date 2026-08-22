/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.data.source.local.model

data class CurrencyItem(
    val id: String,
    val code: String,       // THB, USD, BTC …
    val name: String,
    val symbol: String
)