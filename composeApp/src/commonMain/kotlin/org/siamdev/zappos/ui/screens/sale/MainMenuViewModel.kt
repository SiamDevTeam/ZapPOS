/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.screens.sale

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

data class MenuItem(
    val id: Int,
    val imageUrl: String,
    val name: String,
    val priceBaht: String,
    val priceSat: String,
    val category: String = "",
    val count: UInt = 0u
)

class MainMenuViewModel : ViewModel() {

    var isLoading by mutableStateOf(false)
        private set

    private var hasLoaded = false // guard ไม่ให้ load ซ้ำ

    private val _items = mutableStateListOf<MenuItem>()
    val items: List<MenuItem> get() = _items

    private val _selectedKeys = mutableStateListOf<Int>()
    val selectedKeys: List<Int> get() = _selectedKeys


    fun loadProductsData() {
        if (hasLoaded || isLoading) return
        isLoading = true
        hasLoaded = true
        viewModelScope.launch {
            println("start...")

            delay(2000.milliseconds)

            _items.addAll(
                listOf(
                    MenuItem(1, "https://images.pexels.com/photos/350478/pexels-photo-350478.jpeg",
                        "Mocha", "70.00", "17,500", "coffee"),
                    MenuItem(2, "https://images.pexels.com/photos/17486832/pexels-photo-17486832.jpeg",
                        "Latte", "70.00", "17,500", "coffee"),
                    MenuItem(3, "https://images.pexels.com/photos/2611811/pexels-photo-2611811.jpeg",
                        "Matcha Latte", "100.00", "26,000", "matcha"),
                    MenuItem(4, "https://images.pexels.com/photos/18635175/pexels-photo-18635175.jpeg",
                        "Matcha Coffee", "100.00", "26,000", "matcha"),
                    MenuItem(5, "https://images.pexels.com/photos/302899/pexels-photo-302899.jpeg",
                        "Espresso", "50.00", "12,500", "coffee"),
                    MenuItem(6, "https://images.pexels.com/photos/414555/pexels-photo-414555.jpeg",
                        "Americano", "60.00", "15,000", "coffee"),
                    MenuItem(7, "https://images.pexels.com/photos/585750/pexels-photo-585750.jpeg",
                        "Cappuccino", "75.00", "18,750", "coffee"),
                    MenuItem(8, "https://images.pexels.com/photos/312418/pexels-photo-312418.jpeg",
                        "Flat White", "80.00", "20,000", "coffee"),
                    MenuItem(9, "https://images.pexels.com/photos/2103949/pexels-photo-2103949.jpeg",
                        "Caramel Macchiato", "90.00", "22,500", "coffee"),
                    MenuItem(10, "https://images.pexels.com/photos/302902/pexels-photo-302902.jpeg",
                        "Iced Coffee", "65.00", "16,250", "coffee"),
                    MenuItem(11, "https://images.pexels.com/photos/2907301/pexels-photo-2907301.jpeg",
                        "Thai Tea", "60.00", "15,000", "tea"),
                    MenuItem(12, "https://images.pexels.com/photos/1337825/pexels-photo-1337825.jpeg",
                        "Green Tea", "55.00", "13,750", "tea"),
                    MenuItem(13, "https://images.pexels.com/photos/374885/pexels-photo-374885.jpeg",
                        "Hot Chocolate", "85.00", "21,250", "other"),
                    MenuItem(14, "https://images.pexels.com/photos/416656/pexels-photo-416656.jpeg",
                        "Milk", "50.00", "12,500", "other")
                )
            )

            isLoading = false
            println("end...")
        }
    }

    private fun updateSelectedKeys(id: Int) {
        val item = _items.firstOrNull { it.id == id } ?: return
        if (item.count > 0u && !_selectedKeys.contains(id)) {
            _selectedKeys.add(id)
        } else if (item.count == 0u && _selectedKeys.contains(id)) {
            _selectedKeys.remove(id)
        }
    }

    fun addItem(id: Int) {
        val index = _items.indexOfFirst { it.id == id }
        if (index != -1) {
            val item = _items[index]
            _items[index] = item.copy(count = item.count + 1u)
            updateSelectedKeys(id)
        }
    }

    fun reduceItem(id: Int) {
        val index = _items.indexOfFirst { it.id == id }
        if (index != -1) {
            val item = _items[index]
            if (item.count > 0u) {
                _items[index] = item.copy(count = item.count - 1u)
                updateSelectedKeys(id)
            }
        }
    }

    fun setItemCount(id: Int, count: UInt) {
        val index = _items.indexOfFirst { it.id == id }
        if (index != -1) {
            _items[index] = _items[index].copy(count = count)
            updateSelectedKeys(id)
        }
    }

    fun clearAllItems() {
        for (i in _items.indices) {
            val item = _items[i]
            if (item.count > 0u) {
                _items[i] = item.copy(count = 0u)
            }
        }
        _selectedKeys.clear()
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