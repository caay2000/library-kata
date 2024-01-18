package com.github.caay2000.librarykata.hexagonal.context.application.loan.search

import arrow.core.Either
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.Loan
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.LoanRepository
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.SearchLoanCriteria

class LoanSearcher(private val loanRepository: LoanRepository) {
    fun invoke(criteria: SearchLoanCriteria): Either<LoanSearcherError, List<Loan>> =
        loanRepository.search(criteria)
            .mapLeft { throw it }
}

sealed class LoanSearcherError(throwable: Throwable) : RuntimeException(throwable)
