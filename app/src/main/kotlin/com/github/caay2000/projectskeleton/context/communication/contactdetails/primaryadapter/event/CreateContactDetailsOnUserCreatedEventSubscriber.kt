package com.github.caay2000.projectskeleton.context.communication.contactdetails.primaryadapter.event

import com.github.caay2000.common.event.api.DomainEventSubscriber
import com.github.caay2000.common.event.events.account.AccountCreatedEvent
import com.github.caay2000.projectskeleton.context.communication.contactdetails.application.ContactDetailsRepository
import com.github.caay2000.projectskeleton.context.communication.contactdetails.application.create.CreateContactDetailsCommand
import com.github.caay2000.projectskeleton.context.communication.contactdetails.application.create.CreateContactDetailsCommandHandler
import mu.KLogger
import mu.KotlinLogging

class CreateContactDetailsOnUserCreatedEventSubscriber(
    contactDetailsRepository: ContactDetailsRepository,
) : DomainEventSubscriber<AccountCreatedEvent>() {

    override val logger: KLogger = KotlinLogging.logger {}
    private val commandHandler = CreateContactDetailsCommandHandler(contactDetailsRepository)

    override fun handleEvent(event: AccountCreatedEvent) {
        commandHandler.invoke(
            CreateContactDetailsCommand(
                accountNumber = event.accountNumber,
                email = event.email,
                phoneNumber = event.phoneNumber,
                phonePrefix = event.phonePrefix,
            ),
        )
    }
}
