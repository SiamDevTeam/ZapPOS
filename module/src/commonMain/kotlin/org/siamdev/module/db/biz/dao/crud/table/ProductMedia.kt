/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.module.db.biz.dao.crud.table

import org.siamdev.module.db.TxStepBuilder
import org.siamdev.module.db.WriteOp
import org.siamdev.module.db.biz.dao.crud.BizTxScope

// M_PRODUCT_MEDIA
fun BizTxScope.ProductMedia(block: ProductMediaBuilder.() -> Unit): TxStepBuilder<Unit> {
    val builder = ProductMediaBuilder().apply(block)
    val writeOp = currentDetailMode ?: ctx.writeOp
    val actor = ctx.actor
    return TxStepBuilder({
        when (writeOp) {
            WriteOp.INSERT -> db.m_PRODUCT_MEDIA_CRUDQueries.insert(
                builder.I_MEDIA_ID, builder.I_PRD_ID,
                builder.I_MEDIA_TYPE, builder.I_MEDIA_URL, builder.I_LOCAL_HASH,
                builder.I_SORT_ORDER,
                actor.at, actor.userId
            )

            WriteOp.UPDATE -> db.m_PRODUCT_MEDIA_CRUDQueries.updateSortOrder(
                builder.I_SORT_ORDER, actor.at, actor.userId, builder.I_MEDIA_ID
            )

            WriteOp.DELETE -> db.m_PRODUCT_MEDIA_CRUDQueries.delete(builder.I_MEDIA_ID)
        }
    }, this)
}

class ProductMediaBuilder {
    var I_MEDIA_ID: String = ""
    var I_PRD_ID: String = ""
    var I_MEDIA_TYPE: String = "01"
    var I_MEDIA_URL: String = ""
    var I_LOCAL_HASH: String? = null
    var I_SORT_ORDER: Long = 0L
}