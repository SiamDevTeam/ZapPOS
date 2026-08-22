/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.module.db.biz.dao.crud.table

import org.siamdev.module.db.TxStepBuilder
import org.siamdev.module.db.WriteOp
import org.siamdev.module.db.biz.dao.crud.BizTxScope

// M_PRODUCT_SERVICE
fun BizTxScope.ProductService(block: ProductServiceBuilder.() -> Unit): TxStepBuilder<Unit> {
    val builder = ProductServiceBuilder().apply(block)
    val writeOp = ctx.writeOp
    val actor = ctx.actor
    return TxStepBuilder({
        when (writeOp) {
            WriteOp.INSERT -> db.m_PRODUCT_SERVICE_CRUDQueries.insert(
                builder.I_PRD_ID,
                builder.I_CHARGED_BY, builder.I_CAPACITY, builder.I_DURATION_MIN,
                builder.I_OPENS_HH, builder.I_OPENS_MM, builder.I_CLOSES_HH, builder.I_CLOSES_MM,
                builder.I_ACTIVE_DAYS, builder.I_INSTRUCTOR, builder.I_REQUIRES_BOOKING,
                actor.at, actor.userId
            )

            WriteOp.UPDATE -> db.m_PRODUCT_SERVICE_CRUDQueries.update(
                builder.I_CHARGED_BY, builder.I_CAPACITY, builder.I_DURATION_MIN,
                builder.I_OPENS_HH, builder.I_OPENS_MM, builder.I_CLOSES_HH, builder.I_CLOSES_MM,
                builder.I_ACTIVE_DAYS, builder.I_INSTRUCTOR, builder.I_REQUIRES_BOOKING,
                actor.at, actor.userId, builder.I_PRD_ID
            )

            WriteOp.DELETE -> db.m_PRODUCT_SERVICE_CRUDQueries.delete(builder.I_PRD_ID)
        }
    }, this)
}

class ProductServiceBuilder {
    var I_PRD_ID: String = ""
    var I_CHARGED_BY: String = "00"
    var I_CAPACITY: Long = 0L
    var I_DURATION_MIN: Long = 60L
    var I_OPENS_HH: Long = 9L
    var I_OPENS_MM: Long = 0L
    var I_CLOSES_HH: Long = 18L
    var I_CLOSES_MM: Long = 0L
    var I_ACTIVE_DAYS: String = ""
    var I_INSTRUCTOR: String? = null
    var I_REQUIRES_BOOKING: Long = 0L
}