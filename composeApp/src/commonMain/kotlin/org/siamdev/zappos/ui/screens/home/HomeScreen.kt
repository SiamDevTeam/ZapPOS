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
package org.siamdev.zappos.ui.screens.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.painterResource
import org.siamdev.core.nostr.*
import org.siamdev.core.nostr.keys.NostrKeys
import org.siamdev.core.nostr.types.NostrKindStd
import org.siamdev.zappos.Platform
import zappos.composeapp.generated.resources.Res
import zappos.composeapp.generated.resources.compose_multiplatform
import org.siamdev.zappos.data.repository.*

@Composable
fun HomeScreen() {
    var showContent by remember { mutableStateOf(false) }
    val keys = remember { NostrKeys.generate() }
    val client2 = remember { NostrClient() }
    val filter = remember { NostrFilter().kind(NostrKind.fromStd(NostrKindStd.METADATA)).limit(5U) }

    LaunchedEffect(Unit) {
        // Fetch events
        val target = RelayUrl.parse("wss://fenrir-s.notoshi.win")
        client2.addRelay(target)
        client2.connect()
        val events = client2.fetchEvents(filter, NostrDuration.seconds(10L))
        println(events.first())
        println(events.first()?.toJson())
        println(keys.secretKey().toBech32())
        println(keys.publicKey().toBech32())

        val profile: NostrEvent = events.first()!!
        runLmdb { tx ->
            val profile: NostrEvent = events.first()!!

            tx.set("profile", profile.id, profile.toJson())

            val jsonString = tx.get("profile", profile.id)
            val loadedProfile = jsonString?.let { NostrEvent.fromJson(it) }
            println("Loaded NostrEvent: ${loadedProfile?.id}")
        }


    }

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primaryContainer)
            .safeContentPadding()
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { showContent = !showContent }) {
            Text("Click me!")
        }

        AnimatedVisibility(showContent) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(Res.drawable.compose_multiplatform),
                    contentDescription = null
                )
                Text("Compose: ${Platform().message}")
            }
        }
    }
}
