/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.data.source

import kotlinx.serialization.json.*

// ── Kind constants ────────────────────────────────────────────────────────────

object EventKind {
    const val PRODUCT = 1000
    const val SERVICE = 1001
    const val RENTAL  = 1002
}

// ── Value objects ─────────────────────────────────────────────────────────────

data class EventOption(val name: String, val priceModifier: Int = 0)

data class EventOptionGroup(
    val name: String,
    val required: Boolean = false,
    val multiSelect: Boolean = false,
    val items: List<EventOption> = emptyList(),
)

// ── Private JSON helpers ──────────────────────────────────────────────────────

private fun JsonObject?.str(key: String, default: String = "") =
    this?.get(key)?.jsonPrimitive?.contentOrNull ?: default

private fun JsonObject?.dbl(key: String, default: Double = 0.0) =
    this?.get(key)?.jsonPrimitive?.doubleOrNull ?: default

private fun JsonObject?.int(key: String, default: Int = 0) =
    this?.get(key)?.jsonPrimitive?.intOrNull ?: default

private fun JsonObject?.bool(key: String, default: Boolean = false) =
    this?.get(key)?.jsonPrimitive?.booleanOrNull ?: default

private fun JsonObject?.intOrNull(key: String): Int? =
    this?.get(key)?.jsonPrimitive?.intOrNull

private fun JsonObject?.dblOrNull(key: String): Double? =
    this?.get(key)?.jsonPrimitive?.doubleOrNull

// ── Data model ────────────────────────────────────────────────────────────────

data class MasterEvent(
    val id: String,
    val kind: Int,
    val tags: List<List<String>>,
    val content: List<List<JsonObject>>,
    val createdAt: Long = 0L,
    val createdBy: Long = 0L,
    val updatedAt: Long? = null,
    val updatedBy: Long? = null,
    val isActive: Boolean = true,
) {
    // ── Private query helpers ─────────────────────────────────────────────────

    private fun tag(key: String): String? =
        tags.firstOrNull { it.firstOrNull() == key }?.getOrNull(1)

    private fun section(type: String): JsonObject? =
        content.asSequence().flatten().firstNotNullOfOrNull { it[type]?.jsonObject }

    private fun sections(type: String): List<List<JsonObject>> =
        content.filter { it.firstOrNull()?.containsKey(type) == true }

    private val itemInfo: JsonObject? get() = section("item_info")

    // ── item_info properties ──────────────────────────────────────────────────

    val name: String           get() = itemInfo.str("name")
    val description: String    get() = itemInfo.str("description")
    val price: Double          get() = itemInfo.dbl("price")
    val unit: String           get() = itemInfo.str("unit")
    val stockQty: Int?         get() = itemInfo.intOrNull("stockQty")
    val stockMax: Int?         get() = itemInfo.intOrNull("stockMax")
    val lowStockAlert: Int?    get() = itemInfo.intOrNull("lowStockAlert")
    val supplier: String       get() = itemInfo.str("supplier")
    val chargeVat: Boolean     get() = itemInfo.bool("chargeVat", default = true)
    val costPrice: Double?     get() = itemInfo.dblOrNull("costPrice")
    val openPrice: Boolean     get() = itemInfo.bool("openPrice")
    val soldThisWeek: Int      get() = itemInfo.int("soldThisWeek")
    val revenue7d: Double      get() = itemInfo.dbl("revenue7d")

    // ── tag-derived properties ────────────────────────────────────────────────

    val category: String       get() = tag("category") ?: ""
    val subCategory: String?   get() = tag("subcategory")
    val sku: String            get() = tag("sku") ?: ""
    val isAvailable: Boolean   get() = tag("available") != "false"
    val isRecommended: Boolean get() = tag("recommended") == "true"
    val imageUrl: String       get() = tags.firstOrNull { it.firstOrNull() == "image" }?.getOrNull(2) ?: ""

    // ── option groups ─────────────────────────────────────────────────────────

    val optionGroups: List<EventOptionGroup>
        get() = sections("option_group").map { section ->
            val groupData = section.first()["option_group"]!!.jsonObject
            val options   = section.drop(1).mapNotNull { it["option"]?.jsonObject }
            EventOptionGroup(
                name        = groupData.str("name"),
                required    = groupData.bool("required"),
                multiSelect = groupData.bool("multi_select"),
                items       = options.map { opt ->
                    EventOption(name = opt.str("name"), priceModifier = opt.int("modifier"))
                },
            )
        }
}

// ── Minimal builder ───────────────────────────────────────────────────────────

class MasterEventBuilder {
    var id: String = ""
    var kind: Int = EventKind.PRODUCT
    val tags: MutableList<List<String>> = mutableListOf()
    val content: MutableList<List<JsonObject>> = mutableListOf()
    var createdAt: Long = 0L
    var createdBy: Long = 0L
    var updatedAt: Long? = null
    var updatedBy: Long? = null
    var isActive: Boolean = true

    fun build(): MasterEvent = MasterEvent(
        id        = id,
        kind      = kind,
        tags      = tags.toList(),
        content   = content.toList(),
        createdAt = createdAt,
        createdBy = createdBy,
        updatedAt = updatedAt,
        updatedBy = updatedBy,
        isActive  = isActive,
    )
}

fun masterEvent(block: MasterEventBuilder.() -> Unit): MasterEvent =
    MasterEventBuilder().apply(block).build()

// ── List filter ───────────────────────────────────────────────────────────────

val List<MasterEvent>.products: List<MasterEvent>
    get() = filter { it.kind == EventKind.PRODUCT || it.kind == EventKind.SERVICE || it.kind == EventKind.RENTAL }