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

expect sealed class NostrTagKind {
    object Alt : NostrTagKind
    object Client : NostrTagKind
    object Commit : NostrTagKind
    object Dependency : NostrTagKind
    object Extension : NostrTagKind
    object License : NostrTagKind
    object Maintainers : NostrTagKind
    object Protected : NostrTagKind
    object RelayUrl : NostrTagKind
    object Nonce : NostrTagKind
    object ContentWarning : NostrTagKind
    object Expiration : NostrTagKind
    object Subject : NostrTagKind
    object Challenge : NostrTagKind
    object Title : NostrTagKind
    object Image : NostrTagKind
    object Thumb : NostrTagKind
    object Summary : NostrTagKind
    object PublishedAt : NostrTagKind
    object Description : NostrTagKind
    object Bolt11 : NostrTagKind
    object Preimage : NostrTagKind
    object Relays : NostrTagKind
    object Amount : NostrTagKind
    object Lnurl : NostrTagKind
    object MlsProtocolVersion : NostrTagKind
    object MlsCiphersuite : NostrTagKind
    object MlsExtensions : NostrTagKind
    object Name : NostrTagKind
    object Option : NostrTagKind
    object Url : NostrTagKind
    object Aes256Gcm : NostrTagKind
    object Size : NostrTagKind
    object Dim : NostrTagKind
    object File : NostrTagKind
    object Magnet : NostrTagKind
    object Blurhash : NostrTagKind
    object Streaming : NostrTagKind
    object Recording : NostrTagKind
    object Server : NostrTagKind
    object Starts : NostrTagKind
    object Ends : NostrTagKind
    object Status : NostrTagKind
    object CurrentParticipants : NostrTagKind
    object TotalParticipants : NostrTagKind
    object Tracker : NostrTagKind
    object Method : NostrTagKind
    object Payload : NostrTagKind
    object Anon : NostrTagKind
    object Proxy : NostrTagKind
    object Emoji : NostrTagKind
    object Encrypted : NostrTagKind
    object Repository : NostrTagKind
    object Request : NostrTagKind
    object Runtime : NostrTagKind
    object PollType : NostrTagKind
    object Response : NostrTagKind
    object Web : NostrTagKind
    object Word : NostrTagKind

    class SingleLetter(singleLetter: NostrSingleLetterTag) : NostrTagKind {
        val singleLetter: NostrSingleLetterTag
    }

    class Unknown(name: String) : NostrTagKind {
        val name: String
    }

    companion object {
        internal fun of(native: Any): NostrTagKind
    }

}
