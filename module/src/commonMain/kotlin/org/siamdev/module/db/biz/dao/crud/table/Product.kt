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
    val builder = ProductBuilder().apply(block)
    val writeOp = ctx.writeOp
    val actor = ctx.actor
    return TxStepBuilder({
        when (writeOp) {
            WriteOp.INSERT -> db.m_PRODUCT_CRUDQueries.insert(
                builder.I_PRD_ID,
                builder.I_CAT_ID,
                builder.I_TAX_ID,
                builder.I_PRD_KIND,
                builder.I_PRD_NAME,
                builder.I_PRD_DESC,
                builder.I_UNIT,
                builder.I_PRICE,
                builder.I_COST_PRICE,
                builder.I_CHARGE_VAT,
                builder.I_OPEN_PRICE,
                builder.I_SKU,
                builder.I_BARCODE,
                builder.I_SEND_ORDER_TO,
                builder.I_DISPLAY_ORDER,
                builder.I_IS_AVAILABLE,
                builder.I_IS_RECOMMENDED,
                builder.I_IS_ACTIVE,
                actor.at,
                actor.userId
            )

            WriteOp.UPDATE -> db.m_PRODUCT_CRUDQueries.update(
                builder.I_CAT_ID, builder.I_TAX_ID, builder.I_PRD_NAME, builder.I_PRD_DESC,
                builder.I_UNIT, builder.I_PRICE, builder.I_COST_PRICE,
                builder.I_CHARGE_VAT, builder.I_OPEN_PRICE, builder.I_SKU, builder.I_BARCODE,
                builder.I_SEND_ORDER_TO, builder.I_DISPLAY_ORDER,
                builder.I_IS_AVAILABLE, builder.I_IS_RECOMMENDED, builder.I_IS_ACTIVE,
                actor.at, actor.userId, builder.I_PRD_ID
            )

            WriteOp.DELETE -> db.m_PRODUCT_CRUDQueries.delete(builder.I_PRD_ID)
        }
    }, this)
}

class ProductBuilder {
    var I_PRD_ID: String = ""
    var I_CAT_ID: String? = null
    var I_TAX_ID: String? = null
    var I_PRD_KIND: String = "1000"
    var I_PRD_NAME: String = ""
    var I_PRD_DESC: String? = null
    var I_UNIT: String = ""
    var I_PRICE: Double = 0.0
    var I_COST_PRICE: Double? = null
    var I_CHARGE_VAT: Long = 1L
    var I_OPEN_PRICE: Long = 0L
    var I_SKU: String? = null
    var I_BARCODE: String? = null
    var I_SEND_ORDER_TO: String? = null
    var I_DISPLAY_ORDER: Long = 0L
    var I_IS_AVAILABLE: Long = 1L
    var I_IS_RECOMMENDED: Long = 0L
    var I_IS_ACTIVE: Long = 1L
}