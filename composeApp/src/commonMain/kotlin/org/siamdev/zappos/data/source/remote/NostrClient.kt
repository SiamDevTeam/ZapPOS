
package org.siamdev.zappos.data.source.remote

import rust.nostr.sdk.*
import org.siamdev.zappos.data.source.remote.ConnectionPool.getConnection
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

object NostrClient {

    class FilterBuilder {

        var ids: List<EventId>? = null
        var authors: List<PublicKey>? = null
        var kinds: List<Kind>? = null
        var limit: ULong? = null
        var hashtags: List<String>? = null

        fun build(): Filter {
            val f = Filter()

            ids?.let { f.ids(it) }
            authors?.let { f.authors(it) }
            kinds?.let { f.kinds(it) }
            limit?.let { f.limit(it) }
            hashtags?.let { f.hashtags(it) }

            return f
        }
    }

    fun Filter(block: FilterBuilder.() -> Unit): Filter {
        val builder = FilterBuilder()
        builder.block()
        return builder.build()
    }


    suspend fun fetch(
        filter: Filter,
        timeout: Duration = 3.seconds
    ): List<Event> {
        val client = getConnection()
        val events = client.fetchEvents(filter, timeout)
        return events.toVec()
    }

    suspend fun fetch(
        timeout: Duration = 3.seconds,
        block: FilterBuilder.() -> Unit,
    ): List<Event> {
        val filter = Filter(block)
        return fetch(filter, timeout)
    }
}

