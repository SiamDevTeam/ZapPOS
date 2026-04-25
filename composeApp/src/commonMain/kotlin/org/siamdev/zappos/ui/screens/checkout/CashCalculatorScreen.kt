package org.siamdev.zappos.ui.screens.checkout

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.filled.CurrencyLira
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
import org.siamdev.zappos.theme.YellowPrimary
import org.siamdev.zappos.ui.components.MaterialButton
import zappos.composeapp.generated.resources.Res
import zappos.composeapp.generated.resources.sat_unit

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
        val isDesktop = maxWidth >= 600.dp

        if (isDesktop) {
            DesktopCashLayout(viewModel = viewModel, onBack = onBack)
        } else {
            MobileCashLayout(viewModel = viewModel, onBack = onBack)
        }
    }
}

@Composable
private fun MobileCashLayout(
    viewModel: CheckoutViewModel,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CashHeader(onBack = onBack)
        Spacer(Modifier.height(16.dp))
        CashTotalDue(totalFiat = viewModel.totalFiat)
        Spacer(Modifier.height(12.dp))
        CashReceivedDisplay(viewModel = viewModel)
        Spacer(Modifier.height(12.dp))
        CashChangeDisplay(viewModel = viewModel)
        Spacer(Modifier.height(20.dp))
        CashNumpad(viewModel = viewModel)
        MaterialButton(
            modifier = Modifier
                .padding(top = 40.dp)
                .fillMaxWidth(),
            text = "Confirm Cash",
            onClick = { viewModel.confirmCash() }
        )
        Spacer(Modifier.height(8.dp))
    }
}

@Composable
private fun DesktopCashLayout(
    viewModel: CheckoutViewModel,
    onBack: () -> Unit
) {
    Row(modifier = Modifier.fillMaxSize()) {

        // ── Left: Order summary + totals ──
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(horizontal = 40.dp, vertical = 24.dp)
        ) {
            CashHeader(onBack = onBack)

            Spacer(Modifier.height(24.dp))

            // Order items list
            Text(
                "ORDER SUMMARY",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                letterSpacing = 2.sp
            )

            Spacer(Modifier.height(12.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(horizontal = 20.dp)
            ) {
                viewModel.orderItems.forEachIndexed { index, item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(YellowPrimary.copy(alpha = 0.12f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${item.count}",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = YellowPrimary
                                )
                            }
                            Text(
                                text = item.name,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(3.dp)
                            ) {
                                Icon(
                                    Icons.Default.CurrencyLira, null,
                                    modifier = Modifier.size(13.dp),
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = item.priceBaht,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(3.dp)
                            ) {
                                Icon(
                                    painter = painterResource(Res.drawable.sat_unit),
                                    contentDescription = null,
                                    tint = Color(0xFFFFB700),
                                    modifier = Modifier.size(11.dp)
                                )
                                Text(
                                    text = item.priceSat,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFFFFB700)
                                )
                            }
                        }
                    }
                    if (index < viewModel.orderItems.lastIndex) {
                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f)
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Amount Due
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Amount Due",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Icon(
                        Icons.Default.CurrencyLira, null,
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        viewModel.totalFiat,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(Modifier.weight(1f))

            // Received
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        if (viewModel.isChangeValid) YellowPrimary.copy(alpha = 0.08f)
                        else MaterialTheme.colorScheme.surface
                    )
                    .border(
                        1.5.dp,
                        if (viewModel.isChangeValid) YellowPrimary.copy(alpha = 0.5f)
                        else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.07f),
                        RoundedCornerShape(16.dp)
                    )
                    .padding(20.dp)
            ) {
                Text(
                    "Received",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                    letterSpacing = 1.sp
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = viewModel.receivedAmount.ifEmpty { "0" },
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                    color = if (viewModel.isChangeValid) YellowPrimary
                    else MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(Modifier.height(12.dp))

            // Change
            AnimatedVisibility(
                visible = viewModel.isChangeValid,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFF4CAF50).copy(alpha = 0.1f))
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Change",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color(0xFF4CAF50),
                        fontWeight = FontWeight.SemiBold
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Icon(
                            Icons.Default.CurrencyLira, null,
                            modifier = Modifier.size(18.dp),
                            tint = Color(0xFF4CAF50)
                        )
                        Text(
                            viewModel.formatChange(),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4CAF50)
                        )
                    }
                }
            }

            Spacer(Modifier.height(24.dp))
        }

        // Divider
        Box(
            modifier = Modifier
                .width(1.dp)
                .fillMaxHeight()
                .padding(vertical = 32.dp)
                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f))
        )

        // ── Right: Numpad ──
        Column(
            modifier = Modifier
                .width(380.dp)
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.surface)
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "ENTER AMOUNT",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.35f),
                letterSpacing = 2.sp,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            val digitKeys = listOf(
                listOf("1", "2", "3"),
                listOf("4", "5", "6"),
                listOf("7", "8", "9"),
                listOf(".", "0", null)
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                digitKeys.forEach { row ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        row.forEach { key ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1.4f)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(
                                        if (key == null)
                                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f)
                                        else MaterialTheme.colorScheme.background
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
                                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                        modifier = Modifier.size(22.dp)
                                    )
                                } else {
                                    Text(
                                        text = key,
                                        style = MaterialTheme.typography.headlineSmall,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            MaterialButton(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxWidth(),
                text = "Confirm Cash",
                onClick = { viewModel.confirmCash() }
            )
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
            .padding(vertical = 12.dp),
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
        Text(
            "Cash Payment",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun CashTotalDue(totalFiat: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "Total Due",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(Icons.Default.CurrencyLira, null, modifier = Modifier.size(16.dp))
            Text(
                totalFiat,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun CashReceivedDisplay(viewModel: CheckoutViewModel) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(
                if (viewModel.isChangeValid) YellowPrimary.copy(alpha = 0.08f)
                else MaterialTheme.colorScheme.surface
            )
            .border(
                1.dp,
                if (viewModel.isChangeValid) YellowPrimary.copy(alpha = 0.4f)
                else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
                RoundedCornerShape(16.dp)
            )
            .padding(20.dp)
    ) {
        Column {
            Text(
                "Received",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                letterSpacing = 1.sp
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = viewModel.receivedAmount.ifEmpty { "0" },
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = if (viewModel.isChangeValid) YellowPrimary
                else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun CashChangeDisplay(viewModel: CheckoutViewModel) {
    AnimatedVisibility(
        visible = viewModel.isChangeValid,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF4CAF50).copy(alpha = 0.1f))
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Change",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF4CAF50),
                fontWeight = FontWeight.Medium
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    Icons.Default.CurrencyLira, null,
                    modifier = Modifier.size(16.dp),
                    tint = Color(0xFF4CAF50)
                )
                Text(
                    viewModel.formatChange(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4CAF50)
                )
            }
        }
    }
}

@Composable
private fun CashNumpad(viewModel: CheckoutViewModel) {
    val digitKeys = listOf(
        listOf("1", "2", "3"),
        listOf("4", "5", "6"),
        listOf("7", "8", "9"),
        listOf(".", "0", null)
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        digitKeys.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
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
                                else MaterialTheme.colorScheme.background
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
                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                modifier = Modifier.size(22.dp)
                            )
                        } else {
                            Text(
                                text = key,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}

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