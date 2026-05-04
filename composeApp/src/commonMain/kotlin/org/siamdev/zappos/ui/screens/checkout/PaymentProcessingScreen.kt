/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.screens.checkout

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.siamdev.zappos.theme.YellowPrimary
import org.siamdev.zappos.ui.components.MaterialButton
import org.siamdev.zappos.utils.DateTimeUtils
import zappos.composeapp.generated.resources.Res
import zappos.composeapp.generated.resources.sat_unit

@Composable
fun PaymentProcessingScreen(
    viewModel: CheckoutViewModel,
    onConfirm: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    val txId = remember { "TX${(10000..99999).random()}" }
    val title = when (viewModel.selectedMethod) {
        PaymentMethod.BITCOIN_LIGHTNING -> "Lightning Invoice"
        PaymentMethod.NFC_LIGHTNING -> "NFC Lightning"
        PaymentMethod.PROMPT_PAY -> "Prompt Pay"
        PaymentMethod.CASH -> "Cash Payment"
        null -> "Payment"
    }
    val showQr = viewModel.selectedMethod == PaymentMethod.BITCOIN_LIGHTNING ||
            viewModel.selectedMethod == PaymentMethod.PROMPT_PAY

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(WindowInsets.statusBars)
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        val isDesktop = maxWidth >= 750.dp

        if (isDesktop) {
            DesktopProcessingLayout(
                viewModel = viewModel,
                txId = txId,
                title = title,
                showQr = showQr,
                onConfirm = onConfirm,
                onBack = onBack
            )
        } else {
            MobileProcessingLayout(
                viewModel = viewModel,
                txId = txId,
                title = title,
                showQr = showQr,
                onConfirm = onConfirm,
                onBack = onBack
            )
        }
    }
}

@Composable
private fun MobileProcessingLayout(
    viewModel: CheckoutViewModel,
    txId: String,
    title: String,
    showQr: Boolean,
    onConfirm: () -> Unit,
    onBack: () -> Unit
) {
    var orderExpanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {

        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(YellowPrimary.copy(alpha = 0.15f))
                    .clickable { onBack() }
                    .align(Alignment.CenterStart),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = YellowPrimary, modifier = Modifier.size(18.dp))
            }
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // TX ID bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 20.dp, vertical = 10.dp)
        ) {
            Text(
                text = txId,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f)
            )
        }

        // Scrollable content
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // QR
            if (showQr) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f), RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(200.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.background),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.QrCode2, null,
                                modifier = Modifier.size(170.dp),
                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.background)
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = "lnbc50n1p588zyfpp.....52mczpfq7fatgrfh",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                                maxLines = 1
                            )
                        }
                    }
                }
            }

            // Order Summary toggle
            item {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(
                                RoundedCornerShape(
                                    topStart = 12.dp, topEnd = 12.dp,
                                    bottomStart = if (orderExpanded) 0.dp else 12.dp,
                                    bottomEnd = if (orderExpanded) 0.dp else 12.dp
                                )
                            )
                            .background(MaterialTheme.colorScheme.surface)
                            .border(
                                1.dp,
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                                RoundedCornerShape(
                                    topStart = 12.dp, topEnd = 12.dp,
                                    bottomStart = if (orderExpanded) 0.dp else 12.dp,
                                    bottomEnd = if (orderExpanded) 0.dp else 12.dp
                                )
                            )
                            .clickable { orderExpanded = !orderExpanded }
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Default.Receipt, null, modifier = Modifier.size(16.dp), tint = YellowPrimary)
                            Text(
                                text = "Order Summary",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(YellowPrimary.copy(alpha = 0.15f))
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "${viewModel.orderItems.size} items",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = YellowPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        Icon(
                            imageVector = if (orderExpanded) Icons.Default.KeyboardArrowUp
                            else Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    }

                    AnimatedVisibility(
                        visible = orderExpanded,
                        enter = expandVertically(),
                        exit = shrinkVertically()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp))
                                .border(
                                    1.dp,
                                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                                    RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp)
                                )
                                .background(MaterialTheme.colorScheme.surface)
                                .padding(8.dp)
                        ) {
                            viewModel.orderItems.forEachIndexed { index, item ->
                                CheckoutItemRow(item = item, isEven = index % 2 == 1)
                            }
                        }
                    }
                }
            }

            // Detail card
            item {
                ProcessingDetailCard(viewModel = viewModel, txId = txId)
            }
        }

        // Done button
        MaterialButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            text = "Done",
            onClick = onConfirm
        )
    }
}


