/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.screens.product.goods

import org.siamdev.zappos.data.source.EventKind
import org.siamdev.zappos.data.source.MasterEvent
import org.siamdev.zappos.data.source.products

enum class ProductType {
    GOODS,
    SERVICE,
    RENTAL
}

enum class DetailTab {
    PRODUCT_DETAIL,
    MONITOR_STOCK
}

val MasterEvent.productType: ProductType
    get() =
        when (kind) {
            EventKind.SERVICE -> ProductType.SERVICE
            EventKind.RENTAL -> ProductType.RENTAL
            else -> ProductType.GOODS
        }

private const val SAMPLE_IMAGE = "https://images.pexels.com/photos/350478/pexels-photo-350478.jpeg"

internal fun sampleEvents(): List<MasterEvent> =
    listOf(
        MasterEvent {
            id = "PRD202606070001"
            category("beverages")
            subCategory("tea_coffee")
            sku("ZP-1085")
            recommended()
            image(SAMPLE_IMAGE)
            content { index ->
                index["item_info"] {
                    name = "Green Tea Latte"
                    description =
                        "Smooth and creamy matcha latte made with premium Japanese matcha and oat milk."
                    price = 85.0
                    unit = "cup"
                    stockQty = 50
                    stockMax = 200
                    soldThisWeek = 186
                    revenue7d = 15820.0
                }
                index["option_group"]("Size", required = true) {
                    option("Small", -10)
                    option("Medium", 0)
                    option("Large", 15)
                }
                index["option_group"]("Milk") {
                    option("Whole Milk", 0)
                    option("Oat Milk", 10)
                    option("Almond Milk", 10)
                    option("Coconut Milk", 5)
                }
                index["option_group"]("Add-ons", multiSelect = true) {
                    option("Extra Matcha Shot", 15)
                    option("Whipped Cream", 10)
                    option("Less Sweet", 0)
                    option("No Ice", 0)
                }
            }
        },
        MasterEvent {
            id = "PRD202606070002"
            category("beverages")
            subCategory("tea_coffee")
            sku("ZP-1086")
            image(SAMPLE_IMAGE)
            content { index ->
                index["item_info"] {
                    name = "Americano"
                    description = "Classic espresso with hot water. Bold and rich flavor."
                    price = 65.0
                    unit = "cup"
                    stockQty = 195
                    stockMax = 300
                    soldThisWeek = 94
                    revenue7d = 6110.0
                }
            }
        },
        MasterEvent {
            id = "PRD202606070003"
            category("beverages")
            subCategory("tea_coffee")
            sku("ZP-1087")
            recommended()
            image(SAMPLE_IMAGE)
            content { index ->
                index["item_info"] {
                    name = "Thai Milk Tea"
                    description = "Traditional Thai tea with condensed milk and sweet cream."
                    price = 60.0
                    unit = "cup"
                    stockQty = 30
                    stockMax = 100
                    soldThisWeek = 120
                    revenue7d = 7200.0
                }
            }
        },
        MasterEvent {
            id = "PRD202606070004"
            category("beverages")
            subCategory("energy")
            sku("ZP-1088")
            image(SAMPLE_IMAGE)
            content { index ->
                index["item_info"] {
                    name = "Red Bull"
                    description = "Classic energy drink for a quick boost."
                    price = 35.0
                    unit = "bottle"
                    stockQty = 100
                    stockMax = 300
                    soldThisWeek = 210
                    revenue7d = 7350.0
                }
            }
        },
        MasterEvent {
            id = "PRD202606070005"
            category("beverages")
            subCategory("soft_drinks")
            sku("ZP-1089")
            image(SAMPLE_IMAGE)
            content { index ->
                index["item_info"] {
                    name = "Sprite"
                    description = "Refreshing lemon-lime soft drink."
                    price = 20.0
                    unit = "bottle"
                    stockQty = 80
                    stockMax = 250
                    soldThisWeek = 165
                    revenue7d = 3300.0
                }
            }
        },
        MasterEvent {
            id = "PRD202606070006"
            category("food")
            subCategory("noodles")
            sku("ZP-1090")
            recommended()
            image(SAMPLE_IMAGE)
            content { index ->
                index["item_info"] {
                    name = "Pad Thai"
                    description = "Stir-fried rice noodles with eggs, tofu, and bean sprouts."
                    price = 120.0
                    unit = "plate"
                    soldThisWeek = 78
                    revenue7d = 9360.0
                }
            }
        },
        MasterEvent {
            id = "PRD202606070007"
            category("food")
            subCategory("rice")
            sku("ZP-1091")
            image(SAMPLE_IMAGE)
            content { index ->
                index["item_info"] {
                    name = "Khao Man Gai"
                    description = "Thai steamed chicken rice served with a savory broth."
                    price = 80.0
                    unit = "plate"
                    soldThisWeek = 55
                    revenue7d = 4400.0
                }
            }
        },
        MasterEvent {
            id = "PRD202606070008"
            category("food")
            subCategory("soup")
            sku("ZP-1092")
            recommended()
            image(SAMPLE_IMAGE)
            content { index ->
                index["item_info"] {
                    name = "Tom Yum Soup"
                    description = "Spicy and sour Thai soup with shrimp, mushrooms, and lemongrass."
                    price = 150.0
                    unit = "bowl"
                    soldThisWeek = 42
                    revenue7d = 6300.0
                }
            }
        },
        MasterEvent {
            id = "PRD202606070009"
            category("snack")
            subCategory("chips")
            sku("ZP-1093")
            image(SAMPLE_IMAGE)
            content { index ->
                index["item_info"] {
                    name = "Potato Chips"
                    description = "Crispy potato chips with original flavor."
                    price = 25.0
                    unit = "pack"
                    stockQty = 200
                    stockMax = 500
                    soldThisWeek = 300
                    revenue7d = 7500.0
                }
            }
        },
        MasterEvent {
            id = "PRD2026060700010"
            category("dessert")
            subCategory("thai_dessert")
            sku("ZP-1094")
            recommended()
            image(SAMPLE_IMAGE)
            content { index ->
                index["item_info"] {
                    name = "Mango Sticky Rice"
                    description = "Sweet glutinous rice topped with fresh mango and coconut milk."
                    price = 95.0
                    unit = "plate"
                    soldThisWeek = 63
                    revenue7d = 5985.0
                }
            }
        },
        MasterEvent {
            id = "PRD2026060700011"
            category("dessert")
            subCategory("ice_cream")
            sku("ZP-1095")
            available(false)
            image(SAMPLE_IMAGE)
            content { index ->
                index["item_info"] {
                    name = "Ice Cream Cone"
                    description = "Soft serve vanilla ice cream in a waffle cone."
                    price = 55.0
                    unit = "piece"
                    stockQty = 0
                    stockMax = 50
                    soldThisWeek = 0
                    revenue7d = 0.0
                }
            }
        },
        MasterEvent {
            id = "PRD2026060700012"
            category("beverages")
            subCategory("electrolyte")
            sku("ZP-1096")
            image(SAMPLE_IMAGE)
            content { index ->
                index["item_info"] {
                    name = "Electrolyte Water"
                    description = "Mineral-rich electrolyte water for hydration."
                    price = 30.0
                    unit = "bottle"
                    stockQty = 150
                    stockMax = 400
                    soldThisWeek = 280
                    revenue7d = 8400.0
                }
            }
        },
        MasterEvent {
            id = "PRD2026060700013"
            category("beverages")
            subCategory("tea_coffee")
            sku("ZP-1097")
            recommended()
            image(SAMPLE_IMAGE)
            content { index ->
                index["item_info"] {
                    name = "Earl Grey Milk Tea"
                    description =
                        "Fragrant Earl Grey tea with fresh milk and a hint of bergamot. Best served warm."
                    price = 75.0
                    unit = "cup"
                    stockQty = 40
                    stockMax = 150
                    lowStockAlert = 15
                    supplier = "Doi Chang Tea Estate"
                    chargeVat = true
                    costPrice = 32.0
                    soldThisWeek = 95
                    revenue7d = 7125.0
                }
            }
        },
    )

internal fun sampleProducts(): List<MasterEvent> = sampleEvents().products
