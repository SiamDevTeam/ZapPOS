/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.data

import kotlinx.serialization.json.Json
import org.siamdev.zappos.ui.screens.sale.MenuItem

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

fun loadMenuItems(): List<MenuItem> =
    menuJson.decodeFromString<List<MenuItem>>(MOCK_MENU_JSON)