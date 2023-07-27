package com.github.caay2000.projectskeleton.context.communication.communication.primaryadapter.event

import com.github.caay2000.common.event.api.DomainEventPublisher
import com.github.caay2000.common.event.api.DomainEventSubscriber
import com.github.caay2000.common.event.events.communication.CommunicationRequestCreatedEvent
import com.github.caay2000.projectskeleton.context.communication.communication.application.CommunicationRequestRepository
import com.github.caay2000.projectskeleton.context.communication.communication.application.TemplateRepository
import com.github.caay2000.projectskeleton.context.communication.communication.application.send.CommunicationRequestEmailSender
import com.github.caay2000.projectskeleton.context.communication.communication.application.send.CommunicationRequestSmsSender
import com.github.caay2000.projectskeleton.context.communication.communication.application.send.SendCommunicationRequestCommand
import com.github.caay2000.projectskeleton.context.communication.communication.application.send.SendCommunicationRequestCommandHandler
import com.github.caay2000.projectskeleton.context.communication.contactdetails.application.ContactDetailsRepository
import mu.KLogger
import mu.KotlinLogging

class SendCommunicationRequestOnCommunicationRequestCreatedEventSubscriber(
    contactDetailsRepository: ContactDetailsRepository,
    templateRepository: TemplateRepository,
    communicationRequestRepository: CommunicationRequestRepository,
    communicationRequestEmailSender: CommunicationRequestEmailSender,
    communicationRequestSmsSender: CommunicationRequestSmsSender,
    eventPublisher: DomainEventPublisher,
) : DomainEventSubscriber<CommunicationRequestCreatedEvent>() {

    override val logger: KLogger = KotlinLogging.logger {}
    private val commandHandler = SendCommunicationRequestCommandHandler(
        contactDetailsRepository = contactDetailsRepository,
        templateRepository = templateRepository,
        communicationRequestRepository = communicationRequestRepository,
        communicationRequestEmailSender = communicationRequestEmailSender,
        communicationRequestSmsSender = communicationRequestSmsSender,
        eventPublisher = eventPublisher,
    )

    override fun handleEvent(event: CommunicationRequestCreatedEvent) {
        commandHandler.invoke(
            SendCommunicationRequestCommand(
                communicationId = event.communicationId,
            ),
        )
    }
}
