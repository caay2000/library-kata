package com.github.caay2000.projectskeleton.context.communication.application.send

import com.github.caay2000.archkata.common.cqrs.Command
import com.github.caay2000.archkata.common.cqrs.CommandHandler
import com.github.caay2000.common.arrow.getOrThrow
import com.github.caay2000.common.event.api.DomainEventPublisher
import com.github.caay2000.projectskeleton.context.communication.application.CommunicationProcessor
import com.github.caay2000.projectskeleton.context.communication.application.TemplateRepository
import com.github.caay2000.projectskeleton.context.communication.domain.CommunicationId
import com.github.caay2000.projectskeleton.context.communication.domain.Email
import com.github.caay2000.projectskeleton.context.communication.domain.TemplateId
import java.time.LocalDateTime
import java.util.UUID

class SendCommunicationCommandHandler(
    templateRepository: TemplateRepository,
    communicationProcessor: CommunicationProcessor,
    eventPublisher: DomainEventPublisher,
) : CommandHandler<SendCommunicationCommand> {

    private val sender = CommunicationSender(templateRepository, communicationProcessor, eventPublisher)

    override fun invoke(command: SendCommunicationCommand): Unit =
        sender.invoke(
            communicationId = CommunicationId(command.communicationId),
            requestedDateTime = command.requestedDateTime,
            email = Email(command.email),
            templateId = TemplateId.valueOf(command.templateId),
        ).getOrThrow()
}

data class SendCommunicationCommand(
    val communicationId: UUID,
    val requestedDateTime: LocalDateTime,
    val email: String,
    val templateId: String,
) : Command
