/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.components.picker

import androidx.compose.foundation.interaction.FocusInteraction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.material3.OutlinedTextFieldDefaults.FocusedBorderThickness
import androidx.compose.material3.OutlinedTextFieldDefaults.UnfocusedBorderThickness
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.siamdev.zappos.utils.DateTimeUtils
import org.siamdev.zappos.utils.TimeValue

/**
 * Display and manual-input component for a 24-hour HH:MM time value.
 *
 * The field is stateless with regard to any picker UI. Callers that want
 * a wheel picker should pass [onPickerRequest] and render [TimePickerDialog]
 * themselves when that callback fires. Omitting [onPickerRequest] leaves the
 * field as a pure keyboard-entry control.
 *
 * Validation per keystroke: hours are clamped to 00–23, minutes to 00–59.
 * Focus auto-advances from the hour segment to the minute segment after a
 * valid 2-digit hour is typed.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StructuredTimeField(
    value: TimeValue,
    onValueChange: (TimeValue) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    onPickerRequest: (() -> Unit)? = null,
) {
    val minuteFocus = remember { FocusRequester() }
    val hourFocus = remember { FocusRequester() }

    var hourRaw by remember(value.hour) { mutableStateOf(DateTimeUtils.pad2(value.hour)) }
    var minuteRaw by remember(value.minute) { mutableStateOf(DateTimeUtils.pad2(value.minute)) }
    var isHourFocused by remember { mutableStateOf(false) }
    var isMinuteFocused by remember { mutableStateOf(false) }
    val isFocused = isHourFocused || isMinuteFocused

    val interactionSource = remember { MutableInteractionSource() }
    val activeFocusInteraction = remember { mutableStateOf<FocusInteraction.Focus?>(null) }

    LaunchedEffect(isFocused) {
        if (isFocused) {
            val interaction = FocusInteraction.Focus()
            activeFocusInteraction.value = interaction
            interactionSource.emit(interaction)
        } else {
            activeFocusInteraction.value?.let { interactionSource.emit(FocusInteraction.Unfocus(it)) }
            activeFocusInteraction.value = null
        }
    }

    val fieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = MaterialTheme.colorScheme.primary,
        focusedLabelColor = MaterialTheme.colorScheme.primary,
        cursorColor = MaterialTheme.colorScheme.primary,
    )

    Box(modifier = modifier) {
        OutlinedTextFieldDefaults.DecorationBox(
            value = "${hourRaw}:${minuteRaw}",
            innerTextField = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                ) {
                    TimeSegmentField(
                        value = hourRaw,
                        onValueChange = { input ->
                            val digits = input.filter(Char::isDigit).take(2)
                            when (digits.length) {
                                0 -> hourRaw = ""
                                1 -> {
                                    val d = digits[0].digitToInt()
                                    if (d >= 3) {
                                        // 3–9 can never form a valid two-digit hour ≤ 23
                                        hourRaw = "0$d"
                                        onValueChange(value.copy(hour = d))
                                        minuteFocus.requestFocus()
                                    } else {
                                        hourRaw = digits
                                    }
                                }
                                2 -> {
                                    val h = digits.toInt()
                                    if (h <= 23) {
                                        hourRaw = DateTimeUtils.pad2(h)
                                        onValueChange(value.copy(hour = h))
                                        minuteFocus.requestFocus()
                                    }
                                    // else: silently reject (e.g. "25", "29")
                                }
                            }
                        },
                        onFocusChange = { focused ->
                            isHourFocused = focused
                            if (focused) onPickerRequest?.invoke()
                            if (!focused) {
                                val h = hourRaw.toIntOrNull()?.coerceIn(0, 23) ?: value.hour
                                hourRaw = DateTimeUtils.pad2(h)
                                onValueChange(value.copy(hour = h))
                            }
                        },
                        focusRequester = hourFocus,
                        imeAction = ImeAction.Next,
                        keyboardActions = KeyboardActions(onNext = { minuteFocus.requestFocus() }),
                    )

                    Text(
                        text = ":",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                    )

                    TimeSegmentField(
                        value = minuteRaw,
                        onValueChange = { input ->
                            val digits = input.filter(Char::isDigit).take(2)
                            when (digits.length) {
                                0 -> minuteRaw = ""
                                1 -> {
                                    val d = digits[0].digitToInt()
                                    if (d >= 6) {
                                        // 6–9 can never form a valid two-digit minute ≤ 59
                                        minuteRaw = "0$d"
                                        onValueChange(value.copy(minute = d))
                                    } else {
                                        minuteRaw = digits
                                    }
                                }
                                2 -> {
                                    val m = digits.toInt()
                                    if (m <= 59) {
                                        minuteRaw = DateTimeUtils.pad2(m)
                                        onValueChange(value.copy(minute = m))
                                    }
                                }
                            }
                        },
                        onFocusChange = { focused ->
                            isMinuteFocused = focused
                            if (focused) onPickerRequest?.invoke()
                            if (!focused) {
                                val m = minuteRaw.toIntOrNull()?.coerceIn(0, 59) ?: value.minute
                                minuteRaw = DateTimeUtils.pad2(m)
                                onValueChange(value.copy(minute = m))
                            }
                        },
                        focusRequester = minuteFocus,
                        imeAction = ImeAction.Done,
                        keyboardActions = KeyboardActions(),
                    )
                }
            },
            enabled = true,
            singleLine = true,
            visualTransformation = VisualTransformation.None,
            interactionSource = interactionSource,
            label = { Text(label) },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Schedule,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = if (isFocused) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurfaceVariant,
                )
            },
            colors = fieldColors,
            container = {
                OutlinedTextFieldDefaults.Container(
                    enabled = true,
                    isError = false,
                    interactionSource = interactionSource,
                    colors = fieldColors,
                    shape = RoundedCornerShape(12.dp),
                    focusedBorderThickness = FocusedBorderThickness,
                    unfocusedBorderThickness = UnfocusedBorderThickness,
                )
            },
        )
    }
}

@Composable
private fun TimeSegmentField(
    value: String,
    onValueChange: (String) -> Unit,
    onFocusChange: (Boolean) -> Unit,
    focusRequester: FocusRequester,
    imeAction: ImeAction,
    keyboardActions: KeyboardActions,
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = imeAction,
        ),
        keyboardActions = keyboardActions,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        textStyle = MaterialTheme.typography.bodyLarge.copy(
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface,
        ),
        modifier = Modifier
            .width(28.dp)
            .focusRequester(focusRequester)
            .onFocusChanged { onFocusChange(it.isFocused) },
        decorationBox = { innerTextField ->
            Box(modifier = Modifier.width(28.dp), contentAlignment = Alignment.Center) {
                innerTextField()
            }
        },
    )
}


@Preview(showBackground = true, widthDp = 200)
@Composable
private fun StructuredTimeFieldPreview() {
    MaterialTheme {
        StructuredTimeField(
            value = TimeValue(9, 30),
            onValueChange = {},
            label = "Opens",
            modifier = Modifier.padding(16.dp),
        )
    }
}