package com.github.caay2000.librarykata.eventdriven.context.loan.primaryadapter.event.account

import com.github.caay2000.common.event.DomainEventSubscriber
import com.github.caay2000.librarykata.eventdriven.context.loan.application.account.update.UpdateAccountLoansCommand
import com.github.caay2000.librarykata.eventdriven.context.loan.application.account.update.UpdateAccountLoansCommandHandler
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.AccountId
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.AccountRepository
import com.github.caay2000.librarykata.eventdriven.events.loan.LoanFinishedEvent
import mu.KLogger
import mu.KotlinLogging

class DecreaseLoansOnLoanCreatedEventSubscriber(accountRepository: AccountRepository) : DomainEventSubscriber<LoanFinishedEvent>() {
    override val logger: KLogger = KotlinLogging.logger {}

    private val commandHandler = UpdateAccountLoansCommandHandler(accountRepository)

    override fun handleEvent(event: LoanFinishedEvent) {
        commandHandler.invoke(UpdateAccountLoansCommand.DecreaseAccountLoansCommand(AccountId(event.accountId)))
    }
}
