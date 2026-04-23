package org.siamdev.zappos.ui.screens.checkout

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.siamdev.zappos.LocalMainMenuViewModel
import org.siamdev.zappos.theme.YellowPrimary
import org.siamdev.zappos.ui.components.MaterialButton
import zappos.composeapp.generated.resources.Res
import zappos.composeapp.generated.resources.sat_unit

@Composable
fun CheckoutScreen(
    onBack: () -> Unit = {},
    onSuccess: () -> Unit = {}
) {
    val menuViewModel = LocalMainMenuViewModel.current
    val checkoutViewModel = remember(menuViewModel.selectedKeys.toList()) {
        CheckoutViewModel(
            orderItems = menuViewModel.selectedKeys.map { key ->
                val item = menuViewModel.items.first { it.id == key }
                CheckoutItem(
                    name = item.name,
                    count = item.count,
                    priceBaht = item.priceBaht,
                    priceSat = item.priceSat
                )
            },
            totalFiat = menuViewModel.totalFiat,
            totalSat = menuViewModel.totalSat
        )
    }

    CheckoutContent(
        viewModel = checkoutViewModel,
        onBack = onBack,
        onSuccess = {
            menuViewModel.clearAllItems()
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
            CashCalculatorScreen(
                viewModel = viewModel,
                onBack = { viewModel.backToSelectPayment() }
            )
            return
        }
        CheckoutStep.PROCESSING -> {
            PaymentProcessingScreen(
                viewModel = viewModel,
                onConfirm = {
                    viewModel.confirmProcessing()
                    onSuccess()
                },
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
        }

        if (viewModel.step == CheckoutStep.SELECT_PAYMENT) {
            PaymentMethodDialog(
                viewModel = viewModel,
                onDismiss = { viewModel.backToOrder() }
            )
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

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 4.dp)
        ) {
            items(viewModel.orderItems) { item ->
                CheckoutItemRow(item = item)
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            CheckoutTotalSection(
                viewModel = viewModel,
                onCheckout = { viewModel.openSelectPayment() }
            )
        }
    }
}

@Composable
private fun DesktopCheckoutLayout(
    viewModel: CheckoutViewModel,
    onBack: () -> Unit
) {
    Row(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(start = 32.dp, end = 16.dp, bottom = 24.dp)
        ) {
            CheckoutHeader(onBack = onBack)

            Text(
                text = "ORDER SUMMARY",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                letterSpacing = 2.sp,
                modifier = Modifier.padding(vertical = 12.dp)
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(viewModel.orderItems) { item ->
                    CheckoutItemRow(item = item)
                }
            }
        }

        Column(
            modifier = Modifier
                .width(360.dp)
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.surface)
                .padding(24.dp)
        ) {
            Text(
                text = "Payment Summary",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "${viewModel.orderItems.sumOf { it.count.toInt() }} items · ${viewModel.orderItems.size} types",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f),
                modifier = Modifier.padding(top = 2.dp, bottom = 20.dp)
            )

            HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f))
            Spacer(Modifier.height(16.dp))

            viewModel.orderItems.forEach { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(22.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(YellowPrimary.copy(alpha = 0.12f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${item.count}",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = YellowPrimary,
                                fontSize = 10.sp
                            )
                        }
                        Text(
                            text = item.name,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                            maxLines = 1
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CurrencyLira,
                            contentDescription = null,
                            modifier = Modifier.size(11.dp),
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                        Text(
                            text = item.priceBaht,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
            }

            Spacer(Modifier.weight(1f))
            HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f))
            Spacer(Modifier.height(16.dp))

            CheckoutTotalSection(
                viewModel = viewModel,
                onCheckout = { viewModel.openSelectPayment() }
            )
        }
    }
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
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null,
                tint = YellowPrimary,
                modifier = Modifier.size(18.dp)
            )
        }
        Spacer(Modifier.width(14.dp))
        Column {
            Text(
                "Checkout",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                "Review your order",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f)
            )
        }
    }
}

@Composable
internal fun CheckoutItemRow(item: CheckoutItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "×${item.count}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f)
            )
        }
        Column(horizontalAlignment = Alignment.End) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CurrencyLira,
                    contentDescription = null,
                    modifier = Modifier.size(13.dp),
                    tint = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = item.priceBaht,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.sat_unit),
                    contentDescription = null,
                    tint = Color(0xFFFFB700),
                    modifier = Modifier.size(12.dp)
                )
                Text(
                    text = item.priceSat,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFFFFB700)
                )
            }
        }
    }
    HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f))
}

@Composable
internal fun CheckoutTotalSection(
    viewModel: CheckoutViewModel,
    onCheckout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Total",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
            Column(horizontalAlignment = Alignment.End) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CurrencyLira,
                        contentDescription = null,
                        modifier = Modifier.size(15.dp),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        viewModel.totalFiat,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.sat_unit),
                        contentDescription = null,
                        tint = Color(0xFFFFB700),
                        modifier = Modifier.size(13.dp)
                    )
                    Text(
                        viewModel.totalSat,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFFFB700)
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        MaterialButton(
            modifier = Modifier.fillMaxWidth(),
            text = "Choose Payment Method",
            iconStart = Icons.Default.Payment,
            onClick = onCheckout
        )
    }
}

@Composable
private fun PaymentMethodDialog(
    viewModel: CheckoutViewModel,
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
            Column(
                modifier = Modifier
                    .width(340.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .clickable(enabled = false) {}
                    .padding(24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Choose payment method",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f))
                            .clickable { onDismiss() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Close, null, modifier = Modifier.size(16.dp))
                    }
                }

                Spacer(Modifier.height(16.dp))

                PaymentMethod.entries.forEach { method ->
                    val isSelected = viewModel.selectedMethod == method
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (isSelected) YellowPrimary.copy(alpha = 0.1f)
                                else MaterialTheme.colorScheme.background
                            )
                            .border(
                                width = if (isSelected) 1.5.dp else 0.dp,
                                color = if (isSelected) YellowPrimary else Color.Transparent,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable { viewModel.selectMethod(method) }
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(
                                    if (isSelected) YellowPrimary.copy(alpha = 0.2f)
                                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = paymentMethodIcon(method),
                                contentDescription = null,
                                tint = if (isSelected) YellowPrimary
                                else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Text(
                            text = method.label,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                            color = if (isSelected) MaterialTheme.colorScheme.onSurface
                            else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                MaterialButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Confirm",
                    onClick = { if (viewModel.selectedMethod != null) viewModel.confirmPayment() }
                )
            }
        }
    }
}

internal fun paymentMethodIcon(method: PaymentMethod) = when (method) {
    PaymentMethod.NFC_LIGHTNING -> Icons.Default.CreditCard
    PaymentMethod.BITCOIN_LIGHTNING -> Icons.Default.Bolt
    PaymentMethod.PROMPT_PAY -> Icons.Default.QrCode
    PaymentMethod.CASH -> Icons.Default.Money
}

private val previewItems = listOf(
    CheckoutItem("Mocha", 2u, "70.00", "17,500"),
    CheckoutItem("Matcha Latte", 1u, "100.00", "26,000"),
    CheckoutItem("Latte", 3u, "70.00", "17,500")
)

private val previewViewModel = CheckoutViewModel(
    orderItems = previewItems,
    totalFiat = "380.00",
    totalSat = "95,000"
)

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