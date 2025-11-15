package org.siamdev.core.nostr

import android.os.Build
import java.time.Duration

@androidx.annotation.RequiresApi(Build.VERSION_CODES.O)
actual class NostrDuration internal constructor(
    internal val native: Duration
) {

    actual companion object {
        actual fun milliseconds(value: Long): NostrDuration =
            NostrDuration(Duration.ofMillis(value))

        actual fun seconds(value: Long): NostrDuration =
            NostrDuration(Duration.ofSeconds(value))
    }

    actual fun inWholeMilliseconds(): Long = native.toMillis()
    actual fun inWholeSeconds(): Long = native.seconds
    actual fun toNative(): NostrDuration = NostrDuration(native)

    internal actual fun unwrap(): Any = native
}
