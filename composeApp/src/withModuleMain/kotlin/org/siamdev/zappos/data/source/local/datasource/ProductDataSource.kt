/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.data.source.local.datasource

import kotlin.random.Random
import org.siamdev.module.db.AppDatabase
import org.siamdev.module.db.biz.ZapPOSBiz
import org.siamdev.zappos.data.source.local.contract.ProductSource
import org.siamdev.zappos.data.source.local.mapper.toModel
import org.siamdev.zappos.data.source.local.model.InventoryConfig
import org.siamdev.zappos.data.source.local.model.OptionGroupModel
import org.siamdev.zappos.data.source.local.model.ProductModel
import org.siamdev.zappos.data.source.local.model.RentalConfig
import org.siamdev.zappos.data.source.local.model.ServiceConfig
import org.siamdev.zappos.utils.DateTimeUtils

class ProductDataSource(private val db: AppDatabase) : ProductSource {

    override suspend fun getAll(): List<ProductModel> =
        db.biz { m_PRODUCT_CRUDQueries.selectAll().executeAsList() }
            .map { it.toModel() }

    override suspend fun getById(id: String): ProductModel? {
        val row = db.biz { m_PRODUCT_CRUDQueries.selectById(id).executeAsOneOrNull() }
            ?: return null

        val inventory =
            db.biz { m_PRODUCT_INVENTORY_CRUDQueries.selectById(id).executeAsOneOrNull() }
        val service = db.biz { m_PRODUCT_SERVICE_CRUDQueries.selectById(id).executeAsOneOrNull() }
        val rental = db.biz { m_PRODUCT_RENTAL_CRUDQueries.selectById(id).executeAsOneOrNull() }
        val media = db.biz { m_PRODUCT_MEDIA_CRUDQueries.selectByProduct(id).executeAsList() }

        val groups =
            db.biz { m_PRODUCT_OPTION_GROUP_CRUDQueries.selectByProduct(id).executeAsList() }
        val groupsWithItems = groups.map { group ->
            val items = db.biz {
                m_PRODUCT_OPTION_ITEM_CRUDQueries.selectByGroup(group.I_OPT_GRP_ID).executeAsList()
            }
            group to items
        }

        return row.toModel(
            inventory = inventory,
            service = service,
            rental = rental,
            media = media,
            optionGroups = groupsWithItems,
        )
    }

    override suspend fun save(model: ProductModel): Result<Unit> = runCatching {
        val now = DateTimeUtils.nowEpochMillis()
        db.biz {
            transactionWithResult {
                upsertProduct(model, now)
                model.inventory?.let { upsertInventory(model.id, it, now) }
                model.service?.let { upsertService(model.id, it, now) }
                model.rental?.let { upsertRental(model.id, it, now) }
                syncOptionGroups(model.id, model.optionGroups, now)
            }
        }
    }

    override suspend fun setAvailability(id: String, available: Boolean): Result<Unit> =
        runCatching {
            db.biz {
                m_PRODUCT_CRUDQueries.setAvailability(
                    isAvailable = if (available) 1L else 0L,
                    updatedAt = DateTimeUtils.nowEpochMillis(),
                    updatedBy = "USER",
                    prdId = id,
                )
            }
        }

    override suspend fun delete(id: String): Result<Unit> = runCatching {
        db.biz {
            m_PRODUCT_CRUDQueries.deactivate(
                updatedAt = DateTimeUtils.nowEpochMillis(),
                updatedBy = "USER",
                prdId = id,
            )
        }
    }

