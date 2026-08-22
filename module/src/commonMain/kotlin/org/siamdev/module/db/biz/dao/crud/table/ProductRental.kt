/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.module.db.biz.dao.crud.table

import org.siamdev.module.db.TxStepBuilder
import org.siamdev.module.db.WriteOp
import org.siamdev.module.db.biz.dao.crud.BizTxScope

// M_PRODUCT_RENTAL
fun BizTxScope.ProductRental(block: ProductRentalBuilder.() -> Unit): TxStepBuilder<Unit> {
    val builder = ProductRentalBuilder().apply(block)
    val writeOp = ctx.writeOp
    val actor = ctx.actor
    return TxStepBuilder({
        when (writeOp) {
            WriteOp.INSERT -> db.m_PRODUCT_RENTAL_CRUDQueries.insert(
                builder.I_PRD_ID,
                builder.I_UNITS_COUNT, builder.I_SLOT_DURATION_MIN,
                builder.I_MIN_BOOKING, builder.I_BUFFER_MIN,
                builder.I_OPENS_HH, builder.I_OPENS_MM, builder.I_CLOSES_HH, builder.I_CLOSES_MM,
                builder.I_DEPOSIT_AMT, builder.I_REQUIRES_BOOKING,
                actor.at, actor.userId
            )

            WriteOp.UPDATE -> db.m_PRODUCT_RENTAL_CRUDQueries.update(
                builder.I_UNITS_COUNT, builder.I_SLOT_DURATION_MIN,
                builder.I_MIN_BOOKING, builder.I_BUFFER_MIN,
                builder.I_OPENS_HH, builder.I_OPENS_MM, builder.I_CLOSES_HH, builder.I_CLOSES_MM,
                builder.I_DEPOSIT_AMT, builder.I_REQUIRES_BOOKING,
                actor.at, actor.userId, builder.I_PRD_ID
            )

            WriteOp.DELETE -> db.m_PRODUCT_RENTAL_CRUDQueries.delete(builder.I_PRD_ID)
        }
    }, this)
}

class ProductRentalBuilder {
    var I_PRD_ID: String = ""
    var I_UNITS_COUNT: Long = 1L
    var I_SLOT_DURATION_MIN: Long = 60L
    var I_MIN_BOOKING: Long = 1L
    var I_BUFFER_MIN: Long = 0L
    var I_OPENS_HH: Long = 9L
    var I_OPENS_MM: Long = 0L
    var I_CLOSES_HH: Long = 18L
    var I_CLOSES_MM: Long = 0L
    var I_DEPOSIT_AMT: Double = 0.0
    var I_REQUIRES_BOOKING: Long = 0L
}