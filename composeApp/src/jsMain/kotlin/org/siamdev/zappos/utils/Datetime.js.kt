package org.siamdev.zappos.utils

actual object DateTimeUtils {
    actual fun currentDateString(): String {
        val date = js("new Date()")
        val d = js("String(date.getDate()).padStart(2,'0')")
        val m = js("String(date.getMonth()+1).padStart(2,'0')")
        val y = js("date.getFullYear()")
        return "$d-$m-$y"
    }

    actual fun currentTimeString(): String {
        val date = js("new Date()")
        val h = js("String(date.getHours()).padStart(2,'0')")
        val m = js("String(date.getMinutes()).padStart(2,'0')")
        val s = js("String(date.getSeconds()).padStart(2,'0')")
        return "$h:$m:$s"
    }

    actual fun currentDateTimeString(): String =
        "${currentDateString()} ${currentTimeString()}"

    actual fun nowEpochMillis(): Long = (js("Date.now()") as Double).toLong()
}