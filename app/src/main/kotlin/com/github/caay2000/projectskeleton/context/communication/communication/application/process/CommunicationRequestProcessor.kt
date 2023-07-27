package com.github.caay2000.projectskeleton.context.communication.communication.application.process

import arrow.core.Either
import arrow.core.flatMap
import com.github.caay2000.common.database.RepositoryError
import com.github.caay2000.common.event.api.DomainEventPublisher
import com.github.caay2000.projectskeleton.context.communication.communication.application.CommunicationRequestRepository
import com.github.caay2000.projectskeleton.context.communication.communication.application.TemplateRepository
import com.github.caay2000.projectskeleton.context.communication.communication.domain.CommunicationId
import com.github.caay2000.projectskeleton.context.communication.communication.domain.CommunicationRequest
import com.github.caay2000.projectskeleton.context.communication.communication.domain.RequestedAt
import com.github.caay2000.projectskeleton.context.communication.communication.domain.Template
import com.github.caay2000.projectskeleton.context.communication.communication.domain.TemplateId
import com.github.caay2000.projectskeleton.context.communication.contactdetails.application.ContactDetailsRepository
import com.github.caay2000.projectskeleton.context.communication.contactdetails.domain.AccountNumber
import com.github.caay2000.projectskeleton.context.communication.contactdetails.domain.ContactDetails

class CommunicationRequestProcessor(
    private val contactDetailsRepository: ContactDetailsRepository,
    private val templateRepository: TemplateRepository,
    private val communicationRequestRepository: CommunicationRequestRepository,
    private val eventPublisher: DomainEventPublisher,
) {

    fun invoke(
        communicationId: CommunicationId,
        accountNumber: AccountNumber,
        templateId: TemplateId,
        requestedAt: RequestedAt,
    ): Either<CommunicationRequestProcessorError, Unit> =
        guardContactDetailsExists(accountNumber)
            .flatMap { guardTemplateExists(templateId) }
            .map { createCommunicationRequest(communicationId, accountNumber, templateId, requestedAt) }
            .flatMap { request -> request.save() }
            .flatMap { request -> request.publishEvents() }

    private fun guardContactDetailsExists(accountNumber: AccountNumber): Either<CommunicationRequestProcessorError, ContactDetails> =
        contactDetailsRepository.findByAccountNumber(accountNumber)
            .mapLeft {
                when (it) {
                    is RepositoryError.NotFoundError -> CommunicationRequestProcessorError.ContactDetailsNotFound(accountNumber)
                    else -> CommunicationRequestProcessorError.Unknown(it)
                }
            }

    private fun guardTemplateExists(templateId: TemplateId): Either<CommunicationRequestProcessorError, Template> =
        templateRepository.findTemplateById(templateId)
            .mapLeft {
                when (it) {
                    is RepositoryError.NotFoundError -> CommunicationRequestProcessorError.TemplateNotFound(templateId)
                    else -> CommunicationRequestProcessorError.Unknown(it)
                }
            }

    private fun createCommunicationRequest(
        communicationId: CommunicationId,
        accountNumber: AccountNumber,
        templateId: TemplateId,
        requestedAt: RequestedAt,
    ) = CommunicationRequest.create(
        id = communicationId,
        accountNumber = accountNumber,
        templateId = templateId,
        requestedAt = requestedAt,
    )

    private fun CommunicationRequest.save(): Either<CommunicationRequestProcessorError, CommunicationRequest> =
        communicationRequestRepository.save(this)
            .mapLeft { CommunicationRequestProcessorError.Unknown(it) }
            .map { this }

    private fun CommunicationRequest.publishEvents(): Either<CommunicationRequestProcessorError, Unit> =
        eventPublisher.publish(pullEvents())
            .mapLeft { CommunicationRequestProcessorError.Unknown(it) }
}

sealed class CommunicationRequestProcessorError : RuntimeException {
    constructor(message: String) : super(message)
    constructor(throwable: Throwable) : super(throwable)

    class Unknown(error: Throwable) : CommunicationRequestProcessorError(error)
    class TemplateNotFound(templateId: TemplateId) : CommunicationRequestProcessorError("template $templateId not found")
    class ContactDetailsNotFound(accountNumber: AccountNumber) : CommunicationRequestProcessorError("contact details for account $accountNumber not found")
}