    private suspend fun ZapPOSBiz.upsertProduct(model: ProductModel, now: Long) {
        val exists = m_PRODUCT_CRUDQueries.selectById(model.id).executeAsOneOrNull() != null
        if (exists) {
            m_PRODUCT_CRUDQueries.update(
                catId = model.catId.ifBlank { null },
                taxId = model.taxId.ifBlank { null },
                prdName = model.name,
                prdDesc = model.description,
                unit = model.unit,
                price = model.price,
                costPrice = model.costPrice,
                chargeVat = if (model.chargeVat) 1L else 0L,
                openPrice = if (model.openPrice) 1L else 0L,
                sku = model.sku,
                barcode = model.barcode,
                sendOrderTo = model.sendOrderTo,
                displayOrder = model.displayOrder.toLong(),
                isAvailable = if (model.isAvailable) 1L else 0L,
                isRecommended = if (model.isRecommended) 1L else 0L,
                isActive = if (model.isActive) 1L else 0L,
                updatedAt = now,
                updatedBy = "USER",
                prdId = model.id,
            )
        } else {
            m_PRODUCT_CRUDQueries.insert(
                prdId = model.id,
                catId = model.catId.ifBlank { null },
                taxId = model.taxId.ifBlank { null },
                prdKind = model.kind,
                prdName = model.name,
                prdDesc = model.description,
                unit = model.unit,
                price = model.price,
                costPrice = model.costPrice,
                chargeVat = if (model.chargeVat) 1L else 0L,
                openPrice = if (model.openPrice) 1L else 0L,
                sku = model.sku,
                barcode = model.barcode,
                sendOrderTo = model.sendOrderTo,
                displayOrder = model.displayOrder.toLong(),
                isAvailable = if (model.isAvailable) 1L else 0L,
                isRecommended = if (model.isRecommended) 1L else 0L,
                isActive = 1L,
                createdAt = now,
                createdBy = "USER",
            )
        }
    }

    private suspend fun ZapPOSBiz.upsertInventory(
        prdId: String,
        inventory: InventoryConfig,
        now: Long
    ) {
        val exists = m_PRODUCT_INVENTORY_CRUDQueries.selectById(prdId).executeAsOneOrNull() != null
        if (exists) {
            m_PRODUCT_INVENTORY_CRUDQueries.update(
                trackStock = if (inventory.trackStock) 1L else 0L,
                maxCapacity = inventory.maxCapacity,
                lowStockAlert = inventory.lowStockAlert,
                supplierName = inventory.supplierName,
                trackExpiry = if (inventory.trackExpiry) 1L else 0L,
                updatedAt = now,
                updatedBy = "USER",
                prdId = prdId,
            )
        } else {
            m_PRODUCT_INVENTORY_CRUDQueries.insert(
                prdId = prdId,
                trackStock = if (inventory.trackStock) 1L else 0L,
                maxCapacity = inventory.maxCapacity,
                lowStockAlert = inventory.lowStockAlert,
                supplierName = inventory.supplierName,
                trackExpiry = if (inventory.trackExpiry) 1L else 0L,
                createdAt = now,
                createdBy = "USER",
            )
        }
    }

    private suspend fun ZapPOSBiz.upsertService(prdId: String, service: ServiceConfig, now: Long) {
        val activeDays = service.activeDays.sorted().joinToString(",")
        val exists = m_PRODUCT_SERVICE_CRUDQueries.selectById(prdId).executeAsOneOrNull() != null
        if (exists) {
            m_PRODUCT_SERVICE_CRUDQueries.update(
                chargedBy = service.chargedBy,
                capacity = service.capacity.toLong(),
                durationMin = service.durationMin.toLong(),
                opensHh = service.opensHh.toLong(),
                opensMm = service.opensMm.toLong(),
                closesHh = service.closesHh.toLong(),
                closesMm = service.closesMm.toLong(),
                activeDays = activeDays,
                instructor = service.instructor,
                requiresBooking = if (service.requiresBooking) 1L else 0L,
                updatedAt = now,
                updatedBy = "USER",
                prdId = prdId,
            )
        } else {
            m_PRODUCT_SERVICE_CRUDQueries.insert(
                prdId = prdId,
                chargedBy = service.chargedBy,
                capacity = service.capacity.toLong(),
                durationMin = service.durationMin.toLong(),
                opensHh = service.opensHh.toLong(),
                opensMm = service.opensMm.toLong(),
                closesHh = service.closesHh.toLong(),
                closesMm = service.closesMm.toLong(),
                activeDays = activeDays,
                instructor = service.instructor,
                requiresBooking = if (service.requiresBooking) 1L else 0L,
                createdAt = now,
                createdBy = "USER",
            )
        }
    }

