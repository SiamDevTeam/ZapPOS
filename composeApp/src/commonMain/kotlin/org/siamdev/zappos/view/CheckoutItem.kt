package org.siamdev.zappos.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview
import zappos.composeapp.generated.resources.Res
import zappos.composeapp.generated.resources.ic_add
import zappos.composeapp.generated.resources.ic_remove

@Composable
fun CheckoutItem(
    menu: String,
    sats: String,
    quantity: Int,
    onAddItemClick: () -> Unit,
    onRemoveItemClick: () -> Unit,
    onDeleteItemClick: () -> Unit
) {
    Column (
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.LightGray, RoundedCornerShape(8.dp))
                .padding(16.dp),
        ) {
            Column {
                Text(text = menu, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(text = sats, fontSize = 12.sp)
            }
            Spacer(Modifier.weight(1f))
            MaterialIconButton(
                modifier = Modifier
                    .width(36.dp).height(36.dp)
                    .background(color = Color.White, RoundedCornerShape(8.dp))
                    .wrapContentWidth(),
                resource = Res.drawable.ic_remove,
                contentDescription = "Remove Item"
            ) {
                onRemoveItemClick()
            }
            Spacer(Modifier.width(16.dp))
            Text(
                text = quantity.toString(),
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .wrapContentHeight()
            )
            Spacer(Modifier.width(16.dp))
            MaterialIconButton(
                modifier = Modifier
                    .width(36.dp).height(36.dp)
                    .background(color = Color.White, RoundedCornerShape(8.dp))
                    .wrapContentWidth(),
                resource = Res.drawable.ic_add,
                contentDescription = "Add Item"
            ) {
                onAddItemClick()
            }
            Spacer(Modifier.width(16.dp))
            RemoveItem(
                modifier = Modifier
                    .width(36.dp).height(36.dp)
                    .background(color = Color.Red, RoundedCornerShape(8.dp))
                    .wrapContentWidth()
            ) {
                onDeleteItemClick()
            }
        }
        Spacer(Modifier.height(8.dp))
    }

}

@Preview
@Composable
fun previewCheckoutItem() {
    Column {
        CheckoutItem(
            menu = "Spaghetti Carbonara",
            sats = "150 sats",
            quantity = 2,
            onAddItemClick = {},
            onRemoveItemClick = {},
            onDeleteItemClick = {}
        )
        CheckoutItem(
            menu = "item 2",
            sats = "150 sats",
            quantity = 2,
            onAddItemClick = {},
            onRemoveItemClick = {},
            onDeleteItemClick = {}
        )
    }

}