package org.siamdev.zappos.utils

expect object DateTimeUtils {
    fun currentDateString(): String  // DD-MM-YYYY
    fun currentTimeString(): String  // HH:MM:SS
    fun currentDateTimeString(): String // DD-MM-YYYY HH:MM:SS
}