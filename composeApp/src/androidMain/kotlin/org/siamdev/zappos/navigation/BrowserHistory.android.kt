/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.navigation


actual object BrowserHistory {
    actual fun push(path: String) {}
    actual fun replace(path: String) {}
    actual fun onPopState(callback: (String) -> Unit) {}
    actual fun currentPath(): String = "/"
}