@Composable
private fun DesktopProcessingLayout(
    viewModel: CheckoutViewModel,
    txId: String,
    title: String,
    showQr: Boolean,
    onConfirm: () -> Unit,
    onBack: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {

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
                Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = YellowPrimary, modifier = Modifier.size(18.dp))
            }
            Spacer(Modifier.width(14.dp))
            Column {
                Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                Text(txId, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f))
            }
        }

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
                text = "PAYMENT DETAIL",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                letterSpacing = 2.sp,
                modifier = Modifier.width(380.dp)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(start = 32.dp, end = 32.dp, bottom = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Left: QR ขนาดคงที่บน + list ล่างใช้พื้นที่ที่เหลือ
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (showQr) {
                    // QR — กว้างเต็ม สูงคงที่ ไม่ใช้ aspectRatio
                    ProcessingQrSection(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    )
                }

                // List — ขยายพื้นที่ที่เหลือ
                ProcessingOrderList(
                    viewModel = viewModel,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            }

            // Right: Detail card + Done button
            Column(
                modifier = Modifier
                    .width(380.dp)
                    .wrapContentHeight(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ProcessingDetailCard(viewModel = viewModel, txId = txId)

                MaterialButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Done",
                    onClick = onConfirm
                )
            }
        }
    }
}

// แก้ QrSection — ไม่ใช้ aspectRatio แล้ว ใช้ fillMaxHeight แทน
@Composable
private fun ProcessingQrSection(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f), RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // QR code section
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.QrCode2, null,
                modifier = Modifier.fillMaxSize(0.85f),
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Invoice",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                letterSpacing = 1.sp,
                modifier = Modifier.padding(bottom = 6.dp)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = 10.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "lnbc50n1p588zyfpp.....52mczpfq7fatgrfh",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                    maxLines = 2
                )
            }
        }
    }
}

@Composable
private fun ProcessingOrderList(
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
private fun ProcessingDetailCard(
    viewModel: CheckoutViewModel,
    txId: String,
    modifier: Modifier = Modifier
) {
    val taxPercent = viewModel.taxPercent
    val hasVat = taxPercent > 0f

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f), RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        listOf(
            "Order ID" to txId,
            "Status" to "Pending",
            "Date" to DateTimeUtils.currentDateString(),
            "Time" to DateTimeUtils.currentTimeString()
        ).forEach { (label, value) ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                Text(
                    value,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold,
                    color = if (value == "Pending") Color(0xFFFF9800) else MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("VAT", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
            Text(
                text = if (hasVat) "${taxPercent.toInt()}%" else "No VAT",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.SemiBold,
                color = if (hasVat) Color(0xFFFF9800) else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 10.dp),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Total Payment", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
            Column(horizontalAlignment = Alignment.End) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(Icons.Default.CurrencyLira, null, modifier = Modifier.size(15.dp))
                    Text(viewModel.grandTotalFiat, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                }
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(painter = painterResource(Res.drawable.sat_unit), contentDescription = null, tint = Color(0xFFFFB700), modifier = Modifier.size(13.dp))
                    Text(viewModel.grandTotalSat, style = MaterialTheme.typography.bodyMedium, color = Color(0xFFFFB700), fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

private val processingPreviewViewModel = CheckoutViewModel().apply {
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
    selectMethod(PaymentMethod.BITCOIN_LIGHTNING)
    confirmPayment()
}

@Preview(showBackground = true, widthDp = 411, heightDp = 891)
@Composable
fun PaymentProcessingMobilePreview() {
    MaterialTheme { PaymentProcessingScreen(viewModel = processingPreviewViewModel) }
}

@Preview(showBackground = true, widthDp = 1280, heightDp = 800)
@Composable
fun PaymentProcessingDesktopPreview() {
    MaterialTheme { PaymentProcessingScreen(viewModel = processingPreviewViewModel) }
}