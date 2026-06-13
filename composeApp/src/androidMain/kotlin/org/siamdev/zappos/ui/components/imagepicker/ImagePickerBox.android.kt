/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.components.imagepicker

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File

actual val platformHasCamera: Boolean = true

@Composable
actual fun rememberCameraLauncher(onResult: (ByteArray?) -> Unit): () -> Unit {
    val context = LocalContext.current
    val latestOnResult = rememberUpdatedState(onResult)
    val capturedFile = remember { mutableStateOf<File?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        val file = capturedFile.value
        val bytes = if (success && file != null && file.exists()) file.readBytes() else null
        latestOnResult.value(bytes)
        file?.delete()
        capturedFile.value = null
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            val file = File(context.cacheDir, "zappos_cam_${System.currentTimeMillis()}.jpg")
            capturedFile.value = file
            cameraLauncher.launch(
                FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
            )
        }
    }

    return remember(context) {
        {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED
            ) {
                val file = File(context.cacheDir, "zappos_cam_${System.currentTimeMillis()}.jpg")
                capturedFile.value = file
                cameraLauncher.launch(
                    FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
                )
            } else {
                permissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }
}

@Composable
actual fun Modifier.withImageDrop(onImageDropped: (ByteArray) -> Unit): Modifier = this