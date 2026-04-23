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

import androidx.navigation3.runtime.NavKey
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.siamdev.zappos.ui.components.NavigationList
import androidx.compose.material3.MaterialTheme
import androidx.navigation3.runtime.NavBackStack

import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.ui.input.pointer.pointerInput

@Composable
fun NavConfig(
    backStack: NavBackStack<NavKey>,
    modifier: Modifier = Modifier,
    enableDrawer: Boolean = true,
    content: @Composable (NavActions, openDrawer: () -> Unit) -> Unit
) {
    val drawerWidth = 280.dp
    var drawerOpen by remember { mutableStateOf(false) }

    val offsetX by animateDpAsState(
        targetValue = if (drawerOpen) -drawerWidth else 0.dp,
        animationSpec = tween(300)
    )

    val navActions = remember(backStack) { NavActions(backStack) }

    var totalDragX by remember { mutableFloatStateOf(0f) }
    val swipeThreshold = 80f

    Box(modifier.fillMaxSize()) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .offset(x = offsetX)
                .background(MaterialTheme.colorScheme.background)
                .pointerInput(enableDrawer, drawerOpen) {
                    if (!enableDrawer) return@pointerInput

                    detectHorizontalDragGestures(
                        onDragStart = { totalDragX = 0f },
                        onHorizontalDrag = { _, dragAmount ->
                            totalDragX += dragAmount
                        },
                        onDragEnd = {
                            when {
                                !drawerOpen && totalDragX < -swipeThreshold -> drawerOpen = true
                                drawerOpen && totalDragX > swipeThreshold -> drawerOpen = false
                            }
                            totalDragX = 0f
                        },
                        onDragCancel = { totalDragX = 0f }
                    )
                }
        ) {
            content(navActions) { if (enableDrawer) drawerOpen = true }
        }

        if (enableDrawer) {
            NavigationList(
                isOpen = drawerOpen,
                onDismiss = { drawerOpen = false },
                onNavigateToHome = { navActions.to(Route.Home); drawerOpen = false },
                onNavigateToMenu = { navActions.to(Route.Menu); drawerOpen = false },
                onNavigateToCounter = { navActions.to(Route.Counter); drawerOpen = false },
                onNavigateToGlow = { navActions.to(Route.GlowEffects); drawerOpen = false },
                onNavigateToSetting = { navActions.to(Route.Setting); drawerOpen = false }
            )
        }
    }
}


class NavActions(
    internal val backStack: NavBackStack<NavKey>
) {

    fun currentRoute(): NavKey? =
        backStack.lastOrNull()

    fun to(route: NavKey) {
        if (currentRoute() != route) {
            backStack.add(route)
        }
    }

    fun back() {
        if (backStack.size > 1) {
            backStack.removeAt(backStack.lastIndex)
        }
    }

    fun switchTo(route: NavKey) {
        backStack.removeAt(backStack.lastIndex)
        backStack.add(route)
    }

    fun clearAndTo(route: NavKey) {
        backStack.clear()
        backStack.add(route)
    }

    fun logout() {
        clearAndTo(Route.Login)
    }
}
