/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.utils

import platform.Foundation.NSCalendar
import platform.Foundation.NSCalendarUnitDay
import platform.Foundation.NSCalendarUnitMonth
import platform.Foundation.NSCalendarUnitYear
import platform.Foundation.NSCalendarUnitHour
import platform.Foundation.NSCalendarUnitMinute
import platform.Foundation.NSCalendarUnitSecond
import platform.Foundation.NSDate

actual object DateTimeUtils {
    actual fun currentDateString(): String {
        val cal = NSCalendar.currentCalendar
        val components = cal.components(
            NSCalendarUnitDay or NSCalendarUnitMonth or NSCalendarUnitYear,
            fromDate = NSDate()
        )
        val d = components.day.toString().padStart(2, '0')
        val m = components.month.toString().padStart(2, '0')
        val y = components.year
        return "$d-$m-$y"
    }

    actual fun currentTimeString(): String {
        val cal = NSCalendar.currentCalendar
        val components = cal.components(
            NSCalendarUnitHour or NSCalendarUnitMinute or NSCalendarUnitSecond,
            fromDate = NSDate()
        )
        val h = components.hour.toString().padStart(2, '0')
        val m = components.minute.toString().padStart(2, '0')
        val s = components.second.toString().padStart(2, '0')
        return "$h:$m:$s"
    }

    actual fun currentDateTimeString(): String =
        "${currentDateString()} ${currentTimeString()}"

    actual fun nowEpochMillis(): Long = (NSDate().timeIntervalSince1970 * 1000).toLong()
}