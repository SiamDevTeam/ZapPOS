/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.module.db.biz.dao.crud.table

import org.siamdev.module.db.TxStepBuilder
import org.siamdev.module.db.WriteOp
import org.siamdev.module.db.biz.dao.crud.BizTxScope

// M_PRODUCT_OPTION_ITEM
fun BizTxScope.ProductOptionItem(block: ProductOptionItemBuilder.() -> Unit): TxStepBuilder<Unit> {
    val builder = ProductOptionItemBuilder().apply(block)
    val writeOp = currentDetailMode ?: ctx.writeOp
    val actor = ctx.actor
    return TxStepBuilder({
        when (writeOp) {
            WriteOp.INSERT -> db.m_PRODUCT_OPTION_ITEM_CRUDQueries.insert(
                builder.I_OPT_ITEM_ID, builder.I_OPT_GRP_ID, builder.I_PRD_ID,
                builder.I_ITEM_NAME, builder.I_PRICE_MODIFIER, builder.I_SORT_ORDER,
                actor.at, actor.userId
            )

            WriteOp.UPDATE -> db.m_PRODUCT_OPTION_ITEM_CRUDQueries.update(
                builder.I_ITEM_NAME, builder.I_PRICE_MODIFIER, builder.I_SORT_ORDER,
                actor.at, actor.userId, builder.I_OPT_ITEM_ID
            )

            WriteOp.DELETE -> db.m_PRODUCT_OPTION_ITEM_CRUDQueries.delete(builder.I_OPT_ITEM_ID)
        }
    }, this)
}

class ProductOptionItemBuilder {
    var I_OPT_ITEM_ID: String = ""
    var I_OPT_GRP_ID: String = ""
    var I_PRD_ID: String = ""
    var I_ITEM_NAME: String = ""
    var I_PRICE_MODIFIER: Double = 0.0
    var I_SORT_ORDER: Long = 0L
}