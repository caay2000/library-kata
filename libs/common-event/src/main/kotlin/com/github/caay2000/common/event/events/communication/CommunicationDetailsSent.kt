package com.github.caay2000.common.event.events.communication

import com.github.caay2000.common.event.api.DomainEvent
import java.time.LocalDateTime
import java.util.UUID

data class CommunicationDetailsSent(
    val communicationId: UUID,
    val sentTo: String,
    val requestDate: LocalDateTime,
    val templateId: String,
) : DomainEvent(communicationId)
