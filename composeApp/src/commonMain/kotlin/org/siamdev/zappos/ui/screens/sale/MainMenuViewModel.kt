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
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.Json
import kotlin.time.Duration.Companion.milliseconds

@Serializable
data class MenuItem(
    val id: Int,
    val imageUrl: String,
    val name: String,
    val priceBaht: String,
    val priceSat: String,
    val category: String = "",
    val isRecommended: Boolean = false,
    val isAvailable: Boolean = true,
    @Transient val count: UInt = 0u
)

private val menuJson = Json { ignoreUnknownKeys = true }

private const val MOCK_MENU_JSON = """
[
  { "id": 1,  "imageUrl": "https://images.pexels.com/photos/350478/pexels-photo-350478.jpeg",    "name": "Mocha",             "priceBaht": "70.00",  "priceSat": "17,500", "category": "coffee", "isRecommended": true,  "isAvailable": true  },
  { "id": 2,  "imageUrl": "https://images.pexels.com/photos/17486832/pexels-photo-17486832.jpeg","name": "Latte",             "priceBaht": "70.00",  "priceSat": "17,500", "category": "coffee", "isRecommended": false, "isAvailable": true  },
  { "id": 3,  "imageUrl": "https://images.pexels.com/photos/2611811/pexels-photo-2611811.jpeg",  "name": "Matcha Latte",      "priceBaht": "100.00", "priceSat": "26,000", "category": "matcha", "isRecommended": true,  "isAvailable": true  },
  { "id": 4,  "imageUrl": "https://images.pexels.com/photos/18635175/pexels-photo-18635175.jpeg","name": "Matcha Coffee",     "priceBaht": "100.00", "priceSat": "26,000", "category": "matcha", "isRecommended": false, "isAvailable": true  },
  { "id": 5,  "imageUrl": "https://images.pexels.com/photos/302899/pexels-photo-302899.jpeg",    "name": "Espresso",          "priceBaht": "50.00",  "priceSat": "12,500", "category": "coffee", "isRecommended": false, "isAvailable": true  },
  { "id": 6,  "imageUrl": "https://images.pexels.com/photos/414555/pexels-photo-414555.jpeg",    "name": "Americano",         "priceBaht": "60.00",  "priceSat": "15,000", "category": "coffee", "isRecommended": false, "isAvailable": true  },
  { "id": 7,  "imageUrl": "https://images.pexels.com/photos/585750/pexels-photo-585750.jpeg",    "name": "Cappuccino",        "priceBaht": "75.00",  "priceSat": "18,750", "category": "coffee", "isRecommended": true,  "isAvailable": true  },
  { "id": 8,  "imageUrl": "https://images.pexels.com/photos/312418/pexels-photo-312418.jpeg",    "name": "Flat White",        "priceBaht": "80.00",  "priceSat": "20,000", "category": "coffee", "isRecommended": false, "isAvailable": true  },
  { "id": 9,  "imageUrl": "https://images.pexels.com/photos/2103949/pexels-photo-2103949.jpeg",  "name": "Caramel Macchiato", "priceBaht": "90.00",  "priceSat": "22,500", "category": "coffee", "isRecommended": false, "isAvailable": true  },
  { "id": 10, "imageUrl": "https://images.pexels.com/photos/302902/pexels-photo-302902.jpeg",    "name": "Iced Coffee",       "priceBaht": "65.00",  "priceSat": "16,250", "category": "coffee", "isRecommended": false, "isAvailable": true  },
  { "id": 11, "imageUrl": "https://images.pexels.com/photos/2907301/pexels-photo-2907301.jpeg",  "name": "Thai Tea",          "priceBaht": "60.00",  "priceSat": "15,000", "category": "tea",    "isRecommended": true,  "isAvailable": true  },
  { "id": 12, "imageUrl": "https://images.pexels.com/photos/1337825/pexels-photo-1337825.jpeg",  "name": "Green Tea",         "priceBaht": "55.00",  "priceSat": "13,750", "category": "tea",    "isRecommended": false, "isAvailable": true  },
  { "id": 13, "imageUrl": "https://images.pexels.com/photos/374885/pexels-photo-374885.jpeg",    "name": "Hot Chocolate",     "priceBaht": "85.00",  "priceSat": "21,250", "category": "other",  "isRecommended": false, "isAvailable": true  },
  { "id": 14, "imageUrl": "https://images.pexels.com/photos/416656/pexels-photo-416656.jpeg",    "name": "Milk",              "priceBaht": "50.00",  "priceSat": "12,500", "category": "other",  "isRecommended": false, "isAvailable": false }
]
"""


class MainMenuViewModel : ViewModel() {

    var isLoading by mutableStateOf(false)
        private set

    private var hasLoaded = false // guard ไม่ให้ load ซ้ำ

    private val _items = mutableStateListOf<MenuItem>()
    val items: List<MenuItem> get() = _items

    private val _selectedKeys = mutableStateListOf<Int>()
    val selectedKeys: List<Int> get() = _selectedKeys


    fun reloadProductsData() {
        hasLoaded = false
        _items.clear()
        loadProductsData()
    }

    fun loadProductsData() {
        if (hasLoaded || isLoading) return
        isLoading = true
        hasLoaded = true
        viewModelScope.launch {
            println("start...")

            delay(2000.milliseconds)

            _items.addAll(menuJson.decodeFromString<List<MenuItem>>(MOCK_MENU_JSON))

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