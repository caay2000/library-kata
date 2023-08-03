package com.github.caay2000.librarykata.context.account.primaryadapter.event

import com.github.caay2000.common.event.DomainEventSubscriber
import com.github.caay2000.librarykata.context.account.application.LoanRepository
import com.github.caay2000.librarykata.context.account.application.loan.finish.FinishLoanCommand
import com.github.caay2000.librarykata.context.account.application.loan.finish.FinishLoanCommandHandler
import com.github.caay2000.librarykata.events.loan.LoanFinishedEvent
import mu.KLogger
import mu.KotlinLogging

class FinishLoanOnLoanFinishedEventSubscriber(
    loanRepository: LoanRepository,
) : DomainEventSubscriber<LoanFinishedEvent>() {

    override val logger: KLogger = KotlinLogging.logger {}
    private val commandHandler = FinishLoanCommandHandler(loanRepository)

    override fun handleEvent(event: LoanFinishedEvent) {
        commandHandler.invoke(
            FinishLoanCommand(
                id = event.loanId,
                finishedAt = event.finishedAt,
            ),
        )
    }
}
