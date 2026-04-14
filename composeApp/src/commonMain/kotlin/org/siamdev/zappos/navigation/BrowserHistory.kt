package org.siamdev.zappos.navigation

expect object BrowserHistory {
    fun push(path: String)
    fun replace(path: String)
    fun onPopState(callback: (String) -> Unit)
    fun currentPath(): String
}