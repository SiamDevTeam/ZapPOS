/*
 * MIT License
 * Copyright (c) 2025 SiamDevTeam
 */
package org.siamdev.zappos.cache

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import org.siamdev.zappos.data.loadMenuItems
import org.siamdev.zappos.ui.screens.sale.MenuItem

private const val TAG = "ImagePreloader"

object ImagePreloader {

    private val httpClient by lazy { HttpClient() }

    suspend fun preloadMenuItems(
        section: ThumbnailSection = ThumbnailSection.PRODUCTS,
        onProgress: (Float) -> Unit = {}
    ) {
        val cache = thumbnailCache

        if (cache == null) {
            println("[$TAG] No thumbnail cache available")
            return
        }

        println("[$TAG] Starting preload : section=${section.dirName}")

        preloadAll(
            cache = cache,
            section = section,
            items = loadMenuItems(),
            onProgress = onProgress
        )
    }

    private suspend fun preloadAll(
        cache: ThumbnailCache,
        section: ThumbnailSection,
        items: List<MenuItem>,
        onProgress: (Float) -> Unit
    ) {
        val totalItems = items.size

        var downloadedCount = 0
        var skippedCount = 0
        var failedCount = 0

        println("[$TAG] Processing $totalItems items")

        items.forEachIndexed { index, item ->

            val category = sanitizeCategory(item.category)
            val itemId = item.id.toString()
            val label = "${section.dirName}/$category/$itemId"

            val existingHash = cache.getHash(
                section = section,
                category = category,
                id = itemId
            )

            val alreadyCached =
                existingHash != null &&
                        cache.exists(section, category, existingHash)

            if (alreadyCached) {
                println("[$TAG] SKIP  $label : already cached ($existingHash)")
                skippedCount++

            } else {

                println("[$TAG] FETCH $label : ${item.imageUrl}")

                runCatching {
                    val bytes: ByteArray =
                        httpClient.get(item.imageUrl).body()

                    val hash = cache.write(
                        section = section,
                        category = category,
                        id = itemId,
                        data = bytes
                    )

                    println("[$TAG] SAVED $label : hash=$hash (${bytes.size} bytes)")
                    downloadedCount++
                }.onFailure { error ->
                    println("[$TAG] ERROR $label : ${error.message}")
                    failedCount++
                }
            }

            onProgress((index + 1f) / totalItems)
        }
        

        println("""
            [$TAG] Done : downloaded=$downloadedCount,
                skipped=$skippedCount, failed=$failedCount
        """.trimIndent())
    }
}