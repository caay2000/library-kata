package com.github.caay2000.common.event

import com.github.caay2000.eventbus.Event
import java.time.LocalDateTime
import java.util.UUID

abstract class DomainEvent(override val aggregateId: String) : Event {
    constructor(aggregateUUID: UUID) : this(aggregateUUID.toString())

    override val eventId: UUID = UUID.randomUUID()
    override val datetime: LocalDateTime = LocalDateTime.now()

    var correlationId: String? = null
}

fun DomainEvent.setCorrelationId(correlationId: String): DomainEvent =
    with(this) {
        this.correlationId = correlationId
        return this
    }
