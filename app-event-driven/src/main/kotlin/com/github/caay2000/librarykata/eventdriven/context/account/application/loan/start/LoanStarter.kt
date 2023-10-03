package com.github.caay2000.librarykata.eventdriven.context.account.application.loan.start

import arrow.core.Either
import arrow.core.flatMap
import com.github.caay2000.librarykata.eventdriven.context.account.application.LoanRepository
import com.github.caay2000.librarykata.eventdriven.context.account.domain.AccountId
import com.github.caay2000.librarykata.eventdriven.context.account.domain.BookId
import com.github.caay2000.librarykata.eventdriven.context.account.domain.Loan
import com.github.caay2000.librarykata.eventdriven.context.account.domain.LoanId
import com.github.caay2000.librarykata.eventdriven.context.account.domain.StartLoanDateTime

class LoanStarter(private val loanRepository: LoanRepository) {

    fun invoke(
        id: LoanId,
        accountId: AccountId,
        bookId: BookId,
        startedAt: StartLoanDateTime,
    ): Either<LoanStarterError, Unit> =
        startLoan(id, accountId, bookId, startedAt)
            .flatMap { loan -> loan.save() }

    private fun startLoan(id: LoanId, accountId: AccountId, bookId: BookId, startedAt: StartLoanDateTime): Either<LoanStarterError, Loan> =
        Either.catch { Loan(id, accountId, bookId, startedAt) }
            .mapLeft { LoanStarterError.UnknownError(it) }

    private fun Loan.save(): Either<LoanStarterError, Unit> =
        loanRepository.save(this)
            .mapLeft { com.github.caay2000.librarykata.eventdriven.context.account.application.loan.start.LoanStarterError.UnknownError(it) }
}

sealed class LoanStarterError : RuntimeException {
    constructor(throwable: Throwable) : super(throwable)

    class UnknownError(error: Throwable) : LoanStarterError(error)
}
