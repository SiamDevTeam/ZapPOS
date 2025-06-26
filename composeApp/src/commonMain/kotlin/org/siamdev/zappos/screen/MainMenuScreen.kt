package org.siamdev.zappos.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun MainMenuScreen() {
    Column(
        modifier = Modifier.background(color = Color.White).fillMaxHeight()
    ) {
        MenuScreen(
            menuList = listOf(
                MenuItem("Item 1", "100 sat", "3.00 baht", "https://example.com/image1.jpg"),
                MenuItem("Item 2", "200 sat", "6.00 baht", "https://example.com/image2.jpg"),
                MenuItem("Item 3", "300 sat", "9.00 baht", "https://example.com/image3.jpg"),
                MenuItem("Item 4", "400 sat", "12.00 baht", "https://example.com/image4.jpg")
            )
        )
        CheckoutScreen(
            checkoutList = emptyList(),
            onAddItemClick = {},
            onRemoveItemClick = {},
            onDeleteItemClick = {}
        )
    }
}

@Composable
fun HistoryScreen() {
    HistoryScreen(
        totalSats = "10000 sats",
        totalBaht = "about ฿21.00",
        historyList = listOf(
            TransactionHistory(
                transactionId = "1",
                date = "June 1, 2025",
                totalSats = "1000 sats",
                totalBaht = "฿20.00",
                items = listOf(
                    TransactionItem("Item 1", "2", "500 sats", "฿10.00"),
                    TransactionItem("Item 2", "1", "500 sats", "฿10.00")
                )
            ),
            TransactionHistory(
                transactionId = "2",
                date = "June 2, 2025",
                totalSats = "2000 sats",
                totalBaht = "฿40.00",
                items = listOf(
                    TransactionItem("Item 3", "4", "1000 sats", "฿20.00"),
                    TransactionItem("Item 4", "2", "1000 sats", "฿20.00")
                )
            ),
            TransactionHistory(
                transactionId = "3",
                date = "June 3, 2025",
                totalSats = "3000 sats",
                totalBaht = "฿60.00",
                items = listOf(
                    TransactionItem("Item 5", "3", "1500 sats", "฿30.00"),
                    TransactionItem("Item 6", "3", "1500 sats", "฿30.00")
                )
            )
        )
    )
}