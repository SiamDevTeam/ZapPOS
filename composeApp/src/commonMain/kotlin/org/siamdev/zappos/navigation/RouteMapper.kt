package org.siamdev.zappos.navigation

object RouteMapper {
    fun toPath(route: Route): String = when (route) {
        is Route.Splash  -> "/"
        is Route.Login   -> "/login"
        is Route.NostrLogin -> "/nostr-login"
        is Route.Home    -> "/home"
        is Route.Menu    -> "/menu"
        is Route.Counter -> "/counter"
        is Route.Setting -> "/setting"
        is Route.GlowEffects -> "/effects"
        is Route.TopBarStyle -> "/top-bar"
    }

    fun fromPath(path: String): Route = when (path) {
        "/login"   -> Route.Login
        "/nostr-login" -> Route.NostrLogin
        "/home"    -> Route.Home
        "/menu"    -> Route.Menu
        "/counter" -> Route.Counter
        "/setting" -> Route.Setting
        "/effects" -> Route.GlowEffects
        "/top-bar" -> Route.TopBarStyle
        else       -> Route.Login
    }
}