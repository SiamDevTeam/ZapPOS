/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.cache

/**
 * Top-level split inside the thumbnails root.
 *
 * thumbnails/
 *   products/   ← menu / food items
 *     {category}/
 *       {hash}.jpg
 *       .index          ← id=hash mapping
 *   services/   ← service items (future use)
 *     {category}/
 *       {hash}.jpg
 *       .index
 */
enum class ThumbnailSection(val dirName: String) {
    PRODUCTS("products"),
    SERVICES("services")
}

interface ThumbnailCache {
    /** True when a file with this content hash already exists on disk. */
    fun exists(section: ThumbnailSection, category: String, hash: String): Boolean

    /**
     * Persist [data], compute its content hash, record it in the `.index`
     * file under [id], and return the hash.
     */
    fun write(section: ThumbnailSection, category: String, id: String, data: ByteArray): String

    /** Absolute path to the cached file (no scheme prefix). Convert with `.toPath()` for Coil3. */
    fun filePath(section: ThumbnailSection, category: String, hash: String): String

    /** Look up the content hash recorded for [id] (null if not yet cached). */
    fun getHash(section: ThumbnailSection, category: String, id: String): String?
}

var thumbnailCache: ThumbnailCache? = null

fun sanitizeCategory(raw: String): String =
    raw.ifBlank { "uncategorized" }
       .lowercase()
       .replace(Regex("[^a-z0-9_-]"), "_")

fun ByteArray.toThumbnailHash(): String {
    var h = -3750763034362895579L   // FNV-1a 64-bit offset basis
    for (b in this) {
        h = h xor (b.toLong() and 0xFF)
        h *= 1099511628211L          // FNV prime
    }
    val hi = ((h ushr 32) and 0xFFFFFFFFL).toString(16).padStart(8, '0')
    val lo = (h and 0xFFFFFFFFL).toString(16).padStart(8, '0')
    return hi + lo
}