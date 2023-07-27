package com.github.caay2000.common.event.events.communication

import com.github.caay2000.common.event.api.DomainEvent
import java.time.LocalDateTime
import java.util.UUID

data class CommunicationRequestSentEvent(
    val communicationId: UUID,
    val status: String,
    val sentAt: LocalDateTime,
) : DomainEvent(communicationId)
