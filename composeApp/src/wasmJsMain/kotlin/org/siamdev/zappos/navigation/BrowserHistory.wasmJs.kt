/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.navigation

import kotlinx.browser.window

@OptIn(ExperimentalWasmJsInterop::class)
actual object BrowserHistory {
    actual fun push(path: String) = window.history.pushState(null, "", path)

    actual fun replace(path: String) = window.history.replaceState(null, "", path)
    actual fun onPopState(callback: (String) -> Unit) {
        window.onpopstate = {
            callback(window.location.pathname)
        }
    }
    actual fun currentPath(): String = window.location.pathname
}