/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
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
import org.siamdev.zappos.ui.screens.checkout.CheckoutScreen
import org.siamdev.zappos.ui.screens.count.CounterScreen
import org.siamdev.zappos.ui.screens.demo.TopBarScreen
import org.siamdev.zappos.ui.screens.glow.GlowStyleScreen
import org.siamdev.zappos.ui.screens.login.LoginScreen
import org.siamdev.zappos.ui.screens.home.HomeScreen
import org.siamdev.zappos.ui.screens.login.NostrLoginScreen
import org.siamdev.zappos.ui.screens.sale.MainMenuScreen
import org.siamdev.zappos.ui.screens.product.ProductEntryMasterScreen
import org.siamdev.zappos.ui.screens.product.ProductListDetailScreen
import org.siamdev.zappos.ui.screens.setting.appearance.AppearanceSettingScreen
import org.siamdev.zappos.ui.screens.setting.currency.CurrencySettingScreen
import org.siamdev.zappos.ui.screens.setting.SettingScreen
import org.siamdev.zappos.ui.screens.setting.SettingInfo
import org.siamdev.zappos.ui.screens.splash.SplashScreen
import org.siamdev.zappos.ui.screens.splash.SplashViewModel
import org.siamdev.zappos.LocalSettingVM

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
                    subclass(Route.NostrLogin::class, Route.NostrLogin.serializer())
                    subclass(Route.Home::class, Route.Home.serializer())
                    subclass(Route.Menu::class, Route.Menu.serializer())
                    subclass(Route.Checkout::class, Route.Checkout.serializer())
                    subclass(Route.Counter::class, Route.Counter.serializer())
                    subclass(Route.GlowEffects::class, Route.GlowEffects.serializer())
                    subclass(Route.TopBarStyle::class, Route.TopBarStyle.serializer())
                    subclass(Route.Setting::class, Route.Setting.serializer())
                    subclass(Route.AppearanceSetting::class, Route.AppearanceSetting.serializer())
                    subclass(Route.CurrencySetting::class, Route.CurrencySetting.serializer())
                    subclass(Route.ProductEntryMaster::class, Route.ProductEntryMaster.serializer())
                    subclass(Route.ProductList::class, Route.ProductList.serializer())
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
                        is Route.Login -> {
                            LoginScreen(
                                onLoginNostr = { backStack.add(Route.NostrLogin) },
                                onLoginAnonymous = { backStack.add(Route.Home) }
                            )
                        }

                        // Nostr Login
                        is Route.NostrLogin -> {
                            NostrLoginScreen(
                                onBack = { backStack.removeAt(backStack.lastIndex) },
                                onLoginSuccess = { backStack.add(Route.Home) }
                            )
                        }

                        // Logout
                        is Route.Logout -> {
                            LaunchedEffect(Unit) {
                                backStack.clear()
                                backStack.add(Route.Login)
                            }
                        }

                        // Home
                        is Route.Home -> NavConfig(
                            backStack = backStack
                        ) { navActions, openDrawer ->
                            HomeScreen(
                                onOpenDrawer = openDrawer,
                                onNavigateToMenu = { navActions.to(Route.Menu) }
                            )
                        }

                        // Menu
                        is Route.Menu -> NavConfig(
                            backStack = backStack
                        ) { navActions, openDrawer ->
                            MainMenuScreen(
                                onOpenDrawer = openDrawer,
                                onCheckout = { navActions.to(Route.Checkout) }
                            )
                        }

                        // Product Entry Master
                        is Route.ProductList -> NavConfig(
                            backStack = backStack,
                            enableDrawer = true
                        ) { navActions, openDrawer ->
                            ProductListDetailScreen(
                                onOpenDrawer = openDrawer,
                                onEditProduct = { navActions.to(Route.ProductEntryMaster) }
                            )
                        }

                        is Route.ProductEntryMaster -> NavConfig(
                            backStack = backStack,
                            enableDrawer = true
                        ) { navActions, openDrawer ->
                            val prevRoute = backStack.toList().dropLast(1).lastOrNull()
                            ProductEntryMasterScreen(
                                onNavigateBack = { navActions.back() },
                                onOpenDrawer = openDrawer,
                                onSave = { navActions.back() },
                                showBackButton = prevRoute is Route.ProductList
                            )
                        }

                        // Checkout
                        is Route.Checkout -> NavConfig(
                            backStack = backStack
                        ) { navActions, _ ->
                            CheckoutScreen(
                                onBack = { navActions.back() },
                                onSuccess = {
                                    navActions.back()
                                }
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
                                onNavigateBack = { navActions.back() },
                                onNavigateTo = { info ->
                                    when (info) {
                                        SettingInfo.APPEARANCE -> navActions.to(Route.AppearanceSetting)
                                        SettingInfo.CURRENCY -> navActions.to(Route.CurrencySetting)
                                        else -> { /* TODO */ }
                                    }
                                },
                                onLogout = { navActions.logout() }
                            )
                        }

                        // Appearance Setting (theme + font)
                        is Route.AppearanceSetting -> NavConfig(
                            backStack = backStack,
                            enableDrawer = false
                        ) { navActions, _ ->
                            val vm = LocalSettingVM.current
                            AppearanceSettingScreen(
                                onNavigateBack = { navActions.back() },
                                viewModel = vm
                            )
                        }

                        // Currency Setting
                        is Route.CurrencySetting -> NavConfig(
                            backStack = backStack,
                            enableDrawer = false
                        ) { navActions, _ ->
                            val vm = LocalSettingVM.current
                            CurrencySettingScreen(
                                onNavigateBack = { navActions.back() },
                                viewModel = vm
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