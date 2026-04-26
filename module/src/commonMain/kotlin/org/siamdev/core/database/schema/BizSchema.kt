package org.siamdev.core.database.schema

/**
 * ZapPOS-BIZ.db — ข้อมูลธุรกิจ (Master, Transaction, Log)
 *
 * PK format ของ _H : {prefix}{YYYYMMDD}{5-digit-seq}  max 20 chars
 * PK ของ _D        : composite (header_id, line_no) — ใช้ id เดิมจาก _H เพื่อ JOIN
 *
 * Prefix สำหรับ _H:
 *   M_CATEGORY      → CAT   e.g. CAT2025042600001  (16)
 *   M_PRODUCT_H     → PRD   e.g. PRD2025042600001  (16)
 *   M_TAX           → TAX   e.g. TAX2025042600001  (16)
 *   M_PAYMENT_TYPE  → PMT   e.g. PMT2025042600001  (16)
 *   T_SALE_ORDER_H  → SO    e.g. SO2025042600001   (15)
 *   T_INVOICE_H     → INV   e.g. INV2025042600001  (16)
 *   T_PAYMENT_H     → PAY   e.g. PAY2025042600001  (16)
 *   L_SALE_ORDER_H  → LSO   e.g. LSO2025042600001  (16)
 */
internal object BizSchema {

