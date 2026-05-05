/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CurrencyLira
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.siamdev.zappos.theme.YellowPrimary
import org.siamdev.zappos.ui.screens.checkout.formatDouble
import zappos.composeapp.generated.resources.Res
import zappos.composeapp.generated.resources.sat_unit


@Composable
fun OrderSummaryCard(
    subtotalFiat: String,
    subtotalSat: String,
    taxPercent: Float,
    onTaxChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    val subtotal = subtotalFiat.replace(",", "").toDoubleOrNull() ?: 0.0
    val taxAmount = subtotal * taxPercent / 100
    val grandTotal = subtotal + taxAmount

    val satValue = subtotalSat.replace(",", "").toDoubleOrNull() ?: 0.0
    val grandTotalSat = satValue * (1 + taxPercent / 100)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
                shape = RoundedCornerShape(12.dp)
            )
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Subtotal row
        SummaryRow(label = "Subtotal") {
            Column(horizontalAlignment = Alignment.End) {
                FiatAmount(
                    value = subtotalFiat,
                    iconSize = 13.dp,
                    textStyle = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
                SatAmount(value = subtotalSat, iconSize = 12.dp, textStyle = MaterialTheme.typography.bodySmall)
            }
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f))

        // VAT selector row
        SummaryRow(label = "VAT") {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(0f, 7f, 10f).forEach { rate ->
                    VatChip(
                        rate = rate,
                        isSelected = taxPercent == rate,
                        onClick = { onTaxChange(rate) }
                    )
                }
            }
        }

        // VAT amount row (only visible when VAT > 0)
        if (taxPercent > 0f) {
            SummaryRow(
                label = "VAT ${taxPercent.toInt()}%",
                labelAlpha = 0.45f
            ) {
                FiatAmount(
                    value = formatDouble(taxAmount),
                    iconSize = 12.dp,
                    textStyle = MaterialTheme.typography.bodySmall,
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f)
                )
            }
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f))

        // Grand total row
        SummaryRow(
            label = "Total",
            labelStyle = MaterialTheme.typography.titleSmall,
            labelWeight = FontWeight.Bold
        ) {
            Column(horizontalAlignment = Alignment.End) {
                FiatAmount(
                    value = formatDouble(grandTotal),
                    iconSize = 16.dp,
                    textStyle = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                SatAmount(value = formatDouble(grandTotalSat), iconSize = 13.dp, textStyle = MaterialTheme.typography.bodySmall)
            }
        }
    }
}


@Composable
private fun SummaryRow(
    label: String,
    labelAlpha: Float = 0.6f,
    labelStyle: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.bodyMedium,
    labelWeight: FontWeight = FontWeight.Normal,
    content: @Composable () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = labelStyle,
            fontWeight = labelWeight,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = labelAlpha)
        )
        content()
    }
}

@Composable
private fun VatChip(rate: Float, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) YellowPrimary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f))
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 5.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (rate == 0f) "No VAT" else "${rate.toInt()}%",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            color = if (isSelected) MaterialTheme.colorScheme.onSurface
            else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
        )
    }
}

@Composable
internal fun FiatAmount(
    value: String,
    iconSize: Dp,
    textStyle: androidx.compose.ui.text.TextStyle,
    fontWeight: FontWeight = FontWeight.Normal,
    tint: Color = MaterialTheme.colorScheme.onSurface,
    color: Color = MaterialTheme.colorScheme.onSurface
) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        Icon(Icons.Default.CurrencyLira, contentDescription = null, modifier = Modifier.size(iconSize), tint = tint)
        Text(value, style = textStyle, fontWeight = fontWeight, color = color)
    }
}

@Composable
internal fun SatAmount(
    value: String,
    iconSize: Dp,
    textStyle: androidx.compose.ui.text.TextStyle
) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        Icon(
            painter = painterResource(Res.drawable.sat_unit),
            contentDescription = null,
            tint = Color(0xFFFFB700),
            modifier = Modifier.size(iconSize)
        )
        Text(value, style = textStyle, color = Color(0xFFFFB700))
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 300)
@Composable
fun OrderSummaryCardNoVatPreview() {
    MaterialTheme {
        OrderSummaryCard(
            subtotalFiat = "845.00",
            subtotalSat = "214,250",
            taxPercent = 0f,
            onTaxChange = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 300)
@Composable
fun OrderSummaryCard7PercentPreview() {
    MaterialTheme {
        OrderSummaryCard(
            subtotalFiat = "845.00",
            subtotalSat = "214,250",
            taxPercent = 7f,
            onTaxChange = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 300)
@Composable
fun OrderSummaryCard10PercentPreview() {
    MaterialTheme {
        OrderSummaryCard(
            subtotalFiat = "845.00",
            subtotalSat = "214,250",
            taxPercent = 10f,
            onTaxChange = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 300)
@Composable
fun OrderSummaryCardInteractivePreview() {
    MaterialTheme {
        var tax by remember { mutableFloatStateOf(7f) }
        OrderSummaryCard(
            subtotalFiat = "845.00",
            subtotalSat = "214,250",
            taxPercent = tax,
            onTaxChange = { tax = it },
            modifier = Modifier.padding(16.dp)
        )
    }
}
