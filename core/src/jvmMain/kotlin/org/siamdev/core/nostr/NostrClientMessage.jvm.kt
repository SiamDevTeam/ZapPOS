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

import rust.nostr.sdk.ClientMessage as NativeClientMessage
import rust.nostr.sdk.ClientMessageEnum

actual class NostrClientMessage internal constructor(
    internal val native: NativeClientMessage
) {

    actual fun asEnum(): NostrClientMessageEnum =
        when (val enumNative = native.asEnum()) {
            is ClientMessageEnum.EventMsg -> NostrClientMessageEnum.EventMsg(
                NostrEvent(enumNative.event)
            )
            is ClientMessageEnum.Req -> NostrClientMessageEnum.Req(
                enumNative.subscriptionId,
                NostrFilter(enumNative.filter)
            )
            is ClientMessageEnum.ReqMultiFilter -> NostrClientMessageEnum.ReqMultiFilter(
                enumNative.subscriptionId,
                enumNative.filters.map { NostrFilter(it) }
            )
            is ClientMessageEnum.Count -> NostrClientMessageEnum.Count(
                enumNative.subscriptionId,
                NostrFilter(enumNative.filter)
            )
            is ClientMessageEnum.Close -> NostrClientMessageEnum.Close(
                enumNative.subscriptionId
            )
            is ClientMessageEnum.Auth -> NostrClientMessageEnum.Auth(
                NostrEvent(enumNative.event)
            )
            is ClientMessageEnum.NegOpen -> NostrClientMessageEnum.NegOpen(
                enumNative.subscriptionId,
                NostrFilter(enumNative.filter),
                enumNative.idSize,
                enumNative.initialMessage
            )
            is ClientMessageEnum.NegMsg -> NostrClientMessageEnum.NegMsg(
                enumNative.subscriptionId,
                enumNative.message
            )
            is ClientMessageEnum.NegClose -> NostrClientMessageEnum.NegClose(
                enumNative.subscriptionId
            )
            else -> throw IllegalArgumentException("Unknown ClientMessageEnum: $enumNative")
        }

    actual fun asJson(): String = native.asJson()

    actual fun unwrap(): Any = native

}