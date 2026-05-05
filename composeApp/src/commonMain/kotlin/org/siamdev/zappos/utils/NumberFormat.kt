/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.utils

import kotlin.math.abs
import kotlin.math.roundToLong

fun Double.formatPrice(): String {
    val rounded = roundToLong()
    val abs = abs(rounded)
    val intStr = abs.toString().reversed().chunked(3).joinToString(",").reversed()
    return if (rounded < 0) "-$intStr" else intStr
}

fun Double.formatAmount(decimals: Int = 2): String {
    val intPart = toLong()
    var factor = 1L
    repeat(decimals) { factor *= 10 }
    val fracPart = abs(((this - intPart) * factor).roundToLong())
    val sign = if (this < 0) "-" else ""
    val intStr = abs(intPart).toString().reversed().chunked(3).joinToString(",").reversed()
    return if (decimals > 0) "$sign$intStr.${fracPart.toString().padStart(decimals, '0')}"
    else "$sign$intStr"
}