/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.screens.product.entry.sections

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.siamdev.zappos.ui.components.common.ChipRow
import org.siamdev.zappos.ui.components.common.EntryField
import org.siamdev.zappos.ui.components.common.NumberUnitField
import org.siamdev.zappos.ui.components.common.SectionCard
import org.siamdev.zappos.ui.components.common.TextIconButton
import org.siamdev.zappos.ui.components.common.ToggleItem
import org.siamdev.zappos.ui.screens.product.entry.EntryFormState
import org.siamdev.zappos.ui.screens.product.entry.EntryType
import org.siamdev.zappos.ui.screens.product.entry.rememberEntryFormState
import org.siamdev.zappos.utils.formatAmount

@Composable
internal fun PricingSection(state: EntryFormState) {
    val margin =
        remember(state.price, state.costPrice) {
            val p = state.price.toDoubleOrNull() ?: 0.0
            val c = state.costPrice.toDoubleOrNull() ?: 0.0
            if (p > 0.0) (((p - c) / p) * 100.0).formatAmount(0) + "%" else ""
        }

    SectionCard(
        icon = Icons.Default.AttachMoney,
        title = "Pricing & unit",
        subtitle = "What the customer pays, and how it's counted",
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            EntryField(
                value = state.price,
                onValueChange = { state.price = it.filter { c -> c.isDigit() || c == '.' } },
                label = "Price",
                placeholder = "0.00",
                required = true,
                keyboardType = KeyboardType.Decimal,
                prefix = { Text("฿") },
                modifier = Modifier.weight(1f),
            )
            EntryField(
                value = state.unit,
                onValueChange = { state.unit = it },
                label = "Unit",
                placeholder = "type your own",
                required = true,
                prefix = { Text("/") },
                modifier = Modifier.weight(1f),
            )
        }

        ChipRow(
            items = state.unitOptions,
            selected = state.unit,
            onSelect = { state.unit = it },
        )

        if (state.entryType == EntryType.GOODS) {
            ToggleItem(
                icon = Icons.Default.SwapHoriz,
                label = "Open / variable price",
                subtitle = "Cashier enters the amount at checkout — e.g. sold by weight",
                checked = state.openPrice,
                onCheckedChange = { state.openPrice = it },
            )
        }

        if (state.entryType == EntryType.SERVICE) {
            ChargedBySelector(
                selected = state.chargedBy,
                onSelect = { state.chargedBy = it },
            )
        }

        if (state.entryType == EntryType.RENTAL) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                NumberUnitField(
                    value = state.bookingDuration,
                    onValueChange = { state.bookingDuration = it },
                    label = "Booking duration",
                    unitLabel = "min / slot",
                    modifier = Modifier.weight(1f),
                )
                NumberUnitField(
                    value = state.minBooking,
                    onValueChange = { state.minBooking = it },
                    label = "Min. booking",
                    unitLabel = "slot(s)",
                    modifier = Modifier.weight(1f),
                )
            }
        }

        ToggleItem(
            icon = Icons.Default.AccountBalance,
            label = "Charge VAT on this item",
            subtitle = "Applies the store VAT rate (7%) at checkout",
            checked = state.chargeVat,
            onCheckedChange = { state.chargeVat = it },
        )

        TextIconButton(
            icon = if (state.showCostPrice) Icons.Default.Remove else Icons.Default.Add,
            text = if (state.showCostPrice) "Hide cost price" else "Add cost price (track margin)",
            onClick = { state.showCostPrice = !state.showCostPrice },
            fontWeight = FontWeight.Medium,
        )

        AnimatedVisibility(
            visible = state.showCostPrice,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically(),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = "Cost price",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Text(
                        text = "Margin",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    EntryField(
                        value = state.costPrice,
                        onValueChange = { state.costPrice = it.filter { c -> c.isDigit() || c == '.' } },
                        label = "Cost price",
                        placeholder = "0.00",
                        keyboardType = KeyboardType.Decimal,
                        prefix = { Text("฿") },
                        suffix = { Text("/ ${state.unit.ifEmpty { "unit" }}") },
                        modifier = Modifier.weight(1f),
                    )
                    EntryField(
                        value = margin,
                        onValueChange = {},
                        label = "Margin",
                        placeholder = "Enter price & cost",
                        readOnly = true,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
    }
}

@Composable
private fun ChargedBySelector(
    selected: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val labels = listOf("Per person", "Per session", "Per hour")

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "Charged by",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            labels.forEachIndexed { index, label ->
                val isSelected = index == selected
                OutlinedButton(
                    onClick = { onSelect(index) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp),
                    colors =
                        ButtonDefaults.outlinedButtonColors(
                            containerColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                            contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                        ),
                    border =
                        BorderStroke(
                            width = if (isSelected) 1.5.dp else 1.dp,
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
                        ),
                ) {
                    Text(label, style = MaterialTheme.typography.labelSmall, maxLines = 1)
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 411, name = "Pricing · Goods")
@Composable
private fun PricingSectionGoodsPreview() {
    MaterialTheme { PricingSection(rememberEntryFormState()) }
}

@Preview(showBackground = true, widthDp = 411, name = "Pricing · Service")
@Composable
private fun PricingSectionServicePreview() {
    MaterialTheme { PricingSection(rememberEntryFormState().also { it.entryType = EntryType.SERVICE }) }
}

@Preview(showBackground = true, widthDp = 411, name = "Pricing · Rental")
@Composable
private fun PricingSectionRentalPreview() {
    MaterialTheme { PricingSection(rememberEntryFormState().also { it.entryType = EntryType.RENTAL }) }
}
