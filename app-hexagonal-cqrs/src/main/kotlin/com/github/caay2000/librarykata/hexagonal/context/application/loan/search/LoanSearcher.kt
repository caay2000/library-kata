package com.github.caay2000.librarykata.hexagonal.context.application.loan.search

import arrow.core.Either
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.Loan
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.LoanRepository
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.SearchLoanCriteria

class LoanSearcher(private val loanRepository: LoanRepository) {
    fun invoke(criteria: SearchLoanCriteria): Either<LoanSearcherError, List<Loan>> =
        loanRepository.search(criteria)
            .mapLeft { LoanSearcherError.UnknownError(it) }
}

sealed class LoanSearcherError : RuntimeException {
    constructor(throwable: Throwable) : super(throwable)

    class UnknownError(error: Throwable) : LoanSearcherError(error)
}
