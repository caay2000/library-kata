package com.github.caay2000.common.event.events.communication

import com.github.caay2000.common.event.api.DomainEvent
import java.util.UUID

data class CommunicationRequestFailedEvent(
    val communicationId: UUID,
    val status: String,
    val reason: String,
) : DomainEvent(communicationId)
