/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.data.source

import kotlinx.serialization.json.*

object EventKind {
    const val PRODUCT = 1000
    const val SERVICE = 1001
    const val RENTAL = 1002
}

data class EventOption(val name: String, val priceModifier: Int = 0)

data class EventOptionGroup(
    val name: String,
    val required: Boolean = false,
    val multiSelect: Boolean = false,
    val items: List<EventOption> = emptyList(),
)

private fun JsonObject?.str(key: String, default: String = "") =
    this?.get(key)?.jsonPrimitive?.contentOrNull ?: default

private fun JsonObject?.dbl(key: String, default: Double = 0.0) =
    this?.get(key)?.jsonPrimitive?.doubleOrNull ?: default

private fun JsonObject?.int(key: String, default: Int = 0) =
    this?.get(key)?.jsonPrimitive?.intOrNull ?: default

private fun JsonObject?.bool(key: String, default: Boolean = false) =
    this?.get(key)?.jsonPrimitive?.booleanOrNull ?: default

private fun JsonObject?.intOrNull(key: String): Int? = this?.get(key)?.jsonPrimitive?.intOrNull

private fun JsonObject?.dblOrNull(key: String): Double? =
    this?.get(key)?.jsonPrimitive?.doubleOrNull

/**
 * Unified Nostr-inspired event entity.
 *
 * Create via the DSL factory:
 * ```
 * MasterEvent {
 *     id   = "PRD001"
 *     kind = EventKind.PRODUCT
 *     tag("goods")
 *     category("beverages")
 *     content { index ->
 *         index["item_info"] { name = "Latte"; price = 85.0; unit = "cup" }
 *         index["option_group"]("Size", required = true) {
 *             option("Small", -10); option("Medium", 0); option("Large", 15)
 *         }
 *     }
 *     isActive = true
 * }
 * ```
 */
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

    fun tag(key: String): String? = tags.firstOrNull { it.firstOrNull() == key }?.getOrNull(1)

    fun allTags(key: String): List<String> =
        tags.filter { it.firstOrNull() == key }.mapNotNull { it.getOrNull(1) }

    fun section(type: String): JsonObject? =
        content.asSequence().flatten().firstNotNullOfOrNull { it[type]?.jsonObject }

    fun sections(type: String): List<List<JsonObject>> =
        content.filter { it.firstOrNull()?.containsKey(type) == true }

    private val itemInfo: JsonObject?
        get() = section("item_info")

    val name: String
        get() = itemInfo.str("name")

    val description: String
        get() = itemInfo.str("description")

    val price: Double
        get() = itemInfo.dbl("price")

    val unit: String
        get() = itemInfo.str("unit")

    val stockQty: Int?
        get() = itemInfo.intOrNull("stockQty")

    val stockMax: Int?
        get() = itemInfo.intOrNull("stockMax")

    val lowStockAlert: Int?
        get() = itemInfo.intOrNull("lowStockAlert")

    val supplier: String
        get() = itemInfo.str("supplier")

    val chargeVat: Boolean
        get() = itemInfo.bool("chargeVat", default = true)

    val costPrice: Double?
        get() = itemInfo.dblOrNull("costPrice")

    val openPrice: Boolean
        get() = itemInfo.bool("openPrice")

    val soldThisWeek: Int
        get() = itemInfo.int("soldThisWeek")

    val revenue7d: Double
        get() = itemInfo.dbl("revenue7d")

    val category: String
        get() = tag("category") ?: ""

    val subCategory: String?
        get() = tag("subcategory")

    val sku: String
        get() = tag("sku") ?: ""

    val isAvailable: Boolean
        get() = tag("available") != "false"

    val isRecommended: Boolean
        get() = tag("recommended") == "true"

    val imageUrl: String
        get() = tags.firstOrNull { it.firstOrNull() == "image" }?.getOrNull(2) ?: ""

    val optionGroups: List<EventOptionGroup>
        get() =
            sections("option_group").map { section ->
                val groupData = section.first()["option_group"]!!.jsonObject
                val options = section.drop(1).mapNotNull { it["option"]?.jsonObject }
                EventOptionGroup(
                    name = groupData.str("name"),
                    required = groupData.bool("required"),
                    multiSelect = groupData.bool("multi_select"),
                    items =
                        options.map { opt ->
                            EventOption(name = opt.str("name"), priceModifier = opt.int("modifier"))
                        },
                )
            }

    class ItemInfoBuilder {
        var name: String = ""
        var description: String = ""
        var price: Double = 0.0
        var unit: String = ""
        var stockQty: Int? = null
        var stockMax: Int? = null
        var lowStockAlert: Int? = null
        var supplier: String = ""
        var chargeVat: Boolean = true
        var costPrice: Double? = null
        var openPrice: Boolean = false
        var soldThisWeek: Int = 0
        var revenue7d: Double = 0.0

        internal fun build(): JsonObject = buildJsonObject {
            put("name", name)
            if (description.isNotEmpty()) put("description", description)
            put("price", price)
            if (unit.isNotEmpty()) put("unit", unit)
            stockQty?.let { put("stockQty", it) }
            stockMax?.let { put("stockMax", it) }
            lowStockAlert?.let { put("lowStockAlert", it) }
            if (supplier.isNotEmpty()) put("supplier", supplier)
            put("chargeVat", chargeVat)
            costPrice?.let { put("costPrice", it) }
            if (openPrice) put("openPrice", true)
            if (soldThisWeek != 0) put("soldThisWeek", soldThisWeek)
            if (revenue7d != 0.0) put("revenue7d", revenue7d)
        }
    }

    class OptionGroupBuilder {
        private val items = mutableListOf<JsonObject>()

        fun option(name: String, modifier: Int = 0) {
            items.add(
                buildJsonObject {
                    put(
                        "option",
                        buildJsonObject {
                            put("name", name)
                            put("modifier", modifier)
                        }
                    )
                }
            )
        }

        internal fun build(): List<JsonObject> = items.toList()
    }

    /**
     * Receiver of the `content { index -> }` lambda.
     *
     * ```
     * content { index ->
     *     index["item_info"] {
     *         name  = "Latte"
     *         price = 85.0
     *         unit  = "cup"
     *     }
     *     index["option_group"]("Size", required = true) {
     *         option("Small", -10)
     *         option("Medium",  0)
     *         option("Large",  15)
     *     }
     * }
     * ```
     */
    class ContentIndex {
        internal var hasItemInfo = false
        internal val itemInfo = ItemInfoBuilder()
        internal val optionGroups = mutableListOf<Pair<OGMeta, OptionGroupBuilder>>()

        operator fun get(key: String): SectionAccessor = SectionAccessor(key)

        inner class SectionAccessor(private val key: String) {

            /** `index["item_info"] { name = "…"; price = 85.0 }` */
            operator fun invoke(block: ItemInfoBuilder.() -> Unit) {
                check(key == "item_info") {
                    "'$key' does not take a plain block — use invoke(name, …) for option_group"
                }
                hasItemInfo = true
                itemInfo.apply(block)
            }

            /** `index["option_group"]("Size", required = true) { option("Small", -10) }` */
            operator fun invoke(
                name: String,
                required: Boolean = false,
                multiSelect: Boolean = false,
                block: OptionGroupBuilder.() -> Unit,
            ) {
                check(key == "option_group") { "'$key' does not accept group parameters" }
                optionGroups +=
                    OGMeta(name, required, multiSelect) to OptionGroupBuilder().apply(block)
            }
        }

        internal fun buildContent(): List<List<JsonObject>> = buildList {
            if (hasItemInfo) {
                add(listOf(buildJsonObject { put("item_info", itemInfo.build()) }))
            }
            optionGroups.forEach { (meta, og) ->
                add(
                    buildList {
                        add(
                            buildJsonObject {
                                put(
                                    "option_group",
                                    buildJsonObject {
                                        put("name", meta.name)
                                        put("required", meta.required)
                                        put("multi_select", meta.multiSelect)
                                    }
                                )
                            }
                        )
                        addAll(og.build())
                    }
                )
            }
        }
    }

    internal data class OGMeta(val name: String, val required: Boolean, val multiSelect: Boolean)

    class Builder {
        var id: String = ""
        var kind: Int = EventKind.PRODUCT
        var createdAt: Long = 0L
        var createdBy: Long = 0L
        var updatedAt: Long? = null
        var updatedBy: Long? = null
        var isActive: Boolean = true

        private val _tags = mutableListOf<List<String>>()
        private var _index: ContentIndex? = null

        // tag helpers
        fun tag(vararg values: String) {
            _tags.add(values.toList())
        }

        fun category(value: String) = tag("category", value)

        fun subCategory(value: String) = tag("subcategory", value)

        fun sku(value: String) = tag("sku", value)

        fun available(value: Boolean = true) = tag("available", value.toString())

        fun recommended(value: Boolean = true) = tag("recommended", value.toString())

        fun image(url: String, source: String = "remote") = tag("image", source, url)

        /** Define content sections. The [block] receives a [ContentIndex] as `index`. */
        fun content(block: (ContentIndex) -> Unit) {
            val index = ContentIndex()
            block(index)
            _index = index
        }

        internal fun build(): MasterEvent =
            MasterEvent(
                id = id,
                kind = kind,
                tags = _tags.toList(),
                content = _index?.buildContent() ?: emptyList(),
                createdAt = createdAt,
                createdBy = createdBy,
                updatedAt = updatedAt,
                updatedBy = updatedBy,
                isActive = isActive,
            )
    }

    companion object {
        operator fun invoke(block: Builder.() -> Unit): MasterEvent = Builder().apply(block).build()
    }
}

val List<MasterEvent>.products: List<MasterEvent>
    get() = filter {
        it.kind == EventKind.PRODUCT || it.kind == EventKind.SERVICE || it.kind == EventKind.RENTAL
    }
