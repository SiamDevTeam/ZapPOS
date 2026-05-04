package org.siamdev.zappos.ui.components

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

enum class ScreenType {
    Mobile,
    Tablet,
    Desktop
}

@Composable
fun DebugScreenSize() {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val w = maxWidth
        val h = maxHeight

        val screenType = when {
            w < 600.dp -> ScreenType.Mobile
            w < 900.dp -> ScreenType.Tablet
            else -> ScreenType.Desktop
        }

        LaunchedEffect(w, h) {
            println(
                "[Screen] Width=${w.value.toInt()} × Height=${h.value.toInt()} dp [$screenType]"
            )
        }
    }
}