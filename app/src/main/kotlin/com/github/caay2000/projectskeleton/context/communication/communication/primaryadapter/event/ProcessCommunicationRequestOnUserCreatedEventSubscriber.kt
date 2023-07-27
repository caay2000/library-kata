package com.github.caay2000.projectskeleton.context.communication.communication.primaryadapter.event

import com.github.caay2000.common.event.api.DomainEventPublisher
import com.github.caay2000.common.event.api.DomainEventSubscriber
import com.github.caay2000.common.event.events.account.AccountCreatedEvent
import com.github.caay2000.common.idgenerator.IdGenerator
import com.github.caay2000.projectskeleton.context.communication.communication.application.CommunicationRequestRepository
import com.github.caay2000.projectskeleton.context.communication.communication.application.TemplateRepository
import com.github.caay2000.projectskeleton.context.communication.communication.application.process.ProcessCommunicationRequestCommand
import com.github.caay2000.projectskeleton.context.communication.communication.application.process.ProcessCommunicationRequestCommandHandler
import com.github.caay2000.projectskeleton.context.communication.communication.domain.TemplateId
import com.github.caay2000.projectskeleton.context.communication.contactdetails.application.ContactDetailsRepository
import mu.KLogger
import mu.KotlinLogging
import java.util.UUID

class ProcessCommunicationRequestOnUserCreatedEventSubscriber(
    private val idGenerator: IdGenerator,
    contactDetailsRepository: ContactDetailsRepository,
    templateRepository: TemplateRepository,
    communicationRequestRepository: CommunicationRequestRepository,
    eventPublisher: DomainEventPublisher,
) : DomainEventSubscriber<AccountCreatedEvent>() {

    override val logger: KLogger = KotlinLogging.logger {}
    private val commandHandler = ProcessCommunicationRequestCommandHandler(contactDetailsRepository, templateRepository, communicationRequestRepository, eventPublisher)

    override fun handleEvent(event: AccountCreatedEvent) {
        val communicationId = UUID.fromString(idGenerator.generate())
        commandHandler.invoke(
            ProcessCommunicationRequestCommand(
                communicationId = communicationId,
                accountNumber = event.accountNumber,
                templateId = TemplateId.USER_CREATED.name,
                requestedAt = event.datetime,
            ),
        )
    }
}
