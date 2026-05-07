/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.module.db.sys.dao

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.siamdev.module.db.AppDatabase
import org.siamdev.module.db.sys.schema.T_SYS_NUMBERING_CONFIG

class NumberingDao(private val db: AppDatabase) {

    val generate: suspend (codeType: String, format: String) -> String = { codeType, format ->
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val cfg = loadOrInit(codeType, format)

        val digitLen = format.count { it == 'x' }
        val breakPattern = format.detectBreakPattern()
        val breakKey = buildBreakKey(breakPattern, now)
        val seq = if (breakKey != cfg.I_LAST_BREAK_KEY) 1L else cfg.I_LAST_SEQ + 1L

        db.sys { nUMBERING_CONFIG_CRUDQueries.updateSeq(seq, breakKey, codeType) }

        buildNumber(format, breakPattern, breakKey, digitLen, seq)
    }

    private suspend fun loadOrInit(codeType: String, format: String): T_SYS_NUMBERING_CONFIG {
        val existing = db.sys {
            nUMBERING_CONFIG_CRUDQueries.selectByCodeType(codeType).executeAsOneOrNull()
        }
        return when {
            existing == null -> {
                db.sys { nUMBERING_CONFIG_CRUDQueries.insertNew(codeType, format) }
                T_SYS_NUMBERING_CONFIG(codeType, format, 0L, "")
            }
            existing.I_FORMAT != format -> {
                db.sys { nUMBERING_CONFIG_CRUDQueries.resetFormat(format, codeType) }
                T_SYS_NUMBERING_CONFIG(codeType, format, 0L, "")
            }
            else -> existing
        }
    }
}

private fun String.detectBreakPattern() = when {
    ":hhmmss"  in this -> ":hhmmss"
    ":hhmm"    in this -> ":hhmm"
    ":hh"      in this -> ":hh"
    ":mm"      in this -> ":mm"
    ":ss"      in this -> ":ss"
    "yyyymmdd" in this -> "yyyymmdd"
    "yyyymm"   in this -> "yyyymm"
    "yymmdd"   in this -> "yymmdd"
    "yymm"     in this -> "yymm"
    "yyyy"     in this -> "yyyy"
    "yy"       in this -> "yy"
    "mm"       in this -> "mm"
    "dd"       in this -> "dd"
    else       -> ""
}

private fun buildBreakKey(pattern: String, dt: LocalDateTime): String {
    fun Int.p() = toString().padStart(2, '0')
    return when (pattern) {
        ":hhmmss"  -> ":${dt.hour.p()}${dt.minute.p()}${dt.second.p()}"
        ":hhmm"    -> ":${dt.hour.p()}${dt.minute.p()}"
        ":hh"      -> ":${dt.hour.p()}"
        ":mm"      -> ":${dt.minute.p()}"
        ":ss"      -> ":${dt.second.p()}"
        "yyyymmdd" -> "${dt.year}${dt.monthNumber.p()}${dt.dayOfMonth.p()}"
        "yyyymm"   -> "${dt.year}${dt.monthNumber.p()}"
        "yymmdd"   -> "${dt.year.toString().takeLast(2)}${dt.monthNumber.p()}${dt.dayOfMonth.p()}"
        "yymm"     -> "${dt.year.toString().takeLast(2)}${dt.monthNumber.p()}"
        "yyyy"     -> "${dt.year}"
        "yy"       -> dt.year.toString().takeLast(2)
        "mm"       -> dt.monthNumber.p()
        "dd"       -> dt.dayOfMonth.p()
        else       -> ""
    }
}

private fun buildNumber(format: String, breakPattern: String, breakKey: String, digitLen: Int, seq: Long): String {
    var result = format
    if (breakPattern.isNotEmpty())
        result = result.replace(breakPattern, breakKey.replace(":", ""))
    if (digitLen > 0)
        result = result.replace("x".repeat(digitLen), seq.toString().padStart(digitLen, '0'))
    return result.replace(":", "")
}