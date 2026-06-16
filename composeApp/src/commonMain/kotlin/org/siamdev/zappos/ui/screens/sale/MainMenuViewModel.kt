/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.screens.sale

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.Json
import org.siamdev.zappos.cache.ImagePreloader
import org.siamdev.zappos.cache.ThumbnailSection
import org.siamdev.zappos.ui.components.product.ProductBrowser
import kotlin.time.Duration.Companion.milliseconds

private const val TAG = "MainMenu"

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

internal val menuJson = Json { ignoreUnknownKeys = true }

const val MOCK_MENU_JSON = """
[
  { "id": 1,  "imageUrl": "https://images.pexels.com/photos/350478/pexels-photo-350478.jpeg",     "name": "Mocha",             "priceBaht": "70.00",  "priceSat": "17,500", "category": "coffee", "isRecommended": true,  "isAvailable": true  },
  { "id": 2,  "imageUrl": "https://images.pexels.com/photos/17486832/pexels-photo-17486832.jpeg", "name": "Latte",             "priceBaht": "70.00",  "priceSat": "17,500", "category": "coffee", "isRecommended": false, "isAvailable": true  },
  { "id": 3,  "imageUrl": "https://images.pexels.com/photos/2611811/pexels-photo-2611811.jpeg",   "name": "Matcha Latte",      "priceBaht": "100.00", "priceSat": "26,000", "category": "matcha", "isRecommended": true,  "isAvailable": true  },
  { "id": 4,  "imageUrl": "https://images.pexels.com/photos/18635175/pexels-photo-18635175.jpeg", "name": "Matcha Coffee",     "priceBaht": "100.00", "priceSat": "26,000", "category": "matcha", "isRecommended": false, "isAvailable": true  },
  { "id": 5,  "imageUrl": "https://images.pexels.com/photos/302899/pexels-photo-302899.jpeg",     "name": "Espresso",          "priceBaht": "50.00",  "priceSat": "12,500", "category": "coffee", "isRecommended": false, "isAvailable": true  },
  { "id": 6,  "imageUrl": "https://images.pexels.com/photos/414555/pexels-photo-414555.jpeg",     "name": "Americano",         "priceBaht": "60.00",  "priceSat": "15,000", "category": "coffee", "isRecommended": false, "isAvailable": true  },
  { "id": 7,  "imageUrl": "https://images.pexels.com/photos/585750/pexels-photo-585750.jpeg",     "name": "Cappuccino",        "priceBaht": "75.00",  "priceSat": "18,750", "category": "coffee", "isRecommended": true,  "isAvailable": true  },
  { "id": 8,  "imageUrl": "https://images.pexels.com/photos/312418/pexels-photo-312418.jpeg",     "name": "Flat White",        "priceBaht": "80.00",  "priceSat": "20,000", "category": "coffee", "isRecommended": false, "isAvailable": true  },
  { "id": 9,  "imageUrl": "https://images.pexels.com/photos/2103949/pexels-photo-2103949.jpeg",   "name": "Caramel Macchiato", "priceBaht": "90.00",  "priceSat": "22,500", "category": "coffee", "isRecommended": false, "isAvailable": true  },
  { "id": 10, "imageUrl": "https://images.pexels.com/photos/302902/pexels-photo-302902.jpeg",     "name": "Iced Coffee",       "priceBaht": "65.00",  "priceSat": "16,250", "category": "coffee", "isRecommended": false, "isAvailable": true  },
  { "id": 11, "imageUrl": "https://images.pexels.com/photos/2907301/pexels-photo-2907301.jpeg",   "name": "Thai Tea",          "priceBaht": "60.00",  "priceSat": "15,000", "category": "tea",    "isRecommended": true,  "isAvailable": true  },
  { "id": 12, "imageUrl": "https://images.pexels.com/photos/1337825/pexels-photo-1337825.jpeg",   "name": "Green Tea",         "priceBaht": "55.00",  "priceSat": "13,750", "category": "tea",    "isRecommended": false, "isAvailable": true  },
  { "id": 13, "imageUrl": "https://images.pexels.com/photos/374885/pexels-photo-374885.jpeg",     "name": "Hot Chocolate",     "priceBaht": "85.00",  "priceSat": "21,250", "category": "other",  "isRecommended": false, "isAvailable": true  },
  { "id": 14, "imageUrl": "https://images.pexels.com/photos/416656/pexels-photo-416656.jpeg",     "name": "Milk",              "priceBaht": "50.00",  "priceSat": "12,500", "category": "other",  "isRecommended": false, "isAvailable": false }
]
"""

val SaleOrderSteps = listOf("Confirm", "Checkout", "Payment", "Successful")

fun loadMenuItems(): List<MenuItem> =
    menuJson.decodeFromString<List<MenuItem>>(MOCK_MENU_JSON)


