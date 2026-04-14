package org.siamdev.zappos

class WasmPlatform: Platform {
    override val name: String = "wasm"
}

actual fun getPlatform(): Platform = WasmPlatform()