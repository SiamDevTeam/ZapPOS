/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.components.picker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.siamdev.zappos.utils.TimeValue

@Composable
fun TimeRangeRow(
    opensValue: TimeValue,
    closesValue: TimeValue,
    onOpensChange: (TimeValue) -> Unit,
    onClosesChange: (TimeValue) -> Unit,
    modifier: Modifier = Modifier,
    enableManualInput: Boolean = true,
) {
    var showOpensPicker by remember { mutableStateOf(false) }
    var showClosesPicker by remember { mutableStateOf(false) }

    if (showOpensPicker) {
        TimePickerDialog(
            value = opensValue,
            onConfirm = { onOpensChange(it); showOpensPicker = false },
            onDismiss = { showOpensPicker = false },
        )
    }
    if (showClosesPicker) {
        TimePickerDialog(
            value = closesValue,
            onConfirm = { onClosesChange(it); showClosesPicker = false },
            onDismiss = { showClosesPicker = false },
        )
    }

    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        StructuredTimeField(
            value = opensValue,
            onValueChange = onOpensChange,
            label = "From",
            onPickerRequest = { showOpensPicker = true },
            modifier = Modifier.weight(1f),
            enableManualInput = enableManualInput,
        )
        StructuredTimeField(
            value = closesValue,
            onValueChange = onClosesChange,
            label = "Until",
            onPickerRequest = { showClosesPicker = true },
            modifier = Modifier.weight(1f),
            enableManualInput = enableManualInput,
        )
    }
}