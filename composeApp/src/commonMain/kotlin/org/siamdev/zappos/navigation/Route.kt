/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route : NavKey {

    @Serializable
    object Splash : Route, NavKey

    @Serializable
    object Login : Route, NavKey

    @Serializable
    object NostrLogin : Route, NavKey

    @Serializable
    object Logout : Route, NavKey

    @Serializable
    object Home : Route, NavKey

    @Serializable
    object Menu : Route, NavKey

    @Serializable
    object Checkout : Route, NavKey

    @Serializable
    object Counter : Route, NavKey

    @Serializable
    object GlowEffects : Route, NavKey

    @Serializable
    object TopBarStyle : Route, NavKey

    @Serializable
    object Setting : Route, NavKey

    @Serializable
    object AppearanceSetting : Route, NavKey

    @Serializable
    object CurrencySetting : Route, NavKey
}