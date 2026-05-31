/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.components.order

import org.siamdev.zappos.ui.components.common.MaterialButton

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.*
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import org.siamdev.zappos.LocalSettingVM
import org.siamdev.zappos.ui.components.common.PrimaryAmt
import org.siamdev.zappos.ui.components.common.SecondaryAmt
import org.siamdev.zappos.ui.screens.sale.MenuItem
import org.siamdev.zappos.ui.screens.setting.SettingViewModel

@Composable
fun OrderItemCard(
    item: MenuItem,
    onAddClick: () -> Unit,
    onReduceClick: () -> Unit,
    onCountChange: (UInt) -> Unit = {},
    onDelete: (() -> Unit)? = null,
    isDesktop: Boolean = false
) {
    var isEditing by remember { mutableStateOf(false) }
    var editText by remember { mutableStateOf(TextFieldValue("")) }
    val focusRequester = remember { FocusRequester() }

    fun confirmEdit() {
        onCountChange(editText.text.toUIntOrNull() ?: item.count)
        isEditing = false
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.height(3.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PrimaryAmt(
                    value = item.priceBaht,
                    iconSize = 11.dp,
                    textStyle = MaterialTheme.typography.bodySmall,
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                SecondaryAmt(
                    value = item.priceSat,
                    iconSize = 10.dp,
                    textStyle = MaterialTheme.typography.bodySmall
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            MaterialButton(
                modifier = Modifier.size(28.dp),
                iconCenter = Icons.Default.Remove,
                iconColor = Color.White,
                buttonColor = Color(0xFFE12729),
                onClick = {
                    if (isEditing) isEditing = false
                    onReduceClick()
                }
            )

            if (isEditing) {
                BasicTextField(
                    value = editText,
                    onValueChange = { new ->
                        var filtered = new.text.filter { c -> c.isDigit() }.take(4)
                        if (filtered.length > 1 && filtered.startsWith("0")) {
                            filtered = filtered.dropWhile { it == '0' }.ifEmpty { "0" }
                        }
                        editText = new.copy(
                            text = filtered,
                            selection = TextRange(minOf(new.selection.start, filtered.length))
                        )
                    },
                    textStyle = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    ),
                    singleLine = true,
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = { confirmEdit() }),
                    modifier = Modifier
                        .widthIn(min = 32.dp, max = 60.dp)
                        .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                        .focusRequester(focusRequester)
                        .onKeyEvent { event ->
                            when (event.key) {
                                Key.Enter if event.type == KeyEventType.KeyDown -> {
                                    confirmEdit(); true
                                }
                                Key.Escape if event.type == KeyEventType.KeyDown -> {
                                    isEditing = false; true
                                }
                                else -> false
                            }
                        }
                )
                LaunchedEffect(Unit) { focusRequester.requestFocus() }
            } else {
                Text(
                    text = "${item.count}",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .widthIn(min = 20.dp)
                        .then(
                            if (isDesktop)
                                Modifier
                                    .border(
                                        1.dp,
                                        MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                                        RoundedCornerShape(4.dp)
                                    )
                                    .padding(horizontal = 4.dp, vertical = 1.dp)
                            else Modifier
                        )
                        .clickable {
                            val s = "${item.count}"
                            editText = TextFieldValue(s, selection = TextRange(s.length))
                            isEditing = true
                        },
                    textAlign = TextAlign.Center
                )
            }

            MaterialButton(
                modifier = Modifier.size(28.dp),
                iconCenter = Icons.Default.Add,
                iconColor = Color.White,
                buttonColor = Color(0xFF22BB2E),
                onClick = {
                    if (isEditing) isEditing = false
                    onAddClick()
                }
            )

            if (onDelete != null) {
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Remove item",
                        tint = MaterialTheme.colorScheme.error.copy(alpha = 0.75f),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }

    HorizontalDivider(
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f),
        thickness = 1.dp
    )
}


@Preview(showBackground = true)
@Composable
fun OrderItemCardPreview() {
    val settingVM = remember { SettingViewModel() }
    CompositionLocalProvider(LocalSettingVM provides settingVM) {
        OrderItemCard(
            item = MenuItem(
                id = 1,
                imageUrl = "",
                name = "Matcha Latte",
                priceBaht = "100.00",
                priceSat = "26,000",
                count = 2u
            ),
            onAddClick = {},
            onReduceClick = {}
        )
    }
}