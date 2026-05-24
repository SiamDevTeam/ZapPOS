/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.components.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.CurrencyLira
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.resources.painterResource
import zappos.composeapp.generated.resources.Res
import zappos.composeapp.generated.resources.bitcoin_unit
import zappos.composeapp.generated.resources.sat_unit

/**
 * Renders the icon for a currency identified by [code].
 *
 * - BTC  → bitcoin_unit drawable (SVG colours preserved, tint ignored)
 * - SATS → sat_unit drawable
 * - THB  → CurrencyLira Material icon
 * - USD  → AttachMoney Material icon
 * - else → CurrencyExchange Material icon
 */
@Composable
fun CurrencyCodeIcon(
    code: String,
    modifier: Modifier = Modifier,
    tint: Color = Color.Unspecified
) {
    when (code) {
        "BTC"  -> Icon(
            painter = painterResource(Res.drawable.bitcoin_unit),
            contentDescription = null,
            modifier = modifier,
            tint = Color.Unspecified
        )
        "SATS" -> Icon(
            painter = painterResource(Res.drawable.sat_unit),
            contentDescription = null,
            modifier = modifier,
            tint = tint
        )
        "THB"  -> Icon(
            imageVector = Icons.Default.CurrencyLira,
            contentDescription = null,
            modifier = modifier,
            tint = tint
        )
        "USD"  -> Icon(
            imageVector = Icons.Default.AttachMoney,
            contentDescription = null,
            modifier = modifier,
            tint = tint
        )
        else   -> Icon(
            imageVector = Icons.Default.CurrencyExchange,
            contentDescription = null,
            modifier = modifier,
            tint = tint
        )
    }
}