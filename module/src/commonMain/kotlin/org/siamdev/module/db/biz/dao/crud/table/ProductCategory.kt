/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.module.db.biz.dao.crud.table

import org.siamdev.module.db.TxStepBuilder
import org.siamdev.module.db.WriteOp
import org.siamdev.module.db.biz.dao.crud.BizTxScope

// M_PRODUCT_CATEGORY
fun BizTxScope.ProductCategory(block: ProductCategoryBuilder.() -> Unit): TxStepBuilder<Unit> {
    val builder = ProductCategoryBuilder().apply(block)
    val writeOp = ctx.writeOp
    val actor = ctx.actor
    return TxStepBuilder({
        when (writeOp) {
            WriteOp.INSERT -> db.m_PRODUCT_CATEGORY_CRUDQueries.insert(
                builder.I_CAT_ID, builder.I_CAT_NAME, builder.I_SUBCAT_NAME, 1L,
                actor.at, actor.userId
            )

            WriteOp.UPDATE -> db.m_PRODUCT_CATEGORY_CRUDQueries.update(
                builder.I_CAT_NAME, builder.I_SUBCAT_NAME, builder.I_IS_ACTIVE,
                actor.at, actor.userId, builder.I_CAT_ID
            )

            WriteOp.DELETE -> db.m_PRODUCT_CATEGORY_CRUDQueries.delete(builder.I_CAT_ID)
        }
    }, this)
}

class ProductCategoryBuilder {
    var I_CAT_ID: String = ""
    var I_CAT_NAME: String = ""
    var I_SUBCAT_NAME: String? = null
    var I_IS_ACTIVE: Long = 1L
}