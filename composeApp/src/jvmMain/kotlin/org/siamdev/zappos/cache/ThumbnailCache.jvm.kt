/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.cache

import java.io.File

/**
 * Desktop thumbnail store.
 *
 * Root: ~/.zappos/thumbnails/
 *   products/
 *     coffee/
 *       a3f5c2b8d1e4f9a2.jpg
 *       .index               ← "1=a3f5c2b8d1e4f9a2\n2=…"
 *     matcha/ …
 *   services/ …
 */
class DesktopThumbnailCache : ThumbnailCache {

    private val root = File(System.getProperty("user.home"), ".zappos${File.separator}thumbnails")

    // In-memory index: "section/category/id" → hash
    private val memIndex = HashMap<String, String>()

    private fun dir(section: ThumbnailSection, category: String): File =
        File(root, "${section.dirName}${File.separator}$category")

    private fun imageFile(section: ThumbnailSection, category: String, hash: String): File =
        File(dir(section, category), "$hash.jpg")

    private fun indexFile(section: ThumbnailSection, category: String): File =
        File(dir(section, category), ".index")

    private fun memKey(section: ThumbnailSection, category: String, id: String) =
        "${section.dirName}/$category/$id"

    private fun loadIndex(section: ThumbnailSection, category: String) {
        val f = indexFile(section, category)
        if (!f.exists()) return
        f.readLines().forEach { line ->
            val eq = line.indexOf('=')
            if (eq > 0) memIndex[memKey(section, category, line.substring(0, eq))] = line.substring(eq + 1)
        }
    }

    private fun flushIndex(section: ThumbnailSection, category: String) {
        val prefix = "${section.dirName}/$category/"
        val content = memIndex.entries
            .filter  { it.key.startsWith(prefix) }
            .joinToString("\n") { "${it.key.removePrefix(prefix)}=${it.value}" }
        val f = indexFile(section, category)
        f.parentFile?.mkdirs()
        f.writeText(content)
    }

    override fun exists(section: ThumbnailSection, category: String, hash: String): Boolean =
        imageFile(section, category, hash).exists()

    override fun write(section: ThumbnailSection, category: String, id: String, data: ByteArray): String {
        val hash = data.toThumbnailHash()
        val f = imageFile(section, category, hash)
        f.parentFile?.mkdirs()
        f.writeBytes(data)
        memIndex[memKey(section, category, id)] = hash
        flushIndex(section, category)
        return hash
    }

    override fun filePath(section: ThumbnailSection, category: String, hash: String): String =
        imageFile(section, category, hash).absolutePath

    override fun getHash(section: ThumbnailSection, category: String, id: String): String? {
        val key = memKey(section, category, id)
        return memIndex[key] ?: run {
            loadIndex(section, category)
            memIndex[key]
        }
    }
}