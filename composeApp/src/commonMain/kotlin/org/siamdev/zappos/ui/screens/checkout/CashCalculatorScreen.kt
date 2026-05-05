/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.screens.checkout

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.CurrencyLira
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.siamdev.zappos.theme.YellowPrimary
import org.siamdev.zappos.ui.components.MaterialButton
import org.siamdev.zappos.ui.components.OrderItemList

private val GreenSuccess = Color(0xFF4CAF50)
private val QuickAmounts = listOf("20", "50", "100", "500", "1000")
private val NumpadKeys = listOf(
    listOf("1", "2", "3"),
    listOf("4", "5", "6"),
    listOf("7", "8", "9"),
    listOf(".", "0", null)
)

@Composable
fun CashCalculatorScreen(
    viewModel: CheckoutViewModel,
    onBack: () -> Unit = {}
) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(WindowInsets.statusBars)
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        if (maxWidth >= 900.dp) {
            DesktopCashLayout(viewModel = viewModel, onBack = onBack)
        } else {
            MobileCashLayout(viewModel = viewModel, onBack = onBack)
        }
    }
}

// ── Mobile ───────────────────────────────────────────────────────────────────

@Composable
private fun MobileCashLayout(
    viewModel: CheckoutViewModel,
    onBack: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        CashHeader(onBack = onBack)

        Text(
            text = "ENTER AMOUNT",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
            letterSpacing = 2.sp,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 20.dp)
        ) {
            TotalDueRow(totalFiat = viewModel.totalFiat)
            Spacer(Modifier.height(14.dp))
            ReceivedDisplay(
                receivedAmount = viewModel.receivedAmount,
                isValid = viewModel.isChangeValid,
                onClear = { viewModel.clearReceivedAmount() }
            )
            ChangeRow(viewModel = viewModel)
            Spacer(Modifier.weight(1f))
            QuickAmountRow(onAdd = { viewModel.appendQuickAmount(it) })
            Spacer(Modifier.height(10.dp))
            CashNumpad(viewModel = viewModel)
            Spacer(Modifier.height(12.dp))
            MaterialButton(
                modifier = Modifier.fillMaxWidth(),
                text = "Confirm Cash",
                buttonColor = if (viewModel.isChangeValid) YellowPrimary
                else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                onClick = { viewModel.confirmCash() }
            )
        }

        Spacer(Modifier.height(8.dp))
    }
}

// ── Desktop ──────────────────────────────────────────────────────────────────

@Composable
private fun DesktopCashLayout(
    viewModel: CheckoutViewModel,
    onBack: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        CashHeader(onBack = onBack)

        // Labels
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "ORDER SUMMARY",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                letterSpacing = 2.sp,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "ENTER AMOUNT",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                letterSpacing = 2.sp,
                modifier = Modifier.width(380.dp)
            )
        }

        // Content row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(start = 32.dp, end = 32.dp, bottom = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Left: Order list
            OrderItemList(
                items = viewModel.orderItems,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            )

            // Right: Numpad + totals
            Column(
                modifier = Modifier
                    .width(380.dp)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f), RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(horizontal = 28.dp, vertical = 28.dp)
            ) {
                TotalDueRow(totalFiat = viewModel.totalFiat)
                Spacer(Modifier.height(8.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.07f))
                Spacer(Modifier.height(12.dp))

                ReceivedDisplay(
                    receivedAmount = viewModel.receivedAmount,
                    isValid = viewModel.isChangeValid,
                    onClear = { viewModel.clearReceivedAmount() },
                    isLarge = true
                )

                ChangeRow(viewModel = viewModel)

                Spacer(Modifier.weight(1f))

                QuickAmountRow(onAdd = { viewModel.appendQuickAmount(it) })
                Spacer(Modifier.height(12.dp))
                CashNumpad(viewModel = viewModel)
                Spacer(Modifier.height(16.dp))
                MaterialButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Confirm Cash",
                    buttonColor = if (viewModel.isChangeValid) YellowPrimary
                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                    onClick = { viewModel.confirmCash() }
                )
            }
        }
    }
}


