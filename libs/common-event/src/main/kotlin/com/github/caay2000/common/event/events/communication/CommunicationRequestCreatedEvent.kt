package com.github.caay2000.common.event.events.communication

import com.github.caay2000.common.event.api.DomainEvent
import java.time.LocalDateTime
import java.util.UUID

data class CommunicationRequestCreatedEvent(
    val communicationId: UUID,
    val accountNumber: String,
    val templateId: String,
    val requestedAt: LocalDateTime,
    val status: String,
) : DomainEvent(communicationId)
