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
    val builder = SaleOrderDBuilder().apply(block)
    val writeOp = currentDetailMode ?: ctx.writeOp
    val actor = ctx.actor
    return TxStepBuilder({
        when (writeOp) {
            WriteOp.INSERT -> db.sALE_ORDER_D_CRUDQueries.insert(
                builder.I_SO_ID, builder.I_INTERNAL_NO, builder.I_PRD_ID, builder.I_TAX_ID,
                builder.I_UNIT_PRICE, builder.I_QTY, builder.I_DISCOUNT_AMT, builder.I_AMOUNT,
                actor.at, actor.userId
            )

            WriteOp.UPDATE -> db.sALE_ORDER_D_CRUDQueries.update(
                builder.I_PRD_ID, builder.I_TAX_ID, builder.I_UNIT_PRICE, builder.I_QTY,
                builder.I_DISCOUNT_AMT, builder.I_AMOUNT,
                actor.at, actor.userId, builder.I_SO_ID, builder.I_INTERNAL_NO
            )

            WriteOp.DELETE -> db.sALE_ORDER_D_CRUDQueries.delete(
                builder.I_SO_ID, builder.I_INTERNAL_NO
            )
        }
    }, this)
}

class SaleOrderDBuilder {
    var I_SO_ID: String = ""
    var I_INTERNAL_NO: String = ""
    var I_PRD_ID: String = ""
    var I_TAX_ID: String = ""
    var I_UNIT_PRICE: Double = 0.0
    var I_QTY: Double = 0.0
    var I_DISCOUNT_AMT: Double = 0.0
    var I_AMOUNT: Double = 0.0
}