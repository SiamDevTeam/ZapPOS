/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.data.source.local.contract

import org.siamdev.zappos.data.source.local.model.ProductModel

interface ProductSource {
    suspend fun getAll(): List<ProductModel>
    suspend fun getById(id: String): ProductModel?
    suspend fun save(model: ProductModel): Result<Unit>
    suspend fun setAvailability(id: String, available: Boolean): Result<Unit>
    suspend fun delete(id: String): Result<Unit>
}