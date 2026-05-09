/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.module.db.biz.dao.crud.table

import org.siamdev.module.db.biz.dao.crud.BizTxScope
import org.siamdev.module.db.TxStepBuilder
import org.siamdev.module.db.WriteOp

// T_SALE_ORDER_D
fun BizTxScope.SaleOrderD(block: SaleOrderDBuilder.() -> Unit): TxStepBuilder<Unit> {
    val b    = SaleOrderDBuilder().apply(block)
    val mode = currentDetailMode ?: ctx.writeOp
    val a    = ctx.actor
    return TxStepBuilder({
        when (mode) {
            WriteOp.INSERT -> db.sALE_ORDER_D_CRUDQueries.insert(
                b.I_SO_ID, b.I_INTERNAL_NO, b.I_PRD_ID, b.I_TAX_ID,
                b.I_UNIT_PRICE, b.I_QTY, b.I_DISCOUNT_AMT, b.I_AMOUNT,
                a.at, a.userId
            )

            WriteOp.UPDATE -> db.sALE_ORDER_D_CRUDQueries.update(
                b.I_PRD_ID, b.I_TAX_ID, b.I_UNIT_PRICE, b.I_QTY,
                b.I_DISCOUNT_AMT, b.I_AMOUNT,
                a.at, a.userId, b.I_SO_ID, b.I_INTERNAL_NO
            )

            WriteOp.DELETE -> db.sALE_ORDER_D_CRUDQueries.delete(
                b.I_SO_ID, b.I_INTERNAL_NO
            )
        }
    }, this)
}

class SaleOrderDBuilder {
    var I_SO_ID: String        = ""
    var I_INTERNAL_NO: String  = ""
    var I_PRD_ID: String       = ""
    var I_TAX_ID: String       = ""
    var I_UNIT_PRICE: Double   = 0.0
    var I_QTY: Double          = 0.0
    var I_DISCOUNT_AMT: Double = 0.0
    var I_AMOUNT: Double       = 0.0
}