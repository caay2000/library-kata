package com.github.caay2000.librarykata.eventdriven.context.account.primaryadapter.event

import com.github.caay2000.common.event.DomainEventSubscriber
import com.github.caay2000.librarykata.eventdriven.context.account.application.loan.create.CreateLoanCommand
import com.github.caay2000.librarykata.eventdriven.context.account.application.loan.create.CreateLoanCommandHandler
import com.github.caay2000.librarykata.eventdriven.context.account.domain.AccountId
import com.github.caay2000.librarykata.eventdriven.context.account.domain.BookId
import com.github.caay2000.librarykata.eventdriven.context.account.domain.LoanId
import com.github.caay2000.librarykata.eventdriven.context.account.domain.LoanRepository
import com.github.caay2000.librarykata.eventdriven.events.loan.LoanCreatedEvent
import mu.KLogger
import mu.KotlinLogging

class CreateLoanOnLoanCreatedEventSubscriber(loanRepository: LoanRepository) : DomainEventSubscriber<LoanCreatedEvent>() {
    override val logger: KLogger = KotlinLogging.logger {}

    private val commandHandler = CreateLoanCommandHandler(loanRepository)

    override fun handleEvent(event: LoanCreatedEvent) {
        commandHandler.invoke(CreateLoanCommand(LoanId(event.loanId), AccountId(event.accountId), BookId(event.bookId)))
    }
}
