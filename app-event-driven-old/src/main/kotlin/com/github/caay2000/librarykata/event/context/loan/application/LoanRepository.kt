package com.github.caay2000.librarykata.event.context.loan.application

import arrow.core.Either
import com.github.caay2000.common.database.RepositoryError
import com.github.caay2000.librarykata.event.context.loan.domain.BookId
import com.github.caay2000.librarykata.event.context.loan.domain.Loan
import com.github.caay2000.librarykata.event.context.loan.domain.LoanId

interface LoanRepository {
    fun save(loan: Loan): Loan

    fun findBy(criteria: FindLoanCriteria): Either<RepositoryError, Loan>
}

sealed class FindLoanCriteria {
    class ById(val id: LoanId) : FindLoanCriteria()

    class ByBookIdAndNotFinished(val bookId: BookId) : FindLoanCriteria()
}