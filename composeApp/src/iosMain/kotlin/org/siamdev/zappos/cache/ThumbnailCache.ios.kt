/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.cache

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.Foundation.NSCachesDirectory
import platform.Foundation.NSData
import platform.Foundation.NSFileManager
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.NSUserDomainMask
import platform.Foundation.create
import platform.Foundation.stringWithContentsOfFile

/**
 * iOS thumbnail store (NSCachesDirectory).
 *
 * Root: {NSCachesDirectory}/thumbnails/
 *   products/
 *     coffee/
 *       a3f5c2b8d1e4f9a2.jpg
 *       .index
 *     …
 *   services/ …
 */
@OptIn(ExperimentalForeignApi::class)
class IosThumbnailCache : ThumbnailCache {

    private val root: String by lazy {
        val paths = NSSearchPathForDirectoriesInDomains(NSCachesDirectory, NSUserDomainMask, true)
        val cachesDir = paths.firstOrNull() as? String
            ?: error("iOS caches directory unavailable")
        "$cachesDir/thumbnails"
    }

    private val memIndex = HashMap<String, String>()

    private fun dirPath(section: ThumbnailSection, category: String) =
        "$root/${section.dirName}/$category"

    private fun imagePath(section: ThumbnailSection, category: String, hash: String) =
        "${dirPath(section, category)}/$hash.jpg"

    private fun indexPath(section: ThumbnailSection, category: String) =
        "${dirPath(section, category)}/.index"

    private fun memKey(section: ThumbnailSection, category: String, id: String) =
        "${section.dirName}/$category/$id"

    private fun loadIndex(section: ThumbnailSection, category: String) {
        val content = NSString.stringWithContentsOfFile(
            indexPath(section, category), NSUTF8StringEncoding, null
        ) as? String ?: return
        content.lines().forEach { line ->
            val eq = line.indexOf('=')
            if (eq > 0) memIndex[memKey(section, category, line.substring(0, eq))] = line.substring(eq + 1)
        }
    }

    private fun flushIndex(section: ThumbnailSection, category: String) {
        val prefix = "${section.dirName}/$category/"
        val content = memIndex.entries
            .filter  { it.key.startsWith(prefix) }
            .joinToString("\n") { "${it.key.removePrefix(prefix)}=${it.value}" }
        val nsContent = NSString.create(string = content)
        nsContent.writeToFile(
            indexPath(section, category),
            atomically  = true,
            encoding    = NSUTF8StringEncoding,
            error       = null
        )
    }

    private fun mkdirs(path: String) {
        NSFileManager.defaultManager.createDirectoryAtPath(
            path                        = path,
            withIntermediateDirectories = true,
            attributes                  = null,
            error                       = null
        )
    }

    // ── ThumbnailCache ────────────────────────────────────────────────────────

    override fun exists(section: ThumbnailSection, category: String, hash: String): Boolean =
        NSFileManager.defaultManager.fileExistsAtPath(imagePath(section, category, hash))

    override fun write(section: ThumbnailSection, category: String, id: String, data: ByteArray): String {
        val hash = data.toThumbnailHash()
        mkdirs(dirPath(section, category))
        data.usePinned { pinned ->
            NSData.create(bytes = pinned.addressOf(0), length = data.size.toULong())
        }?.writeToFile(imagePath(section, category, hash), atomically = true)
        memIndex[memKey(section, category, id)] = hash
        flushIndex(section, category)
        return hash
    }

    override fun filePath(section: ThumbnailSection, category: String, hash: String): String =
        imagePath(section, category, hash)

    override fun getHash(section: ThumbnailSection, category: String, id: String): String? {
        val key = memKey(section, category, id)
        return memIndex[key] ?: run {
            loadIndex(section, category)
            memIndex[key]
        }
    }
}