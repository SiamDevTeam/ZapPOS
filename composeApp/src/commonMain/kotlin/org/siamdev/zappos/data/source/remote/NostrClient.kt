/*
 * MIT License
 *
 * Copyright (c) 2025 SiamDevTeam
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.siamdev.zappos.data.source.remote

import org.siamdev.zappos.data.source.remote.RelayConfig.getConnection
import rust.nostr.sdk.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

object NostrClient {


    class FilterBuilder {
        var ids: List<EventId>? = null
        var authors: List<PublicKey>? = null
        var kinds: List<Kind>? = null
        var limit: ULong? = null
        var hashtags: List<String>? = null

        var since: Timestamp? = null
        var until: Timestamp? = null
        var search: String? = null

        fun build(): Filter {
            var f = Filter()
            ids?.let { f = f.ids(it) }
            authors?.let { f = f.authors(it) }
            kinds?.let { f = f.kinds(it) }
            limit?.let { f = f.limit(it) }
            hashtags?.let { f = f.hashtags(it) }
            since?.let { f = f.since(it) }
            until?.let { f = f.until(it) }
            search?.let { f = f.search(it) }
            return f
        }
    }

    private fun buildFilter(block: FilterBuilder.() -> Unit): Filter {
        val builder = FilterBuilder()
        builder.apply(block)
        return builder.build()
    }

    suspend fun fetch(filter: Filter, timeout: Duration = 3.seconds): List<Event> {
        val client = getConnection()
        val events = client.fetchEvents(filter, timeout)
        return events.toVec()
    }

    suspend fun fetch(timeout: Duration = 3.seconds, block: FilterBuilder.() -> Unit): List<Event> {
        val filter = buildFilter(block)
        return fetch(filter, timeout)
    }


    suspend fun saveEvent(event: Event): SendEventOutput =
        getConnection().sendEvent(event)

}
