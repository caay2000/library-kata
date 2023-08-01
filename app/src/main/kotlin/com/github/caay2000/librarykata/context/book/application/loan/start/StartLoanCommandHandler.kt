package com.github.caay2000.librarykata.context.book.application.loan.start

import com.github.caay2000.common.arrow.getOrThrow
import com.github.caay2000.common.cqrs.Command
import com.github.caay2000.common.cqrs.CommandHandler
import com.github.caay2000.librarykata.context.book.application.LoanRepository
import com.github.caay2000.librarykata.context.book.domain.BookId
import com.github.caay2000.librarykata.context.book.domain.LoanId
import com.github.caay2000.librarykata.context.book.domain.StartLoanDateTime
import com.github.caay2000.librarykata.context.book.domain.UserId
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
            bookId = BookId(command.bookId),
            userId = UserId(command.userId),
            startedAt = StartLoanDateTime(command.startedAt),
        ).getOrThrow()
}

data class StartLoanCommand(
    val id: UUID,
    val bookId: UUID,
    val userId: UUID,
    val startedAt: LocalDateTime,
) : Command
