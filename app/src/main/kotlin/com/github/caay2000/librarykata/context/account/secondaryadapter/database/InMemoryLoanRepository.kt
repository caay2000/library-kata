package com.github.caay2000.librarykata.context.account.secondaryadapter.database

import arrow.core.Either
import com.github.caay2000.common.database.RepositoryError
import com.github.caay2000.librarykata.context.account.application.LoanRepository
import com.github.caay2000.librarykata.context.account.domain.AccountId
import com.github.caay2000.librarykata.context.account.domain.Loan
import com.github.caay2000.librarykata.context.account.domain.LoanId
import com.github.caay2000.memorydb.InMemoryDatasource

class InMemoryLoanRepository(private val datasource: InMemoryDatasource) : LoanRepository {

    override fun save(loan: Loan): Either<RepositoryError, Unit> =
        Either.catch { datasource.save(TABLE_NAME, loan.id.toString(), loan) }
            .mapLeft { RepositoryError.Unknown(it) }
            .map { }

    override fun searchByAccountId(accountId: AccountId): Either<RepositoryError, List<Loan>> =
        Either.catch {
            datasource.getAll<Loan>(TABLE_NAME).filter { it.accountId == accountId }
        }.mapLeft { RepositoryError.Unknown(it) }

    override fun findById(id: LoanId): Either<RepositoryError, Loan> =
        Either.catch { datasource.getById<Loan>(TABLE_NAME, id.toString())!! }
            .mapLeft { error ->
                when (error) {
                    is NullPointerException -> RepositoryError.NotFoundError()
                    is NoSuchElementException -> RepositoryError.NotFoundError()
                    else -> RepositoryError.Unknown(error)
                }
            }

    companion object {
        private const val TABLE_NAME = "account.loan"
    }
}
