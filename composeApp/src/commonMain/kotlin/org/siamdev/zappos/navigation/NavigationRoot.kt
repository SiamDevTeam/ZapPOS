/*
 * MIT License
 *
 * Copyright (c) 2025 SiamDevTeam
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.siamdev.zappos.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.runtime.NavEntry
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.siamdev.zappos.ui.components.GlowEffects
import org.siamdev.zappos.ui.screens.demo.CounterScreen
import org.siamdev.zappos.ui.screens.demo.EffectScreen
import org.siamdev.zappos.ui.screens.login.LoginScreen
import org.siamdev.zappos.ui.screens.home.HomeScreen
import org.siamdev.zappos.ui.screens.menu.MainMenuScreen
import org.siamdev.zappos.ui.screens.splash.SplashScreen
import org.siamdev.zappos.ui.screens.splash.SplashViewModel


@Composable
fun NavigationRoot(
    startDestination: Route,
    splashViewModel: SplashViewModel
) {
    val backStack = rememberNavBackStack(
        configuration = SavedStateConfiguration {
            serializersModule = SerializersModule {
                polymorphic(NavKey::class) {
                    subclass(Route.Splash::class, Route.Splash.serializer())
                    subclass(Route.Login::class, Route.Login.serializer())
                    subclass(Route.Home::class, Route.Home.serializer())
                    subclass(Route.Menu::class, Route.Menu.serializer())
                    subclass(Route.Counter::class, Route.Counter.serializer())
                    subclass(Route.GlowEffects::class, Route.GlowEffects.serializer())
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
            NavEntry(key) {
                var visible by remember { mutableStateOf(false) }
                LaunchedEffect(Unit) { visible = true }

                val (enterAnim, exitAnim) = when (key) {
                    /*is Route.Splash -> {
                        fadeIn(animationSpec = tween(500)) + slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(500)) to
                                fadeOut(animationSpec = tween(500)) + slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(500))
                    }*/
                    is Route.Login -> {
                        /*fadeIn(animationSpec = tween(300)) + slideInHorizontally(initialOffsetX = { it / 2 }, animationSpec = tween(300)) to
                                fadeOut(animationSpec = tween(300)) + slideOutHorizontally(targetOffsetX = { -it / 2 }, animationSpec = tween(300))*/
                        fadeIn(animationSpec = tween(400)) to fadeOut(animationSpec = tween(400))
                    }
                    is Route.Menu -> {
                        fadeIn(animationSpec = tween(400)) to fadeOut(animationSpec = tween(400))
                    }
                    else -> {
                        fadeIn() to fadeOut()
                    }
                }

                AnimatedVisibility(
                    visible = visible,
                    enter = enterAnim,
                    exit = exitAnim
                ) {
                    when (key) {
                        is Route.Splash -> SplashScreen(viewModel = splashViewModel) {
                            if (splashViewModel.relayReady.value) {
                                backStack.add(Route.Login)
                            }
                        }
                        is Route.Login -> LoginScreen(
                            onLoginNostr = { backStack.add(Route.GlowEffects) },
                            onLoginAnonymous = { backStack.add(Route.Menu) }
                        )
                        is Route.Home -> HomeScreen()
                        is Route.Menu -> MainMenuScreen()
                        is Route.Counter -> CounterScreen()
                        is Route.GlowEffects -> EffectScreen()
                        else -> error("Unknown NavKey: $key")
                    }
                }
            }
        }
    )
}
