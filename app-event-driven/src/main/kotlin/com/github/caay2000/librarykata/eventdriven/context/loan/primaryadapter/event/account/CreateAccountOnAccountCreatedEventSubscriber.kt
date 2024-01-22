package com.github.caay2000.librarykata.eventdriven.context.loan.primaryadapter.event.account

import com.github.caay2000.common.event.DomainEventSubscriber
import com.github.caay2000.librarykata.eventdriven.context.loan.application.account.create.CreateAccountCommand
import com.github.caay2000.librarykata.eventdriven.context.loan.application.account.create.CreateAccountCommandHandler
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.AccountId
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.AccountRepository
import com.github.caay2000.librarykata.eventdriven.events.account.AccountCreatedEvent
import mu.KLogger
import mu.KotlinLogging

class CreateAccountOnAccountCreatedEventSubscriber(accountRepository: AccountRepository) : DomainEventSubscriber<AccountCreatedEvent>() {
    override val logger: KLogger = KotlinLogging.logger {}
    private val commandHandler = CreateAccountCommandHandler(accountRepository)

    override fun handleEvent(event: AccountCreatedEvent) {
        commandHandler.invoke(CreateAccountCommand(AccountId(event.id)))
    }
}
