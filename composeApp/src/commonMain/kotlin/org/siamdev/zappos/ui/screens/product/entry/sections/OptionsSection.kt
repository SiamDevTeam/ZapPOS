/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.screens.product.entry.sections

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.siamdev.zappos.ui.components.common.EntryChip
import org.siamdev.zappos.ui.components.common.EntryField
import org.siamdev.zappos.ui.components.common.SectionCard
import org.siamdev.zappos.ui.screens.product.entry.EntryFormState
import org.siamdev.zappos.ui.screens.product.entry.OptionGroup
import org.siamdev.zappos.ui.screens.product.entry.OptionItem
import org.siamdev.zappos.ui.screens.product.entry.PickMode
import org.siamdev.zappos.ui.screens.product.entry.rememberEntryFormState

@Composable
internal fun OptionsSection(state: EntryFormState) {
    SectionCard(
        icon = Icons.Default.Add,
        title = "Options & add-ons",
        subtitle = "Choices and extras the customer picks at checkout",
        badge = "OPTIONAL",
    ) {
        state.optionGroups.forEachIndexed { gi, group ->
            OptionGroupCard(
                group = group,
                onUpdate = { updated ->
                    state.optionGroups = state.optionGroups.toMutableList().also { it[gi] = updated }
                },
                onDelete = {
                    state.optionGroups = state.optionGroups.toMutableList().also { it.removeAt(gi) }
                },
            )
        }

        OutlinedButton(
            onClick = { state.optionGroups += OptionGroup() },
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(Modifier.width(6.dp))
            Text("Add option group")
        }
    }
}

@Composable
private fun OptionGroupCard(
    group: OptionGroup,
    onUpdate: (OptionGroup) -> Unit,
    onDelete: () -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(12.dp))
                .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            EntryField(
                value = group.name,
                onValueChange = { onUpdate(group.copy(name = it)) },
                label = "Group name",
                placeholder = "e.g. Size, Extras, Toppings",
                modifier = Modifier.weight(1f),
            )
            Spacer(Modifier.width(4.dp))
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete group",
                    tint = MaterialTheme.colorScheme.error,
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            listOf(PickMode.ONE to "Pick one", PickMode.MANY to "Pick many").forEach { (mode, label) ->
                EntryChip(
                    selected = group.pickMode == mode,
                    onClick = { onUpdate(group.copy(pickMode = mode)) },
                    label = label,
                )
            }
            Spacer(Modifier.weight(1f))
            Text(
                text = "Required",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Switch(
                checked = group.required,
                onCheckedChange = { onUpdate(group.copy(required = it)) },
                colors =
                    SwitchDefaults.colors(
                        checkedTrackColor = MaterialTheme.colorScheme.primary,
                        checkedThumbColor = Color.White,
                    ),
            )
        }

        group.items.forEachIndexed { ii, item ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                EntryField(
                    value = item.name,
                    onValueChange = { name ->
                        val updated = group.items.toMutableList().also { it[ii] = item.copy(name = name) }
                        onUpdate(group.copy(items = updated))
                    },
                    label = "Option name",
                    modifier = Modifier.weight(1f),
                )
                EntryField(
                    value = if (item.priceModifier == 0) "" else item.priceModifier.toString(),
                    onValueChange = { v ->
                        val updated =
                            group.items.toMutableList().also {
                                it[ii] = item.copy(priceModifier = v.filter(Char::isDigit).toIntOrNull() ?: 0)
                            }
                        onUpdate(group.copy(items = updated))
                    },
                    label = "+Price",
                    placeholder = "0",
                    prefix = { Text("฿") },
                    keyboardType = KeyboardType.Number,
                    modifier = Modifier.width(110.dp)
                )
                IconButton(onClick = {
                    val updated = group.items.toMutableList().also { it.removeAt(ii) }
                    onUpdate(group.copy(items = updated))
                }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Remove option",
                        modifier = Modifier.size(18.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }

        TextButton(
            onClick = { onUpdate(group.copy(items = group.items + OptionItem())) },
            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 2.dp),
        ) {
            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(Modifier.width(4.dp))
            Text(
                text = "Add option",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 411, name = "Options · empty")
@Composable
private fun OptionsSectionEmptyPreview() {
    MaterialTheme { OptionsSection(rememberEntryFormState()) }
}

@Preview(showBackground = true, widthDp = 411, name = "Options · with group")
@Composable
private fun OptionsSectionWithGroupPreview() {
    MaterialTheme {
        OptionsSection(
            rememberEntryFormState().also {
                it.optionGroups =
                    listOf(
                        OptionGroup(
                            name = "Size",
                            items = listOf(
                                OptionItem(name = "Small"),
                                OptionItem(name = "Large", priceModifier = 20)
                            ),
                        )
                    )
            }
        )
    }
}