    private suspend fun ZapPOSBiz.upsertRental(prdId: String, rental: RentalConfig, now: Long) {
        val exists = m_PRODUCT_RENTAL_CRUDQueries.selectById(prdId).executeAsOneOrNull() != null
        if (exists) {
            m_PRODUCT_RENTAL_CRUDQueries.update(
                unitsCount = rental.unitsCount.toLong(),
                slotDurationMin = rental.slotDurationMin.toLong(),
                minBooking = rental.minBooking.toLong(),
                bufferMin = rental.bufferMin.toLong(),
                opensHh = rental.opensHh.toLong(),
                opensMm = rental.opensMm.toLong(),
                closesHh = rental.closesHh.toLong(),
                closesMm = rental.closesMm.toLong(),
                depositAmt = rental.depositAmt,
                requiresBooking = if (rental.requiresBooking) 1L else 0L,
                updatedAt = now,
                updatedBy = "USER",
                prdId = prdId,
            )
        } else {
            m_PRODUCT_RENTAL_CRUDQueries.insert(
                prdId = prdId,
                unitsCount = rental.unitsCount.toLong(),
                slotDurationMin = rental.slotDurationMin.toLong(),
                minBooking = rental.minBooking.toLong(),
                bufferMin = rental.bufferMin.toLong(),
                opensHh = rental.opensHh.toLong(),
                opensMm = rental.opensMm.toLong(),
                closesHh = rental.closesHh.toLong(),
                closesMm = rental.closesMm.toLong(),
                depositAmt = rental.depositAmt,
                requiresBooking = if (rental.requiresBooking) 1L else 0L,
                createdAt = now,
                createdBy = "USER",
            )
        }
    }

    private suspend fun ZapPOSBiz.syncOptionGroups(
        prdId: String,
        groups: List<OptionGroupModel>,
        now: Long
    ) {
        val existingGroupIds = m_PRODUCT_OPTION_GROUP_CRUDQueries
            .selectByProduct(prdId)
            .executeAsList()
            .map { it.I_OPT_GRP_ID }
            .toSet()

        val incomingIds = groups.map { it.id }.toSet()

        existingGroupIds.subtract(incomingIds).forEach { removedId ->
            m_PRODUCT_OPTION_ITEM_CRUDQueries.deleteByGroup(removedId)
            m_PRODUCT_OPTION_GROUP_CRUDQueries.delete(removedId)
        }

        groups.forEachIndexed { index, group ->
            if (group.id in existingGroupIds) {
                m_PRODUCT_OPTION_GROUP_CRUDQueries.update(
                    grpName = group.name,
                    pickMode = group.pickMode,
                    required = if (group.required) 1L else 0L,
                    sortOrder = index.toLong(),
                    updatedAt = now,
                    updatedBy = "USER",
                    optGrpId = group.id,
                )
            } else {
                m_PRODUCT_OPTION_GROUP_CRUDQueries.insert(
                    optGrpId = group.id,
                    prdId = prdId,
                    grpName = group.name,
                    pickMode = group.pickMode,
                    required = if (group.required) 1L else 0L,
                    sortOrder = index.toLong(),
                    createdAt = now,
                    createdBy = "USER",
                )
            }
            syncOptionItems(group, prdId, now)
        }
    }

    private suspend fun ZapPOSBiz.syncOptionItems(
        group: OptionGroupModel,
        prdId: String,
        now: Long
    ) {
        val existingItemIds = m_PRODUCT_OPTION_ITEM_CRUDQueries
            .selectByGroup(group.id)
            .executeAsList()
            .map { it.I_OPT_ITEM_ID }
            .toSet()

        val incomingIds = group.items.map { it.id }.toSet()

        existingItemIds.subtract(incomingIds).forEach { removedId ->
            m_PRODUCT_OPTION_ITEM_CRUDQueries.delete(removedId)
        }

        group.items.forEachIndexed { index, item ->
            if (item.id in existingItemIds) {
                m_PRODUCT_OPTION_ITEM_CRUDQueries.update(
                    itemName = item.name,
                    priceModifier = item.priceModifier,
                    sortOrder = index.toLong(),
                    updatedAt = now,
                    updatedBy = "USER",
                    optItemId = item.id,
                )
            } else {
                m_PRODUCT_OPTION_ITEM_CRUDQueries.insert(
                    optItemId = item.id,
                    optGrpId = group.id,
                    prdId = prdId,
                    itemName = item.name,
                    priceModifier = item.priceModifier,
                    sortOrder = index.toLong(),
                    createdAt = now,
                    createdBy = "USER",
                )
            }
        }
    }
}

fun newProductId(): String = newLocalId("prd")
fun newGroupId(): String = newLocalId("grp")
fun newItemId(): String = newLocalId("itm")

private fun newLocalId(prefix: String): String {
    val random = Random.Default.nextInt(100_000, 999_999)
    return "$prefix-${DateTimeUtils.nowEpochMillis()}-$random"
}