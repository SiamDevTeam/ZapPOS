/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.screens.product.goods

import kotlinx.serialization.json.*
import org.siamdev.zappos.data.source.EventKind
import org.siamdev.zappos.data.source.MasterEvent
import org.siamdev.zappos.data.source.products

enum class ProductType { GOODS, SERVICE, RENTAL }

enum class DetailTab { PRODUCT_DETAIL, MONITOR_STOCK }

val MasterEvent.productType: ProductType
    get() = when (kind) {
        EventKind.SERVICE -> ProductType.SERVICE
        EventKind.RENTAL -> ProductType.RENTAL
        else -> ProductType.GOODS
    }


private const val SAMPLE_IMAGE = "https://images.pexels.com/photos/350478/pexels-photo-350478.jpeg"

private fun itemInfo(block: JsonObjectBuilder.() -> Unit): List<JsonObject> =
    listOf(buildJsonObject { put("item_info", buildJsonObject(block)) })

private fun optionGroup(
    name: String,
    required: Boolean = false,
    multiSelect: Boolean = false,
    vararg options: Pair<String, Int>,
): List<JsonObject> = buildList {
    add(buildJsonObject {
        put("option_group", buildJsonObject {
            put("name", name)
            put("required", required)
            put("multi_select", multiSelect)
        })
    })
    options.forEach { (optName, mod) ->
        add(buildJsonObject {
            put("option", buildJsonObject { put("name", optName); put("modifier", mod) })
        })
    }
}

