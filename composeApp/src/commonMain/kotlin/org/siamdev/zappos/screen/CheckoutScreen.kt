package org.siamdev.zappos.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview
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
            .padding(8.dp)
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.secondaryContainer, RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Row {
            Text(
                text = "Current Order",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.weight(1f)
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
                modifier = Modifier.weight(1f).alignByBaseline(),
                textAlign = TextAlign.End
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (checkoutList.isEmpty()) {
            CartEmpty()
        } else {
            CartItemList(
                checkoutList = checkoutList,
                onAddItemClick = onAddItemClick,
                onRemoveItemClick = onRemoveItemClick,
                onDeleteItemClick = onDeleteItemClick,
                onCheckout = onCheckout,
                onClearCart = onClearCart
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

@Composable
private fun CartItemList(
    checkoutList: List<CheckoutOrder>,
    onAddItemClick: (String) -> Unit,
    onRemoveItemClick: (String) -> Unit,
    onDeleteItemClick: (String) -> Unit,
    onCheckout: () -> Unit,
    onClearCart: () -> Unit
) {
    Column {
        LazyColumn {
            items(checkoutList.size) { index ->
                val item = checkoutList[index]
                CheckoutItem(
                    menu = item.name,
                    sats = item.sats,
                    quantity = item.quantity,
                    onAddItemClick = { onAddItemClick(item.name) },
                    onRemoveItemClick = { onRemoveItemClick(item.name) },
                    onDeleteItemClick = { onDeleteItemClick(item.name) }
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(thickness = 2.dp)
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            Text(
                text = "Total",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.weight(1f)
            )
            val totalSats = checkoutList.sumOf {
                it.sats.filter { c -> c.isDigit() }.toIntOrNull()?.times(it.quantity) ?: 0
            }
            Text(
                text = "$totalSats sat",
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f).alignByBaseline(),
                textAlign = TextAlign.End
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
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

// เปลี่ยน class เป็น data class
data class CheckoutOrder(
    val name: String,
    val sats: String,
    val quantity: Int,
    val total: String
)
