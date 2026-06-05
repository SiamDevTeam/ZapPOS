package org.siamdev.zappos.utils

data class TimeValue(val hour: Int, val minute: Int) {

    init {
        require(hour in 0..23) { "hour must be 0–23" }
        require(minute in 0..59) { "minute must be 0–59" }
    }

    override fun toString(): String = DateTimeUtils.formatTime(hour, minute)

    companion object {
        fun parse(s: String): TimeValue {
            val parts = s.split(":")
            val h = parts.getOrNull(0)?.toIntOrNull()?.coerceIn(0, 23) ?: 0
            val m = parts.getOrNull(1)?.toIntOrNull()?.coerceIn(0, 59) ?: 0
            return TimeValue(h, m)
        }
    }
}