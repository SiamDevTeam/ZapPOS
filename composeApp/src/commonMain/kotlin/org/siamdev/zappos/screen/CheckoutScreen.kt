package org.siamdev.zappos.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.siamdev.zappos.CheckoutOrder
import org.siamdev.zappos.view.CheckoutItem
import org.siamdev.zappos.view.MaterialOutlinedButton
import org.siamdev.zappos.view.TextIconButton
import zappos.composeapp.generated.resources.Res
import zappos.composeapp.generated.resources.ic_checkout

@Composable
fun CheckoutScreen(
    checkoutList: List<CheckoutOrder> = emptyList(),
    onAddItemClick: (String) -> Unit,
    onRemoveItemClick: (String) -> Unit,
    onDeleteItemClick: (String) -> Unit,
    onCheckout: () -> Unit,
    onClearCart: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.secondaryContainer, RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Current Order",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            // Calculate total dynamically
            val totalSats = checkoutList.sumOf {
                it.sats.filter { c -> c.isDigit() }.toIntOrNull()?.times(it.quantity) ?: 0
            }
            Text(
                text = "$totalSats sat",
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.End
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (checkoutList.isEmpty()) {
            CartEmpty()
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                checkoutList.forEach { item ->
                    CheckoutItem(
                        menu = item.name,
                        sats = item.sats,
                        quantity = item.quantity,
                        onAddItemClick = { onAddItemClick(item.name) },
                        onRemoveItemClick = { onRemoveItemClick(item.name) },
                        onDeleteItemClick = { onDeleteItemClick(item.name) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(thickness = 2.dp)
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Total",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                val totalSats = checkoutList.sumOf {
                    it.sats.filter { c -> c.isDigit() }.toIntOrNull()?.times(it.quantity) ?: 0
                }
                Text(
                    text = "$totalSats sat",
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.End
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            TextIconButton(
                text = "Checkout",
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp))
                    .fillMaxWidth(),
                resource = Res.drawable.ic_checkout,
                onClick = onCheckout
            )
            Spacer(modifier = Modifier.height(8.dp))
            MaterialOutlinedButton(
                text = "Clear Cart",
                onClick = onClearCart
            )
        }
    }
}

@Composable
fun CartEmpty() {
    Box(
        modifier = Modifier.fillMaxWidth().height(48.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Cart is empty",
            fontSize = 16.sp
        )
    }
}

@Preview
@Composable
fun CheckoutScreenEmptyPreview() {
    CheckoutScreen(
        checkoutList = listOf(),
        onAddItemClick = {},
        onRemoveItemClick = {},
        onDeleteItemClick = {},
        onCheckout = {},
        onClearCart = {}
    )
}

@Preview
@Composable
fun CheckoutScreenNotEmptyPreview() {
    CheckoutScreen(
        checkoutList = listOf(
            CheckoutOrder("Item 1", "100 sat", 1, "100 sat"),
            CheckoutOrder("Item 2", "200 sat", 2, "400 sat")
        ),
        onAddItemClick = {},
        onRemoveItemClick = {},
        onDeleteItemClick = {},
        onCheckout = {},
        onClearCart = {}
    )
}
