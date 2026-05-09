/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CurrencyLira
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.siamdev.zappos.theme.YellowPrimary
import org.siamdev.zappos.ui.screens.sale.MenuItem
import zappos.composeapp.generated.resources.Res
import zappos.composeapp.generated.resources.sat_unit

@Composable
fun OrderPanel(
    selectedKeys: List<Int>,
    items: List<MenuItem>,
    totalFiat: String,
    totalSat: String,
    onAddItem: (Int) -> Unit,
    onReduceItem: (Int) -> Unit,
    onCountChange: (Int, UInt) -> Unit,
    onCheckout: () -> Unit,
    onClearCart: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = null,
                    tint = YellowPrimary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "Order",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))
            Spacer(Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                if (selectedKeys.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No items yet",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                            )
                        }
                    }
                } else {
                    items(selectedKeys.size) { index ->
                        val key = selectedKeys[index]
                        val item = items.first { it.id == key }
                        OrderItemCard(
                            item = item,
                            onAddClick = { onAddItem(item.id) },
                            onReduceClick = { onReduceItem(item.id) },
                            onCountChange = { onCountChange(item.id, it) },
                            isDesktop = true
                        )
                    }
                }
            }

            Spacer(Modifier.height(12.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))
            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                Column(horizontalAlignment = Alignment.End) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CurrencyLira,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            totalFiat,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painterResource(Res.drawable.sat_unit),
                            contentDescription = null,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            totalSat,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            MaterialButton(
                modifier = Modifier.fillMaxWidth(),
                text = "Checkout",
                iconStart = Icons.Default.ShoppingCart,
                enabled = selectedKeys.isNotEmpty(),
                onClick = onCheckout
            )
            Spacer(Modifier.height(8.dp))
            MaterialButton(
                modifier = Modifier.fillMaxWidth(),
                text = "Clear Cart",
                buttonColor = Color.Transparent,
                showBorder = true,
                onClick = onClearCart
            )
        }
    }
}

private val previewOrderItems = listOf(
    MenuItem(1, "", "Mocha", "70.00", "17,500", "coffee", count = 2u),
    MenuItem(3, "", "Matcha Latte", "100.00", "26,000", "matcha", count = 1u),
    MenuItem(7, "", "Cappuccino", "75.00", "18,750", "coffee", count = 2u),
)

@Preview(showBackground = true, widthDp = 320, heightDp = 600)
@Composable
fun OrderPanelEmptyPreview() {
    MaterialTheme {
        OrderPanel(
            selectedKeys = emptyList(),
            items = emptyList(),
            totalFiat = "0.00",
            totalSat = "0",
            onAddItem = {},
            onReduceItem = {},
            onCountChange = { _, _ -> },
            onCheckout = {},
            onClearCart = {},
            modifier = Modifier.fillMaxSize().padding(16.dp)
        )
    }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 600)
@Composable
fun OrderPanelWithItemsPreview() {
    MaterialTheme {
        OrderPanel(
            selectedKeys = listOf(1, 3, 7),
            items = previewOrderItems,
            totalFiat = "390.00",
            totalSat = "96,250",
            onAddItem = {},
            onReduceItem = {},
            onCountChange = { _, _ -> },
            onCheckout = {},
            onClearCart = {},
            modifier = Modifier.fillMaxSize().padding(16.dp)
        )
    }
}
