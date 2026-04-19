package org.siamdev.zappos.utils

import android.app.Activity
import android.content.pm.ActivityInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun LockOrientation(orientation: Orientation) {
    val context = LocalContext.current
    val activity = context as? Activity ?: return

    DisposableEffect(orientation) {
        val original = activity.requestedOrientation

        activity.requestedOrientation = when (orientation) {
            Orientation.PORTRAIT  -> ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            Orientation.LANDSCAPE -> ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            Orientation.FREE      -> ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }

        onDispose {
            activity.requestedOrientation = original
        }
    }
}