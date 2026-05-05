/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.screens.checkout

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.siamdev.zappos.LocalCheckoutVM
import org.siamdev.zappos.LocalMenuVM
import org.siamdev.zappos.theme.YellowPrimary
import org.siamdev.zappos.ui.components.FiatAmount
import org.siamdev.zappos.ui.components.MaterialButton
import org.siamdev.zappos.ui.components.PaymentMethodDialogCard
import org.siamdev.zappos.ui.components.PaymentMethodList
import org.siamdev.zappos.ui.components.OrderItemList
import org.siamdev.zappos.ui.components.OrderSummaryCard
import org.siamdev.zappos.ui.components.SatAmount


@Composable
fun CheckoutScreen(
    onBack: () -> Unit = {},
    onSuccess: () -> Unit = {}
) {
    val menuVM = LocalMenuVM.current
    val checkoutVM = LocalCheckoutVM.current

    LaunchedEffect(menuVM.selectedKeys.toList()) {
        checkoutVM.syncFromMenu(
            items = menuVM.selectedKeys.map { key ->
                val item = menuVM.items.first { it.id == key }
                CheckoutItem(
                    name = item.name,
                    count = item.count,
                    priceBaht = item.priceBaht,
                    priceSat = item.priceSat
                )
            },
            fiat = menuVM.totalFiat,
            sat = menuVM.totalSat
        )
    }

    CheckoutContent(
        viewModel = checkoutVM,
        onBack = onBack,
        onSuccess = {
            menuVM.clearAllItems()
            onSuccess()
        }
    )
}

// Root content / step router

@Composable
fun CheckoutContent(
    viewModel: CheckoutViewModel,
    onBack: () -> Unit = {},
    onSuccess: () -> Unit = {}
) {
    // Sub-screens handled by separate composables — exit early
    when (viewModel.step) {
        CheckoutStep.CASH_CALCULATOR -> {
            CashCalculatorScreen(viewModel = viewModel, onBack = { viewModel.backToSelectPayment() })
            return
        }
        CheckoutStep.PROCESSING -> {
            PaymentProcessingScreen(
                viewModel = viewModel,
                onConfirm = { viewModel.confirmProcessing(); onSuccess() },
                onBack = { viewModel.backToSelectPayment() }
            )
            return
        }
        else -> {}
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(WindowInsets.statusBars)
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        if (maxWidth >= 750.dp) {
            DesktopCheckoutLayout(viewModel = viewModel, onBack = onBack)
        } else {
            MobileCheckoutLayout(viewModel = viewModel, onBack = onBack)
            if (viewModel.step == CheckoutStep.SELECT_PAYMENT) {
                PaymentMethodDialogCard(
                    selectedMethod = viewModel.selectedMethod,
                    onSelectMethod = { method ->
                        viewModel.selectMethod(method)
                        viewModel.confirmPayment()
                    },
                    onDismiss = { viewModel.backToOrder() }
                )
            }
        }
    }
}


@Composable
private fun MobileCheckoutLayout(
    viewModel: CheckoutViewModel,
    onBack: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        CheckoutHeader(onBack = onBack)

        SectionLabel(text = "ORDER SUMMARY", modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp))

        OrderItemList(
            items = viewModel.orderItems,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 20.dp)
        )

        Spacer(Modifier.height(12.dp))

        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
            SectionLabel(text = "SUMMARY", modifier = Modifier.padding(bottom = 8.dp))

            OrderSummaryCard(
                subtotalFiat = viewModel.totalFiat,
                subtotalSat = viewModel.totalSat,
                taxPercent = viewModel.taxPercent,
                onTaxChange = { viewModel.setTax(it) }
            )

            Spacer(Modifier.height(12.dp))

            MaterialButton(
                modifier = Modifier.fillMaxWidth(),
                text = "Choose Payment Method",
                iconStart = Icons.Default.Payment,
                onClick = { viewModel.openSelectPayment() }
            )
        }

        Spacer(Modifier.height(8.dp))
    }
}

