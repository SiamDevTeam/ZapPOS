/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.ui.components.imagepicker

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

actual val platformHasCamera: Boolean = false

@Composable
actual fun rememberCameraLauncher(onResult: (ByteArray?) -> Unit): () -> Unit = {}

@Composable
actual fun Modifier.withImageDrop(onImageDropped: (ByteArray) -> Unit): Modifier = this