package com.github.caay2000.librarykata.context.book.application.loan.search

import arrow.core.Either
import com.github.caay2000.librarykata.context.book.application.LoanRepository
import com.github.caay2000.librarykata.context.book.domain.Loan
import com.github.caay2000.librarykata.context.book.domain.UserId

class LoanSearcher(private val loanRepository: LoanRepository) {

    fun invoke(userId: UserId): Either<LoanSearcherError, List<Loan>> =
        loanRepository.searchByUserId(userId)
            .mapLeft { LoanSearcherError.UnknownError(it) }
}

sealed class LoanSearcherError : RuntimeException {
    constructor(throwable: Throwable) : super(throwable)

    class UnknownError(error: Throwable) : LoanSearcherError(error)
}