@Composable
private fun DesktopCheckoutLayout(
    viewModel: CheckoutViewModel,
    onBack: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        CheckoutHeader(onBack = onBack)

        // Column labels
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SectionLabel(text = "ORDER SUMMARY", modifier = Modifier.weight(1f))
            SectionLabel(text = "SUMMARY", modifier = Modifier.width(380.dp))
        }

        // Main content row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(start = 32.dp, end = 32.dp, bottom = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Left: scrollable order list
            OrderItemList(
                items = viewModel.orderItems,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            )

            // Right: summary + payment
            Column(
                modifier = Modifier
                    .width(380.dp)
                    .wrapContentHeight(),
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                OrderSummaryCard(
                    subtotalFiat = viewModel.totalFiat,
                    subtotalSat = viewModel.totalSat,
                    taxPercent = viewModel.taxPercent,
                    onTaxChange = { viewModel.setTax(it) }
                )

                Spacer(Modifier.height(20.dp))

                SectionLabel(
                    text = "PAYMENT METHOD",
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                PaymentMethodList(
                    selectedMethod = viewModel.selectedMethod,
                    onSelectMethod = { method ->
                        viewModel.selectMethod(method)
                        viewModel.confirmPayment()
                    },
                    showBorder = true
                )
            }
        }
    }
}

/** Uppercase spaced section label — reused in both layouts. */
@Composable
internal fun SectionLabel(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
        letterSpacing = 2.sp,
        modifier = modifier
    )
}

@Composable
internal fun CheckoutHeader(onBack: () -> Unit) {
    Row(
        modifier = Modifier
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
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null,
                tint = YellowPrimary,
                modifier = Modifier.size(18.dp)
            )
        }
        Spacer(Modifier.width(14.dp))
        Column {
            Text(
                text = "Checkout",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Review your order",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f)
            )
        }
    }
}

@Composable
internal fun CheckoutItemRow(item: CheckoutItem, isEven: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(
                if (isEven) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.04f)
                else androidx.compose.ui.graphics.Color.Transparent
            )
            .padding(horizontal = 12.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "×${item.count}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f)
            )
        }
        Column(horizontalAlignment = Alignment.End) {
            FiatAmount(
                value = item.priceBaht,
                iconSize = 13.dp,
                textStyle = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
            SatAmount(
                value = item.priceSat,
                iconSize = 12.dp,
                textStyle = MaterialTheme.typography.bodySmall
            )
        }
    }
}

internal fun formatDouble(value: Double): String {
    val intPart = value.toLong()
    val decPart = ((value - intPart) * 100).toInt()
    val intStr = intPart.toString().reversed().chunked(3).joinToString(",").reversed()
    return "$intStr.${decPart.toString().padStart(2, '0')}"
}


private val previewViewModel = CheckoutViewModel().apply {
    syncFromMenu(
        items = listOf(
            CheckoutItem("Mocha", 2u, "70.00", "17,500"),
            CheckoutItem("Matcha Latte", 1u, "100.00", "26,000"),
            CheckoutItem("Latte", 3u, "70.00", "17,500"),
            CheckoutItem("Espresso", 2u, "50.00", "12,500"),
            CheckoutItem("Americano", 1u, "60.00", "15,000"),
            CheckoutItem("Cappuccino", 2u, "75.00", "18,750"),
            CheckoutItem("Flat White", 1u, "80.00", "20,000"),
            CheckoutItem("Caramel Macchiato", 3u, "90.00", "22,500"),
        ),
        fiat = "61,020.00",
        sat = "1,683,138"
    )
}

@Preview(showBackground = true, widthDp = 411, heightDp = 891)
@Composable
fun CheckoutScreenMobilePreview() {
    MaterialTheme { CheckoutContent(viewModel = previewViewModel) }
}

@Preview(showBackground = true, widthDp = 1280, heightDp = 800)
@Composable
fun CheckoutScreenDesktopPreview() {
    MaterialTheme { CheckoutContent(viewModel = previewViewModel) }
}