package com.github.caay2000.librarykata.eventdriven.context.account.application.loan.remove

import com.github.caay2000.common.cqrs.Command
import com.github.caay2000.common.cqrs.CommandHandler
import com.github.caay2000.librarykata.eventdriven.context.account.domain.LoanId
import com.github.caay2000.librarykata.eventdriven.context.account.domain.LoanRepository
import mu.KLogger
import mu.KotlinLogging

class RemoveLoanCommandHandler(
    loanRepository: LoanRepository,
) : CommandHandler<RemoveLoanCommand> {
    override val logger: KLogger = KotlinLogging.logger {}
    private val loanRemover: LoanRemover = LoanRemover(loanRepository)

    override fun handle(command: RemoveLoanCommand) {
        loanRemover.invoke(command.loanId)
    }
}

data class RemoveLoanCommand(
    val loanId: LoanId,
) : Command
