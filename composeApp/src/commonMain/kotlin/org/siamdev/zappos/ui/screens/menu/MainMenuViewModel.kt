package org.siamdev.zappos.ui.screens.menu

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

data class MenuItem(
    val id: Int,
    val imageUrl: String,
    val name: String,
    val priceBaht: String,
    val priceSat: String,
    val count: UInt = 0u
)

class MainMenuViewModel : ViewModel() {

    private val _items = mutableStateListOf<MenuItem>()
    val items: List<MenuItem> get() = _items

    init {
        _items.addAll(
            listOf(
                MenuItem(
                    id = 1,
                    imageUrl = "https://images.pexels.com/photos/350478/pexels-photo-350478.jpeg",
                    name = "Mocha",
                    priceBaht = "70.00",
                    priceSat = "17,500"
                ),
                MenuItem(
                    id = 2,
                    imageUrl = "https://images.pexels.com/photos/17486832/pexels-photo-17486832.jpeg",
                    name = "Latte",
                    priceBaht = "70.00",
                    priceSat = "17,500"
                ),
                MenuItem(
                    id = 3,
                    imageUrl = "https://images.pexels.com/photos/2611811/pexels-photo-2611811.jpeg",
                    name = "Matcha Latte",
                    priceBaht = "100.00",
                    priceSat = "26,000"
                ),
                MenuItem(
                    id = 4,
                    imageUrl = "https://images.pexels.com/photos/18635175/pexels-photo-18635175.jpeg",
                    name = "Matcha Coffee",
                    priceBaht = "100.00",
                    priceSat = "26,000"
                )
            )
        )
    }

    fun addItem(id: Int) {
        val index = _items.indexOfFirst { it.id == id }
        if (index != -1) {
            val item = _items[index]
            _items[index] = item.copy(count = item.count + 1u)
        }
    }

    fun reduceItem(id: Int) {
        val index = _items.indexOfFirst { it.id == id }
        if (index != -1) {
            val item = _items[index]
            if (item.count > 0u) {
                _items[index] = item.copy(count = item.count - 1u)
            }
        }
    }

    fun clearAllItems() {
        for (i in _items.indices) {
            val item = _items[i]
            if (item.count > 0u) {
                _items[i] = item.copy(count = 0u)
            }
        }
    }

    val totalFiat: String
        get() {
            var total = 0.0
            for (item in _items) {
                total += item.priceBaht.toDouble() * item.count.toDouble()
            }
            return formatNumber(total)
        }

    val totalSat: String
        get() {
            var total = 0.0
            for (item in _items) {
                val satValue = item.priceSat.replace(",", "").toDoubleOrNull() ?: 0.0
                total += satValue * item.count.toDouble()
            }
            return formatNumber(total)
        }



    fun formatNumber(value: Double): String {
        val intPart = value.toLong()
        val decimalPart = ((value - intPart) * 100).toInt()

        val intStr = intPart.toString().reversed().chunked(3).joinToString(",").reversed()
        val decimalStr = decimalPart.toString().padStart(2, '0')
        return "$intStr.$decimalStr"
    }

}
