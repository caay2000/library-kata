package com.github.caay2000.librarykata.event.context.account.application.loan.start

import com.github.caay2000.common.cqrs.Command
import com.github.caay2000.common.cqrs.CommandHandler
import com.github.caay2000.librarykata.event.context.account.application.LoanRepository
import com.github.caay2000.librarykata.event.context.account.domain.AccountId
import com.github.caay2000.librarykata.event.context.account.domain.BookId
import com.github.caay2000.librarykata.event.context.account.domain.LoanId
import com.github.caay2000.librarykata.event.context.account.domain.StartLoanDateTime
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
            accountId = AccountId(command.accountId.toString()),
            bookId = BookId(command.bookId),
            startedAt = StartLoanDateTime(command.startedAt),
        )
}

data class StartLoanCommand(
    val id: UUID,
    val accountId: UUID,
    val bookId: UUID,
    val startedAt: LocalDateTime,
) : Command
