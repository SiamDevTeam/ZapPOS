/*
 * MIT License
 *
 * Copyright (c) 2025 SiamDev by SiamDharmar
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.siamdev.core.nostr

import rust.nostr.sdk.JsonValue as NativeJsonValue

actual sealed class NostrJsonValue {

    actual class Bool actual constructor(bool: Boolean) : NostrJsonValue()
    actual class NumberPosInt actual constructor(number: ULong) : NostrJsonValue()
    actual class NumberNegInt actual constructor(number: Long) : NostrJsonValue()
    actual class NumberFloat actual constructor(number: Double) : NostrJsonValue()
    actual class Str actual constructor(s: String) : NostrJsonValue()
    actual class Array actual constructor(array: List<NostrJsonValue>) : NostrJsonValue()
    actual class Object actual constructor(map: Map<String, NostrJsonValue>) : NostrJsonValue()
    actual object Null : NostrJsonValue()

    actual companion object {
        internal actual fun of(native: Any): NostrJsonValue {
            return when (native) {
                is NativeJsonValue.Bool -> Bool(native.bool)
                is NativeJsonValue.NumberPosInt -> NumberPosInt(native.number)
                is NativeJsonValue.NumberNegInt -> NumberNegInt(native.number)
                is NativeJsonValue.NumberFloat -> NumberFloat(native.number)
                is NativeJsonValue.Str -> Str(native.s)
                is NativeJsonValue.Array -> Array(native.array.map { of(it) })
                is NativeJsonValue.Object -> Object(native.map.mapValues { (_, v) -> of(v) })
                is NativeJsonValue.Null -> Null
                else -> throw IllegalArgumentException("Unknown JsonValue type: ${native::class}")
            }
        }
    }
}