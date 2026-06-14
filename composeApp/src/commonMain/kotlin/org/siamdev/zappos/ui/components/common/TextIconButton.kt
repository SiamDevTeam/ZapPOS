/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.components.common

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun TextIconButton(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    fontWeight: FontWeight = FontWeight.Normal,
) {
    TextButton(
        onClick = onClick,
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 2.dp),
    ) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(16.dp))
        Spacer(Modifier.width(4.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = fontWeight,
            color = MaterialTheme.colorScheme.primary,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TextIconButtonPreview() {
    MaterialTheme {
        Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TextIconButton(
                icon = Icons.Default.Add,
                text = "Add Item",
                onClick = {}
            )
            TextIconButton(
                icon = Icons.Default.Add,
                text = "Bold Action",
                fontWeight = FontWeight.Bold,
                onClick = {}
            )
        }
    }
}
