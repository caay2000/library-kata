package com.github.caay2000.librarykata.event.context.loan.secondaryadapter.database

import arrow.core.Either
import com.github.caay2000.common.database.RepositoryError
import com.github.caay2000.librarykata.event.context.loan.application.FindLoanCriteria
import com.github.caay2000.librarykata.event.context.loan.application.LoanRepository
import com.github.caay2000.librarykata.event.context.loan.domain.Loan
import com.github.caay2000.memorydb.InMemoryDatasource

class InMemoryLoanRepository(private val datasource: InMemoryDatasource) : LoanRepository {
    companion object {
        private const val TABLE_NAME = "loan.loan"
    }

    override fun save(loan: Loan): Loan = datasource.save(TABLE_NAME, loan.id.toString(), loan)

    override fun findBy(criteria: FindLoanCriteria): Either<RepositoryError, Loan> =
        Either.catch {
            when (criteria) {
                is FindLoanCriteria.ById -> datasource.getById<Loan>(TABLE_NAME, criteria.id.toString())!!
                is FindLoanCriteria.ByBookIdAndNotFinished -> datasource.getAll<Loan>(TABLE_NAME).filter { it.bookId == criteria.bookId }.first { it.isNotFinished }
            }
        }.mapLeft { error ->
            when (error) {
                is NullPointerException -> RepositoryError.NotFoundError()
                is NoSuchElementException -> RepositoryError.NotFoundError()
                else -> throw error
            }
        }
}
