/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.screens.sale.confirm

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCartCheckout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.tooling.preview.Preview
import org.siamdev.zappos.LocalMenuVM
import org.siamdev.zappos.LocalProgressVM
import org.siamdev.zappos.LocalSettingVM
import org.siamdev.zappos.ui.components.common.MaterialButton
import org.siamdev.zappos.ui.components.common.PrimaryAmt
import org.siamdev.zappos.ui.components.order.OrderItemCard
import org.siamdev.zappos.ui.components.progress.ProgressBar
import org.siamdev.zappos.ui.components.common.SecondaryAmt
import org.siamdev.zappos.ui.components.common.WorkspaceHeader
import org.siamdev.zappos.ui.screens.sale.MainMenuViewModel
import org.siamdev.zappos.ui.screens.sale.MenuItem
import org.siamdev.zappos.ui.screens.sale.SaleOrderSteps
import org.siamdev.zappos.ui.screens.setting.SettingViewModel
import org.siamdev.zappos.ui.components.progress.ProgressViewModel

@Composable
fun ConfirmOrderScreen(
    onBack: () -> Unit = {},
    onCheckout: () -> Unit = {}
) {
    val menuVM = LocalMenuVM.current
    val selectedItems = menuVM.selectedKeys.mapNotNull { key ->
        menuVM.items.firstOrNull { it.id == key }
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(WindowInsets.statusBars)
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        val isDesktop = maxWidth >= 750.dp && maxHeight >= 500.dp
        if (isDesktop) {
            DesktopConfirmLayout(
                items = selectedItems,
                menuVM = menuVM,
                onBack = onBack,
                onCheckout = onCheckout
            )
        } else {
            MobileConfirmLayout(
                items = selectedItems,
                menuVM = menuVM,
                onBack = onBack,
                onCheckout = onCheckout
            )
        }
    }
}

@Composable
private fun MobileConfirmLayout(
    items: List<MenuItem>,
    menuVM: MainMenuViewModel,
    onBack: () -> Unit,
    onCheckout: () -> Unit
) {
    val progressVM = LocalProgressVM.current
    SideEffect { progressVM.setup(SaleOrderSteps, 0) }

    Column(modifier = Modifier.fillMaxSize()) {
        WorkspaceHeader(
            title = "Confirm Order",
            subtitle = "Sales · review order",
            onNavigateBack = onBack
        )
        ProgressBar()

        ConfirmSectionLabel(
            text = "ORDER ITEMS",
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
        )

        ConfirmItemList(
            items = items,
            menuVM = menuVM,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 20.dp)
        )

        Spacer(Modifier.height(12.dp))

        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
            ConfirmSectionLabel(
                text = "SUMMARY",
                modifier = Modifier.padding(bottom = 8.dp)
            )
            ConfirmSummaryCard(menuVM = menuVM)
            Spacer(Modifier.height(12.dp))
            CheckoutButton(enabled = items.isNotEmpty(), onClick = onCheckout)
        }

        Spacer(Modifier.height(8.dp))
    }
}

@Composable
private fun DesktopConfirmLayout(
    items: List<MenuItem>,
    menuVM: MainMenuViewModel,
    onBack: () -> Unit,
    onCheckout: () -> Unit
) {
    val progressVM = LocalProgressVM.current
    SideEffect { progressVM.setup(SaleOrderSteps, 0) }

    Column(modifier = Modifier.fillMaxSize()) {
        WorkspaceHeader(
            title = "Confirm Order",
            subtitle = "Sales · review order",
            onNavigateBack = onBack
        )
        ProgressBar()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ConfirmSectionLabel(text = "ORDER ITEMS", modifier = Modifier.weight(1f))
            ConfirmSectionLabel(text = "SUMMARY", modifier = Modifier.width(340.dp))
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(start = 32.dp, end = 32.dp, bottom = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            ConfirmItemList(
                items = items,
                menuVM = menuVM,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            )

            Column(
                modifier = Modifier
                    .width(340.dp)
                    .wrapContentHeight(),
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                ConfirmSummaryCard(menuVM = menuVM)
                Spacer(Modifier.height(20.dp))
                CheckoutButton(enabled = items.isNotEmpty(), onClick = onCheckout)
            }
        }
    }
}

