/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.module.db.biz.dao.crud.table

import org.siamdev.module.db.biz.dao.crud.BizTxScope
import org.siamdev.module.db.TxStepBuilder
import org.siamdev.module.db.WriteOp

// T_SALE_ORDER_H
fun BizTxScope.SaleOrderH(block: SaleOrderHBuilder.() -> Unit): TxStepBuilder<Unit> {
    val b    = SaleOrderHBuilder().apply(block)
    val mode = ctx.writeOp
    val a    = ctx.actor
    return TxStepBuilder({
        when (mode) {
            WriteOp.INSERT -> db.sALE_ORDER_H_CRUDQueries.insert(
                b.I_SO_ID, b.I_CUS_ID, b.I_CURRENCY_CODE,
                a.at, b.I_STATUS, b.I_REMARK,
                a.at, a.userId
            )

            WriteOp.UPDATE -> db.sALE_ORDER_H_CRUDQueries.update(
                b.I_CUS_ID, b.I_CURRENCY_CODE, b.I_STATUS, b.I_REMARK,
                a.at, a.userId, b.I_SO_ID
            )

            WriteOp.DELETE -> db.sALE_ORDER_H_CRUDQueries.delete(b.I_SO_ID)
        }
    }, this)
}

class SaleOrderHBuilder {
    var I_SO_ID: String         = ""
    var I_CUS_ID: String?       = null
    var I_CURRENCY_CODE: String = ""
    var I_STATUS: String        = "01"
    var I_REMARK: String?       = null
}