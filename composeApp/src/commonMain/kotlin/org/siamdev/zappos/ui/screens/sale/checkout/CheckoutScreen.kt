/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.screens.sale.checkout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import org.siamdev.zappos.LocalCheckoutVM
import org.siamdev.zappos.LocalMenuVM
import org.siamdev.zappos.LocalProgressVM
import org.siamdev.zappos.LocalSettingVM
import org.siamdev.zappos.ui.components.common.PrimaryAmt
import org.siamdev.zappos.ui.components.common.MaterialButton
import org.siamdev.zappos.ui.components.order.OrderItemList
import org.siamdev.zappos.ui.components.order.OrderSummaryCard
import org.siamdev.zappos.ui.components.payment.PaymentMethodDialogCard
import org.siamdev.zappos.ui.components.payment.PaymentMethodList
import org.siamdev.zappos.ui.components.progress.ProgressBar
import org.siamdev.zappos.ui.components.progress.ProgressFacadeImpl
import org.siamdev.zappos.ui.components.common.SecondaryAmt
import org.siamdev.zappos.ui.components.common.WorkspaceHeader
import org.siamdev.zappos.ui.screens.sale.SaleOrderSteps
import org.siamdev.zappos.ui.screens.sale.MainMenuFacadeImpl
import org.siamdev.zappos.ui.screens.sale.MainMenuViewModel
import org.siamdev.zappos.ui.screens.setting.SettingFacadeImpl
import org.siamdev.zappos.ui.screens.setting.SettingViewModel
import org.siamdev.zappos.ui.components.progress.ProgressViewModel


@Composable
fun CheckoutScreen(
    onBack: () -> Unit = {},
    onSuccess: () -> Unit = {}
) {
    val menu = LocalMenuVM.current
    val checkout = LocalCheckoutVM.current
    val progress = LocalProgressVM.current

    LaunchedEffect(menu.selectedKeys) {
        val selectedKeys = menu.selectedKeys
        val items = menu.items
        checkout.syncFromMenu(
            items = selectedKeys.map { key ->
                val item = items.first { it.id == key }
                CheckoutItem(
                    name = item.name,
                    count = item.count,
                    priceBaht = item.priceBaht,
                    priceSat = item.priceSat
                )
            },
            fiat = menu.totalFiat,
            sat = menu.totalSat
        )
    }

    CheckoutContent(
        checkout = checkout,
        onBack = onBack,
        onSuccess = {
            menu.clearAllItems()
            checkout.reset()
            progress.setup(SaleOrderSteps, 0)
            onSuccess()
        }
    )
}

@Composable
fun CheckoutContent(
    checkout: CheckoutFacade,
    onBack: () -> Unit = {},
    onSuccess: () -> Unit = {}
) {
    val step = checkout.step

    when (step) {
        CheckoutStep.CASH_CALCULATOR -> {
            CashCalculatorScreen(
                checkout = checkout,
                onBack = { checkout.backToSelectPayment() })
            return
        }

        CheckoutStep.PROCESSING -> {
            PaymentProcessingScreen(
                checkout = checkout,
                onConfirm = { checkout.confirmProcessing() },
                onBack = { checkout.backToSelectPayment() }
            )
            return
        }

        CheckoutStep.SUCCESS -> {
            SuccessScreen(onOpen = onSuccess)
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
            DesktopCheckoutLayout(checkout = checkout, onBack = onBack)
        } else {
            MobileCheckoutLayout(checkout = checkout, onBack = onBack)
            if (step == CheckoutStep.SELECT_PAYMENT) {
                PaymentMethodDialogCard(
                    selectedMethod = checkout.selectedPaymentMethod,
                    onSelectMethod = { method ->
                        checkout.selectMethod(method)
                        checkout.confirmPayment()
                    },
                    onDismiss = { checkout.backToOrder() }
                )
            }
        }
    }
}


@Composable
private fun MobileCheckoutLayout(
    checkout: CheckoutFacade,
    onBack: () -> Unit
) {
    val progress = LocalProgressVM.current
    SideEffect { progress.setup(SaleOrderSteps, 1) }

    Column(modifier = Modifier.fillMaxSize()) {
        WorkspaceHeader(title = "Checkout", subtitle = "Sales · payment", onNavigateBack = onBack)
        ProgressBar()

        SectionLabel(
            text = "ORDER SUMMARY",
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
        )

        OrderItemList(
            items = checkout.orderItems,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 20.dp)
        )

        Spacer(Modifier.height(12.dp))

        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
            SectionLabel(text = "SUMMARY", modifier = Modifier.padding(bottom = 8.dp))

            OrderSummaryCard(
                subtotalFiat = checkout.totalFiat,
                subtotalSat = checkout.totalSat,
                taxPercent = checkout.taxPercent,
                onTaxChange = { checkout.setTax(it) }
            )

            Spacer(Modifier.height(12.dp))

            MaterialButton(
                modifier = Modifier.fillMaxWidth(),
                text = "Choose Payment Method",
                iconStart = Icons.Default.Payment,
                onClick = { checkout.openSelectPayment() }
            )
        }

        Spacer(Modifier.height(8.dp))
    }
}

