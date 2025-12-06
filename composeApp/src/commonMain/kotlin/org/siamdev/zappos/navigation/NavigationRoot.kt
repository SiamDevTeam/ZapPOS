package org.siamdev.zappos.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.runtime.NavEntry
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.siamdev.zappos.ui.screens.demo.CounterScreen
import org.siamdev.zappos.ui.screens.login.LoginScreen
import org.siamdev.zappos.ui.screens.splash.SplashScreen

@Composable
fun NavigationRoot(
    startDestination: Route
) {
    val backStack = rememberNavBackStack(
        configuration = SavedStateConfiguration {
            serializersModule = SerializersModule {
                polymorphic(NavKey::class) {
                    subclass(Route.Splash::class, Route.Splash.serializer())
                    subclass(Route.Login::class, Route.Login.serializer())
                    subclass(Route.Home::class, Route.Home.serializer())
                    subclass(Route.Counter::class, Route.Counter.serializer())
                }
            }
        },
        startDestination
    )

    NavDisplay(
        backStack = backStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = { key ->
            when (key) {
                is Route.Splash -> {
                    NavEntry(key) {
                        // SplashScreen receives a callback to navigate to Login
                        SplashScreen(
                            onSplashFinished = { backStack.add(Route.Login) }
                        )
                    }
                }
                is Route.Login -> {
                    NavEntry(key) {
                        LoginScreen(
                            onLoginAnonymous = { backStack.add(Route.Counter) }
                        )
                    }
                }
                is Route.Counter -> NavEntry(key) { CounterScreen() }
                is Route.Home -> {
                    NavEntry(key) {
                        // HomeScreen()
                        CounterScreen()
                    }
                }
                else -> error("Unknown NavKey: $key")
            }
        }
    )
}
