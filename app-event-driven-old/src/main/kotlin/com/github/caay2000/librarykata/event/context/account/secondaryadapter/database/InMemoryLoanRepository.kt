package com.github.caay2000.librarykata.event.context.account.secondaryadapter.database

import arrow.core.Either
import com.github.caay2000.common.database.RepositoryError
import com.github.caay2000.librarykata.event.context.account.application.LoanRepository
import com.github.caay2000.librarykata.event.context.account.domain.AccountId
import com.github.caay2000.librarykata.event.context.account.domain.Loan
import com.github.caay2000.librarykata.event.context.account.domain.LoanId
import com.github.caay2000.memorydb.InMemoryDatasource

class InMemoryLoanRepository(private val datasource: InMemoryDatasource) : LoanRepository {
    override fun save(loan: Loan): Loan = datasource.save(TABLE_NAME, loan.id.value, loan)

    override fun searchByAccountId(accountId: AccountId): List<Loan> = datasource.getAll<Loan>(TABLE_NAME).filter { it.accountId == accountId }

    override fun find(id: LoanId): Either<RepositoryError, Loan> =
        Either.catch { datasource.getById<Loan>(TABLE_NAME, id.value)!! }
            .mapLeft { error ->
                when (error) {
                    is NullPointerException -> RepositoryError.NotFoundError()
                    is NoSuchElementException -> RepositoryError.NotFoundError()
                    else -> throw error
                }
            }

    companion object {
        private const val TABLE_NAME = "account.loan"
    }
}
