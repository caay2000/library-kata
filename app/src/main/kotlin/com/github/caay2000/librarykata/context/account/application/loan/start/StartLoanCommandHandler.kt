package com.github.caay2000.librarykata.context.account.application.loan.start

import com.github.caay2000.common.arrow.getOrThrow
import com.github.caay2000.common.cqrs.Command
import com.github.caay2000.common.cqrs.CommandHandler
import com.github.caay2000.librarykata.context.account.application.LoanRepository
import com.github.caay2000.librarykata.context.account.domain.AccountId
import com.github.caay2000.librarykata.context.account.domain.BookId
import com.github.caay2000.librarykata.context.account.domain.LoanId
import com.github.caay2000.librarykata.context.account.domain.StartLoanDateTime
import mu.KLogger
import mu.KotlinLogging
import java.time.LocalDateTime
import java.util.UUID

class StartLoanCommandHandler(loanRepository: LoanRepository) : CommandHandler<StartLoanCommand> {

    override val logger: KLogger = KotlinLogging.logger {}
    private val starter = LoanStarter(loanRepository)

    override fun handle(command: StartLoanCommand): Unit =
        starter.invoke(
            id = LoanId(command.id),
            accountId = AccountId(command.accountId),
            bookId = BookId(command.bookId),
            startedAt = StartLoanDateTime(command.startedAt),
        ).getOrThrow()
}

data class StartLoanCommand(
    val id: UUID,
    val accountId: UUID,
    val bookId: UUID,
    val startedAt: LocalDateTime,
) : Command
