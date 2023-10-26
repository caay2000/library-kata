package com.github.caay2000.librarykata.hexagonal.context.application.loan.create

import com.github.caay2000.common.arrow.getOrThrow
import com.github.caay2000.common.cqrs.Command
import com.github.caay2000.common.cqrs.CommandHandler
import com.github.caay2000.librarykata.hexagonal.context.domain.account.AccountId
import com.github.caay2000.librarykata.hexagonal.context.domain.account.AccountRepository
import com.github.caay2000.librarykata.hexagonal.context.domain.book.BookIsbn
import com.github.caay2000.librarykata.hexagonal.context.domain.book.BookRepository
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.CreatedAt
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.LoanId
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.LoanRepository
import mu.KLogger
import mu.KotlinLogging
import java.time.LocalDateTime
import java.util.UUID

class CreateLoanCommandHandler(
    bookRepository: BookRepository,
    accountRepository: AccountRepository,
    loanRepository: LoanRepository,
) : CommandHandler<CreateLoanCommand> {

    override val logger: KLogger = KotlinLogging.logger {}
    private val creator = LoanCreator(bookRepository, accountRepository, loanRepository)

    override fun handle(command: CreateLoanCommand): Unit =
        creator.invoke(
            loanId = LoanId(command.loanId.toString()),
            accountId = AccountId(command.accountId.toString()),
            bookIsbn = BookIsbn(command.bookIsbn),
            createdAt = CreatedAt(command.createdAt),
        ).getOrThrow()
}

data class CreateLoanCommand(
    val loanId: UUID,
    val accountId: UUID,
    val bookIsbn: String,
    val createdAt: LocalDateTime,
) : Command
