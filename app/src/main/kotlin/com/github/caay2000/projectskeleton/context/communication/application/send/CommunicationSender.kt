package com.github.caay2000.projectskeleton.context.communication.application.send

import arrow.core.Either
import arrow.core.flatMap
import com.github.caay2000.common.event.api.DomainEventPublisher
import com.github.caay2000.projectskeleton.context.communication.application.CommunicationProcessor
import com.github.caay2000.projectskeleton.context.communication.application.TemplateRepository
import com.github.caay2000.projectskeleton.context.communication.domain.CommunicationId
import com.github.caay2000.projectskeleton.context.communication.domain.CommunicationRequest
import com.github.caay2000.projectskeleton.context.communication.domain.Email
import com.github.caay2000.projectskeleton.context.communication.domain.Template
import com.github.caay2000.projectskeleton.context.communication.domain.TemplateId
import java.time.LocalDateTime

class CommunicationSender(
    private val templateRepository: TemplateRepository,
    private val communicationProcessor: CommunicationProcessor,
    private val eventPublisher: DomainEventPublisher,
) {

    fun invoke(
        communicationId: CommunicationId,
        requestedDateTime: LocalDateTime,
        email: Email,
        templateId: TemplateId,
    ): Either<CommunicationRequestSenderError, Unit> =
        findTemplate(templateId)
            .map { template -> createCommunicationRequest(communicationId, requestedDateTime, email, template) }
            .flatMap { request -> request.send() }
            .flatMap { request -> request.publishEvents() }

    private fun findTemplate(templateId: TemplateId): Either<CommunicationRequestSenderError, Template> =
        templateRepository.findTemplateById(templateId)
            .mapLeft { CommunicationRequestSenderError.Unknown(it) }

    private fun createCommunicationRequest(
        communicationId: CommunicationId,
        requestedDateTime: LocalDateTime,
        email: Email,
        template: Template,
    ) = CommunicationRequest.create(
        id = communicationId,
        requestDate = requestedDateTime,
        to = email,
        template = template,
    )

    private fun CommunicationRequest.send(): Either<CommunicationRequestSenderError, CommunicationRequest> =
        communicationProcessor.invoke(this)
            .mapLeft { CommunicationRequestSenderError.Unknown(it) }
            .map { this }

    private fun CommunicationRequest.publishEvents(): Either<CommunicationRequestSenderError, Unit> =
        eventPublisher.publish(pullEvents())
            .mapLeft { CommunicationRequestSenderError.Unknown(it) }
}

sealed class CommunicationRequestSenderError : RuntimeException {
    constructor(message: String) : super(message)
    constructor(throwable: Throwable) : super(throwable)

    class Unknown(error: Throwable) : CommunicationRequestSenderError(error)
}
