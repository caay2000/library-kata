package com.github.caay2000.librarykata.context.book.application.loan.start

import arrow.core.Either
import arrow.core.flatMap
import com.github.caay2000.librarykata.context.book.application.LoanRepository
import com.github.caay2000.librarykata.context.book.domain.BookId
import com.github.caay2000.librarykata.context.book.domain.Loan
import com.github.caay2000.librarykata.context.book.domain.LoanId
import com.github.caay2000.librarykata.context.book.domain.StartLoanDateTime
import com.github.caay2000.librarykata.context.book.domain.UserId

class LoanStarter(private val loanRepository: LoanRepository) {

    fun invoke(
        id: LoanId,
        bookId: BookId,
        userId: UserId,
        startedAt: StartLoanDateTime,
    ): Either<LoanStarterError, Unit> =
        startLoan(id, bookId, userId, startedAt)
            .flatMap { loan -> loan.save() }

    private fun startLoan(id: LoanId, bookId: BookId, userId: UserId, startedAt: StartLoanDateTime): Either<LoanStarterError, Loan> =
        Either.catch { Loan.create(id, bookId, userId, startedAt) }
            .mapLeft { LoanStarterError.UnknownError(it) }

    private fun Loan.save(): Either<LoanStarterError, Unit> =
        loanRepository.save(this)
            .mapLeft { LoanStarterError.UnknownError(it) }
}

sealed class LoanStarterError : RuntimeException {
    constructor(throwable: Throwable) : super(throwable)

    class UnknownError(error: Throwable) : LoanStarterError(error)
}
