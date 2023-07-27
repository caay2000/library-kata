package com.github.caay2000.projectskeleton.context.communication.communication.application.send

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.recover
import com.github.caay2000.common.arrow.getOrThrow
import com.github.caay2000.common.database.RepositoryError
import com.github.caay2000.common.event.api.DomainEventPublisher
import com.github.caay2000.projectskeleton.context.communication.communication.application.CommunicationRequestRepository
import com.github.caay2000.projectskeleton.context.communication.communication.application.FindCommunicationRequestCriteria
import com.github.caay2000.projectskeleton.context.communication.communication.application.TemplateRepository
import com.github.caay2000.projectskeleton.context.communication.communication.domain.Body
import com.github.caay2000.projectskeleton.context.communication.communication.domain.CommunicationId
import com.github.caay2000.projectskeleton.context.communication.communication.domain.CommunicationRequest
import com.github.caay2000.projectskeleton.context.communication.communication.domain.EmailTemplate
import com.github.caay2000.projectskeleton.context.communication.communication.domain.Message
import com.github.caay2000.projectskeleton.context.communication.communication.domain.SMSTemplate
import com.github.caay2000.projectskeleton.context.communication.communication.domain.Subject
import com.github.caay2000.projectskeleton.context.communication.communication.domain.Template
import com.github.caay2000.projectskeleton.context.communication.communication.domain.TemplateId
import com.github.caay2000.projectskeleton.context.communication.contactdetails.application.ContactDetailsRepository
import com.github.caay2000.projectskeleton.context.communication.contactdetails.domain.AccountNumber
import com.github.caay2000.projectskeleton.context.communication.contactdetails.domain.ContactDetails
import com.github.caay2000.projectskeleton.context.communication.contactdetails.domain.Email
import com.github.caay2000.projectskeleton.context.communication.contactdetails.domain.PhoneNumber
import com.github.caay2000.projectskeleton.context.communication.contactdetails.domain.PhonePrefix

class CommunicationRequestSender(
    private val contactDetailsRepository: ContactDetailsRepository,
    private val templateRepository: TemplateRepository,
    private val communicationRequestRepository: CommunicationRequestRepository,
    private val communicationRequestEmailSender: CommunicationRequestEmailSender,
    private val communicationRequestSmsSender: CommunicationRequestSmsSender,
    private val eventPublisher: DomainEventPublisher,
) {

    fun invoke(communicationId: CommunicationId): Either<CommunicationRequestSenderError, Unit> =
        findCommunicationRequest(communicationId)
            .flatMap { request -> request.enrichContext() }
            .flatMap { request -> request.send() }
            .flatMap { request -> request.save() }
            .flatMap { request -> request.publishEvents() }

    private fun findCommunicationRequest(communicationId: CommunicationId): Either<CommunicationRequestSenderError, CommunicationRequest> =
        communicationRequestRepository.findBy(FindCommunicationRequestCriteria.ByCommunicationId(communicationId))
            .mapLeft {
                when (it) {
                    is RepositoryError.NotFoundError -> CommunicationRequestSenderError.CommunicationRequestNotFound(communicationId)
                    is RepositoryError.Unknown -> CommunicationRequestSenderError.Unknown(it)
                }
            }

    private fun CommunicationRequest.enrichContext(): Either<CommunicationRequestSenderError, CommunicationContext> =
        Either.catch {
            CommunicationContext(
                communicationRequest = this,
                contactDetails = findContactDetails().getOrThrow(),
                template = findTemplate().getOrThrow(),
            )
        }.mapLeft {
            when (it) {
                is CommunicationRequestSenderError -> it
                else -> CommunicationRequestSenderError.Unknown(it)
            }
        }

    private fun CommunicationRequest.findTemplate(): Either<CommunicationRequestSenderError, Template> =
        templateRepository.findTemplateById(templateId)
            .mapLeft {
                when (it) {
                    is RepositoryError.NotFoundError -> CommunicationRequestSenderError.TemplateNotFound(templateId)
                    is RepositoryError.Unknown -> CommunicationRequestSenderError.Unknown(it)
                }
            }

    private fun CommunicationRequest.findContactDetails(): Either<CommunicationRequestSenderError, ContactDetails> =
        contactDetailsRepository.findByAccountNumber(accountNumber)
            .mapLeft {
                when (it) {
                    is RepositoryError.NotFoundError -> CommunicationRequestSenderError.ContactDetailsNotFound(accountNumber)
                    is RepositoryError.Unknown -> CommunicationRequestSenderError.Unknown(it)
                }
            }

    private fun CommunicationContext.send(): Either<CommunicationRequestSenderError, CommunicationRequest> =
        when (template) {
            is SMSTemplate -> sendSms(communicationRequest.id, contactDetails.phonePrefix, contactDetails.phoneNumber, template.message)
            is EmailTemplate -> sendEmail(communicationRequest.id, contactDetails.email, template.subject, template.body)
        }.map { this.communicationRequest.sent() }
            .recover { this@send.communicationRequest.failed() }

    private fun sendSms(communicationId: CommunicationId, phonePrefix: PhonePrefix, phoneNumber: PhoneNumber, message: Message): Either<CommunicationRequestSenderError, Unit> =
        Either.catch { communicationRequestSmsSender.invoke(communicationId, phonePrefix, phoneNumber, message) }
            .mapLeft { CommunicationRequestSenderError.Unknown(it) }

    private fun sendEmail(communicationId: CommunicationId, email: Email, subject: Subject, body: Body): Either<CommunicationRequestSenderError, Unit> =
        Either.catch { communicationRequestEmailSender.invoke(communicationId, email, subject, body) }
            .mapLeft { CommunicationRequestSenderError.Unknown(it) }

    private fun CommunicationRequest.save(): Either<CommunicationRequestSenderError, CommunicationRequest> =
        communicationRequestRepository.save(this)
            .mapLeft { CommunicationRequestSenderError.Unknown(it) }
            .map { this }

    private fun CommunicationRequest.publishEvents(): Either<CommunicationRequestSenderError, Unit> =
        eventPublisher.publish(pullEvents())
            .mapLeft { CommunicationRequestSenderError.Unknown(it) }

    class CommunicationContext(
        val communicationRequest: CommunicationRequest,
        val contactDetails: ContactDetails,
        val template: Template,
    )
}

sealed class CommunicationRequestSenderError : RuntimeException {
    constructor(message: String) : super(message)
    constructor(throwable: Throwable) : super(throwable)

    class Unknown(error: Throwable) : CommunicationRequestSenderError(error)
    class CommunicationRequestNotFound(communicationId: CommunicationId) : CommunicationRequestSenderError("communication $communicationId not found")
    class TemplateNotFound(templateId: TemplateId) : CommunicationRequestSenderError("template $templateId not found")
    class ContactDetailsNotFound(accountNumber: AccountNumber) : CommunicationRequestSenderError("contact details for account $accountNumber not found")
}
