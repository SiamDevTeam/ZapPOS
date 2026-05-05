/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.siamdev.zappos.ui.screens.checkout.CheckoutItem
import org.siamdev.zappos.ui.screens.checkout.CheckoutItemRow


@Composable
fun OrderItemList(
    items: List<CheckoutItem>,
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
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 4.dp)
        ) {
            itemsIndexed(items) { index, item ->
                CheckoutItemRow(item = item, isEven = index % 2 == 1)
            }
        }
    }
}

private val previewItems = listOf(
    CheckoutItem("Mocha", 2u, "70.00", "17,500"),
    CheckoutItem("Matcha Latte", 1u, "100.00", "26,000"),
    CheckoutItem("Latte", 3u, "70.00", "17,500"),
    CheckoutItem("Espresso", 2u, "50.00", "12,500"),
    CheckoutItem("Americano", 1u, "60.00", "15,000"),
)

@Preview(showBackground = true, widthDp = 411, heightDp = 400)
@Composable
fun OrderItemListPreview() {
    MaterialTheme {
        OrderItemList(
            items = previewItems,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}