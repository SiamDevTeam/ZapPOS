package org.siamdev.zappos.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
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
import org.siamdev.zappos.nostr.NostrEvent
import org.siamdev.zappos.nostr.keys.NIP44VERSION
import org.siamdev.zappos.nostr.keys.NostrKeys
import org.siamdev.zappos.nostr.keys.NostrSigner

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

    val sharedKey = NostrKeys.getSharedKey(secretKey, publicKey)
    val sharedSecretKey = sharedKey.secretKey().toHex()
    val sharedPublicKey = sharedKey.publicKey().toHex()

    val ciphertext = NostrKeys.NIP04Encrypt(secretKey, publicKey, "Hello, NIP-04!")
    val plaintext = NostrKeys.NIP04Decrypt(secretKey, publicKey, ciphertext)

    val ciphertextChaCha20 = NostrKeys.NIP44Encrypt(secretKey, publicKey, "Hello, NIP-44!", NIP44VERSION.V2)
    val plaintextChaCha20 = NostrKeys.NIP44Decrypt(secretKey, publicKey, ciphertextChaCha20)


    val originalJson = """
        {"content":"```\nThis is out of date, a lot of the functionality has been built in, read if you're bored.\n```\n\n**This guide uses cashu.me and the 0xchat app**\n\nFirst go to cashu.me. Make sure you've added a mint. I suggest mint.0xchat.com (note for newbies like I recently was, it's the number zero and not the letter O). Make sure you have some balance. Then hit SEND and choose ECASH.\n\n![image](https://yakihonne.s3.ap-east-1.amazonaws.com/b90c3cb71d66343e01104d5c9adf7db05d36653b17601ff9b2eebaa81be67823/files/1741949878006-YAKIHONNES3.png)\n\nNext type in a small amount to send. As always with bleeding-edge stuff accept the token can go poof. Only after you've typed in a number will you see the LOCK button appear next to the SEND button.\n\n![image](https://yakihonne.s3.ap-east-1.amazonaws.com/b90c3cb71d66343e01104d5c9adf7db05d36653b17601ff9b2eebaa81be67823/files/1741949984612-YAKIHONNES3.png)\n\nHitting LOCK will reveal the \"Receiver public key\" field as below. This works for public keys generated within cashu.me (go to cashu.me settings) as well as for Nostr public keys.\n\n![image](https://yakihonne.s3.ap-east-1.amazonaws.com/b90c3cb71d66343e01104d5c9adf7db05d36653b17601ff9b2eebaa81be67823/files/1741950007867-YAKIHONNES3.png)\n\nNext copy your own public key (or that belonging to whoever you want to test this with) and head over to <https://nostrcheck.me/converter/> and paste it in there. This will convert it to hex format, which is needed in this case.\n\n![image](https://yakihonne.s3.ap-east-1.amazonaws.com/b90c3cb71d66343e01104d5c9adf7db05d36653b17601ff9b2eebaa81be67823/files/1741950078295-YAKIHONNES3.png)\n\nAfter that go back to cashu.me and paste the hex format key in that field. It'll be in this red color \"Invalid public key\" state, don't worry.\n\n![image](https://yakihonne.s3.ap-east-1.amazonaws.com/b90c3cb71d66343e01104d5c9adf7db05d36653b17601ff9b2eebaa81be67823/files/1741950135557-YAKIHONNES3.png)\n\nAdd 02 (number zero, number two) in front of the hex key. That'll make it valid. It's just the scheme. You'll see the locked icon appear next to the SEND button.\n\n![image](https://yakihonne.s3.ap-east-1.amazonaws.com/b90c3cb71d66343e01104d5c9adf7db05d36653b17601ff9b2eebaa81be67823/files/1741950188866-YAKIHONNES3.png)\n\nThen hit SEND and, voila, you now have a Cashu token that only the person in control of the nsec of whatever public key you used just then can redeem. Post it publicly to taunt others. (FYI the P2PK abbreviation there means pay-to-public-key.) Hit COPY to grab the token string.\n\n![image](https://yakihonne.s3.ap-east-1.amazonaws.com/b90c3cb71d66343e01104d5c9adf7db05d36653b17601ff9b2eebaa81be67823/files/1741950247001-YAKIHONNES3.png)\n\nLast up, open 0xchat. Assuming you're doing this yourself, make sure you're logged in with the nsec of the public key you used to create the test token. Also make sure the token string resides in the clipboard of the device you're using 0xchat on.\n\n![image](https://yakihonne.s3.ap-east-1.amazonaws.com/b90c3cb71d66343e01104d5c9adf7db05d36653b17601ff9b2eebaa81be67823/files/1741950380267-YAKIHONNES3.png)\n\nThen go to th \"Me\" tab in 0xchat to open the Cashu wallet. Set it up if you haven't already. Hit \"Receive\" and then \"Redeem Ecash\". As long as the token is in your clipboard and you did it all the steps right it should be redeem straight to your Cashu wallet. Well hopefully.\n\nThat's it. This guide will self-descrut if it goes out of date or was wrong somehow.\n","created_at":1751728531,"id":"fb5a820e27da4701b8d2500d449032a98bb39c5c3f8ce1bad4924b475f4b270b","kind":30023,"pubkey":"b90c3cb71d66343e01104d5c9adf7db05d36653b17601ff9b2eebaa81be67823","sig":"83b4d16c6b07f21b6b0ac8f7596d4ecfe6da0ca93477a8bd1da5ed3c3776def713a69199b14d48b375081ff2ebf77e2453d829e3f4ac1217649b98449fa057ef","tags":[["title","How to create and redeem a Cashu token locked to an npub (out of date)\n"],["summary","How to create a Cashu token locked to a Nostr public key on cashu.me and then redeem it on 0xchat."],["image","https://image.nostr.build/b8babff24274f854bcd883cc3664550d226437a0e61b0ce17506c92504a0ee13.png"],["d","UQJrrh-pGFERh6MN5sFHc"],["t","cahsu"],["t","p2pk"],["t","0xchat"],["client","Primal"],["published_at","1741951992"]]}
    """.trimIndent()

    val event = NostrEvent.fromJson(originalJson)

    val tag = event.tags.toVec().first().content()!!
    val json = event.toJson()

    val cardList = listOf(
        NostrCardData("Private Key (hex)", secretKey.toHex()),
        NostrCardData("Public Key (hex)", publicKey.toHex()),
        NostrCardData("nsec key", nsec),
        NostrCardData("npub key", npub),
        NostrCardData("Sign Message", signature),
        NostrCardData("Nostr Signer", signerPublicKey.toHex()),
        NostrCardData("Parsed from nsec to hex", parsedSecretKey.toHex()),
        NostrCardData("Parsed from nsec to hex", parsedPublicKey.toHex()),
        NostrCardData("Profile URI", npubUri),
        NostrCardData("Shared Secret Key", sharedSecretKey),
        NostrCardData("Shared Public Key", sharedPublicKey),
        NostrCardData("NIP 04 Encrypted Message", ciphertext),
        NostrCardData("NIP 04 Decrypted Message", plaintext),
        NostrCardData("NIP 44 Encrypted Message", ciphertextChaCha20),
        NostrCardData("NIP 44 Decrypted Message", plaintextChaCha20),
        NostrCardData("Original JSON", originalJson),
        NostrCardData("Event to JSON", json),
        NostrCardData("Event ID", event.id),
        NostrCardData("Event Pubkey", event.pubkey),
        NostrCardData("Event Created At", event.createdAt.toString()),
        NostrCardData("Event Kind", event.kind.toString()),
        NostrCardData("Event Tags", tag),
        NostrCardData("Event Content", event.content),
        NostrCardData("Event Signature", event.sig)
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
            .padding(vertical = 4.dp, horizontal = 0.dp)
            .widthIn(min = 370.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F7F7))
    ) {
        Column(
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