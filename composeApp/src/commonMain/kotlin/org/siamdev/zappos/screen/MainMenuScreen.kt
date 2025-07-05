package org.siamdev.zappos.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.siamdev.zappos.view.MaterialButton
import org.siamdev.zappos.view.MaterialOutlinedButton

@Composable
fun MainMenuScreen() {
    val cart = remember { mutableStateListOf<CheckoutOrder>() }
    var showQrDialog by remember { mutableStateOf(false) }

    fun addToCart(menuItem: MenuItem) {
        val index = cart.indexOfFirst { it.name == menuItem.name }
        if (index >= 0) {
            // Update existing item quantity
            val item = cart[index]
            cart[index] = item.copy(
                quantity = item.quantity + 1,
                total = "${(item.quantity + 1) * (item.sats.filter { it.isDigit() }.toIntOrNull() ?: 0)} sat"
            )
        } else {
            // Add new item
            val satsInt = menuItem.priceSats.filter { it.isDigit() }.toIntOrNull() ?: 0
            cart.add(
                CheckoutOrder(
                    name = menuItem.name,
                    sats = menuItem.priceSats,
                    quantity = 1,
                    total = "$satsInt sat"
                )
            )
        }
    }

    fun increaseItem(name: String) {
        val index = cart.indexOfFirst { it.name == name }
        if (index >= 0) {
            val item = cart[index]
            val satsInt = item.sats.filter { it.isDigit() }.toIntOrNull() ?: 0
            cart[index] = item.copy(
                quantity = item.quantity + 1,
                total = "${(item.quantity + 1) * satsInt} sat"
            )
        }
    }

    fun decreaseItem(name: String) {
        val index = cart.indexOfFirst { it.name == name }
        if (index >= 0) {
            val item = cart[index]
            if (item.quantity > 1) {
                val satsInt = item.sats.filter { it.isDigit() }.toIntOrNull() ?: 0
                cart[index] = item.copy(
                    quantity = item.quantity - 1,
                    total = "${(item.quantity - 1) * satsInt} sat"
                )
            } else {
                cart.removeAt(index)
            }
        }
    }

    fun deleteItem(name: String) {
        val index = cart.indexOfFirst { it.name == name }
        if (index >= 0) {
            cart.removeAt(index)
        }
    }

    fun onCheckout() {
        showQrDialog = true
    }

    fun clearCart() {
        cart.clear()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        // Show QR overlay above main content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(8.dp)
        ) {
            MenuScreen(
                menuList = listOf(
                    MenuItem("Item 1", "100 sat", "3.00 baht", "https://example.com/image1.jpg"),
                    MenuItem("Item 2", "200 sat", "6.00 baht", "https://example.com/image2.jpg"),
                    MenuItem("Item 3", "300 sat", "9.00 baht", "https://example.com/image3.jpg"),
                    MenuItem("Item 4", "400 sat", "12.00 baht", "https://example.com/image4.jpg")
                ),
                onMenuItemClick = { addToCart(it) }
            )
            Spacer(modifier = Modifier.height(8.dp))
            CheckoutScreen(
                checkoutList = cart,
                onAddItemClick = { name -> increaseItem(name) },
                onRemoveItemClick = { name -> decreaseItem(name) },
                onDeleteItemClick = { name -> deleteItem(name) },
                onCheckout = { onCheckout() },
                onClearCart = { clearCart() }
            )
        }
        if (showQrDialog) {
            openQrCode(
                onCancel = { showQrDialog = false },
                onInquire = { /* TODO: handle inquire */ }
            )
        }
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

@Composable
fun openQrCode(onCancel: () -> Unit, onInquire: () -> Unit) {
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .wrapContentSize(Alignment.Center)
            .padding(horizontal = 32.dp)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .background(Color.White, shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Scan QR Code",
                style = MaterialTheme.typography.headlineMedium,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .width(200.dp)
                    .height(200.dp)
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Text("[QR Scanner]")
            }
            Spacer(modifier = Modifier.height(24.dp))
            Row {
                MaterialOutlinedButton(
                    text = "Cancel",
                    onClick = onCancel,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(16.dp))
                MaterialButton(
                    text = "Inquire",
                    onClick = onInquire,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}