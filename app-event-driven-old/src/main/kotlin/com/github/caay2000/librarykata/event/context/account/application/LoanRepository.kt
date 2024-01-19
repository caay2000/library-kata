package com.github.caay2000.librarykata.event.context.account.application

import arrow.core.Either
import com.github.caay2000.common.database.RepositoryError
import com.github.caay2000.librarykata.event.context.account.domain.AccountId
import com.github.caay2000.librarykata.event.context.account.domain.Loan
import com.github.caay2000.librarykata.event.context.account.domain.LoanId

interface LoanRepository {
    fun save(loan: Loan): Loan

    // TODO change to search and use Criteria
    fun searchByAccountId(accountId: AccountId): List<Loan>

    fun find(id: LoanId): Either<RepositoryError, Loan>
}
