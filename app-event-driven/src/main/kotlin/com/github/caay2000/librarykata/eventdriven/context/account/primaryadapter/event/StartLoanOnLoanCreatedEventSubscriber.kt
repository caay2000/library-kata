package com.github.caay2000.librarykata.eventdriven.context.account.primaryadapter.event

import com.github.caay2000.common.event.DomainEventSubscriber
import com.github.caay2000.librarykata.eventdriven.context.account.application.LoanRepository
import com.github.caay2000.librarykata.eventdriven.context.account.application.loan.start.StartLoanCommand
import com.github.caay2000.librarykata.eventdriven.context.account.application.loan.start.StartLoanCommandHandler
import com.github.caay2000.librarykata.eventdriven.events.loan.LoanCreatedEvent
import mu.KLogger
import mu.KotlinLogging

class StartLoanOnLoanCreatedEventSubscriber(
    loanRepository: LoanRepository,
) : DomainEventSubscriber<LoanCreatedEvent>() {

    override val logger: KLogger = KotlinLogging.logger {}
    private val commandHandler = StartLoanCommandHandler(loanRepository)

    override fun handleEvent(event: LoanCreatedEvent) {
        commandHandler.invoke(
            StartLoanCommand(
                id = event.loanId,
                accountId = event.userId,
                bookId = event.bookId,
                startedAt = event.createdAt,
            ),
        )
    }
}