    val statements = listOf(

        "PRAGMA foreign_keys = ON",

        // หมวดหมู่สินค้า
        """
        CREATE TABLE IF NOT EXISTS M_CATEGORY (
            cat_id     TEXT    NOT NULL PRIMARY KEY,
            cat_name   TEXT    NOT NULL,
            cat_desc   TEXT,
            sort_order INTEGER NOT NULL DEFAULT 0,
            is_active  INTEGER NOT NULL DEFAULT 1
        )
        """.trimIndent(),

        // สินค้า header
        """
        CREATE TABLE IF NOT EXISTS M_PRODUCT_H (
            product_id   TEXT    NOT NULL PRIMARY KEY,
            product_code TEXT    NOT NULL UNIQUE,
            product_name TEXT    NOT NULL,
            cat_id       TEXT    NOT NULL,
            unit         TEXT    NOT NULL DEFAULT 'EA',
            img_url      TEXT,
            is_active    INTEGER NOT NULL DEFAULT 1,
            created_at   TEXT    NOT NULL DEFAULT (datetime('now', 'localtime')),
            updated_at   TEXT    NOT NULL DEFAULT (datetime('now', 'localtime')),
            FOREIGN KEY (cat_id) REFERENCES M_CATEGORY(cat_id)
        )
        """.trimIndent(),

        // สินค้า detail — ราคาแต่ละประเภท
        // PK composite (product_id, price_type) — product_id เดียวกับ M_PRODUCT_H
        """
        CREATE TABLE IF NOT EXISTS M_PRODUCT_D (
            product_id     TEXT NOT NULL,
            price_type     TEXT NOT NULL DEFAULT 'RETAIL',
            unit_price     REAL NOT NULL,
            effective_date TEXT NOT NULL DEFAULT (datetime('now', 'localtime')),
            PRIMARY KEY (product_id, price_type),
            FOREIGN KEY (product_id) REFERENCES M_PRODUCT_H(product_id)
        )
        """.trimIndent(),

        // อัตราภาษี
        """
        CREATE TABLE IF NOT EXISTS M_TAX (
            tax_id    TEXT NOT NULL PRIMARY KEY,
            tax_name  TEXT NOT NULL,
            tax_rate  REAL NOT NULL,
            is_active INTEGER NOT NULL DEFAULT 1
        )
        """.trimIndent(),

        // ประเภทการชำระเงิน
        """
        CREATE TABLE IF NOT EXISTS M_PAYMENT_TYPE (
            pay_type_id   TEXT NOT NULL PRIMARY KEY,
            pay_type_name TEXT NOT NULL,
            is_active     INTEGER NOT NULL DEFAULT 1
        )
        """.trimIndent(),

        // ใบสั่งขาย header
        """
        CREATE TABLE IF NOT EXISTS T_SALE_ORDER_H (
            order_id     TEXT NOT NULL PRIMARY KEY,
            order_no     TEXT NOT NULL UNIQUE,
            order_date   TEXT NOT NULL DEFAULT (datetime('now', 'localtime')),
            customer_id  TEXT,
            status       TEXT NOT NULL DEFAULT 'DRAFT',
            subtotal     REAL NOT NULL DEFAULT 0,
            tax_amount   REAL NOT NULL DEFAULT 0,
            discount     REAL NOT NULL DEFAULT 0,
            total_amount REAL NOT NULL DEFAULT 0,
            note         TEXT,
            created_at   TEXT NOT NULL DEFAULT (datetime('now', 'localtime')),
            updated_at   TEXT NOT NULL DEFAULT (datetime('now', 'localtime'))
        )
        """.trimIndent(),

        // ใบสั่งขาย detail — PK composite (order_id, line_no)
        // order_id เดียวกับ T_SALE_ORDER_H → JOIN ON h.order_id = d.order_id
        """
        CREATE TABLE IF NOT EXISTS T_SALE_ORDER_D (
            order_id    TEXT    NOT NULL,
            line_no     INTEGER NOT NULL,
            product_id  TEXT    NOT NULL,
            qty         REAL    NOT NULL DEFAULT 1,
            unit_price  REAL    NOT NULL,
            discount    REAL    NOT NULL DEFAULT 0,
            tax_rate    REAL    NOT NULL DEFAULT 0,
            line_amount REAL    NOT NULL,
            PRIMARY KEY (order_id, line_no),
            FOREIGN KEY (order_id)   REFERENCES T_SALE_ORDER_H(order_id),
            FOREIGN KEY (product_id) REFERENCES M_PRODUCT_H(product_id)
        )
        """.trimIndent(),

        // ใบแจ้งหนี้ header
        """
        CREATE TABLE IF NOT EXISTS T_INVOICE_H (
            invoice_id   TEXT NOT NULL PRIMARY KEY,
            invoice_no   TEXT NOT NULL UNIQUE,
            invoice_date TEXT NOT NULL DEFAULT (datetime('now', 'localtime')),
            order_id     TEXT,
            customer_id  TEXT,
            status       TEXT NOT NULL DEFAULT 'UNPAID',
            subtotal     REAL NOT NULL DEFAULT 0,
            tax_amount   REAL NOT NULL DEFAULT 0,
            discount     REAL NOT NULL DEFAULT 0,
            total_amount REAL NOT NULL DEFAULT 0,
            due_date     TEXT,
            note         TEXT,
            created_at   TEXT NOT NULL DEFAULT (datetime('now', 'localtime')),
            updated_at   TEXT NOT NULL DEFAULT (datetime('now', 'localtime')),
            FOREIGN KEY (order_id) REFERENCES T_SALE_ORDER_H(order_id)
        )
        """.trimIndent(),

        // ใบแจ้งหนี้ detail — PK composite (invoice_id, line_no)
        // invoice_id เดียวกับ T_INVOICE_H → JOIN ON h.invoice_id = d.invoice_id
        """
        CREATE TABLE IF NOT EXISTS T_INVOICE_D (
            invoice_id  TEXT    NOT NULL,
            line_no     INTEGER NOT NULL,
            product_id  TEXT    NOT NULL,
            qty         REAL    NOT NULL DEFAULT 1,
            unit_price  REAL    NOT NULL,
            discount    REAL    NOT NULL DEFAULT 0,
            tax_rate    REAL    NOT NULL DEFAULT 0,
            line_amount REAL    NOT NULL,
            PRIMARY KEY (invoice_id, line_no),
            FOREIGN KEY (invoice_id) REFERENCES T_INVOICE_H(invoice_id),
            FOREIGN KEY (product_id) REFERENCES M_PRODUCT_H(product_id)
        )
        """.trimIndent(),

        // การชำระเงิน header
        """
        CREATE TABLE IF NOT EXISTS T_PAYMENT_H (
            payment_id   TEXT NOT NULL PRIMARY KEY,
            payment_no   TEXT NOT NULL UNIQUE,
            payment_date TEXT NOT NULL DEFAULT (datetime('now', 'localtime')),
            invoice_id   TEXT NOT NULL,
            total_paid   REAL NOT NULL DEFAULT 0,
            status       TEXT NOT NULL DEFAULT 'COMPLETED',
            note         TEXT,
            created_at   TEXT NOT NULL DEFAULT (datetime('now', 'localtime')),
            FOREIGN KEY (invoice_id) REFERENCES T_INVOICE_H(invoice_id)
        )
        """.trimIndent(),

        // การชำระเงิน detail — split payment หลายช่องทาง
        // PK composite (payment_id, line_no)
        // payment_id เดียวกับ T_PAYMENT_H → JOIN ON h.payment_id = d.payment_id
        """
        CREATE TABLE IF NOT EXISTS T_PAYMENT_D (
            payment_id   TEXT    NOT NULL,
            line_no      INTEGER NOT NULL,
            pay_type_id  TEXT    NOT NULL,
            amount_paid  REAL    NOT NULL,
            reference_no TEXT,
            PRIMARY KEY (payment_id, line_no),
            FOREIGN KEY (payment_id)  REFERENCES T_PAYMENT_H(payment_id),
            FOREIGN KEY (pay_type_id) REFERENCES M_PAYMENT_TYPE(pay_type_id)
        )
        """.trimIndent(),

        // ══════════════════════════════════════════════════════════════
        // L_ Logs
        // ══════════════════════════════════════════════════════════════

        // Log ใบสั่งขาย header
        """
        CREATE TABLE IF NOT EXISTS L_SALE_ORDER_H (
            log_id    TEXT NOT NULL PRIMARY KEY,
            order_id  TEXT NOT NULL,
            action    TEXT NOT NULL,
            action_by TEXT,
            action_at TEXT NOT NULL DEFAULT (datetime('now', 'localtime')),
            FOREIGN KEY (order_id) REFERENCES T_SALE_ORDER_H(order_id)
        )
        """.trimIndent(),

        // Log ใบสั่งขาย detail — บันทึก field ที่เปลี่ยน
        // PK composite (log_id, line_no)
        // log_id เดียวกับ L_SALE_ORDER_H → JOIN ON h.log_id = d.log_id
        """
        CREATE TABLE IF NOT EXISTS L_SALE_ORDER_D (
            log_id     TEXT    NOT NULL,
            line_no    INTEGER NOT NULL,
            field_name TEXT    NOT NULL,
            old_value  TEXT,
            new_value  TEXT,
            PRIMARY KEY (log_id, line_no),
            FOREIGN KEY (log_id) REFERENCES L_SALE_ORDER_H(log_id)
        )
        """.trimIndent()
    )
}