package org.siamdev.zappos.ui.screens.checkout

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
        val isDesktop = maxWidth >= 600.dp

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
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header — back left, title center
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
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack, null,
                    tint = YellowPrimary,
                    modifier = Modifier.size(18.dp)
                )
            }
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
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

        if (showQr) {
            Spacer(Modifier.height(28.dp))

            // QR
            Box(
                modifier = Modifier
                    .size(240.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surface),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.QrCode2, null,
                    modifier = Modifier.size(200.dp),
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
                )
            }

            Spacer(Modifier.height(16.dp))

            // Invoice string
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
                        RoundedCornerShape(10.dp)
                    )
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Text(
                    text = "lnbc50n1p588zyfpp.....52mczpfq7fatgrfh",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                    maxLines = 1
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        // Order details card
        ProcessingDetailCard(viewModel = viewModel, txId = txId)

        Spacer(Modifier.weight(1f))

        MaterialButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            text = "Done",
            onClick = onConfirm
        )

        Spacer(Modifier.height(16.dp))
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
    Row(modifier = Modifier.fillMaxSize()) {

        // Left: QR + invoice string
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (showQr) {
                Box(
                    modifier = Modifier
                        .size(320.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(MaterialTheme.colorScheme.surface),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.QrCode2, null,
                        modifier = Modifier.size(280.dp),
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
                    )
                }

                Spacer(Modifier.height(20.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f), RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = "lnbc50n1p588zyfpp.....52mczpfq7fatgrfh",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                        maxLines = 1
                    )
                }
            } else {
                Icon(
                    Icons.Default.CheckCircle, null,
                    modifier = Modifier.size(120.dp),
                    tint = YellowPrimary.copy(alpha = 0.3f)
                )
            }
        }

        // Right: header + detail card + button
        Column(
            modifier = Modifier
                .width(400.dp)
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.surface)
                .padding(32.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                // Back + title
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 8.dp)
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
                    Spacer(Modifier.width(12.dp))
                    Text(text = title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                }

                // TX ID
                Text(
                    text = txId,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                ProcessingDetailCard(viewModel = viewModel, txId = txId)
            }

            MaterialButton(
                modifier = Modifier.fillMaxWidth(),
                text = "Done",
                onClick = onConfirm
            )
        }
    }
}

@Composable
private fun ProcessingDetailCard(
    viewModel: CheckoutViewModel,
    txId: String
) {
    val taxPercent = viewModel.taxPercent
    val hasVat = taxPercent > 0f

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f), RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(20.dp)
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

        // VAT row
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
            Text("Total Payment", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
            Column(horizontalAlignment = Alignment.End) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(Icons.Default.CurrencyLira, null, modifier = Modifier.size(15.dp))
                    Text(viewModel.grandTotalFiat, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
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
        items = listOf(CheckoutItem("Mocha", 2u, "70.00", "17,500")),
        fiat = "61,020.00",
        sat = "1,683,138"
    )
    selectMethod(PaymentMethod.BITCOIN_LIGHTNING)
    confirmPayment()
}

@Preview(showBackground = true, widthDp = 411, heightDp = 891)
@Composable
fun PaymentProcessingMobilePreview() {
    MaterialTheme {
        PaymentProcessingScreen(viewModel = processingPreviewViewModel)
    }
}

@Preview(showBackground = true, widthDp = 1280, heightDp = 800)
@Composable
fun PaymentProcessingDesktopPreview() {
    MaterialTheme {
        PaymentProcessingScreen(viewModel = processingPreviewViewModel)
    }
}