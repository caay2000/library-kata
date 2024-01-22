package com.github.caay2000.librarykata.eventdriven.context.loan.primaryadapter.event.account

import com.github.caay2000.common.event.DomainEventSubscriber
import com.github.caay2000.librarykata.eventdriven.context.loan.application.account.update.UpdateAccountLoansCommand
import com.github.caay2000.librarykata.eventdriven.context.loan.application.account.update.UpdateAccountLoansCommandHandler
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.AccountId
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.AccountRepository
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