class MainMenuViewModel(
    private val autoLoad: Boolean = true
) : ViewModel(), ProductBrowser {

    data class State(
        val isLoading: Boolean = false,
        val catalog: Catalog = Catalog(),
        val order: Order = Order()
    ) {
        data class Catalog(val items: List<MenuItem> = emptyList())
        data class Order(
            val selectedKeys: List<Int> = emptyList(),
            val totalFiat: String = "0.00",
            val totalSat: String = "0"
        )
    }

    sealed class SideEffect

    private val _state = MutableStateFlow(State())
    val state: StateFlow<State> = _state.asStateFlow()

    override val browserState: StateFlow<ProductBrowser.BrowserState> = _state
        .map { s ->
            ProductBrowser.BrowserState(
                items = s.catalog.items,
                isLoading = s.isLoading
            )
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, ProductBrowser.BrowserState())

    private var hasLoaded = false

    private fun State.withRecomputedTotals(): State {
        val items = catalog.items
        return copy(
            order = order.copy(
                totalFiat = computeFiat(items),
                totalSat = computeSat(items)
            )
        )
    }

    private fun computeFiat(items: List<MenuItem>): String {
        var total = 0.0
        for (item in items) total += item.priceBaht.toDouble() * item.count.toDouble()
        return formatNumber(total)
    }

    private fun computeSat(items: List<MenuItem>): String {
        var total = 0.0
        for (item in items) {
            val sat = item.priceSat.replace(",", "").toDoubleOrNull() ?: 0.0
            total += sat * item.count.toDouble()
        }
        return formatNumber(total)
    }

    private fun formatNumber(value: Double): String {
        val intPart = value.toLong()
        val decPart = ((value - intPart) * 100).toInt()
        val intStr = intPart.toString().reversed().chunked(3).joinToString(",").reversed()
        return "$intStr.${decPart.toString().padStart(2, '0')}"
    }

    private fun List<MenuItem>.updatedSelectedKeys(): List<Int> =
        filter { it.count > 0u }.map { it.id }

    fun ensureLoaded() {
        if (!autoLoad) return
        loadProductsData()
    }

    fun reloadProductsData() {
        hasLoaded = false
        _state.update {
            it.copy(
                catalog = it.catalog.copy(items = emptyList()),
                order = State.Order()
            )
        }
        loadProductsData()
    }

    fun loadProductsData() {
        if (hasLoaded || _state.value.isLoading) return

        hasLoaded = true
        _state.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            println("[$TAG] Fetching product list from network...")
            delay(2000.milliseconds)
            val loaded = loadMenuItems()
            _state.update {
                it.copy(
                    isLoading = false,
                    catalog = it.catalog.copy(items = loaded)
                ).withRecomputedTotals()
            }
            println("[$TAG] Loaded ${loaded.size} products")
            launch { fetchMissingThumbnails() }
        }
    }

    private suspend fun fetchMissingThumbnails() {
        println("[$TAG] Checking thumbnails for missing entries...")
        ImagePreloader.preloadMenuItems(
            section = ThumbnailSection.PRODUCTS,
            onProgress = { progress ->
                val pct = (progress * 100).toInt()
                if (pct % 25 == 0) println("[$TAG] Thumbnail fetch progress: $pct%")
            }
        )
        println("[$TAG] Thumbnail fetch complete")
    }

    override fun reload() = reloadProductsData()

    override fun addItem(id: Int) {
        _state.update { s ->
            val items = s.catalog.items.map { item ->
                if (item.id == id) item.copy(count = item.count + 1u) else item
            }
            s.copy(
                catalog = s.catalog.copy(items = items),
                order = s.order.copy(selectedKeys = items.updatedSelectedKeys())
            ).withRecomputedTotals()
        }
    }

    override fun reduceItem(id: Int) {
        _state.update { s ->
            val items = s.catalog.items.map { item ->
                if (item.id == id && item.count > 0u) item.copy(count = item.count - 1u) else item
            }
            s.copy(
                catalog = s.catalog.copy(items = items),
                order = s.order.copy(selectedKeys = items.updatedSelectedKeys())
            ).withRecomputedTotals()
        }
    }

    fun setItemCount(id: Int, count: UInt) {
        _state.update { s ->
            val items = s.catalog.items.map { item ->
                if (item.id == id) item.copy(count = count) else item
            }
            s.copy(
                catalog = s.catalog.copy(items = items),
                order = s.order.copy(selectedKeys = items.updatedSelectedKeys())
            ).withRecomputedTotals()
        }
    }

    internal fun loadItemsForPreview(items: List<MenuItem>) {
        _state.update { s ->
            s.copy(
                catalog = s.catalog.copy(items = items),
                order = s.order.copy(selectedKeys = items.updatedSelectedKeys())
            ).withRecomputedTotals()
        }
        hasLoaded = true
    }

    fun clearAllItems() {
        _state.update { s ->
            val cleared = s.catalog.items.map { it.copy(count = 0u) }
            s.copy(
                catalog = s.catalog.copy(items = cleared),
                order = State.Order()
            )
        }
    }
}