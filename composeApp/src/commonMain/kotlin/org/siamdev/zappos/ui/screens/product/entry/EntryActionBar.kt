/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.screens.product.entry

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.siamdev.zappos.ui.components.common.MaterialButton

@Composable
internal fun EntryActionBar(
    isFormValid: Boolean,
    onSave: () -> Unit,
    onDiscard: () -> Unit,
) {
    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        MaterialButton(
            modifier = Modifier.weight(1f).height(48.dp),
            text = "Save product",
            iconStart = Icons.Default.Check,
            enabled = isFormValid,
            onClick = onSave,
        )
        IconButton(
            onClick = onDiscard,
            modifier =
                Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.errorContainer),
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Discard",
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(20.dp),
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 411, name = "ActionBar · save enabled")
@Composable
private fun EntryActionBarEnabledPreview() {
    MaterialTheme { EntryActionBar(isFormValid = true, onSave = {}, onDiscard = {}) }
}

@Preview(showBackground = true, widthDp = 411, name = "ActionBar · save disabled")
@Composable
private fun EntryActionBarDisabledPreview() {
    MaterialTheme { EntryActionBar(isFormValid = false, onSave = {}, onDiscard = {}) }
}