@Composable
private fun TotalDueRow(totalFiat: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "Total Due",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Icon(
                Icons.Default.CurrencyLira, null,
                modifier = Modifier.size(13.dp),
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            Text(
                totalFiat,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun ReceivedDisplay(
    receivedAmount: String,
    isValid: Boolean,
    onClear: () -> Unit,
    isLarge: Boolean = false
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "RECEIVED",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.35f),
                letterSpacing = 2.sp
            )
            AnimatedVisibility(
                visible = receivedAmount.isNotEmpty(),
                enter = fadeIn(tween(150)) + scaleIn(tween(150)),
                exit = fadeOut(tween(100)) + scaleOut(tween(100))
            ) {
                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(YellowPrimary.copy(alpha = 0.1f))
                        .border(1.dp, YellowPrimary.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                        .clickable { onClear() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Clear,
                        contentDescription = "Clear",
                        tint = YellowPrimary,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
        Spacer(Modifier.height(4.dp))
        Text(
            text = receivedAmount.ifEmpty { "0" },
            style = if (isLarge) MaterialTheme.typography.displaySmall
                    else MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.Bold,
            color = if (isValid) YellowPrimary else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.End,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun ChangeRow(viewModel: CheckoutViewModel) {
    AnimatedVisibility(
        visible = viewModel.isChangeValid,
        enter = fadeIn(tween(200)) + expandVertically(tween(200)),
        exit = fadeOut(tween(150)) + shrinkVertically(tween(150))
    ) {
        Column {
            Spacer(Modifier.height(14.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.07f))
            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(18.dp)
                            .clip(RoundedCornerShape(50))
                            .background(GreenSuccess),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Check, null,
                            tint = Color.White,
                            modifier = Modifier.size(11.dp)
                        )
                    }
                    Text(
                        "Change",
                        style = MaterialTheme.typography.bodySmall,
                        color = GreenSuccess,
                        fontWeight = FontWeight.Medium
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Icon(
                        Icons.Default.CurrencyLira, null,
                        modifier = Modifier.size(14.dp),
                        tint = GreenSuccess
                    )
                    Text(
                        viewModel.formatChange(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = GreenSuccess
                    )
                }
            }
        }
    }
}

@Composable
private fun CashHeader(
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(YellowPrimary.copy(alpha = 0.15f))
                .clickable { onBack() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack, null,
                tint = YellowPrimary,
                modifier = Modifier.size(18.dp)
            )
        }
        Spacer(Modifier.width(14.dp))
        Column {
            Text(
                "Cash Payment",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                "Enter amount received",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f)
            )
        }
    }
}

@Composable
private fun QuickAmountRow(
    onAdd: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        QuickAmounts.forEach { amount ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(YellowPrimary.copy(alpha = 0.1f))
                    .border(1.dp, YellowPrimary.copy(alpha = 0.25f), RoundedCornerShape(10.dp))
                    .clickable { onAdd(amount) }
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "+$amount",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = YellowPrimary
                )
            }
        }
    }
}

@Composable
private fun CashNumpad(
    viewModel: CheckoutViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        NumpadKeys.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                row.forEach { key ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1.6f)
                            .clip(RoundedCornerShape(14.dp))
                            .background(
                                if (key == null)
                                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f)
                                else MaterialTheme.colorScheme.surfaceVariant
                            )
                            .clickable {
                                if (key == null) viewModel.deleteCashDigit()
                                else viewModel.appendCashDigit(key)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (key == null) {
                            Icon(
                                Icons.AutoMirrored.Filled.Backspace,
                                contentDescription = "Delete",
                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.55f),
                                modifier = Modifier.size(22.dp)
                            )
                        } else {
                            Text(
                                text = key,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }
    }
}

// ── Previews ──────────────────────────────────────────────────────────────────

private val cashPreviewViewModel = CheckoutViewModel().apply {
    syncFromMenu(
        items = listOf(CheckoutItem("Mocha", 2u, "70.00", "17,500")),
        fiat = "790.00",
        sat = "35,000"
    )
}

@Preview(showBackground = true, widthDp = 411, heightDp = 891)
@Composable
fun CashCalculatorMobilePreview() {
    MaterialTheme {
        CashCalculatorScreen(viewModel = cashPreviewViewModel)
    }
}

@Preview(showBackground = true, widthDp = 1280, heightDp = 800)
@Composable
fun CashCalculatorDesktopPreview() {
    MaterialTheme {
        CashCalculatorScreen(viewModel = cashPreviewViewModel)
    }
}