internal fun sampleEvents(): List<MasterEvent> = listOf(

    MasterEvent(
        id = "PRD202606070001",
        kind = EventKind.PRODUCT,
        tags = listOf(
            listOf("category", "beverages"),
            listOf("subcategory", "tea_coffee"),
            listOf("sku", "ZP-1085"),
            listOf("recommended", "true"),
            listOf("image", "remote", SAMPLE_IMAGE),
        ),
        content = listOf(
            itemInfo {
                put("name", "Green Tea Latte")
                put(
                    "description",
                    "Smooth and creamy matcha latte made with premium Japanese matcha and oat milk."
                )
                put("price", 85.0); put("unit", "cup")
                put("stockQty", 50); put("stockMax", 200)
                put("soldThisWeek", 186); put("revenue7d", 15820.0)
            },
            optionGroup(
                "Size", true, false,
                "Small" to -10, "Medium" to 0, "Large" to 15
            ),
            optionGroup(
                "Milk", false, false,
                "Whole Milk" to 0, "Oat Milk" to 10, "Almond Milk" to 10, "Coconut Milk" to 5
            ),
            optionGroup(
                "Add-ons", false, true,
                "Extra Matcha Shot" to 15, "Whipped Cream" to 10, "Less Sweet" to 0, "No Ice" to 0
            ),
        ),
    ),

    MasterEvent(
        id = "PRD202606070002",
        kind = EventKind.PRODUCT,
        tags = listOf(
            listOf("category", "beverages"),
            listOf("subcategory", "tea_coffee"),
            listOf("sku", "ZP-1086"),
            listOf("image", "remote", SAMPLE_IMAGE),
        ),
        content = listOf(itemInfo {
            put("name", "Americano")
            put("description", "Classic espresso with hot water. Bold and rich flavor.")
            put("price", 65.0); put("unit", "cup")
            put("stockQty", 195); put("stockMax", 300)
            put("soldThisWeek", 94); put("revenue7d", 6110.0)
        }),
    ),

    MasterEvent(
        id = "PRD202606070003",
        kind = EventKind.PRODUCT,
        tags = listOf(
            listOf("category", "beverages"),
            listOf("subcategory", "tea_coffee"),
            listOf("sku", "ZP-1087"),
            listOf("recommended", "true"),
            listOf("image", "remote", SAMPLE_IMAGE),
        ),
        content = listOf(itemInfo {
            put("name", "Thai Milk Tea")
            put("description", "Traditional Thai tea with condensed milk and sweet cream.")
            put("price", 60.0); put("unit", "cup")
            put("stockQty", 30); put("stockMax", 100)
            put("soldThisWeek", 120); put("revenue7d", 7200.0)
        }),
    ),

    MasterEvent(
        id = "PRD202606070004",
        kind = EventKind.PRODUCT,
        tags = listOf(
            listOf("category", "beverages"),
            listOf("subcategory", "energy"),
            listOf("sku", "ZP-1088"),
            listOf("image", "remote", SAMPLE_IMAGE),
        ),
        content = listOf(itemInfo {
            put("name", "Red Bull")
            put("description", "Classic energy drink for a quick boost.")
            put("price", 35.0); put("unit", "bottle")
            put("stockQty", 100); put("stockMax", 300)
            put("soldThisWeek", 210); put("revenue7d", 7350.0)
        }),
    ),

    MasterEvent(
        id = "PRD202606070005",
        kind = EventKind.PRODUCT,
        tags = listOf(
            listOf("category", "beverages"),
            listOf("subcategory", "soft_drinks"),
            listOf("sku", "ZP-1089"),
            listOf("image", "remote", SAMPLE_IMAGE),
        ),
        content = listOf(itemInfo {
            put("name", "Sprite")
            put("description", "Refreshing lemon-lime soft drink.")
            put("price", 20.0); put("unit", "bottle")
            put("stockQty", 80); put("stockMax", 250)
            put("soldThisWeek", 165); put("revenue7d", 3300.0)
        }),
    ),

    MasterEvent(
        id = "PRD202606070006",
        kind = EventKind.PRODUCT,
        tags = listOf(
            listOf("category", "food"),
            listOf("subcategory", "noodles"),
            listOf("sku", "ZP-1090"),
            listOf("recommended", "true"),
            listOf("image", "remote", SAMPLE_IMAGE),
        ),
        content = listOf(itemInfo {
            put("name", "Pad Thai")
            put("description", "Stir-fried rice noodles with eggs, tofu, and bean sprouts.")
            put("price", 120.0); put("unit", "plate")
            put("soldThisWeek", 78); put("revenue7d", 9360.0)
        }),
    ),

    MasterEvent(
        id = "PRD202606070007",
        kind = EventKind.PRODUCT,
        tags = listOf(
            listOf("category", "food"),
            listOf("subcategory", "rice"),
            listOf("sku", "ZP-1091"),
            listOf("image", "remote", SAMPLE_IMAGE),
        ),
        content = listOf(itemInfo {
            put("name", "Khao Man Gai")
            put("description", "Thai steamed chicken rice served with a savory broth.")
            put("price", 80.0); put("unit", "plate")
            put("soldThisWeek", 55); put("revenue7d", 4400.0)
        }),
    ),

    MasterEvent(
        id = "PRD202606070008",
        kind = EventKind.PRODUCT,
        tags = listOf(
            listOf("category", "food"),
            listOf("subcategory", "soup"),
            listOf("sku", "ZP-1092"),
            listOf("recommended", "true"),
            listOf("image", "remote", SAMPLE_IMAGE),
        ),
        content = listOf(itemInfo {
            put("name", "Tom Yum Soup")
            put("description", "Spicy and sour Thai soup with shrimp, mushrooms, and lemongrass.")
            put("price", 150.0); put("unit", "bowl")
            put("soldThisWeek", 42); put("revenue7d", 6300.0)
        }),
    ),

    MasterEvent(
        id = "PRD202606070009",
        kind = EventKind.PRODUCT,
        tags = listOf(
            listOf("category", "snack"),
            listOf("subcategory", "chips"),
            listOf("sku", "ZP-1093"),
            listOf("image", "remote", SAMPLE_IMAGE),
        ),
        content = listOf(itemInfo {
            put("name", "Potato Chips")
            put("description", "Crispy potato chips with original flavor.")
            put("price", 25.0); put("unit", "pack")
            put("stockQty", 200); put("stockMax", 500)
            put("soldThisWeek", 300); put("revenue7d", 7500.0)
        }),
    ),

    MasterEvent(
        id = "PRD2026060700010",
        kind = EventKind.PRODUCT,
        tags = listOf(
            listOf("category", "dessert"),
            listOf("subcategory", "thai_dessert"),
            listOf("sku", "ZP-1094"),
            listOf("recommended", "true"),
            listOf("image", "remote", SAMPLE_IMAGE),
        ),
        content = listOf(itemInfo {
            put("name", "Mango Sticky Rice")
            put("description", "Sweet glutinous rice topped with fresh mango and coconut milk.")
            put("price", 95.0); put("unit", "plate")
            put("soldThisWeek", 63); put("revenue7d", 5985.0)
        }),
    ),

    MasterEvent(
        id = "PRD2026060700011",
        kind = EventKind.PRODUCT,
        tags = listOf(
            listOf("category", "dessert"),
            listOf("subcategory", "ice_cream"),
            listOf("sku", "ZP-1095"),
            listOf("available", "false"),
            listOf("image", "remote", SAMPLE_IMAGE),
        ),
        content = listOf(itemInfo {
            put("name", "Ice Cream Cone")
            put("description", "Soft serve vanilla ice cream in a waffle cone.")
            put("price", 55.0); put("unit", "piece")
            put("stockQty", 0); put("stockMax", 50)
            put("soldThisWeek", 0); put("revenue7d", 0.0)
        }),
    ),

    MasterEvent(
        id = "PRD2026060700012",
        kind = EventKind.PRODUCT,
        tags = listOf(
            listOf("category", "beverages"),
            listOf("subcategory", "electrolyte"),
            listOf("sku", "ZP-1096"),
            listOf("image", "remote", SAMPLE_IMAGE),
        ),
        content = listOf(itemInfo {
            put("name", "Electrolyte Water")
            put("description", "Mineral-rich electrolyte water for hydration.")
            put("price", 30.0); put("unit", "bottle")
            put("stockQty", 150); put("stockMax", 400)
            put("soldThisWeek", 280); put("revenue7d", 8400.0)
        }),
    ),

    MasterEvent(
        id = "PRD2026060700013",
        kind = EventKind.PRODUCT,
        tags = listOf(
            listOf("category", "beverages"),
            listOf("subcategory", "tea_coffee"),
            listOf("sku", "ZP-1097"),
            listOf("recommended", "true"),
            listOf("image", "remote", SAMPLE_IMAGE),
        ),
        content = listOf(itemInfo {
            put("name", "Earl Grey Milk Tea")
            put(
                "description",
                "Fragrant Earl Grey tea with fresh milk and a hint of bergamot. Best served warm."
            )
            put("price", 75.0); put("unit", "cup")
            put("stockQty", 40); put("stockMax", 150)
            put("lowStockAlert", 15)
            put("supplier", "Doi Chang Tea Estate")
            put("chargeVat", true); put("costPrice", 32.0)
            put("soldThisWeek", 95); put("revenue7d", 7125.0)
        }),
    ),
)

internal fun sampleProducts(): List<MasterEvent> = sampleEvents().products