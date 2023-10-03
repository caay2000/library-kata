package com.github.caay2000.librarykata.hexagonal.context.application.loan.search

import arrow.core.Either
import com.github.caay2000.librarykata.hexagonal.context.application.loan.LoanRepository
import com.github.caay2000.librarykata.hexagonal.context.application.loan.SearchLoanCriteria
import com.github.caay2000.librarykata.hexagonal.context.domain.AccountId
import com.github.caay2000.librarykata.hexagonal.context.domain.Loan

class LoanSearcher(private val loanRepository: LoanRepository) {

    fun invoke(accountId: AccountId): Either<LoanSearcherError, List<Loan>> =
        loanRepository.search(SearchLoanCriteria.ByAccountId(accountId))
            .mapLeft { LoanSearcherError.UnknownError(it) }
}

sealed class LoanSearcherError : RuntimeException {
    constructor(throwable: Throwable) : super(throwable)

    class UnknownError(error: Throwable) : LoanSearcherError(error)
}
