/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.components.imagepicker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.Foundation.NSObject
import platform.UIKit.*
import platform.posix.memcpy

actual val platformHasCamera: Boolean =
    UIImagePickerController.isSourceTypeAvailable(UIImagePickerControllerSourceTypeCamera)

// Holds the delegate alive while the camera session is active (UIKit holds it weakly).
private object CameraSessionHolder {
    var delegate: NSObject? = null
}

private class CameraPickerDelegate(
    private val callback: (ByteArray?) -> Unit,
) : NSObject(), UIImagePickerControllerDelegateProtocol, UINavigationControllerDelegateProtocol {

    override fun imagePickerController(
        picker: UIImagePickerController,
        didFinishPickingMediaWithInfo: Map<Any?, *>,
    ) {
        @Suppress("UNCHECKED_CAST")
        val image = didFinishPickingMediaWithInfo[UIImagePickerControllerOriginalImage] as? UIImage
        callback(image?.toJpegBytes())
        CameraSessionHolder.delegate = null
        picker.dismissViewControllerAnimated(true, completion = null)
    }

    override fun imagePickerControllerDidCancel(picker: UIImagePickerController) {
        callback(null)
        CameraSessionHolder.delegate = null
        picker.dismissViewControllerAnimated(true, completion = null)
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun UIImage.toJpegBytes(): ByteArray? {
    val data = UIImageJPEGRepresentation(this, 0.85) ?: return null
    return ByteArray(data.length.toInt()).also { array ->
        array.usePinned { pinned ->
            memcpy(pinned.addressOf(0), data.bytes, data.length)
        }
    }
}

@Composable
actual fun rememberCameraLauncher(onResult: (ByteArray?) -> Unit): () -> Unit {
    val latestOnResult = rememberUpdatedState(onResult)

    return remember {
        {
            val delegate = CameraPickerDelegate { bytes -> latestOnResult.value(bytes) }
            CameraSessionHolder.delegate = delegate

            val picker = UIImagePickerController()
            picker.sourceType = UIImagePickerControllerSourceTypeCamera
            picker.allowsEditing = false
            picker.delegate = delegate

            val rootVC = UIApplication.sharedApplication.windows
                .filterIsInstance<UIWindow>()
                .firstOrNull()
                ?.rootViewController

            rootVC?.presentViewController(picker, animated = true, completion = null)
        }
    }
}

@Composable
actual fun Modifier.withImageDrop(onImageDropped: (ByteArray) -> Unit): Modifier = this