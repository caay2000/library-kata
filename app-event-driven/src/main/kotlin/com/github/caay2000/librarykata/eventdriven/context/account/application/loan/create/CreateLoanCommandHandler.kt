package com.github.caay2000.librarykata.eventdriven.context.account.application.loan.create

import com.github.caay2000.common.cqrs.Command
import com.github.caay2000.common.cqrs.CommandHandler
import com.github.caay2000.librarykata.eventdriven.context.account.domain.AccountId
import com.github.caay2000.librarykata.eventdriven.context.account.domain.BookId
import com.github.caay2000.librarykata.eventdriven.context.account.domain.LoanId
import com.github.caay2000.librarykata.eventdriven.context.account.domain.LoanRepository
import mu.KLogger
import mu.KotlinLogging

class CreateLoanCommandHandler(
    loanRepository: LoanRepository,
) : CommandHandler<CreateLoanCommand> {
    override val logger: KLogger = KotlinLogging.logger {}
    private val loanCreator: LoanCreator = LoanCreator(loanRepository)

    override fun handle(command: CreateLoanCommand) {
        loanCreator.invoke(command.loanId, command.accountId, command.bookId)
    }
}

data class CreateLoanCommand(
    val loanId: LoanId,
    val accountId: AccountId,
    val bookId: BookId,
) : Command
