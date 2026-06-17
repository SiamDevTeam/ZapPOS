/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.components.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun EntryField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier.fillMaxWidth(),
    placeholder: String = "",
    required: Boolean = false,
    optional: Boolean = false,
    singleLine: Boolean = true,
    minLines: Int = 1,
    keyboardType: KeyboardType = KeyboardType.Text,
    readOnly: Boolean = false,
    viewMode: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null
) {
    if (viewMode) {
        EntryFieldView(label = label, value = value, modifier = modifier)
        return
    }

    val errorColor = MaterialTheme.colorScheme.error
    val variantColor = MaterialTheme.colorScheme.onSurfaceVariant
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                buildAnnotatedString {
                    append(label)
                    if (required) withStyle(SpanStyle(color = errorColor)) { append(" *") }
                    if (optional) withStyle(SpanStyle(color = variantColor)) { append(" optional") }
                },
                style = textStyle.copy(fontSize = textStyle.fontSize * 0.85f)
            )
        },
        placeholder = {
            if (placeholder.isNotEmpty())
                Text(
                    placeholder,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.45f),
                    style = textStyle
                )
        },
        textStyle = textStyle,
        singleLine = singleLine,
        minLines = minLines,
        readOnly = readOnly,
        prefix = prefix,
        suffix = suffix,
        trailingIcon = trailingContent,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        shape = RoundedCornerShape(10.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            cursorColor = MaterialTheme.colorScheme.primary
        ),
        modifier = modifier
    )
}

@Composable
fun NumberUnitField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    unitLabel: String,
    modifier: Modifier = Modifier.fillMaxWidth(),
    allowNegative: Boolean = false,
    viewMode: Boolean = false,
) {
    if (viewMode) {
        EntryFieldView(label = label, value = "$value $unitLabel", modifier = modifier)
        return
    }

    EntryField(
        value = value,
        onValueChange = { raw ->
            val negative = allowNegative && raw.startsWith("-")
            val digits = raw.removePrefix("-").filter(Char::isDigit)
            // Normalize: strip insignificant leading zeros ("05"→"5", "023"→"23").
            val normalized = digits.trimStart('0').ifEmpty { if (digits.isEmpty()) "" else "0" }
            onValueChange(if (negative) "-$normalized" else normalized)
        },
        label = label,
        keyboardType = if (allowNegative) KeyboardType.Text else KeyboardType.Decimal,
        suffix = {
            Text(
                text = unitLabel,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        modifier = modifier
    )
}

@Composable
private fun EntryFieldView(
    label: String,
    value: String,
    modifier: Modifier = Modifier.fillMaxWidth(),
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = value.ifEmpty { " — " },
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = if (value.isEmpty())
                MaterialTheme.colorScheme.onSurfaceVariant
            else
                MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Preview(showBackground = true, name = "EntryField Gallery")
@Composable
private fun EntryFieldPreview() {
    MaterialTheme {
        Column(
            modifier = Modifier.padding(16.dp).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Edit Mode", style = MaterialTheme.typography.titleMedium)
            EntryField(
                value = "",
                onValueChange = {},
                label = "Standard Field",
                placeholder = "Enter something here...",
            )
            EntryField(
                value = "ZapPOS User",
                onValueChange = {},
                label = "Required Field",
                required = true
            )
            EntryField(
                value = "",
                onValueChange = {},
                label = "Optional Field",
                optional = true
            )
            EntryField(
                value = "ReadOnly Content",
                onValueChange = {},
                label = "Read Only",
                readOnly = true
            )
            EntryField(
                value = "500",
                onValueChange = {},
                label = "With Prefix & Suffix",
                prefix = { Text("฿") },
                suffix = { Text("THB") }
            )
            EntryField(
                value = "This is a longer text that spans multiple lines to demonstrate how minLines and multi-line support works in this component.",
                onValueChange = {},
                label = "Multi-line Field",
                singleLine = false,
                minLines = 3
            )

            HorizontalDivider()
            Text("NumberUnitField", style = MaterialTheme.typography.titleMedium)
            NumberUnitField(
                value = "150",
                onValueChange = {},
                label = "Inventory",
                unitLabel = "units"
            )
            NumberUnitField(
                value = "-10",
                onValueChange = {},
                label = "Balance (Allow Negative)",
                unitLabel = "฿",
                allowNegative = true
            )

            HorizontalDivider()
            Text("View Mode", style = MaterialTheme.typography.titleMedium)
            EntryField(
                value = "Green Tea Latte",
                onValueChange = {},
                label = "Product Name",
                viewMode = true,
            )
            EntryField(
                value = "",
                onValueChange = {},
                label = "SKU",
                viewMode = true,
            )
            NumberUnitField(
                value = "12",
                onValueChange = {},
                label = "Stock",
                unitLabel = "pcs",
                viewMode = true,
            )
        }
    }
}
