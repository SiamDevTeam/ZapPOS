/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.components.picker

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.siamdev.zappos.ui.components.common.MaterialButton
import org.siamdev.zappos.utils.DateTimeUtils
import org.siamdev.zappos.utils.TimeValue
import kotlin.math.abs

/**
 * Standalone time picker dialog showing side-by-side hour (00–23) and
 * minute (00–59) scroll wheels.
 *
 * The dialog owns no external state. Callers supply [value] as the initial
 * selection; [onConfirm] receives the chosen [TimeValue] only when the user
 * taps "Done". Tapping "Cancel" or the scrim calls [onDismiss] with no change.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    value: TimeValue,
    onConfirm: (TimeValue) -> Unit,
    onDismiss: () -> Unit,
) {
    BasicAlertDialog(onDismissRequest = onDismiss) {
        TimePickerContent(value = value, onConfirm = onConfirm, onDismiss = onDismiss)
    }
}

@Composable
private fun TimePickerContent(
    value: TimeValue,
    onConfirm: (TimeValue) -> Unit,
    onDismiss: () -> Unit,
) {
    var selectedHour by remember { mutableIntStateOf(value.hour) }
    var selectedMinute by remember { mutableIntStateOf(value.minute) }

    val hours = remember { (0..23).map { DateTimeUtils.pad2(it) } }
    val minutes = remember { (0..59).map { DateTimeUtils.pad2(it) } }

    // Width reserved for the ":" glyph so HOUR/MINUTE labels stay aligned.
    val separatorWidth = 32.dp
    val itemHeightDp = 48.dp

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {

            // Title + live preview
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Select time",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = "${DateTimeUtils.pad2(selectedHour)}:${DateTimeUtils.pad2(selectedMinute)}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                )
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "HOUR",
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(Modifier.width(separatorWidth))
                Text(
                    text = "MINUTE",
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            Box(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxWidth()
                        .height(itemHeightDp)
                        .background(
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.10f),
                            shape = RoundedCornerShape(12.dp),
                        )
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    WheelPicker(
                        items = hours,
                        initialIndex = value.hour,
                        onIndexSelected = { selectedHour = it },
                        modifier = Modifier.weight(1f),
                    )

                    Text(
                        text = ":",
                        modifier = Modifier.width(separatorWidth),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                    )

                    WheelPicker(
                        items = minutes,
                        initialIndex = value.minute,
                        onIndexSelected = { selectedMinute = it },
                        modifier = Modifier.weight(1f),
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                MaterialButton(
                    text = "Cancel",
                    buttonColor = Color.Transparent,
                    iconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f),
                )
                MaterialButton(
                    text = "Done",
                    cornerRadius = 10.dp,
                    onClick = {
                        onConfirm(TimeValue(selectedHour, selectedMinute))
                        onDismiss()
                    },
                    modifier = Modifier.weight(1.5f),
                )
            }
        }
    }
}


@Composable
private fun WheelPicker(
    items: List<String>,
    initialIndex: Int,
    onIndexSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val itemHeightDp = 48.dp
    val visibleItemCount = 5
    val density = LocalDensity.current
    val itemHeightPx = remember(density) { with(density) { itemHeightDp.toPx() } }

    val listState = rememberLazyListState(initialFirstVisibleItemIndex = initialIndex)
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)

    // Which item is visually centred — updates every frame during scroll so
    // alpha transitions stay smooth without waiting for snap to settle.
    val centerIndex by remember {
        derivedStateOf {
            val offset = listState.firstVisibleItemScrollOffset
            if (offset > itemHeightPx / 2f) {
                listState.firstVisibleItemIndex + 1
            } else {
                listState.firstVisibleItemIndex
            }.coerceIn(items.indices)
        }
    }

    val currentOnIndexSelected by rememberUpdatedState(onIndexSelected)

    // Emit the snapped index only after scrolling fully settles.
    LaunchedEffect(listState.isScrollInProgress) {
        if (!listState.isScrollInProgress) {
            currentOnIndexSelected(listState.firstVisibleItemIndex.coerceIn(items.indices))
        }
    }

    LazyColumn(
        state = listState,
        flingBehavior = flingBehavior,
        // Top/bottom padding lets first and last items reach the centre slot.
        contentPadding = PaddingValues(vertical = itemHeightDp * (visibleItemCount / 2)),
        modifier = modifier
            .height(itemHeightDp * visibleItemCount)
            .fillMaxWidth(),
    ) {
        itemsIndexed(items) { index, item ->
            val distance = abs(index - centerIndex)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(itemHeightDp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = item,
                    style = if (distance == 0) {
                        MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                    } else {
                        MaterialTheme.typography.bodyLarge
                    },
                    color = MaterialTheme.colorScheme.onSurface.copy(
                        alpha = when (distance) {
                            0 -> 1f
                            1 -> 0.45f
                            else -> 0.18f
                        }
                    ),
                )
            }
        }
    }
}


@Preview(showBackground = true, widthDp = 360)
@Composable
private fun TimePickerDialogPreview() {
    MaterialTheme {
        TimePickerContent(
            value = TimeValue(8, 0),
            onConfirm = {},
            onDismiss = {},
        )
    }
}