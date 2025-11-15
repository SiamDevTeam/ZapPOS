package org.siamdev.core.nostr

expect sealed class NostrClientMessageEnum {

    class EventMsg : NostrClientMessageEnum {
        constructor(event: NostrEvent)
        val event: NostrEvent
    }

    class Req : NostrClientMessageEnum {
        constructor(subscriptionId: String, filter: NostrFilter)
        val subscriptionId: String
        val filter: NostrFilter
    }

    class ReqMultiFilter : NostrClientMessageEnum {
        constructor(subscriptionId: String, filters: List<NostrFilter>)
        val subscriptionId: String
        val filters: List<NostrFilter>
    }

    class Count : NostrClientMessageEnum {
        constructor(subscriptionId: String, filter: NostrFilter)
        val subscriptionId: String
        val filter: NostrFilter
    }

    class Close : NostrClientMessageEnum {
        constructor(subscriptionId: String)
        val subscriptionId: String
    }

    class Auth : NostrClientMessageEnum {
        constructor(event: NostrEvent)
        val event: NostrEvent
    }

    class NegOpen : NostrClientMessageEnum {
        constructor(subscriptionId: String, filter: NostrFilter, idSize: UByte?, initialMessage: String)
        val subscriptionId: String
        val filter: NostrFilter
        val idSize: UByte?
        val initialMessage: String
    }

    class NegMsg : NostrClientMessageEnum {
        constructor(subscriptionId: String, message: String)
        val subscriptionId: String
        val message: String
    }

    class NegClose : NostrClientMessageEnum {
        constructor(subscriptionId: String)
        val subscriptionId: String
    }
}