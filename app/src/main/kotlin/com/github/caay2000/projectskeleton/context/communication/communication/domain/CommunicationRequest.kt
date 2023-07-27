package com.github.caay2000.projectskeleton.context.communication.communication.domain

import com.github.caay2000.common.ddd.Aggregate
import com.github.caay2000.common.ddd.DomainId
import com.github.caay2000.common.event.events.communication.CommunicationRequestCreatedEvent
import com.github.caay2000.projectskeleton.context.communication.contactdetails.domain.AccountNumber
import java.time.LocalDateTime
import java.util.UUID

data class CommunicationRequest(
    override val id: CommunicationId,
    val accountNumber: AccountNumber,
    val templateId: TemplateId,
    val requestedAt: RequestedAt,
    val status: CommunicationRequestStatus,
) : Aggregate() {

    companion object {
        fun create(
            id: CommunicationId,
            accountNumber: AccountNumber,
            templateId: TemplateId,
            requestedAt: RequestedAt,
        ) = CommunicationRequest(
            id = id,
            accountNumber = accountNumber,
            templateId = templateId,
            requestedAt = requestedAt,
            status = CommunicationRequestStatus.REQUESTED,
        ).also { request -> request.pushEvent(request.toCommunicationRequestCreatedEvent()) }
    }

    fun failed(): CommunicationRequest = this.copy(status = CommunicationRequestStatus.FAILED)
        .also { request -> request.pushEvent(request.toCommunicationRequestFailedEvent()) }

    fun sent(): CommunicationRequest = this.copy(status = CommunicationRequestStatus.SENT)
        .also { request -> request.pushEvent(request.toCommunicationRequestSentEvent()) }

    private fun toCommunicationRequestCreatedEvent() = CommunicationRequestCreatedEvent(
        communicationId = id.value,
        accountNumber = accountNumber.toString(),
        templateId = templateId.name,
        requestedAt = requestedAt.value,
        status = status.name,
    )

    private fun toCommunicationRequestFailedEvent() = CommunicationRequestCreatedEvent(
        communicationId = id.value,
        accountNumber = accountNumber.toString(),
        templateId = templateId.name,
        requestedAt = requestedAt.value,
        status = status.name,
    )

    private fun toCommunicationRequestSentEvent() = CommunicationRequestCreatedEvent(
        communicationId = id.value,
        accountNumber = accountNumber.toString(),
        templateId = templateId.name,
        requestedAt = requestedAt.value,
        status = status.name,
    )
}

@JvmInline
value class CommunicationId(val value: UUID) : DomainId {
    override fun toString(): String = value.toString()
}

@JvmInline
value class RequestedAt(val value: LocalDateTime)

enum class CommunicationRequestStatus {
    REQUESTED,
    SENT,
    RECEIVED,
    FAILED,
}
