/*
 * MIT License
 *
 * Copyright (c) 2025 SiamDevTeam
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
package org.siamdev.zappos.ui.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.siamdev.zappos.data.source.local.fetch
import org.siamdev.zappos.data.source.local.transaction
import org.siamdev.zappos.data.source.remote.NostrClient
import org.siamdev.zappos.ui.components.MaterialButton
import rust.nostr.sdk.Event
import rust.nostr.sdk.Kind
import rust.nostr.sdk.PublicKey
import zappos.composeapp.generated.resources.Res
import zappos.composeapp.generated.resources.zappos_dark_horizontal_v2

@Composable
fun LoginScreen() {

    LaunchedEffect(Unit) {
        val myPubKey = PublicKey.parse("e4b2c64f0e4e54abb34d5624cd040e05ecc77f0c467cc46e2cc4d5be98abe3e3")
        val events: List<Event> = NostrClient.fetch {
            authors = listOf(myPubKey)
            kinds = listOf(Kind(0u))
            limit = 1u
        }

        transaction {
            name = "profile"
            key = events.first().id().toHex()
            value = events.first().asJson()
        }

        val profile = fetch<String> {
            name = "profile"
            key = events.first().id().toHex()
        }

        println(profile)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))

        // Logo
        Image(
            painter = painterResource(Res.drawable.zappos_dark_horizontal_v2),
            contentDescription = "Logo",
            modifier = Modifier.size(250.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        BoxWithConstraints(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            val buttonWidth = if (maxWidth < 600.dp) 0.6f else 0.3f

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                MaterialButton(
                    modifier = Modifier.fillMaxWidth(buttonWidth),
                    text = "Login with Nostr account",
                    iconEnd = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    onClick = {}
                )

                Spacer(modifier = Modifier.height(10.dp))

                MaterialButton(
                    modifier = Modifier.fillMaxWidth(buttonWidth),
                    text = "Login with anonymous",
                    iconEnd = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    onClick = {}
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Bottom text
        Text(
            text = "Create an Nostr account",
            color = Color(0xFF515151),
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 46.dp)
        )
    }
}



@Preview
@Composable
fun LoginScreenPreview() {
    LoginScreen()
}
