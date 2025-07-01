package org.siamdev.zappos.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.siamdev.zappos.keys.NostrKeys
import org.siamdev.zappos.keys.NostrPublicKey
import org.siamdev.zappos.keys.NostrSigner

data class NostrCardData(val title: String, val content: String)

@Composable
fun NostrDemoScreen() {
    val keys = NostrKeys.generate()
    val secretKey = keys.secretKey()
    val publicKey = keys.publicKey()
    val nsec = secretKey.toBech32()
    val npub = publicKey.toBech32()
    val parsedSecretKey = NostrKeys.parse(nsec).secretKey()
    val parsedPublicKey = NostrKeys.parse(nsec).publicKey()
    val npubUri = publicKey.toNostrUri()

    val msg = "13adc511de7e1cfcf1c6b7f6365fb5a03442d7bcacf565ea57fa7770912c023d"
    val signature = keys.sign(msg)

    val signerPublicKey = NostrSigner.keys(keys).getPublicKey()


    val cardList = listOf(
        NostrCardData("Private Key (hex)", secretKey.toHex()),
        NostrCardData("Public Key (hex)", publicKey.toHex()),
        NostrCardData("nsec key", nsec),
        NostrCardData("npub key", npub),
        NostrCardData("Sign Message", signature),
        NostrCardData("Nostr Signer", signerPublicKey.toHex()),
        NostrCardData("Parsed from nsec to hex", parsedSecretKey.toHex()),
        NostrCardData("Parsed from nsec to hex", parsedPublicKey.toHex()),
        NostrCardData("Profile URI", npubUri)
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(cardList) { card ->
            NostrCard(
                title = card.title,
                content = card.content
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
fun NostrCard(
    title: String,
    content: String
) {
    Card(
        modifier = Modifier
            .padding(vertical = 4.dp, horizontal = 0.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F7F7))
    ) {
        androidx.compose.foundation.layout.Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = title, color = Color(0xFF2196F3))
            Text(text = content, color = Color.Black)
        }
    }
}

@Preview
@Composable
fun PreviewNostrDemoScreen() {
    NostrDemoScreen()
}