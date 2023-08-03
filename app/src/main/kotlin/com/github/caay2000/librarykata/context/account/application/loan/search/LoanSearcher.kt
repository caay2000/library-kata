package com.github.caay2000.librarykata.context.account.application.loan.search

import arrow.core.Either
import com.github.caay2000.librarykata.context.account.application.LoanRepository
import com.github.caay2000.librarykata.context.account.domain.AccountId
import com.github.caay2000.librarykata.context.account.domain.Loan

class LoanSearcher(private val loanRepository: LoanRepository) {

    fun invoke(accountId: AccountId): Either<LoanSearcherError, List<Loan>> =
        loanRepository.searchByAccountId(accountId)
            .mapLeft { LoanSearcherError.UnknownError(it) }
}

sealed class LoanSearcherError : RuntimeException {
    constructor(throwable: Throwable) : super(throwable)

    class UnknownError(error: Throwable) : LoanSearcherError(error)
}
