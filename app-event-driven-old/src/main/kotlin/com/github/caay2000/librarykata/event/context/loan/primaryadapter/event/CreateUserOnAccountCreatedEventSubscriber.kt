package com.github.caay2000.librarykata.event.context.loan.primaryadapter.event

import com.github.caay2000.common.event.DomainEventSubscriber
import com.github.caay2000.librarykata.event.context.loan.application.UserRepository
import com.github.caay2000.librarykata.event.context.loan.application.user.create.CreateUserCommand
import com.github.caay2000.librarykata.event.context.loan.application.user.create.CreateUserCommandHandler
import com.github.caay2000.librarykata.event.events.account.AccountCreatedEvent
import mu.KLogger
import mu.KotlinLogging

class CreateUserOnAccountCreatedEventSubscriber(userRepository: UserRepository) : DomainEventSubscriber<AccountCreatedEvent>() {
    override val logger: KLogger = KotlinLogging.logger {}
    private val commandHandler = CreateUserCommandHandler(userRepository)

    override fun handleEvent(event: AccountCreatedEvent) {
        commandHandler.invoke(CreateUserCommand(event.id))
    }
}
