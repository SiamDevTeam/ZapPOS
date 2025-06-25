package org.siamdev.zappos.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import zappos.composeapp.generated.resources.Res
import zappos.composeapp.generated.resources.ic_add
import zappos.composeapp.generated.resources.ic_remove
import zappos.composeapp.generated.resources.ic_trash
import zappos.composeapp.generated.resources.ic_checkout

@Composable
fun MaterialButton(
    text: String,
    modifier: Modifier = Modifier
        .background(color = MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp)),
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        onClick = onClick
    ) {
        Text(text)
    }
}

@Composable
fun MaterialOutlinedButton(
    text: String,
    modifier: Modifier = Modifier.fillMaxWidth(),
    onClick: () -> Unit
) {
    OutlinedButton(
        modifier = modifier,
        onClick = onClick
    ) {
        Text(text)
    }
}

@Composable
fun TextIconButton(
    text: String,
    modifier: Modifier = Modifier
        .background(color = MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp)),
    resource: DrawableResource,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        onClick = onClick
    ) {
        Icon(
            painter = painterResource(resource),
            contentDescription = "",
        )
        Spacer(Modifier.width(8.dp))
        Text(text)
    }
}

@Composable
fun MaterialIconButton(
    modifier: Modifier = Modifier
        .background(color = Color.White, RoundedCornerShape(8.dp))
        .wrapContentWidth(),
    resource: DrawableResource,
    contentDescription: String? = null,
    onClick: () -> Unit
) {
    IconButton(
        modifier = modifier,
        onClick = onClick
    ) {
        Icon(
            painter = painterResource(resource),
            contentDescription = contentDescription
        )
    }
}

@Composable
fun RemoveItem(
    modifier: Modifier = Modifier
        .background(color = Color.Red, RoundedCornerShape(8.dp))
        .wrapContentWidth(),
    onClick: () -> Unit
) {
    IconButton(
        modifier = modifier,
        onClick = onClick
    ) {
        Icon(
            painter = painterResource(Res.drawable.ic_trash),
            contentDescription = "Remove Item"
        )
    }
}

@Preview
@Composable
fun previewMaterialButton() {
    MaterialButton(text = "Click Me") {
        // Handle click
    }
}

@Preview
@Composable
fun previewClearCartButton() {
    MaterialOutlinedButton(
        text = "Clear Cart"
    ) {
        // Handle click
    }
}

@Preview
@Composable
fun previewCheckoutButton() {
    TextIconButton(
        text = "Checkout",
        resource = Res.drawable.ic_checkout
    ) {
        // Handle click
    }
}

@Preview
@Composable
fun previewAddItemButton() {
    MaterialIconButton(
        resource = Res.drawable.ic_add,
        contentDescription = "Add Item"
    ) {
        // Handle click
    }
}

@Preview
@Composable
fun previewRemoveItemButton() {
    MaterialIconButton(
        resource = Res.drawable.ic_remove,
        contentDescription = "Remove Item"
    ) {
        // Handle click
    }
}

@Preview
@Composable
fun previewDeleteItemButton() {
    RemoveItem {
        // Handle click
    }
}