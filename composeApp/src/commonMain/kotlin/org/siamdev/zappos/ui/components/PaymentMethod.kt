/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.siamdev.zappos.theme.YellowPrimary
import org.siamdev.zappos.ui.screens.checkout.PaymentMethod
import zappos.composeapp.generated.resources.Res
import zappos.composeapp.generated.resources.prompt_pay
import zappos.composeapp.generated.resources.sat_unit

sealed class PaymentIcon {
    data class Vector(val icon: ImageVector, val size: Dp = 24.dp) : PaymentIcon()
    data class Resource(val painter: Painter, val size: Dp = 24.dp) : PaymentIcon()
}

@Composable
fun paymentMethodIcon(method: PaymentMethod): PaymentIcon = when (method) {
    PaymentMethod.NFC_LIGHTNING -> PaymentIcon.Vector(Icons.Default.CreditCard)
    PaymentMethod.BITCOIN_LIGHTNING -> PaymentIcon.Resource(painterResource(Res.drawable.sat_unit))
    PaymentMethod.PROMPT_PAY -> PaymentIcon.Resource(painterResource(Res.drawable.prompt_pay), size = 52.dp)
    PaymentMethod.CASH -> PaymentIcon.Vector(Icons.Default.Money)
}

@Composable
fun PaymentMethodList(
    selectedMethod: PaymentMethod?,
    onSelectMethod: (PaymentMethod) -> Unit,
    showBorder: Boolean = false
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        PaymentMethod.entries.forEach { method ->
            val isSelected = selectedMethod == method
            val icon = paymentMethodIcon(method)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        when {
                            isSelected && showBorder -> YellowPrimary.copy(alpha = 0.1f)
                            isSelected -> MaterialTheme.colorScheme.surfaceVariant
                            else -> Color.Transparent
                        }
                    )
                    .then(
                        if (showBorder) Modifier.border(
                            width = if (isSelected) 1.5.dp else 1.dp,
                            color = if (isSelected) YellowPrimary
                            else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.07f),
                            shape = RoundedCornerShape(12.dp)
                        ) else Modifier
                    )
                    .clickable { onSelectMethod(method) }
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Box(
                    modifier = Modifier.size(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    when (icon) {
                        is PaymentIcon.Vector -> Icon(
                            imageVector = icon.icon,
                            contentDescription = null,
                            tint = when {
                                isSelected && showBorder -> YellowPrimary
                                isSelected -> MaterialTheme.colorScheme.onSurface
                                else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            },
                            modifier = Modifier.size(icon.size)
                        )
                        is PaymentIcon.Resource -> Image(
                            painter = icon.painter,
                            contentDescription = null,
                            modifier = Modifier.size(icon.size),
                            contentScale = ContentScale.Fit
                        )
                    }
                }

                Text(
                    text = method.label,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                    color = when {
                        isSelected && showBorder -> MaterialTheme.colorScheme.onSurface
                        isSelected -> MaterialTheme.colorScheme.onSurface
                        else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                    },
                    modifier = Modifier.weight(1f)
                )

                if (isSelected && showBorder) {
                    Icon(
                        Icons.Default.CheckCircle, null,
                        tint = YellowPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

// Mobile: Dialog
@Composable
fun PaymentMethodDialogCard(
    selectedMethod: PaymentMethod?,
    onSelectMethod: (PaymentMethod) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f))
                .clickable { onDismiss() },
            contentAlignment = Alignment.Center
        ) {
            PaymentMethodDialogContent(
                selectedMethod = selectedMethod,
                onSelectMethod = onSelectMethod
            )
        }
    }
}

@Composable
fun PaymentMethodDialogContent(
    selectedMethod: PaymentMethod?,
    onSelectMethod: (PaymentMethod) -> Unit
) {
    Column(
        modifier = Modifier
            .width(340.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable(enabled = false) {}
            .padding(24.dp)
    ) {
        Text(
            "Choose payment method",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            textAlign = TextAlign.Center
        )

        PaymentMethodList(
            selectedMethod = selectedMethod,
            onSelectMethod = onSelectMethod,
            showBorder = false
        )

        Spacer(Modifier.height(16.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun PaymentMethodDialogContentPreview() {
    MaterialTheme {
        PaymentMethodDialogContent(
            selectedMethod = null,
            onSelectMethod = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PaymentMethodDialogContentSelectedPreview() {
    MaterialTheme {
        PaymentMethodDialogContent(
            selectedMethod = PaymentMethod.CASH,
            onSelectMethod = {}
        )
    }
}