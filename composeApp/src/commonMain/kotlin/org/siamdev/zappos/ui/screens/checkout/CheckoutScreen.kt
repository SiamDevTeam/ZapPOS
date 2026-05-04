/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.screens.checkout

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.siamdev.zappos.LocalCheckoutVM
import org.siamdev.zappos.LocalMenuVM
import org.siamdev.zappos.theme.YellowPrimary
import org.siamdev.zappos.ui.components.MaterialButton
import org.siamdev.zappos.ui.components.PaymentMethodDialogCard
import org.siamdev.zappos.ui.components.PaymentMethodList
import zappos.composeapp.generated.resources.Res
import zappos.composeapp.generated.resources.sat_unit

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
@Composable
fun CheckoutContent(
    viewModel: CheckoutViewModel,
    onBack: () -> Unit = {},
    onSuccess: () -> Unit = {}
) {
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
        val isDesktop = maxWidth >= 600.dp

        if (isDesktop) {
            DesktopCheckoutLayout(viewModel = viewModel, onBack = onBack)
        } else {
            MobileCheckoutLayout(viewModel = viewModel, onBack = onBack)
            if (viewModel.step == CheckoutStep.SELECT_PAYMENT) {
                PaymentMethodDialogCard(
                    selectedMethod = viewModel.selectedMethod,
                    onSelectMethod = { method -> viewModel.selectMethod(method); viewModel.confirmPayment() },
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

        Text(
            text = "ORDER SUMMARY",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
            letterSpacing = 2.sp,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
        )

        CheckoutOrderList(
            viewModel = viewModel,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 20.dp)
        )

        Spacer(Modifier.height(12.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            Text(
                text = "SUMMARY",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                letterSpacing = 2.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            CheckoutSummaryCard(
                viewModel = viewModel,
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
                text = "SUMMARY",
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
            CheckoutOrderList(
                viewModel = viewModel,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            )

            // Right: Summary card + Payment
            Column(
                modifier = Modifier
                    .width(380.dp)
                    .wrapContentHeight(),
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                // Summary card — กรอบแยกชัดเจน
                CheckoutSummaryCard(
                    viewModel = viewModel,
                    taxPercent = viewModel.taxPercent,
                    onTaxChange = { viewModel.setTax(it) }
                )

                Spacer(Modifier.height(20.dp))

                Text(
                    text = "PAYMENT METHOD",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                    letterSpacing = 2.sp,
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


@Composable
private fun CheckoutOrderList(
    viewModel: CheckoutViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(8.dp)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 4.dp)
        ) {
            itemsIndexed(viewModel.orderItems) { index, item ->
                CheckoutItemRow(item = item, isEven = index % 2 == 1)
            }
        }
    }
}

@Composable
private fun CheckoutSummaryCard(
    viewModel: CheckoutViewModel,
    taxPercent: Float,
    onTaxChange: (Float) -> Unit
) {
    val subtotal = viewModel.totalFiat.replace(",", "").toDoubleOrNull() ?: 0.0
    val taxAmount = subtotal * taxPercent / 100
    val grandTotal = subtotal + taxAmount
    val satValue = viewModel.totalSat.replace(",", "").toDoubleOrNull() ?: 0.0
    val grandTotalSat = satValue * (1 + taxPercent / 100)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f), RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Subtotal
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Subtotal", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
            Column(horizontalAlignment = Alignment.End) {
                PriceRowFiat(value = viewModel.totalFiat, iconSize = 13.dp, textStyle = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                PriceRowSat(value = viewModel.totalSat, iconSize = 12.dp, textStyle = MaterialTheme.typography.bodySmall)
            }
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f))

        // VAT selector
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("VAT", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(0f, 7f, 10f).forEach { rate ->
                    val isActive = taxPercent == rate
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isActive) YellowPrimary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f))
                            .clickable { onTaxChange(rate) }
                            .padding(horizontal = 10.dp, vertical = 5.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (rate == 0f) "No VAT" else "${rate.toInt()}%",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isActive) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    }
                }
            }
        }

        // VAT amount
        if (taxPercent > 0f) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("VAT ${taxPercent.toInt()}%", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f))
                PriceRowFiat(
                    value = formatDouble(taxAmount),
                    iconSize = 12.dp,
                    textStyle = MaterialTheme.typography.bodySmall,
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f)
                )
            }
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f))

        // Grand total
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Total", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            Column(horizontalAlignment = Alignment.End) {
                PriceRowFiat(value = formatDouble(grandTotal), iconSize = 16.dp, textStyle = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                PriceRowSat(value = formatDouble(grandTotalSat), iconSize = 13.dp, textStyle = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
private fun PriceRowFiat(
    value: String,
    iconSize: androidx.compose.ui.unit.Dp,
    textStyle: androidx.compose.ui.text.TextStyle,
    fontWeight: FontWeight = FontWeight.Normal,
    tint: Color = MaterialTheme.colorScheme.onSurface,
    color: Color = MaterialTheme.colorScheme.onSurface
) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        Icon(Icons.Default.CurrencyLira, null, modifier = Modifier.size(iconSize), tint = tint)
        Text(value, style = textStyle, fontWeight = fontWeight, color = color)
    }
}

@Composable
private fun PriceRowSat(
    value: String,
    iconSize: androidx.compose.ui.unit.Dp,
    textStyle: androidx.compose.ui.text.TextStyle
) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        Icon(painter = painterResource(Res.drawable.sat_unit), contentDescription = null, tint = Color(0xFFFFB700), modifier = Modifier.size(iconSize))
        Text(value, style = textStyle, color = Color(0xFFFFB700))
    }
}

@Composable
internal fun CheckoutHeader(onBack: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 12.dp),
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
            Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = YellowPrimary, modifier = Modifier.size(18.dp))
        }
        Spacer(Modifier.width(14.dp))
        Column {
            Text("Checkout", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text("Review your order", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f))
        }
    }
}

@Composable
internal fun CheckoutItemRow(item: CheckoutItem, isEven: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(if (isEven) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.04f) else Color.Transparent)
            .padding(horizontal = 12.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = item.name, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
            Text(text = "×${item.count}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f))
        }
        Column(horizontalAlignment = Alignment.End) {
            PriceRowFiat(value = item.priceBaht, iconSize = 13.dp, textStyle = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
            PriceRowSat(value = item.priceSat, iconSize = 12.dp, textStyle = MaterialTheme.typography.bodySmall)
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
            CheckoutItem("Iced Coffee", 2u, "65.00", "16,250"),
            CheckoutItem("Thai Tea", 4u, "60.00", "15,000"),
            CheckoutItem("Green Tea", 1u, "55.00", "13,750"),
            CheckoutItem("Hot Chocolate", 2u, "85.00", "21,250"),
            CheckoutItem("Milk", 1u, "50.00", "12,500"),
            CheckoutItem("Matcha Coffee", 2u, "100.00", "26,000"),
            CheckoutItem("Vanilla Latte", 1u, "85.00", "21,250"),
            CheckoutItem("Hazelnut Latte", 2u, "90.00", "22,500"),
            CheckoutItem("Cold Brew", 1u, "75.00", "18,750"),
            CheckoutItem("Nitro Coffee", 2u, "95.00", "23,750"),
            CheckoutItem("Oat Milk Latte", 1u, "90.00", "22,500"),
            CheckoutItem("Coconut Latte", 3u, "85.00", "21,250")
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