@Composable
private fun DesktopCheckoutLayout(
    checkout: CheckoutFacade,
    onBack: () -> Unit
) {
    val progress = LocalProgressVM.current
    SideEffect { progress.setup(SaleOrderSteps, 1) }

    Column(modifier = Modifier.fillMaxSize()) {
        WorkspaceHeader(title = "Checkout", subtitle = "Sales · payment", onNavigateBack = onBack)
        ProgressBar()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SectionLabel(text = "ORDER SUMMARY", modifier = Modifier.weight(1f))
            SectionLabel(text = "SUMMARY", modifier = Modifier.width(380.dp))
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(start = 32.dp, end = 32.dp, bottom = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            OrderItemList(
                items = checkout.orderItems,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            )

            Column(
                modifier = Modifier
                    .width(380.dp)
                    .wrapContentHeight(),
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                OrderSummaryCard(
                    subtotalFiat = checkout.totalFiat,
                    subtotalSat = checkout.totalSat,
                    taxPercent = checkout.taxPercent,
                    onTaxChange = { checkout.setTax(it) }
                )

                Spacer(Modifier.height(20.dp))

                SectionLabel(
                    text = "PAYMENT METHOD",
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                PaymentMethodList(
                    selectedMethod = checkout.selectedPaymentMethod,
                    onSelectMethod = { method ->
                        checkout.selectMethod(method)
                        checkout.confirmPayment()
                    },
                    showBorder = true
                )
            }
        }
    }
}

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
internal fun CheckoutItemRow(item: CheckoutItem, isEven: Boolean = false) {
    val setting = LocalSettingVM.current
    val showSecondary = setting.showSecondaryCurrency
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(
                if (isEven) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.04f)
                else Color.Transparent
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
            PrimaryAmt(
                value = item.priceBaht,
                iconSize = 13.dp,
                textStyle = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
            if (showSecondary) {
                SecondaryAmt(
                    value = item.priceSat,
                    iconSize = 12.dp,
                    textStyle = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}


private val previewCheckoutVM = CheckoutViewModel().apply {
    syncFromMenu(
        items = listOf(
            CheckoutItem("Mocha", 2u, "70.00", "17,500"),
            CheckoutItem("Matcha Latte", 1u, "100.00", "26,000"),
            CheckoutItem("Latte", 3u, "70.00", "17,500"),
            CheckoutItem("Espresso", 2u, "50.00", "12,500"),
            CheckoutItem("Americano", 1u, "60.00", "15,000"),
            CheckoutItem("Cappuccino", 2u, "75.00", "18,750"),
            CheckoutItem("Flat White", 1u, "80.00", "20,000"),
            CheckoutItem("Caramel Macchiato", 3u, "90.00", "22,500")
        ),
        fiat = "61,020.00",
        sat = "1,683,138"
    )
}

@Preview(showBackground = true, widthDp = 411, heightDp = 891)
@Composable
fun CheckoutScreenMobilePreview() {
    val progressVM = remember { ProgressViewModel() }
    val settingVM = remember { SettingViewModel() }
    val menuVM = remember { MainMenuViewModel(autoLoad = false) }

    CompositionLocalProvider(
        LocalProgressVM provides ProgressFacadeImpl(progressVM),
        LocalSettingVM provides SettingFacadeImpl(settingVM),
        LocalMenuVM provides MainMenuFacadeImpl(menuVM),
        LocalCheckoutVM provides CheckoutFacadeImpl(previewCheckoutVM)
    ) {
        MaterialTheme { CheckoutContent(checkout = CheckoutFacadeImpl(previewCheckoutVM)) }
    }
}

@Preview(showBackground = true, widthDp = 1280, heightDp = 800)
@Composable
fun CheckoutScreenDesktopPreview() {
    val progressVM = remember { ProgressViewModel() }
    val settingVM = remember { SettingViewModel() }
    val menuVM = remember { MainMenuViewModel(autoLoad = false) }

    CompositionLocalProvider(
        LocalProgressVM provides ProgressFacadeImpl(progressVM),
        LocalSettingVM provides SettingFacadeImpl(settingVM),
        LocalMenuVM provides MainMenuFacadeImpl(menuVM),
        LocalCheckoutVM provides CheckoutFacadeImpl(previewCheckoutVM)
    ) {
        MaterialTheme { CheckoutContent(checkout = CheckoutFacadeImpl(previewCheckoutVM)) }
    }
}