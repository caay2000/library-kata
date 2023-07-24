package com.github.caay2000.common.event.api

import com.github.caay2000.eventbus.Event
import java.time.LocalDateTime
import java.util.UUID

abstract class DomainEvent(override val aggregateId: String) : Event {
    constructor(aggregateUUID: UUID) : this(aggregateUUID.toString())

    override val eventId: UUID = UUID.randomUUID()
    override val datetime: LocalDateTime = LocalDateTime.now()
}
