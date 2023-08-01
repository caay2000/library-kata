package com.github.caay2000.librarykata.context.book.primaryadapter.event

import com.github.caay2000.common.event.DomainEventSubscriber
import com.github.caay2000.librarykata.context.book.application.LoanRepository
import com.github.caay2000.librarykata.context.book.application.loan.start.StartLoanCommand
import com.github.caay2000.librarykata.context.book.application.loan.start.StartLoanCommandHandler
import com.github.caay2000.librarykata.events.loan.LoanCreatedEvent
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
                bookId = event.bookId,
                userId = event.userId,
                startedAt = event.createdAt,
            ),
        )
    }
}
