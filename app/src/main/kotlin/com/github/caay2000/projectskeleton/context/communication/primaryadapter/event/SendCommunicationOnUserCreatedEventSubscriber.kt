package com.github.caay2000.projectskeleton.context.communication.primaryadapter.event

import com.github.caay2000.common.event.api.DomainEventPublisher
import com.github.caay2000.common.event.api.DomainEventSubscriber
import com.github.caay2000.common.event.events.account.AccountCreatedEvent
import com.github.caay2000.common.idgenerator.IdGenerator
import com.github.caay2000.projectskeleton.context.communication.application.CommunicationProcessor
import com.github.caay2000.projectskeleton.context.communication.application.TemplateRepository
import com.github.caay2000.projectskeleton.context.communication.application.send.SendCommunicationCommand
import com.github.caay2000.projectskeleton.context.communication.application.send.SendCommunicationCommandHandler
import com.github.caay2000.projectskeleton.context.communication.domain.TemplateId
import mu.KLogger
import mu.KotlinLogging
import java.util.UUID

class SendCommunicationOnUserCreatedEventSubscriber(
    private val idGenerator: IdGenerator,
    templateRepository: TemplateRepository,
    communicationProcessor: CommunicationProcessor,
    eventPublisher: DomainEventPublisher,
) : DomainEventSubscriber<AccountCreatedEvent>() {

    override val logger: KLogger = KotlinLogging.logger {}
    private val commandHandler = SendCommunicationCommandHandler(templateRepository, communicationProcessor, eventPublisher)

    override fun handleEvent(event: AccountCreatedEvent) {
        val communicationId = UUID.fromString(idGenerator.generate())
        commandHandler.invoke(
            SendCommunicationCommand(
                communicationId = communicationId,
                requestedDateTime = event.datetime,
                email = event.email,
                templateId = TemplateId.USER_CREATED_TEMPLATE.name,
            ),
        )
    }
}
