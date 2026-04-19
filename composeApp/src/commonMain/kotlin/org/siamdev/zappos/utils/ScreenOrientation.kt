package org.siamdev.zappos.utils

import androidx.compose.runtime.Composable

enum class Orientation { PORTRAIT, LANDSCAPE, FREE }

@Composable
expect fun LockOrientation(orientation: Orientation)