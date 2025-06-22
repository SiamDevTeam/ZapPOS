package org.zappos.core.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.zappos.core.view.CheckoutItem
import org.zappos.core.view.MaterialButton
import org.zappos.core.view.TextIconButton
import zappos.composeapp.generated.resources.Res
import zappos.composeapp.generated.resources.ic_checkout

@Composable
fun CheckoutScreen(
    checkoutList: List<CheckoutOrder>,
    onAddItemClick: () -> Unit,
    onRemoveItemClick: () -> Unit,
    onDeleteItemClick: () -> Unit
) {
    Column {
        Row {
            Text(text = "Current Order")
            Text(text = "123 sat")
        }
        Spacer(modifier = Modifier.height(16.dp))
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
        HorizontalDivider(thickness = 2.dp)
        Row {
            Text(text = "Total")
            Text(text = "123 sat")
        }
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
        MaterialButton(
            text = "Clear Cart",
            modifier = Modifier
                .background(color = Color.White, RoundedCornerShape(8.dp))
                .fillMaxWidth()
        ) {
            // Handle click
        }

    }

}

@Preview
@Composable
fun CheckoutScreenPreview() {
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
