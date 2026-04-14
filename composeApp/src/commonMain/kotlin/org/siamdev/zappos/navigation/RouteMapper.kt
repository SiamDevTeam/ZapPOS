package org.siamdev.zappos.navigation

object RouteMapper {
    fun toPath(route: Route): String = when (route) {
        is Route.Splash  -> "/"
        is Route.Login   -> "/login"
        is Route.Home    -> "/home"
        is Route.Menu    -> "/menu"
        is Route.Counter -> "/counter"
        is Route.Setting -> "/setting"
        is Route.GlowEffects -> "/effects"
    }

    fun fromPath(path: String): Route = when (path) {
        "/login"   -> Route.Login
        "/home"    -> Route.Home
        "/menu"    -> Route.Menu
        "/counter" -> Route.Counter
        "/setting" -> Route.Setting
        "/effects" -> Route.GlowEffects
        else       -> Route.Login
    }
}