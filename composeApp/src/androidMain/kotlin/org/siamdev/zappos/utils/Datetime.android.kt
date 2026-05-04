/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.utils

import java.util.Calendar

actual object DateTimeUtils {
    actual fun currentDateString(): String {
        val c = Calendar.getInstance()
        val d = c.get(Calendar.DAY_OF_MONTH).toString().padStart(2, '0')
        val m = (c.get(Calendar.MONTH) + 1).toString().padStart(2, '0')
        val y = c.get(Calendar.YEAR)
        return "$d-$m-$y"
    }

    actual fun currentTimeString(): String {
        val c = Calendar.getInstance()
        val h = c.get(Calendar.HOUR_OF_DAY).toString().padStart(2, '0')
        val m = c.get(Calendar.MINUTE).toString().padStart(2, '0')
        val s = c.get(Calendar.SECOND).toString().padStart(2, '0')
        return "$h:$m:$s"
    }

    actual fun currentDateTimeString(): String =
        "${currentDateString()} ${currentTimeString()}"

    actual fun nowEpochMillis(): Long = System.currentTimeMillis()
}