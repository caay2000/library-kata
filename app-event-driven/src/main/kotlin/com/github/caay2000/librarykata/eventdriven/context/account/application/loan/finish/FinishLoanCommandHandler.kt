package com.github.caay2000.librarykata.eventdriven.context.account.application.loan.finish

import com.github.caay2000.common.arrow.getOrThrow
import com.github.caay2000.common.cqrs.Command
import com.github.caay2000.common.cqrs.CommandHandler
import com.github.caay2000.librarykata.eventdriven.context.account.application.LoanRepository
import com.github.caay2000.librarykata.eventdriven.context.account.domain.FinishLoanDateTime
import com.github.caay2000.librarykata.eventdriven.context.account.domain.LoanId
import mu.KLogger
import mu.KotlinLogging
import java.time.LocalDateTime
import java.util.UUID

class FinishLoanCommandHandler(loanRepository: LoanRepository) : CommandHandler<FinishLoanCommand> {
    override val logger: KLogger = KotlinLogging.logger {}
    private val finisher = LoanFinisher(loanRepository)

    override fun handle(command: FinishLoanCommand): Unit =
        finisher.invoke(
            loanId = LoanId(command.id),
            finishedAt = FinishLoanDateTime(command.finishedAt),
        ).getOrThrow()
}

data class FinishLoanCommand(
    val id: UUID,
    val finishedAt: LocalDateTime,
) : Command
