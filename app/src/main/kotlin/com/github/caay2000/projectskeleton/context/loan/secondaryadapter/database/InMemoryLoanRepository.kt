package com.github.caay2000.projectskeleton.context.loan.secondaryadapter.database

import arrow.core.Either
import com.github.caay2000.common.database.RepositoryError
import com.github.caay2000.memorydb.InMemoryDatasource
import com.github.caay2000.projectskeleton.context.loan.application.LoanRepository
import com.github.caay2000.projectskeleton.context.loan.domain.Loan
import com.github.caay2000.projectskeleton.context.loan.domain.LoanId

class InMemoryLoanRepository(private val datasource: InMemoryDatasource) : LoanRepository {

    companion object {
        private const val TABLE_NAME = "loan.loan"
    }

    override fun save(loan: Loan): Either<RepositoryError, Unit> =
        Either.catch { datasource.save(TABLE_NAME, loan.id.toString(), loan) }
            .mapLeft { RepositoryError.Unknown(it) }
            .map { }

    override fun findById(id: LoanId): Either<RepositoryError, Loan> =
        Either.catch { datasource.getById<Loan>(TABLE_NAME, id.toString())!! }
            .mapLeft { error ->
                when (error) {
                    is NullPointerException -> RepositoryError.NotFoundError()
                    is NoSuchElementException -> RepositoryError.NotFoundError()
                    else -> RepositoryError.Unknown(error)
                }
            }
}
