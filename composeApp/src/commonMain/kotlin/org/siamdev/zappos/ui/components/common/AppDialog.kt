/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.components.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.tooling.preview.Preview

/**
 * A themed confirmation dialog that matches the app's card/button design language.
 *
 * @param icon        Optional leading icon displayed in a tinted box above the title.
 * @param iconTint    Tint color for the icon and its background wash. Defaults to [MaterialTheme.colorScheme.primary].
 * @param title       Short heading shown in bold.
 * @param message     Supporting body text.
 * @param confirmText Label for the primary action button.
 * @param dismissText Label for the secondary (cancel) button.
 * @param confirmButtonColor Background of the confirm button. Pass [MaterialTheme.colorScheme.error] for destructive actions.
 * @param onConfirm   Called when the user taps the confirm button.
 * @param onDismiss   Called when the user taps cancel or dismisses the dialog.
 */
@Composable
fun AppDialog(
    icon: ImageVector? = null,
    iconTint: Color = MaterialTheme.colorScheme.primary,
    title: String,
    message: String,
    confirmText: String = "Confirm",
    dismissText: String = "Cancel",
    confirmButtonColor: Color = MaterialTheme.colorScheme.primary,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surface)
                .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp))
                .padding(24.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                if (icon != null) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(iconTint.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            modifier = Modifier.size(26.dp),
                            tint = iconTint
                        )
                    }
                    Spacer(Modifier.height(16.dp))
                }

                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MaterialButton(
                        modifier = Modifier.weight(1f).height(48.dp),
                        text = dismissText,
                        buttonColor = MaterialTheme.colorScheme.surfaceVariant,
                        showBorder = true,
                        iconColor = MaterialTheme.colorScheme.onSurface,
                        onClick = onDismiss
                    )
                    MaterialButton(
                        modifier = Modifier.weight(1f).height(48.dp),
                        text = confirmText,
                        buttonColor = confirmButtonColor,
                        onClick = onConfirm
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 411, name = "AppDialog · Destructive")
@Composable
private fun AppDialogDestructivePreview() {
    MaterialTheme {
        AppDialog(
            icon = Icons.Default.Delete,
            iconTint = MaterialTheme.colorScheme.error,
            title = "Delete Product",
            message = "Are you sure you want to delete \"Matcha Latte\"? This action cannot be undone.",
            confirmText = "Delete",
            confirmButtonColor = MaterialTheme.colorScheme.error,
            onConfirm = {},
            onDismiss = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 411, name = "AppDialog · Info")
@Composable
private fun AppDialogInfoPreview() {
    MaterialTheme {
        AppDialog(
            icon = Icons.Default.Info,
            title = "Confirm Action",
            message = "Do you want to proceed with this operation?",
            confirmText = "Proceed",
            onConfirm = {},
            onDismiss = {}
        )
    }
}