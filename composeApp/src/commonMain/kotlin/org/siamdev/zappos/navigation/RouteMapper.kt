package org.siamdev.zappos.navigation

object RouteMapper {
    fun toPath(route: Route): String = when (route) {
        is Route.Splash  -> "/"
        is Route.Login   -> "/login"
        is Route.NostrLogin -> "/nostr-login"
        is Route.Logout  -> "/"
        is Route.Home    -> "/home"
        is Route.Menu    -> "/menu"
        is Route.Checkout -> "/checkout"
        is Route.Counter -> "/counter"
        is Route.Setting -> "/setting"
        is Route.GlowEffects -> "/effects"
        is Route.TopBarStyle -> "/top-bar"
    }

    fun fromPath(path: String): Route = when (path) {
        "/login"   -> Route.Login
        "/nostr-login" -> Route.NostrLogin
        "/"        -> Route.Login
        "/home"    -> Route.Home
        "/menu"    -> Route.Menu
        "/checkout" -> Route.Checkout
        "/counter" -> Route.Counter
        "/setting" -> Route.Setting
        "/effects" -> Route.GlowEffects
        "/top-bar" -> Route.TopBarStyle
        else       -> Route.Login
    }
}