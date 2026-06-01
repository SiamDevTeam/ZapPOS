/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.screens.product

/** Represents a single menu/product item with pricing, stock, and sales metadata. */
data class Product(
    val id: String,
    val name: String,
    val imageUrl: String = "",
    val price: Double,
    val unit: String,
    val category: String,
    val subCategory: String? = null,
    val description: String = "",
    val isAvailable: Boolean = true,
    val isRecommended: Boolean = false,
    val stockQty: Int? = null,
    val stockMax: Int? = null,
    val sku: String = "",
    val soldThisWeek: Int = 0,
    val revenue7d: Double = 0.0
)

enum class DetailTab { PRODUCT_DETAIL, MONITOR_STOCK }

private const val SAMPLE_IMAGE = "https://images.pexels.com/photos/350478/pexels-photo-350478.jpeg"

internal fun sampleProducts(): List<Product> = listOf(
    Product("1",  "Green Tea Latte",   SAMPLE_IMAGE, 85.0,  "cup",    "beverages", "tea_coffee",
        "Smooth and creamy matcha latte made with premium Japanese matcha and oat milk.",
        true,  true,  50,   200, "ZP-1085", 186, 15820.0),
    Product("2",  "Americano",         SAMPLE_IMAGE, 65.0,  "cup",    "beverages", "tea_coffee",
        "Classic espresso with hot water. Bold and rich flavor.",
        true,  false, 195,  300, "ZP-1086", 94,  6110.0),
    Product("3",  "Thai Milk Tea",     SAMPLE_IMAGE, 60.0,  "cup",    "beverages", "tea_coffee",
        "Traditional Thai tea with condensed milk and sweet cream.",
        true,  true,  30,   100, "ZP-1087", 120, 7200.0),
    Product("4",  "Red Bull",          SAMPLE_IMAGE, 35.0,  "bottle", "beverages", "energy",
        "Classic energy drink for a quick boost.",
        true,  false, 100,  300, "ZP-1088", 210, 7350.0),
    Product("5",  "Sprite",            SAMPLE_IMAGE, 20.0,  "bottle", "beverages", "soft_drinks",
        "Refreshing lemon-lime soft drink.",
        true,  false, 80,   250, "ZP-1089", 165, 3300.0),
    Product("6",  "Pad Thai",          SAMPLE_IMAGE, 120.0, "plate",  "food",      "noodles",
        "Stir-fried rice noodles with eggs, tofu, and bean sprouts.",
        true,  true,  null, null, "ZP-1090", 78,  9360.0),
    Product("7",  "Khao Man Gai",      SAMPLE_IMAGE, 80.0,  "plate",  "food",      "rice",
        "Thai steamed chicken rice served with a savory broth.",
        true,  false, null, null, "ZP-1091", 55,  4400.0),
    Product("8",  "Tom Yum Soup",      SAMPLE_IMAGE, 150.0, "bowl",   "food",      "soup",
        "Spicy and sour Thai soup with shrimp, mushrooms, and lemongrass.",
        true,  true,  null, null, "ZP-1092", 42,  6300.0),
    Product("9",  "Potato Chips",      SAMPLE_IMAGE, 25.0,  "pack",   "snack",     "chips",
        "Crispy potato chips with original flavor.",
        true,  false, 200,  500, "ZP-1093", 300, 7500.0),
    Product("10", "Mango Sticky Rice", SAMPLE_IMAGE, 95.0,  "plate",  "dessert",   "thai_dessert",
        "Sweet glutinous rice topped with fresh mango and coconut milk.",
        true,  true,  null, null, "ZP-1094", 63,  5985.0),
    Product("11", "Ice Cream Cone",    SAMPLE_IMAGE, 55.0,  "piece",  "dessert",   "ice_cream",
        "Soft serve vanilla ice cream in a waffle cone.",
        false, false, 0,    50,  "ZP-1095", 0,   0.0),
    Product("12", "Electrolyte Water", SAMPLE_IMAGE, 30.0,  "bottle", "beverages", "electrolyte",
        "Mineral-rich electrolyte water for hydration.",
        true,
        isRecommended = false,
        stockQty = 150,
        stockMax = 400,
        sku = "ZP-1096",
        soldThisWeek = 280,
        revenue7d = 8400.0
    ),
)