package com.github.caay2000.librarykata.eventdriven.context.account.primaryadapter.event

import com.github.caay2000.common.event.DomainEventSubscriber
import com.github.caay2000.librarykata.eventdriven.context.account.application.loan.remove.RemoveLoanCommand
import com.github.caay2000.librarykata.eventdriven.context.account.application.loan.remove.RemoveLoanCommandHandler
import com.github.caay2000.librarykata.eventdriven.context.account.domain.LoanId
import com.github.caay2000.librarykata.eventdriven.context.account.domain.LoanRepository
import com.github.caay2000.librarykata.eventdriven.events.loan.LoanCreatedEvent
import mu.KLogger
import mu.KotlinLogging

class RemoveLoanOnLoanFinishedEventSubscriber(loanRepository: LoanRepository) : DomainEventSubscriber<LoanCreatedEvent>() {
    override val logger: KLogger = KotlinLogging.logger {}

    private val commandHandler = RemoveLoanCommandHandler(loanRepository)

    override fun handleEvent(event: LoanCreatedEvent) {
        commandHandler.invoke(RemoveLoanCommand(LoanId(event.loanId)))
    }
}
