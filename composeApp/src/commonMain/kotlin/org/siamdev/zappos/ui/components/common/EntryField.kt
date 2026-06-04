/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.components.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp

@Composable
fun EntryField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    required: Boolean = false,
    optional: Boolean = false,
    singleLine: Boolean = true,
    minLines: Int = 1,
    keyboardType: KeyboardType = KeyboardType.Text,
    readOnly: Boolean = false,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null
) {
    val errorColor   = MaterialTheme.colorScheme.error
    val variantColor = MaterialTheme.colorScheme.onSurfaceVariant
    OutlinedTextField(
        value         = value,
        onValueChange = onValueChange,
        label = {
            Text(
                buildAnnotatedString {
                    append(label)
                    if (required) withStyle(SpanStyle(color = errorColor))   { append(" *") }
                    if (optional) withStyle(SpanStyle(color = variantColor)) { append(" optional") }
                }
            )
        },
        placeholder = {
            if (placeholder.isNotEmpty())
                Text(placeholder, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.45f))
        },
        singleLine      = singleLine,
        minLines        = minLines,
        readOnly        = readOnly,
        prefix          = prefix,
        suffix          = suffix,
        trailingIcon    = trailingContent,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        shape           = RoundedCornerShape(12.dp),
        colors          = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            focusedLabelColor  = MaterialTheme.colorScheme.primary,
            cursorColor        = MaterialTheme.colorScheme.primary
        ),
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
fun NumberUnitField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    unitLabel: String,
    modifier: Modifier = Modifier
) {
    EntryField(
        value         = value,
        onValueChange = { onValueChange(it.filter(Char::isDigit)) },
        label         = label,
        keyboardType  = KeyboardType.Number,
        suffix        = {
            Text(
                text  = unitLabel,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        modifier = modifier
    )
}
