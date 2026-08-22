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
    val builder = SaleOrderHBuilder().apply(block)
    val writeOp = ctx.writeOp
    val actor = ctx.actor
    return TxStepBuilder({
        when (writeOp) {
            WriteOp.INSERT -> db.sALE_ORDER_H_CRUDQueries.insert(
                builder.I_SO_ID, builder.I_CUS_ID, builder.I_CURRENCY_CODE,
                actor.at, builder.I_STATUS, builder.I_REMARK,
                actor.at, actor.userId
            )

            WriteOp.UPDATE -> db.sALE_ORDER_H_CRUDQueries.update(
                builder.I_CUS_ID, builder.I_CURRENCY_CODE, builder.I_STATUS, builder.I_REMARK,
                actor.at, actor.userId, builder.I_SO_ID
            )

            WriteOp.DELETE -> db.sALE_ORDER_H_CRUDQueries.delete(builder.I_SO_ID)
        }
    }, this)
}

class SaleOrderHBuilder {
    var I_SO_ID: String = ""
    var I_CUS_ID: String? = null
    var I_CURRENCY_CODE: String = ""
    var I_STATUS: String = "01"
    var I_REMARK: String? = null
}