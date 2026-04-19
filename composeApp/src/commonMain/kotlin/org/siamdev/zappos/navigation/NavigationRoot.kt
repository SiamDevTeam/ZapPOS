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
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.runtime.NavEntry
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.siamdev.zappos.Platform
import org.siamdev.zappos.PlatformType
import org.siamdev.zappos.ui.screens.count.CounterScreen
import org.siamdev.zappos.ui.screens.demo.TopBarScreen
import org.siamdev.zappos.ui.screens.glow.GlowStyleScreen
import org.siamdev.zappos.ui.screens.login.LoginScreen
import org.siamdev.zappos.ui.screens.home.HomeScreen
import org.siamdev.zappos.ui.screens.menu.MainMenuScreen
import org.siamdev.zappos.ui.screens.setting.SettingScreen
import org.siamdev.zappos.ui.screens.splash.SplashScreen
import org.siamdev.zappos.ui.screens.splash.SplashViewModel

@Composable
fun NavigationRoot(
    platform: Platform,
    startDestination: Route,
    splashViewModel: SplashViewModel
) {
    val backStack: NavBackStack<NavKey> = rememberNavBackStack(
        configuration = SavedStateConfiguration {
            serializersModule = SerializersModule {
                polymorphic(NavKey::class) {
                    subclass(Route.Splash::class, Route.Splash.serializer())
                    subclass(Route.Login::class, Route.Login.serializer())
                    subclass(Route.Home::class, Route.Home.serializer())
                    subclass(Route.Menu::class, Route.Menu.serializer())
                    subclass(Route.Counter::class, Route.Counter.serializer())
                    subclass(Route.GlowEffects::class, Route.GlowEffects.serializer())
                    subclass(Route.TopBarStyle::class, Route.TopBarStyle.serializer())
                    subclass(Route.Setting::class, Route.Setting.serializer())
                }
            }
        },
        startDestination
    )

    if (platform.type == PlatformType.WEB) {
        LaunchedEffect(backStack.toList()) {
            val current = backStack.lastOrNull() as? Route ?: return@LaunchedEffect
            val path = RouteMapper.toPath(current)
            if (path != BrowserHistory.currentPath()) {
                BrowserHistory.push(path)
            }
        }
        LaunchedEffect(Unit) {
            BrowserHistory.onPopState { path ->
                val route = RouteMapper.fromPath(path)
                val current = backStack.lastOrNull()
                if (current != route) {
                    val existingIndex = backStack.indexOfLast { it == route }
                    if (existingIndex >= 0) {
                        while (backStack.lastIndex > existingIndex) {
                            backStack.removeAt(backStack.lastIndex)
                        }
                    } else {
                        backStack.add(route)
                    }
                }
            }
        }
    }

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
                    else -> fadeIn() to fadeOut()
                }

                AnimatedVisibility(
                    visible = visible,
                    enter = enterAnim,
                    exit = exitAnim
                ) {
                    when (key) {

                        // Splash
                        is Route.Splash -> SplashScreen(
                            viewModel = splashViewModel
                        ) {
                            if (splashViewModel.isReady.value) {
                                backStack.add(Route.Login)
                            }
                        }

                        // Login
                        is Route.Login -> LoginScreen(
                            onLoginNostr = { backStack.add(Route.TopBarStyle) },
                            onLoginAnonymous = { backStack.add(Route.Home) }
                        )

                        // Home
                        is Route.Home -> NavConfig(
                            backStack = backStack
                        ) { _, openDrawer ->
                            HomeScreen(
                                onOpenDrawer = openDrawer
                            )
                        }

                        // Menu
                        is Route.Menu -> NavConfig(
                            backStack = backStack
                        ) { _, openDrawer ->
                            MainMenuScreen(
                                onOpenDrawer = openDrawer
                            )
                        }

                        // Counter
                        is Route.Counter -> NavConfig(
                            backStack = backStack
                        ) { _, openDrawer ->
                            CounterScreen(
                                onOpenDrawer = openDrawer
                            )
                        }

                        is Route.GlowEffects -> NavConfig(
                            backStack = backStack
                        ) { _, openDrawer ->
                            GlowStyleScreen(
                                onOpenDrawer = openDrawer
                            )
                        }

                        // Setting
                        is Route.Setting -> NavConfig(
                            backStack = backStack
                        ) { navActions, _ ->
                            SettingScreen(
                                onNavigateBack = {
                                    navActions.back()
                                }
                            )
                        }



                        is Route.TopBarStyle -> TopBarScreen()

                        else -> error("Unknown NavKey: $key")
                    }

                }
            }
        }
    )
}