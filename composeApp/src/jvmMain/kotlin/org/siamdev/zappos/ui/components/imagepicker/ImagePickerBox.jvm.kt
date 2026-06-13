/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.components.imagepicker

import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draganddrop.DragData
import androidx.compose.ui.draganddrop.dragData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URI

actual val platformHasCamera: Boolean = false

@Composable
actual fun rememberCameraLauncher(onResult: (ByteArray?) -> Unit): () -> Unit = {}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
actual fun Modifier.withImageDrop(onImageDropped: (ByteArray) -> Unit): Modifier {
    val scope = rememberCoroutineScope()
    val currentCallback = rememberUpdatedState(onImageDropped)

    val target = remember(scope) {
        object : DragAndDropTarget {
            override fun onDrop(event: DragAndDropEvent): Boolean {
                val files = (event.dragData() as? DragData.FilesList)?.readFiles()
                    ?: return false
                val imageExts = setOf("jpg", "jpeg", "png", "webp", "bmp", "gif")
                val uri = files.firstOrNull {
                    it.substringAfterLast('.', "").lowercase() in imageExts
                } ?: return false
                scope.launch(Dispatchers.IO) {
                    runCatching { java.io.File(URI(uri)).readBytes() }
                        .getOrNull()
                        ?.let { currentCallback.value(it) }
                }
                return true
            }
        }
    }

    val shouldStart = remember {
        { event: DragAndDropEvent -> event.dragData() is DragData.FilesList }
    }

    return dragAndDropTarget(shouldStartDragAndDrop = shouldStart, target = target)
}