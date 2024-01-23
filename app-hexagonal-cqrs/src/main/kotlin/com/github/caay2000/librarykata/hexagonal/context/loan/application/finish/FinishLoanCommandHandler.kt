package com.github.caay2000.librarykata.hexagonal.context.loan.application.finish

import com.github.caay2000.common.arrow.getOrThrow
import com.github.caay2000.common.cqrs.Command
import com.github.caay2000.common.cqrs.CommandHandler
import com.github.caay2000.librarykata.hexagonal.context.account.domain.AccountRepository
import com.github.caay2000.librarykata.hexagonal.context.book.domain.BookId
import com.github.caay2000.librarykata.hexagonal.context.book.domain.BookRepository
import com.github.caay2000.librarykata.hexagonal.context.loan.domain.FinishedAt
import com.github.caay2000.librarykata.hexagonal.context.loan.domain.LoanRepository
import mu.KLogger
import mu.KotlinLogging
import java.time.LocalDateTime
import java.util.UUID

class FinishLoanCommandHandler(
    loanRepository: LoanRepository,
    bookRepository: BookRepository,
    accountRepository: AccountRepository,
) : CommandHandler<FinishLoanCommand> {
    override val logger: KLogger = KotlinLogging.logger {}
    private val finisher = LoanFinisher(loanRepository, bookRepository, accountRepository)

    override fun handle(command: FinishLoanCommand): Unit = finisher.invoke(bookId = BookId(command.bookId.toString()), finishedAt = FinishedAt(command.finishedAt)).getOrThrow()
}

data class FinishLoanCommand(
    val bookId: UUID,
    val finishedAt: LocalDateTime,
) : Command
