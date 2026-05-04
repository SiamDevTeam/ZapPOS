/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.module

import java.io.File

class JVMPlatform : Platform {

    private val runtimeVersion = System.getProperty("java.runtime.version")
    private val runtimeVendor = System.getProperty("java.vm.vendor")

    override val name: String = "Runtime $runtimeVersion by $runtimeVendor"

    override val dataDir: String
        get() {
            val dir = File(System.getProperty("user.home"), ".zappos/lmdb")
            if (!dir.exists()) dir.mkdirs()
            return dir.absolutePath
        }
}

actual fun getPlatform(): Platform = JVMPlatform()
