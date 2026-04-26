package org.siamdev.core.database.schema

/**
 * ZapPOS-SYS.db — ข้อมูลระบบและการตั้งค่าแอป
 *
 * PK format: {prefix}{YYYYMMDD}{5-digit-seq}  max 20 chars
 */
internal object SysSchema {

    val statements = listOf(

        // ─── M_ Master ────────────────────────────────────────────────

        // ตั้งค่าระบบแบบ key-value (theme, font_size, language, etc.)
        // PK: setting_key เป็น TEXT เช่น "theme", "font_size"
        """
        CREATE TABLE IF NOT EXISTS M_SETTINGS (
            setting_key   TEXT NOT NULL PRIMARY KEY,
            setting_value TEXT NOT NULL,
            setting_group TEXT NOT NULL DEFAULT 'GENERAL',
            updated_at    TEXT NOT NULL DEFAULT (datetime('now', 'localtime'))
        )
        """.trimIndent(),

        // ─── L_ Logs ──────────────────────────────────────────────────

        // Log session การเข้าใช้งาน
        // PK prefix: SES  → SES{YYYYMMDD}{00001}  = 16 chars
        """
        CREATE TABLE IF NOT EXISTS L_SESSION_H (
            session_id  TEXT NOT NULL PRIMARY KEY,
            user_name   TEXT NOT NULL,
            login_at    TEXT NOT NULL DEFAULT (datetime('now', 'localtime')),
            logout_at   TEXT,
            device_info TEXT
        )
        """.trimIndent()
    )
}