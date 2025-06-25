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
    onAddItemClick: () -> Unit,
    onRemoveItemClick: () -> Unit,
    onDeleteItemClick: () -> Unit
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
            Text(
                text = "123 sat",
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
            CartItemList(checkoutList, onAddItemClick, onRemoveItemClick, onDeleteItemClick)
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
fun CartItemList(
    checkoutList: List<CheckoutOrder> = emptyList(),
    onAddItemClick: () -> Unit,
    onRemoveItemClick: () -> Unit,
    onDeleteItemClick: () -> Unit
) {
    LazyColumn {
        checkoutList.forEach { item ->
            item {
                CheckoutItem(
                    menu = item.name,
                    sats = item.sats,
                    quantity = item.quantity,
                    onAddItemClick = onAddItemClick,
                    onRemoveItemClick = onRemoveItemClick,
                    onDeleteItemClick = onDeleteItemClick
                )
            }
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
        Text(
            text = "123 sat",
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.weight(1f).alignByBaseline(),
            textAlign = TextAlign.End
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
    TextIconButton(
        text = "checkout",
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp))
            .fillMaxWidth(),
        resource = Res.drawable.ic_checkout
    ) {
        // Handle click
    }
    Spacer(modifier = Modifier.height(8.dp))
    MaterialOutlinedButton(
        text = "Clear Cart"
    ) {
        // Handle click
    }
}

@Preview
@Composable
fun CheckoutScreenEmptyPreview() {
    CheckoutScreen(
        checkoutList = listOf(),
        onAddItemClick = {},
        onRemoveItemClick = {},
        onDeleteItemClick = {}
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
        onDeleteItemClick = {}
    )
}

// TODO: will move
class CheckoutOrder(
    val name: String,
    val sats: String,
    val quantity: Int,
    val total: String
)
