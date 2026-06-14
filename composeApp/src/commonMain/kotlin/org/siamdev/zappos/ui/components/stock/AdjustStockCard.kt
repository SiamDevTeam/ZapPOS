/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.components.stock

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.siamdev.zappos.data.source.MasterEvent
import org.siamdev.zappos.ui.components.common.MaterialButton
import org.siamdev.zappos.ui.components.common.SegmentedTabBar
import org.siamdev.zappos.ui.components.common.TabItem
import org.siamdev.zappos.ui.screens.product.goods.sampleEvents

private val adjustStockTabs = listOf(
    TabItem("Stock In", Icons.Default.MoveToInbox),
    TabItem("Stock Out", Icons.Default.ShoppingCartCheckout),
)

@Composable
fun AdjustStockCard(product: MasterEvent) {
    var mode by remember { mutableStateOf(0) }
    var qty by remember { mutableStateOf(0) }
    var reason by remember { mutableStateOf("") }

    var isEditing by remember { mutableStateOf(false) }
    var editText by remember { mutableStateOf(TextFieldValue("")) }
    val focusRequester = remember { FocusRequester() }

    fun confirmEdit() {
        qty = editText.text.toIntOrNull() ?: qty
        isEditing = false
    }

    fun adjustQty(delta: Int) {
        if (isEditing) {
            val current = editText.text.toIntOrNull() ?: 0
            val next = (current + delta).coerceAtLeast(0)
            val s = next.toString()
            editText = TextFieldValue(s, selection = TextRange(s.length))
        } else {
            qty = (qty + delta).coerceAtLeast(0)
        }
    }

    val modeLabel = if (mode == 0) "Stock In" else "Stock Out"
    val newBalance = product.stockQty?.let {
        if (mode == 0) it + qty else (it - qty).coerceAtLeast(0)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(0.dp),
        border = CardDefaults.outlinedCardBorder(),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                "Adjust stock",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )

            SegmentedTabBar(
                tabs = adjustStockTabs,
                selectedIndex = mode,
                onTabSelect = { mode = it },
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFF44336))
                        .clickable { adjustQty(-1) },
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(Icons.Default.Remove, contentDescription = "Decrease", tint = Color.White, modifier = Modifier.size(22.dp))
                }
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    if (isEditing) {
                        BasicTextField(
                            value = editText,
                            onValueChange = { new ->
                                var filtered = new.text.filter { it.isDigit() }.take(5)
                                if (filtered.length > 1 && filtered.startsWith("0")) {
                                    filtered = filtered.dropWhile { it == '0' }.ifEmpty { "0" }
                                }
                                editText = new.copy(
                                    text = filtered,
                                    selection = TextRange(minOf(new.selection.start, filtered.length)),
                                )
                            },
                            textStyle = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                textAlign = TextAlign.Center,
                            ),
                            singleLine = true,
                            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                            keyboardActions = KeyboardActions(onDone = { confirmEdit() }),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp))
                                .padding(vertical = 4.dp)
                                .focusRequester(focusRequester)
                                .onKeyEvent { event ->
                                    when (event.key) {
                                        Key.Enter if event.type == KeyEventType.KeyDown -> { confirmEdit(); true }
                                        Key.Escape if event.type == KeyEventType.KeyDown -> { isEditing = false; true }
                                        else -> false
                                    }
                                },
                        )
                        LaunchedEffect(Unit) { focusRequester.requestFocus() }
                    } else {
                        Text(
                            qty.toString(),
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .clickable {
                                    editText = TextFieldValue(qty.toString(), selection = TextRange(qty.toString().length))
                                    isEditing = true
                                }
                                .padding(vertical = 4.dp),
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF4CAF50))
                        .clickable { adjustQty(1) },
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Increase", tint = Color.White, modifier = Modifier.size(22.dp))
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                listOf(1, 5, 10, 25).forEach { preset ->
                    OutlinedButton(
                        onClick = { adjustQty(preset) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 10.dp),
                    ) {
                        Text("+$preset", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Medium)
                    }
                }
            }

            Text(
                "REASON",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            OutlinedTextField(
                value = reason,
                onValueChange = { reason = it },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyMedium,
            )

            if (newBalance != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text("New balance", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(
                        "$newBalance ${product.unit}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }

            MaterialButton(
                modifier = Modifier.fillMaxWidth().height(48.dp),
                text = "Confirm $modeLabel",
                iconStart = Icons.Default.Check,
                onClick = {},
            )
        }
    }
}

@Preview
@Composable
private fun AdjustStockCardPreview() {
    MaterialTheme {
        Box(Modifier.padding(16.dp)) {
            AdjustStockCard(product = sampleEvents().first())
        }
    }
}
