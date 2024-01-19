package com.github.caay2000.librarykata.eventdriven.context.loan.account.primaryadapter.event

import com.github.caay2000.common.event.DomainEventSubscriber
import com.github.caay2000.librarykata.eventdriven.context.loan.account.application.create.CreateAccountCommand
import com.github.caay2000.librarykata.eventdriven.context.loan.account.application.create.CreateAccountCommandHandler
import com.github.caay2000.librarykata.eventdriven.context.loan.account.domain.AccountId
import com.github.caay2000.librarykata.eventdriven.context.loan.account.domain.AccountRepository
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
