package com.github.caay2000.librarykata.eventdriven.context.loan.loan.application.create

import com.github.caay2000.common.arrow.getOrThrow
import com.github.caay2000.common.cqrs.Command
import com.github.caay2000.common.cqrs.CommandHandler
import com.github.caay2000.librarykata.eventdriven.context.account.domain.AccountId
import com.github.caay2000.librarykata.eventdriven.context.book.domain.BookIsbn
import com.github.caay2000.librarykata.eventdriven.context.loan.loan.domain.CreatedAt
import com.github.caay2000.librarykata.eventdriven.context.loan.loan.domain.LoanId
import com.github.caay2000.librarykata.eventdriven.context.loan.loan.domain.LoanRepository
import mu.KLogger
import mu.KotlinLogging
import java.time.LocalDateTime
import java.util.UUID

class CreateLoanCommandHandler(
    loanRepository: LoanRepository,
) : CommandHandler<CreateLoanCommand> {
    override val logger: KLogger = KotlinLogging.logger {}
    private val creator = LoanCreator(loanRepository)

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
