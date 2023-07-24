package com.github.caay2000.projectskeleton.context.communication.domain

import com.github.caay2000.common.ddd.Aggregate
import com.github.caay2000.common.ddd.DomainId
import com.github.caay2000.common.event.events.communication.CommunicationDetailsSent
import java.time.LocalDateTime
import java.util.UUID

data class CommunicationRequest(
    override val id: CommunicationId,
    val requestDate: LocalDateTime,
    val to: Email,
    val template: Template,
) : Aggregate() {

    companion object {
        fun create(id: CommunicationId, requestDate: LocalDateTime, to: Email, template: Template) = CommunicationRequest(
            id = id,
            requestDate = requestDate,
            to = to,
            template = template,
        ).also { request -> request.pushEvent(request.toCommunicationDetailsSent()) }
    }

    private fun toCommunicationDetailsSent() = CommunicationDetailsSent(
        communicationId = id.value,
        sentTo = to.value,
        requestDate = requestDate,
        templateId = template.id.name,
    )
}

@JvmInline
value class CommunicationId(val value: UUID) : DomainId {
    override fun toString(): String = value.toString()
}

@JvmInline
value class Email(val value: String) {
    override fun toString(): String = value
}
