/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.module

import platform.Foundation.*
import platform.UIKit.UIDevice
import kotlinx.cinterop.ExperimentalForeignApi

@OptIn(ExperimentalForeignApi::class)
class IOSPlatform : Platform {

    override val name: String =
        UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion


    override val dataDir: String
        get() {
            val paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, true)
            val documentsDir = paths.first() as NSString
            val lmdbDir = documentsDir.stringByAppendingPathComponent("lmdb")
            val fileManager = NSFileManager.defaultManager
            if (!fileManager.fileExistsAtPath(lmdbDir)) {
                fileManager.createDirectoryAtPath(
                    path = lmdbDir,
                    withIntermediateDirectories = true,
                    attributes = null,
                    error = null
                )
            }
            return lmdbDir
        }
}

actual fun getPlatform(): Platform = IOSPlatform()
