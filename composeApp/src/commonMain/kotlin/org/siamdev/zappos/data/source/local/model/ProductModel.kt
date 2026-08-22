/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.data.source.local.model

data class ProductModel(
    val id: String,
    val kind: String,           // "1000"=GOODS  "1001"=SERVICE  "1002"=RENTAL
    val catId: String,
    val taxId: String,
    val name: String,
    val description: String?,
    val unit: String,
    val price: Double,
    val costPrice: Double?,
    val chargeVat: Boolean,
    val openPrice: Boolean,
    val sku: String?,
    val barcode: String?,
    val sendOrderTo: String?,
    val displayOrder: Int,
    val isAvailable: Boolean,
    val isRecommended: Boolean,
    val isActive: Boolean,
    val inventory: InventoryConfig?,
    val service: ServiceConfig?,
    val rental: RentalConfig?,
    val media: List<MediaItem>,
    val optionGroups: List<OptionGroupModel>,
)

data class InventoryConfig(
    val trackStock: Boolean,
    val maxCapacity: Double,
    val lowStockAlert: Double,
    val supplierName: String?,
    val trackExpiry: Boolean,
)

data class ServiceConfig(
    val chargedBy: String,          // "00"=per person  "01"=per session  "02"=per hour
    val capacity: Int,
    val durationMin: Int,
    val opensHh: Int,
    val opensMm: Int,
    val closesHh: Int,
    val closesMm: Int,
    val activeDays: Set<Int>,       // 0=Mon … 6=Sun
    val instructor: String?,
    val requiresBooking: Boolean,
)

data class RentalConfig(
    val unitsCount: Int,
    val slotDurationMin: Int,
    val minBooking: Int,
    val bufferMin: Int,
    val opensHh: Int,
    val opensMm: Int,
    val closesHh: Int,
    val closesMm: Int,
    val depositAmt: Double,
    val requiresBooking: Boolean,
)

data class MediaItem(
    val id: String,
    val mediaType: String,          // "01"=remote URL  "02"=local cache
    val mediaUrl: String?,
    val localHash: String?,
    val sortOrder: Int,
    val isActive: Boolean,
)

data class OptionGroupModel(
    val id: String,
    val name: String,
    val pickMode: String,           // "01"=single select  "02"=multi select
    val required: Boolean,
    val sortOrder: Int,
    val items: List<OptionItemModel>,
)

data class OptionItemModel(
    val id: String,
    val name: String,
    val priceModifier: Double,
    val sortOrder: Int,
)