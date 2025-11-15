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

expect sealed class NostrClientMessageEnum {

    class EventMsg : NostrClientMessageEnum {
        constructor(event: NostrEvent)
        val event: NostrEvent
    }

    class Req : NostrClientMessageEnum {
        constructor(subscriptionId: String, filter: NostrFilter)
        val subscriptionId: String
        val filter: NostrFilter
    }

    class ReqMultiFilter : NostrClientMessageEnum {
        constructor(subscriptionId: String, filters: List<NostrFilter>)
        val subscriptionId: String
        val filters: List<NostrFilter>
    }

    class Count : NostrClientMessageEnum {
        constructor(subscriptionId: String, filter: NostrFilter)
        val subscriptionId: String
        val filter: NostrFilter
    }

    class Close : NostrClientMessageEnum {
        constructor(subscriptionId: String)
        val subscriptionId: String
    }

    class Auth : NostrClientMessageEnum {
        constructor(event: NostrEvent)
        val event: NostrEvent
    }

    class NegOpen : NostrClientMessageEnum {
        constructor(subscriptionId: String, filter: NostrFilter, idSize: UByte?, initialMessage: String)
        val subscriptionId: String
        val filter: NostrFilter
        val idSize: UByte?
        val initialMessage: String
    }

    class NegMsg : NostrClientMessageEnum {
        constructor(subscriptionId: String, message: String)
        val subscriptionId: String
        val message: String
    }

    class NegClose : NostrClientMessageEnum {
        constructor(subscriptionId: String)
        val subscriptionId: String
    }
}