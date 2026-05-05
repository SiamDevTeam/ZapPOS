/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed class Route : NavKey {

    @Serializable
    object Splash : Route()

    @Serializable
    object Login : Route()

    @Serializable
    object NostrLogin : Route()

    @Serializable
    object Logout : Route()

    @Serializable
    object Home : Route()

    @Serializable
    object Menu : Route()

    @Serializable
    object Checkout : Route()

    @Serializable
    object Counter : Route()

    @Serializable
    object GlowEffects : Route()

    @Serializable
    object TopBarStyle : Route()

    @Serializable
    object Setting : Route()

    @Serializable
    object AppearanceSetting : Route()

    @Serializable
    object CurrencySetting : Route()

    @Serializable
    object ProductEntryMaster : Route()

    @Serializable
    object ProductList : Route()
}