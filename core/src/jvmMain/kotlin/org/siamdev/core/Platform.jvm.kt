package org.siamdev.core

class JVMPlatform : Platform {
    private val runtimeVersion = System.getProperty("java.runtime.version")
    private val runtimeVendor = System.getProperty("java.vm.vendor")

    override val name: String = "Runtime $runtimeVersion by $runtimeVendor"
}

actual fun getPlatform(): Platform = JVMPlatform()
