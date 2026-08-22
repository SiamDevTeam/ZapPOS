/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.module.db.biz.dao.crud.table

import org.siamdev.module.db.TxStepBuilder
import org.siamdev.module.db.WriteOp
import org.siamdev.module.db.biz.dao.crud.BizTxScope

// M_PRODUCT_OPTION_GROUP
fun BizTxScope.ProductOptionGroup(block: ProductOptionGroupBuilder.() -> Unit): TxStepBuilder<Unit> {
    val builder = ProductOptionGroupBuilder().apply(block)
    val writeOp = currentDetailMode ?: ctx.writeOp
    val actor = ctx.actor
    return TxStepBuilder({
        when (writeOp) {
            WriteOp.INSERT -> db.m_PRODUCT_OPTION_GROUP_CRUDQueries.insert(
                builder.I_OPT_GRP_ID, builder.I_PRD_ID,
                builder.I_GRP_NAME, builder.I_PICK_MODE, builder.I_REQUIRED, builder.I_SORT_ORDER,
                actor.at, actor.userId
            )

            WriteOp.UPDATE -> db.m_PRODUCT_OPTION_GROUP_CRUDQueries.update(
                builder.I_GRP_NAME, builder.I_PICK_MODE, builder.I_REQUIRED, builder.I_SORT_ORDER,
                actor.at, actor.userId, builder.I_OPT_GRP_ID
            )

            WriteOp.DELETE -> db.m_PRODUCT_OPTION_GROUP_CRUDQueries.delete(builder.I_OPT_GRP_ID)
        }
    }, this)
}

class ProductOptionGroupBuilder {
    var I_OPT_GRP_ID: String = ""
    var I_PRD_ID: String = ""
    var I_GRP_NAME: String = ""
    var I_PICK_MODE: String = "01"
    var I_REQUIRED: Long = 0L
    var I_SORT_ORDER: Long = 0L
}