/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.data.source.local.mapper

import org.siamdev.module.db.biz.schema.M_PRODUCT
import org.siamdev.module.db.biz.schema.M_PRODUCT_INVENTORY
import org.siamdev.module.db.biz.schema.M_PRODUCT_MEDIA
import org.siamdev.module.db.biz.schema.M_PRODUCT_OPTION_GROUP
import org.siamdev.module.db.biz.schema.M_PRODUCT_OPTION_ITEM
import org.siamdev.module.db.biz.schema.M_PRODUCT_RENTAL
import org.siamdev.module.db.biz.schema.M_PRODUCT_SERVICE
import org.siamdev.zappos.data.source.local.model.InventoryConfig
import org.siamdev.zappos.data.source.local.model.MediaItem
import org.siamdev.zappos.data.source.local.model.OptionGroupModel
import org.siamdev.zappos.data.source.local.model.OptionItemModel
import org.siamdev.zappos.data.source.local.model.ProductModel
import org.siamdev.zappos.data.source.local.model.RentalConfig
import org.siamdev.zappos.data.source.local.model.ServiceConfig

internal fun M_PRODUCT.toModel(
    inventory: M_PRODUCT_INVENTORY? = null,
    service: M_PRODUCT_SERVICE? = null,
    rental: M_PRODUCT_RENTAL? = null,
    media: List<M_PRODUCT_MEDIA> = emptyList(),
    optionGroups: List<Pair<M_PRODUCT_OPTION_GROUP, List<M_PRODUCT_OPTION_ITEM>>> = emptyList(),
) = ProductModel(
    id = I_PRD_ID,
    kind = I_PRD_KIND ?: "1000",
    catId = I_CAT_ID ?: "",
    taxId = I_TAX_ID ?: "",
    name = I_PRD_NAME ?: "",
    description = I_PRD_DESC,
    unit = I_UNIT ?: "",
    price = I_PRICE ?: 0.0,
    costPrice = I_COST_PRICE,
    chargeVat = I_CHARGE_VAT == 1L,
    openPrice = I_OPEN_PRICE == 1L,
    sku = I_SKU,
    barcode = I_BARCODE,
    sendOrderTo = I_SEND_ORDER_TO,
    displayOrder = (I_DISPLAY_ORDER ?: 0L).toInt(),
    isAvailable = I_IS_AVAILABLE == 1L,
    isRecommended = I_IS_RECOMMENDED == 1L,
    isActive = I_IS_ACTIVE == 1L,
    inventory = inventory?.toModel(),
    service = service?.toModel(),
    rental = rental?.toModel(),
    media = media.map { it.toModel() },
    optionGroups = optionGroups.map { (group, items) -> group.toModel(items) },
)

internal fun M_PRODUCT_INVENTORY.toModel() = InventoryConfig(
    trackStock = I_TRACK_STOCK == 1L,
    maxCapacity = I_MAX_CAPACITY ?: 0.0,
    lowStockAlert = I_LOW_STOCK_ALERT ?: 0.0,
    supplierName = I_SUPPLIER_NAME,
    trackExpiry = I_TRACK_EXPIRY == 1L,
)

internal fun M_PRODUCT_SERVICE.toModel() = ServiceConfig(
    chargedBy = I_CHARGED_BY ?: "02",
    capacity = (I_CAPACITY ?: 12L).toInt(),
    durationMin = (I_DURATION_MIN ?: 60L).toInt(),
    opensHh = (I_OPENS_HH ?: 9L).toInt(),
    opensMm = (I_OPENS_MM ?: 0L).toInt(),
    closesHh = (I_CLOSES_HH ?: 18L).toInt(),
    closesMm = (I_CLOSES_MM ?: 0L).toInt(),
    activeDays = I_ACTIVE_DAYS
        ?.split(",")
        ?.mapNotNull { it.trim().toIntOrNull() }
        ?.toSet()
        ?: setOf(0, 1, 2, 3, 4),
    instructor = I_INSTRUCTOR,
    requiresBooking = I_REQUIRES_BOOKING == 1L,
)

internal fun M_PRODUCT_RENTAL.toModel() = RentalConfig(
    unitsCount = (I_UNITS_COUNT ?: 2L).toInt(),
    slotDurationMin = (I_SLOT_DURATION_MIN ?: 60L).toInt(),
    minBooking = (I_MIN_BOOKING ?: 1L).toInt(),
    bufferMin = (I_BUFFER_MIN ?: 0L).toInt(),
    opensHh = (I_OPENS_HH ?: 8L).toInt(),
    opensMm = (I_OPENS_MM ?: 0L).toInt(),
    closesHh = (I_CLOSES_HH ?: 22L).toInt(),
    closesMm = (I_CLOSES_MM ?: 0L).toInt(),
    depositAmt = I_DEPOSIT_AMT ?: 0.0,
    requiresBooking = I_REQUIRES_BOOKING == 1L,
)

internal fun M_PRODUCT_MEDIA.toModel() = MediaItem(
    id = I_MEDIA_ID,
    mediaType = I_MEDIA_TYPE ?: "01",
    mediaUrl = I_MEDIA_URL,
    localHash = I_LOCAL_HASH,
    sortOrder = (I_SORT_ORDER ?: 0L).toInt(),
    isActive = I_IS_ACTIVE == 1L,
)

internal fun M_PRODUCT_OPTION_GROUP.toModel(items: List<M_PRODUCT_OPTION_ITEM>) = OptionGroupModel(
    id = I_OPT_GRP_ID,
    name = I_GRP_NAME ?: "",
    pickMode = I_PICK_MODE ?: "01",
    required = I_REQUIRED == 1L,
    sortOrder = (I_SORT_ORDER ?: 0L).toInt(),
    items = items.map { it.toModel() },
)

internal fun M_PRODUCT_OPTION_ITEM.toModel() = OptionItemModel(
    id = I_OPT_ITEM_ID,
    name = I_ITEM_NAME ?: "",
    priceModifier = I_PRICE_MODIFIER ?: 0.0,
    sortOrder = (I_SORT_ORDER ?: 0L).toInt(),
)