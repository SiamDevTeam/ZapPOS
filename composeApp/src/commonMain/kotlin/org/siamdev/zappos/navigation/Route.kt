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
    object Home : Route, NavKey

    @Serializable
    object Counter : Route, NavKey


}