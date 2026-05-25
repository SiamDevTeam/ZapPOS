/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.components.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.CurrencyLira
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.siamdev.zappos.LocalSettingVM
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

@Composable
fun PrimaryAmt(
    value: String,
    iconSize: Dp,
    textStyle: TextStyle,
    fontWeight: FontWeight = FontWeight.Normal,
    tint: Color = Color.Unspecified,
    color: Color = Color.Unspecified
) {
    val primaryCurrency by LocalSettingVM.current.primaryCurrency.collectAsState()
    val code = primaryCurrency?.code ?: "THB"
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
        CurrencyCodeIcon(code = code, modifier = Modifier.size(iconSize), tint = tint)
        Text(value, style = textStyle, fontWeight = fontWeight, color = color)
    }
}

@Composable
fun SecondaryAmt(
    value: String,
    iconSize: Dp,
    textStyle: TextStyle
) {
    val settingVM = LocalSettingVM.current
    val showSecondary by settingVM.showSecondaryCurrency.collectAsState()
    if (!showSecondary) return

    val secondaryCurrency by settingVM.secondaryCurrency.collectAsState()
    val code = secondaryCurrency?.code ?: "SATS"
    val orange = Color(0xFFFFB700)

    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
        when (code) {
            "SATS", "BTC" -> {
                val icon = if (code == "BTC") Res.drawable.bitcoin_unit else Res.drawable.sat_unit
                Icon(
                    painter = painterResource(icon),
                    contentDescription = null,
                    modifier = Modifier.size(iconSize),
                    tint = if (code == "BTC") Color.Unspecified else orange
                )
                Text(value, style = textStyle, color = orange)
            }
            else -> {
                CurrencyCodeIcon(code = code, modifier = Modifier.size(iconSize), tint = orange)
                Text(value, style = textStyle, color = orange)
            }
        }
    }
}
