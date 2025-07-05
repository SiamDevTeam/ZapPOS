package org.siamdev.zappos

// Menu
class MenuItem(
    val name: String,
    val priceSats: String,
    val priceBaht: String,
    val imageUrl: String? = null
)

// Checkout
data class CheckoutOrder(
    val name: String,
    val sats: String,
    val quantity: Int,
    val total: String
)

// Transaction history
class TransactionHistory(
    val transactionId: String,
    val date: String,
    val totalSats: String,
    val totalBaht: String,
    val items: List<TransactionItem> = emptyList()
)

class TransactionItem(
    val itemName: String,
    val quantity: String,
    val sats: String,
    val baht: String
)