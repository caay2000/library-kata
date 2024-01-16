package com.github.caay2000.common.eventbus

import java.time.LocalDateTime
import java.util.UUID

interface Event {
    val aggregateId: String
    val eventId: UUID
    val type: String
        get() = this::class.java.simpleName

    val datetime: LocalDateTime
}
