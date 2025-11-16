package org.siamdev.core

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.runner.RunWith
import org.siamdev.core.nostr.keys.NIP44VERSION
import org.siamdev.core.nostr.keys.NostrKeys
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@RunWith(AndroidJUnit4::class)
class NostrKeysTest {

    @Test
    fun testGenerateKeys() {
        val keys = NostrKeys.generate()
        assertNotNull(keys.secretKey(), "Secret key should not be null")
        assertNotNull(keys.publicKey(), "Public key should not be null")
    }

    @Test
    fun testSignAndVerify() {
        val keys = NostrKeys.generate()
        val message = "8fd1fe5da6f467a5f78e78d8937c2e6c21cc433a3756014102c02beb08d45648"
        val signature = keys.sign(message)
        assertNotNull(signature)
    }

    @Test
    fun testNIP04EncryptDecrypt() {
        val alice = NostrKeys.generate()
        val bob = NostrKeys.generate()
        val message = "Secret message"

        val encrypted = NostrKeys.NIP04Encrypt(alice.secretKey(), bob.publicKey(), message)
        assertNotNull(encrypted)

        val decrypted = NostrKeys.NIP04Decrypt(alice.secretKey(), bob.publicKey(), encrypted)
        assertEquals(message, decrypted)
    }

    @Test
    fun testNIP44EncryptDecrypt() {
        val alice = NostrKeys.generate()
        val bob = NostrKeys.generate()
        val message = "Another secret"

        val encrypted = NostrKeys.NIP44Encrypt(alice.secretKey(), bob.publicKey(), message, NIP44VERSION.V2)
        assertNotNull(encrypted)

        val decrypted = NostrKeys.NIP44Decrypt(alice.secretKey(), bob.publicKey(), encrypted)
        assertEquals(message, decrypted)
    }

    @Test
    fun testKeyConversions() {
        val keys = NostrKeys.generate()
        val pub = keys.publicKey()
        val sec = keys.secretKey()

        assertNotNull(pub.toHex())
        assertNotNull(pub.toBech32())
        assertNotNull(pub.toNostrUri())
        assertNotNull(sec.toHex())
        assertNotNull(sec.toBech32())
    }
}