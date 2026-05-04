package org.siamdev.zappos.ui.components

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DebugScreenSize() {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val w = maxWidth
        val h = maxHeight
        LaunchedEffect(w, h) {
            val layout = if (w >= 600.dp) "Desktop" else "Mobile"
            println("[Screen] ${w.value.toInt()} × ${h.value.toInt()} dp  [$layout]")
        }
    }
}