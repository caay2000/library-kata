package com.github.caay2000.librarykata.eventdriven.context.loan.account.primaryadapter.event

import com.github.caay2000.common.event.DomainEventSubscriber
import com.github.caay2000.librarykata.eventdriven.context.loan.account.application.update.UpdateAccountLoansCommand
import com.github.caay2000.librarykata.eventdriven.context.loan.account.application.update.UpdateAccountLoansCommandHandler
import com.github.caay2000.librarykata.eventdriven.context.loan.account.domain.AccountId
import com.github.caay2000.librarykata.eventdriven.context.loan.account.domain.AccountRepository
import com.github.caay2000.librarykata.eventdriven.events.loan.LoanCreatedEvent
import mu.KLogger
import mu.KotlinLogging

class IncreaseLoansOnLoanCreatedEventSubscriber(accountRepository: AccountRepository) : DomainEventSubscriber<LoanCreatedEvent>() {
    override val logger: KLogger = KotlinLogging.logger {}

    private val commandHandler = UpdateAccountLoansCommandHandler(accountRepository)

    override fun handleEvent(event: LoanCreatedEvent) {
        commandHandler.invoke(UpdateAccountLoansCommand.IncreaseAccountLoansCommand(AccountId(event.accountId)))
    }
}
