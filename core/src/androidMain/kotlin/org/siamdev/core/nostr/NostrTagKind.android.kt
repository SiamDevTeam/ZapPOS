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

import rust.nostr.sdk.TagKind as NativeTagKind

actual sealed class NostrTagKind {

    actual object Alt : NostrTagKind()
    actual object Client : NostrTagKind()
    actual object Commit : NostrTagKind()
    actual object Dependency : NostrTagKind()
    actual object Extension : NostrTagKind()
    actual object License : NostrTagKind()
    actual object Maintainers : NostrTagKind()
    actual object Protected : NostrTagKind()
    actual object RelayUrl : NostrTagKind()
    actual object Nonce : NostrTagKind()
    actual object ContentWarning : NostrTagKind()
    actual object Expiration : NostrTagKind()
    actual object Subject : NostrTagKind()
    actual object Challenge : NostrTagKind()
    actual object Title : NostrTagKind()
    actual object Image : NostrTagKind()
    actual object Thumb : NostrTagKind()
    actual object Summary : NostrTagKind()
    actual object PublishedAt : NostrTagKind()
    actual object Description : NostrTagKind()
    actual object Bolt11 : NostrTagKind()
    actual object Preimage : NostrTagKind()
    actual object Relays : NostrTagKind()
    actual object Amount : NostrTagKind()
    actual object Lnurl : NostrTagKind()
    actual object MlsProtocolVersion : NostrTagKind()
    actual object MlsCiphersuite : NostrTagKind()
    actual object MlsExtensions : NostrTagKind()
    actual object Name : NostrTagKind()
    actual object Option : NostrTagKind()
    actual object Url : NostrTagKind()
    actual object Aes256Gcm : NostrTagKind()
    actual object Size : NostrTagKind()
    actual object Dim : NostrTagKind()
    actual object File : NostrTagKind()
    actual object Magnet : NostrTagKind()
    actual object Blurhash : NostrTagKind()
    actual object Streaming : NostrTagKind()
    actual object Recording : NostrTagKind()
    actual object Server : NostrTagKind()
    actual object Starts : NostrTagKind()
    actual object Ends : NostrTagKind()
    actual object Status : NostrTagKind()
    actual object CurrentParticipants : NostrTagKind()
    actual object TotalParticipants : NostrTagKind()
    actual object Tracker : NostrTagKind()
    actual object Method : NostrTagKind()
    actual object Payload : NostrTagKind()
    actual object Anon : NostrTagKind()
    actual object Proxy : NostrTagKind()
    actual object Emoji : NostrTagKind()
    actual object Encrypted : NostrTagKind()
    actual object Repository : NostrTagKind()
    actual object Request : NostrTagKind()
    actual object Runtime : NostrTagKind()
    actual object PollType : NostrTagKind()
    actual object Response : NostrTagKind()
    actual object Web : NostrTagKind()
    actual object Word : NostrTagKind()

    actual class SingleLetter actual constructor(
        actual val singleLetter: NostrSingleLetterTag
    ) : NostrTagKind()

    actual class Unknown actual constructor(
        actual val name: String
    ) : NostrTagKind()

    actual companion object {
        actual fun of(native: Any): NostrTagKind =
            when (native) {
                NativeTagKind.Alt -> Alt
                NativeTagKind.Client -> Client
                NativeTagKind.Commit -> Commit
                NativeTagKind.Dependency -> Dependency
                NativeTagKind.Extension -> Extension
                NativeTagKind.License -> License
                NativeTagKind.Maintainers -> Maintainers
                NativeTagKind.Protected -> Protected
                NativeTagKind.RelayUrl -> RelayUrl
                NativeTagKind.Nonce -> Nonce
                NativeTagKind.ContentWarning -> ContentWarning
                NativeTagKind.Expiration -> Expiration
                NativeTagKind.Subject -> Subject
                NativeTagKind.Challenge -> Challenge
                NativeTagKind.Title -> Title
                NativeTagKind.Image -> Image
                NativeTagKind.Thumb -> Thumb
                NativeTagKind.Summary -> Summary
                NativeTagKind.PublishedAt -> PublishedAt
                NativeTagKind.Description -> Description
                NativeTagKind.Bolt11 -> Bolt11
                NativeTagKind.Preimage -> Preimage
                NativeTagKind.Relays -> Relays
                NativeTagKind.Amount -> Amount
                NativeTagKind.Lnurl -> Lnurl
                NativeTagKind.MlsProtocolVersion -> MlsProtocolVersion
                NativeTagKind.MlsCiphersuite -> MlsCiphersuite
                NativeTagKind.MlsExtensions -> MlsExtensions
                NativeTagKind.Name -> Name
                NativeTagKind.Option -> Option
                NativeTagKind.Url -> Url
                NativeTagKind.Aes256Gcm -> Aes256Gcm
                NativeTagKind.Size -> Size
                NativeTagKind.Dim -> Dim
                NativeTagKind.File -> File
                NativeTagKind.Magnet -> Magnet
                NativeTagKind.Blurhash -> Blurhash
                NativeTagKind.Streaming -> Streaming
                NativeTagKind.Recording -> Recording
                NativeTagKind.Server -> Server
                NativeTagKind.Starts -> Starts
                NativeTagKind.Ends -> Ends
                NativeTagKind.Status -> Status
                NativeTagKind.CurrentParticipants -> CurrentParticipants
                NativeTagKind.TotalParticipants -> TotalParticipants
                NativeTagKind.Tracker -> Tracker
                NativeTagKind.Method -> Method
                NativeTagKind.Payload -> Payload
                NativeTagKind.Anon -> Anon
                NativeTagKind.Proxy -> Proxy
                NativeTagKind.Emoji -> Emoji
                NativeTagKind.Encrypted -> Encrypted
                NativeTagKind.Repository -> Repository
                NativeTagKind.Request -> Request
                NativeTagKind.Runtime -> Runtime
                NativeTagKind.PollType -> PollType
                NativeTagKind.Response -> Response
                NativeTagKind.Web -> Web
                NativeTagKind.Word -> Word
                is NativeTagKind.SingleLetter -> SingleLetter(NostrSingleLetterTag(native.singleLetter))
                is NativeTagKind.Unknown -> Unknown(native.unknown)
                else -> Unknown("UnknownTagKind:${native.toString()}")
            }
    }
}
