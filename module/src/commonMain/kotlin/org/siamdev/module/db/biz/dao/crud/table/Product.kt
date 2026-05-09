/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.module.db.biz.dao.crud.table

import org.siamdev.module.db.biz.dao.crud.BizTxScope
import org.siamdev.module.db.TxStepBuilder
import org.siamdev.module.db.WriteOp

// M_PRODUCT
fun BizTxScope.Product(block: ProductBuilder.() -> Unit): TxStepBuilder<Unit> {
    val b    = ProductBuilder().apply(block)
    val mode = ctx.writeOp
    val a    = ctx.actor
    return TxStepBuilder({
        when (mode) {
            WriteOp.INSERT -> db.m_PRODUCT_CRUDQueries.insert(
                b.I_PRD_ID, b.I_CAT_ID, b.I_TAX_ID, b.I_PRODUCT_NAME,
                b.I_IMAGE_PATH, b.I_UNIT, b.I_PRICE, b.I_COST, b.I_DESCRIPTION,
                b.I_TRACK_STOCK, b.I_IS_RECOMMENDED, b.I_IS_ACTIVE,
                a.at, a.userId
            )

            WriteOp.UPDATE -> db.m_PRODUCT_CRUDQueries.update(
                b.I_CAT_ID, b.I_TAX_ID, b.I_PRODUCT_NAME,
                b.I_IMAGE_PATH, b.I_UNIT, b.I_PRICE, b.I_COST, b.I_DESCRIPTION,
                b.I_TRACK_STOCK, b.I_IS_RECOMMENDED, b.I_IS_ACTIVE,
                a.at, a.userId, b.I_PRD_ID
            )

            WriteOp.DELETE -> db.m_PRODUCT_CRUDQueries.delete(b.I_PRD_ID)
        }
    }, this)
}

class ProductBuilder {
    var I_PRD_ID: String          = ""
    var I_CAT_ID: String          = ""
    var I_TAX_ID: String          = ""
    var I_PRODUCT_NAME: String    = ""
    var I_IMAGE_PATH: String?     = null
    var I_UNIT: String            = ""
    var I_PRICE: Double           = 0.0
    var I_COST: Double            = 0.0
    var I_DESCRIPTION: String?    = null
    var I_TRACK_STOCK: Long       = 0L
    var I_IS_RECOMMENDED: Long    = 0L
    var I_IS_ACTIVE: Long         = 1L
}