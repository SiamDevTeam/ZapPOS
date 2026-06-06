/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.navigation

object RouteMapper {
    fun toPath(route: Route): String = when (route) {
        is Route.Splash  -> "/"
        is Route.Login   -> "/login"
        is Route.NostrLogin -> "/nostr-login"
        is Route.Logout  -> "/"
        is Route.Home    -> "/home"
        is Route.Menu    -> "/menu"
        is Route.ConfirmOrder -> "/confirm-order"
        is Route.Checkout -> "/checkout"
        is Route.Counter -> "/counter"
        is Route.Setting -> "/setting"
        is Route.GlowEffects -> "/effects"
        is Route.TopBarStyle -> "/top-bar"
        is Route.AppearanceSetting -> "/setting/appearance"
        is Route.CurrencySetting -> "/setting/currency"
        is Route.ProductEntryMaster -> if (route.productId != null) "/product/edit/${route.productId}" else "/product/new"
        is Route.ProductList -> "/product"
    }

    fun fromPath(path: String): Route = when {
        path == "/login"   -> Route.Login
        path == "/nostr-login" -> Route.NostrLogin
        path == "/"        -> Route.Login
        path == "/home"    -> Route.Home
        path == "/menu"    -> Route.Menu
        path == "/confirm-order" -> Route.ConfirmOrder
        path == "/checkout" -> Route.Checkout
        path == "/counter" -> Route.Counter
        path == "/setting" -> Route.Setting
        path == "/setting/appearance" -> Route.AppearanceSetting
        path == "/setting/currency" -> Route.CurrencySetting
        path == "/product/new" -> Route.ProductEntryMaster()
        path.startsWith("/product/edit/") -> Route.ProductEntryMaster(path.removePrefix("/product/edit/"))
        path == "/product" -> Route.ProductList
        path == "/effects" -> Route.GlowEffects
        path == "/top-bar" -> Route.TopBarStyle
        else -> Route.Login
    }
}