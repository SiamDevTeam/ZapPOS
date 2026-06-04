/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.utils

object DateTimeUtils {

    // Pure Kotlin stdlib — works on Android, iOS, JVM Desktop, JS, and WASM.

    /** Zero-pads [n] to at least [length] digits. */
    fun pad(n: Int, length: Int): String = n.toString().padStart(length, '0')

    /** Shorthand for [pad](n, 2) — the most common case (e.g. 9 → "09"). */
    fun pad2(n: Int): String = pad(n, 2)

    /**
     * Formats [hour] and [minute] as a 24-hour time string "HH:MM".
     *
     * Examples: formatTime(9, 0) → "09:00", formatTime(18, 30) → "18:30"
     */
    fun formatTime(hour: Int, minute: Int): String = "${pad2(hour)}:${pad2(minute)}"

    // Uses Howard Hinnant's civil_from_days algorithm — all-integer arithmetic,
    // no platform APIs, identical output on every KMP target.

    private data class UtcComponents(
        val year: Int, val month: Int, val day: Int,
        val hour: Int, val minute: Int, val second: Int,
    )

    private fun decompose(epochMillis: Long): UtcComponents {
        val totalSeconds = epochMillis / 1000L
        // The extra +86400 keeps the result positive for pre-1970 timestamps.
        val secondOfDay = ((totalSeconds % 86400L) + 86400L).toInt() % 86400
        val second = secondOfDay % 60
        val minute = (secondOfDay / 60) % 60
        val hour   = secondOfDay / 3600

        var z = (totalSeconds / 86400L).toInt()   // days since 1970-01-01
        z += 719468                                // shift epoch to 0000-03-01
        val era = if (z >= 0) z / 146097 else (z - 146096) / 146097
        val doe = z - era * 146097                 // day of era  [0, 146096]
        val yoe = (doe - doe / 1460 + doe / 36524 - doe / 146096) / 365
        val y   = yoe + era * 400
        val doy = doe - (365 * yoe + yoe / 4 - yoe / 100)
        val mp  = (5 * doy + 2) / 153
        val d   = doy - (153 * mp + 2) / 5 + 1
        val m   = if (mp < 10) mp + 3 else mp - 9
        return UtcComponents(y + if (m <= 2) 1 else 0, m, d, hour, minute, second)
    }

    /**
     * Formats [epochMillis] as **"DD-MM-YYYY"** (UTC).
     *
     * Example: `toDateString(1_735_689_600_000L)` → `"01-01-2025"`
     */
    fun toDateString(epochMillis: Long): String {
        val c = decompose(epochMillis)
        return "${pad2(c.day)}-${pad2(c.month)}-${c.year}"
    }

    /**
     * Formats [epochMillis] as **"HH:MM:SS"** (UTC).
     *
     * Example: `toTimeString(1_735_689_600_000L)` → `"00:00:00"`
     */
    fun toTimeString(epochMillis: Long): String {
        val c = decompose(epochMillis)
        return "${pad2(c.hour)}:${pad2(c.minute)}:${pad2(c.second)}"
    }

    /**
     * Formats [epochMillis] as **"DD-MM-YYYY HH:MM:SS"** (UTC).
     *
     * Example: `toDateTimeString(1_735_689_600_000L)` → `"01-01-2025 00:00:00"`
     */
    fun toDateTimeString(epochMillis: Long): String {
        val c = decompose(epochMillis)
        return "${pad2(c.day)}-${pad2(c.month)}-${c.year} ${pad2(c.hour)}:${pad2(c.minute)}:${pad2(c.second)}"
    }

    // ── Platform-specific current datetime ────────────────────────────────────

    /** Returns today's date as "DD-MM-YYYY" in local time. */
    fun currentDateString(): String = DateTimePlatform.currentDateString()

    /** Returns the current time as "HH:MM:SS" in local time. */
    fun currentTimeString(): String = DateTimePlatform.currentTimeString()

    /** Returns the current date and time as "DD-MM-YYYY HH:MM:SS" in local time. */
    fun currentDateTimeString(): String = DateTimePlatform.currentDateTimeString()

    /** Returns the current time as milliseconds since the Unix epoch. */
    fun nowEpochMillis(): Long = DateTimePlatform.nowEpochMillis()
}

// Platform-specific implementations live in their respective source sets.
internal expect object DateTimePlatform {
    fun currentDateString(): String
    fun currentTimeString(): String
    fun currentDateTimeString(): String
    fun nowEpochMillis(): Long
}