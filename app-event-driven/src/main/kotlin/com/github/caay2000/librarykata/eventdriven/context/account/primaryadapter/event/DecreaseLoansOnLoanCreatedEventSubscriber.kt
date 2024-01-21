package com.github.caay2000.librarykata.eventdriven.context.account.primaryadapter.event

import com.github.caay2000.common.event.DomainEventSubscriber
import com.github.caay2000.librarykata.eventdriven.context.account.application.update.UpdateAccountLoansCommand
import com.github.caay2000.librarykata.eventdriven.context.account.application.update.UpdateAccountLoansCommandHandler
import com.github.caay2000.librarykata.eventdriven.context.account.domain.AccountId
import com.github.caay2000.librarykata.eventdriven.context.account.domain.AccountRepository
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
