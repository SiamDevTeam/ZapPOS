/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.components.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
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
    textStyle: TextStyle = LocalTextStyle.current,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null
) {
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
    modifier: Modifier = Modifier.fillMaxWidth()
) {
    EntryField(
        value = value,
        onValueChange = { onValueChange(it.filter(Char::isDigit)) },
        label = label,
        keyboardType = KeyboardType.Number,
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

@Preview(showBackground = true)
@Composable
private fun EntryFieldPreview() {
    MaterialTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Standard size
            EntryField(
                value = "",
                onValueChange = {},
                label = "Standard Field",
                placeholder = "Full width default",
            )
            
            // Scaled down by width
            EntryField(
                value = "123",
                onValueChange = {},
                label = "Smaller Width",
                modifier = Modifier.fillMaxWidth(0.5f)
            )

            // Scaled down by font and height
            EntryField(
                value = "Compact",
                onValueChange = {},
                label = "Compact Field",
                textStyle = MaterialTheme.typography.bodySmall,
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
            )
        }
    }
}
