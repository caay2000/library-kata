package com.github.caay2000.projectskeleton.context.communication.communication.application.process

import com.github.caay2000.archkata.common.cqrs.Command
import com.github.caay2000.archkata.common.cqrs.CommandHandler
import com.github.caay2000.common.arrow.getOrThrow
import com.github.caay2000.common.event.api.DomainEventPublisher
import com.github.caay2000.projectskeleton.context.communication.communication.application.CommunicationRequestRepository
import com.github.caay2000.projectskeleton.context.communication.communication.application.TemplateRepository
import com.github.caay2000.projectskeleton.context.communication.communication.domain.CommunicationId
import com.github.caay2000.projectskeleton.context.communication.communication.domain.RequestedAt
import com.github.caay2000.projectskeleton.context.communication.communication.domain.TemplateId
import com.github.caay2000.projectskeleton.context.communication.contactdetails.application.ContactDetailsRepository
import com.github.caay2000.projectskeleton.context.communication.contactdetails.domain.AccountNumber
import java.time.LocalDateTime
import java.util.UUID

class ProcessCommunicationRequestCommandHandler(
    contactDetailsRepository: ContactDetailsRepository,
    templateRepository: TemplateRepository,
    communicationRequestRepository: CommunicationRequestRepository,
    eventPublisher: DomainEventPublisher,
) : CommandHandler<ProcessCommunicationRequestCommand> {

    private val processor = CommunicationRequestProcessor(contactDetailsRepository, templateRepository, communicationRequestRepository, eventPublisher)

    override fun invoke(command: ProcessCommunicationRequestCommand): Unit =
        processor.invoke(
            communicationId = CommunicationId(command.communicationId),
            accountNumber = AccountNumber(command.accountNumber),
            templateId = TemplateId.valueOf(command.templateId),
            requestedAt = RequestedAt(command.requestedAt),
        ).getOrThrow()
}

data class ProcessCommunicationRequestCommand(
    val communicationId: UUID,
    val accountNumber: String,
    val templateId: String,
    val requestedAt: LocalDateTime,
) : Command
