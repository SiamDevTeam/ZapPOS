/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import platform.UIKit.UIApplication
import platform.UIKit.UIDevice
import platform.UIKit.UIDeviceOrientation
import platform.UIKit.UIInterfaceOrientationMask
import platform.UIKit.UIInterfaceOrientationMaskLandscape
import platform.UIKit.UIInterfaceOrientationMaskPortrait
import platform.UIKit.UIInterfaceOrientationMaskAll
import platform.UIKit.UIViewController
import platform.UIKit.UIWindow
import platform.UIKit.setNeedsUpdateOfSupportedInterfaceOrientations

@Composable
actual fun LockOrientation(orientation: Orientation) {
    DisposableEffect(orientation) {
        val mask: UIInterfaceOrientationMask = when (orientation) {
            Orientation.PORTRAIT  -> UIInterfaceOrientationMaskPortrait
            Orientation.LANDSCAPE -> UIInterfaceOrientationMaskLandscape
            Orientation.FREE      -> UIInterfaceOrientationMaskAll
        }

        OrientationManager.setMask(mask)

        onDispose {
            OrientationManager.setMask(UIInterfaceOrientationMaskAll)
        }
    }
}

object OrientationManager {
    var currentMask: UIInterfaceOrientationMask = UIInterfaceOrientationMaskAll

    fun setMask(mask: UIInterfaceOrientationMask) {
        currentMask = mask
        UIApplication.sharedApplication.windows
            .filterIsInstance<UIWindow>()
            .firstOrNull()
            ?.rootViewController
            ?.setNeedsUpdateOfSupportedInterfaceOrientations()
    }
}