@Composable
private fun ConfirmItemList(
    items: List<MenuItem>,
    menuVM: MainMenuViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                shape = RoundedCornerShape(12.dp)
            )
            .background(MaterialTheme.colorScheme.surface)
            .padding(8.dp)
    ) {
        if (items.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    "No items selected",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(start = 16.dp, end = 16.dp),
                contentPadding = PaddingValues(vertical = 4.dp)
            ) {
                itemsIndexed(items) { _, item ->
                    OrderItemCard(
                        item = item,
                        onAddClick = { menuVM.addItem(item.id) },
                        onReduceClick = { menuVM.reduceItem(item.id) },
                        onCountChange = { menuVM.setItemCount(item.id, it) },
                        onDelete = { menuVM.setItemCount(item.id, 0u) },
                        isDesktop = true
                    )
                }
            }
        }
    }
}

@Composable
private fun ConfirmSummaryCard(menuVM: MainMenuViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
                shape = RoundedCornerShape(12.dp)
            )
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Subtotal",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            Column(horizontalAlignment = Alignment.End) {
                PrimaryAmt(
                    value = menuVM.totalFiat,
                    iconSize = 13.dp,
                    textStyle = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
                SecondaryAmt(
                    value = menuVM.totalSat,
                    iconSize = 12.dp,
                    textStyle = MaterialTheme.typography.bodySmall
                )
            }
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Tax",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
            )
            Text(
                "Applied at checkout",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
            )
        }
    }
}

@Composable
private fun CheckoutButton(enabled: Boolean, onClick: () -> Unit) {
    MaterialButton(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        text = "Proceed to Checkout",
        iconStart = Icons.Default.ShoppingCartCheckout,
        enabled = enabled,
        onClick = onClick
    )
}

@Composable
private fun ConfirmSectionLabel(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
        letterSpacing = 2.sp,
        modifier = modifier
    )
}

private val previewOrderItems = listOf(
    MenuItem(1, "", "Mocha", "70.00", "17,500", "coffee", isRecommended = true, count = 2u),
    MenuItem(3, "", "Matcha Latte", "100.00", "26,000", "matcha", isRecommended = true, count = 1u),
    MenuItem(7, "", "Cappuccino", "75.00", "18,750", "coffee", isRecommended = true, count = 3u),
    MenuItem(11, "", "Thai Tea", "60.00", "15,000", "tea", isRecommended = true, count = 1u),
)

private fun confirmPreviewVM() =
    MainMenuViewModel(autoLoad = false).also { it.loadItemsForPreview(previewOrderItems) }

@Preview(showBackground = true, widthDp = 411, heightDp = 891)
@Composable
fun ConfirmOrderScreenMobilePreview() {
    val vm = remember { confirmPreviewVM() }
    val progressVM = remember { ProgressViewModel() }
    val settingVM = remember { SettingViewModel() }

    CompositionLocalProvider(
        LocalMenuVM provides vm,
        LocalProgressVM provides progressVM,
        LocalSettingVM provides settingVM
    ) {
        ConfirmOrderScreen()
    }
}

@Preview(showBackground = true, widthDp = 1280, heightDp = 800)
@Composable
fun ConfirmOrderScreenDesktopPreview() {
    val vm = remember { confirmPreviewVM() }
    val progressVM = remember { ProgressViewModel() }
    val settingVM = remember { SettingViewModel() }

    CompositionLocalProvider(
        LocalMenuVM provides vm,
        LocalProgressVM provides progressVM,
        LocalSettingVM provides settingVM
    ) {
        ConfirmOrderScreen()
    }
}
