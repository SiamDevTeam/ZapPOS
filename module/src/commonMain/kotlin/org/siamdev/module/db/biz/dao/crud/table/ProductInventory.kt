/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.module.db.biz.dao.crud.table

import org.siamdev.module.db.TxStepBuilder
import org.siamdev.module.db.WriteOp
import org.siamdev.module.db.biz.dao.crud.BizTxScope

// M_PRODUCT_INVENTORY
fun BizTxScope.ProductInventory(block: ProductInventoryBuilder.() -> Unit): TxStepBuilder<Unit> {
    val builder = ProductInventoryBuilder().apply(block)
    val writeOp = ctx.writeOp
    val actor = ctx.actor
    return TxStepBuilder({
        when (writeOp) {
            WriteOp.INSERT -> db.m_PRODUCT_INVENTORY_CRUDQueries.insert(
                builder.I_PRD_ID,
                builder.I_TRACK_STOCK, builder.I_MAX_CAPACITY, builder.I_LOW_STOCK_ALERT,
                builder.I_SUPPLIER_NAME, builder.I_TRACK_EXPIRY,
                actor.at, actor.userId
            )

            WriteOp.UPDATE -> db.m_PRODUCT_INVENTORY_CRUDQueries.update(
                builder.I_TRACK_STOCK, builder.I_MAX_CAPACITY, builder.I_LOW_STOCK_ALERT,
                builder.I_SUPPLIER_NAME, builder.I_TRACK_EXPIRY,
                actor.at, actor.userId, builder.I_PRD_ID
            )

            WriteOp.DELETE -> db.m_PRODUCT_INVENTORY_CRUDQueries.delete(builder.I_PRD_ID)
        }
    }, this)
}

class ProductInventoryBuilder {
    var I_PRD_ID: String = ""
    var I_TRACK_STOCK: Long = 1L
    var I_MAX_CAPACITY: Double = 0.0
    var I_LOW_STOCK_ALERT: Double = 0.0
    var I_SUPPLIER_NAME: String? = null
    var I_TRACK_EXPIRY: Long = 0L
}