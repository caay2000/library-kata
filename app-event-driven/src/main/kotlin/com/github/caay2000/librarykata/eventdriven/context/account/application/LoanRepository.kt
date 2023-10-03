package com.github.caay2000.librarykata.eventdriven.context.account.application

import arrow.core.Either
import com.github.caay2000.common.database.RepositoryError
import com.github.caay2000.librarykata.eventdriven.context.account.domain.AccountId
import com.github.caay2000.librarykata.eventdriven.context.account.domain.Loan
import com.github.caay2000.librarykata.eventdriven.context.account.domain.LoanId

interface LoanRepository {

    fun save(loan: Loan): Either<RepositoryError, Unit>
    fun searchByAccountId(accountId: AccountId): Either<RepositoryError, List<Loan>>

    fun findById(id: LoanId): Either<RepositoryError, Loan>
}
