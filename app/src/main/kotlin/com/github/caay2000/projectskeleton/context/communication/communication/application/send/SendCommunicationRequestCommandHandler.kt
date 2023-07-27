package com.github.caay2000.projectskeleton.context.communication.communication.application.send

import com.github.caay2000.archkata.common.cqrs.Command
import com.github.caay2000.archkata.common.cqrs.CommandHandler
import com.github.caay2000.common.arrow.getOrThrow
import com.github.caay2000.common.event.api.DomainEventPublisher
import com.github.caay2000.projectskeleton.context.communication.communication.application.CommunicationRequestRepository
import com.github.caay2000.projectskeleton.context.communication.communication.application.TemplateRepository
import com.github.caay2000.projectskeleton.context.communication.communication.domain.CommunicationId
import com.github.caay2000.projectskeleton.context.communication.contactdetails.application.ContactDetailsRepository
import java.util.UUID

class SendCommunicationRequestCommandHandler(
    contactDetailsRepository: ContactDetailsRepository,
    templateRepository: TemplateRepository,
    communicationRequestRepository: CommunicationRequestRepository,
    communicationRequestEmailSender: CommunicationRequestEmailSender,
    communicationRequestSmsSender: CommunicationRequestSmsSender,
    eventPublisher: DomainEventPublisher,
) : CommandHandler<SendCommunicationRequestCommand> {

    private val processor = CommunicationRequestSender(
        contactDetailsRepository = contactDetailsRepository,
        templateRepository = templateRepository,
        communicationRequestRepository = communicationRequestRepository,
        communicationRequestEmailSender = communicationRequestEmailSender,
        communicationRequestSmsSender = communicationRequestSmsSender,
        eventPublisher = eventPublisher,
    )

    override fun invoke(command: SendCommunicationRequestCommand): Unit =
        processor.invoke(communicationId = CommunicationId(command.communicationId)).getOrThrow()
}

data class SendCommunicationRequestCommand(val communicationId: UUID) : Command
