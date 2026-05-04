/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

actual object DateTimeUtils {
    private val dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")

    actual fun currentDateString(): String =
        LocalDateTime.now().format(dateFormatter)

    actual fun currentTimeString(): String =
        LocalDateTime.now().format(timeFormatter)

    actual fun currentDateTimeString(): String =
        "${currentDateString()} ${currentTimeString()}"

    actual fun nowEpochMillis(): Long = System.currentTimeMillis()
}