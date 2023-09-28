package com.github.caay2000.librarykata.hexagonal.context.secondaryadapter.database

import arrow.core.Either
import com.github.caay2000.common.database.RepositoryError
import com.github.caay2000.librarykata.hexagonal.context.application.loan.FindLoanCriteria
import com.github.caay2000.librarykata.hexagonal.context.application.loan.LoanRepository
import com.github.caay2000.librarykata.hexagonal.context.application.loan.SearchLoanCriteria
import com.github.caay2000.librarykata.hexagonal.context.domain.Loan
import com.github.caay2000.memorydb.InMemoryDatasource

class InMemoryLoanRepository(private val datasource: InMemoryDatasource) : LoanRepository {

    override fun save(loan: Loan): Either<RepositoryError, Unit> =
        Either.catch { datasource.save(TABLE_NAME, loan.id.toString(), loan) }
            .mapLeft { RepositoryError.Unknown(it) }
            .map { }

    override fun find(criteria: FindLoanCriteria): Either<RepositoryError, Loan> =
        Either.catch {
            when (criteria) {
                is FindLoanCriteria.ById -> datasource.getById<Loan>(TABLE_NAME, criteria.id.toString())!!
                is FindLoanCriteria.ByBookIdAndNotFinished -> datasource.getAll<Loan>(TABLE_NAME).filter { it.bookId == criteria.bookId }.first { it.isNotFinished }
            }
        }.mapLeft { error ->
            when (error) {
                is NullPointerException -> RepositoryError.NotFoundError()
                is NoSuchElementException -> RepositoryError.NotFoundError()
                else -> RepositoryError.Unknown(error)
            }
        }

    override fun search(criteria: SearchLoanCriteria): Either<RepositoryError, List<Loan>> =
        when (criteria) {
            is SearchLoanCriteria.ByAccountId -> Either.catch { datasource.getAll<Loan>(TABLE_NAME).filter { it.accountId == criteria.accountId } }
        }.mapLeft { RepositoryError.Unknown(it) }

    companion object {
        private const val TABLE_NAME = "loan"
    }
}
