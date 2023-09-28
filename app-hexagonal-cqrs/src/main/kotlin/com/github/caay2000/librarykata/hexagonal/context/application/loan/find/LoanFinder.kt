package com.github.caay2000.librarykata.hexagonal.context.application.loan.find

import arrow.core.Either
import com.github.caay2000.common.database.RepositoryError
import com.github.caay2000.librarykata.hexagonal.context.application.loan.FindLoanCriteria
import com.github.caay2000.librarykata.hexagonal.context.application.loan.LoanRepository
import com.github.caay2000.librarykata.hexagonal.context.domain.Loan
import com.github.caay2000.librarykata.hexagonal.context.domain.LoanId

class LoanFinder(private val loanRepository: LoanRepository) {

    fun invoke(loanId: LoanId): Either<LoanFinderError, Loan> =
        loanRepository.find(FindLoanCriteria.ById(loanId))
            .mapLeft { error ->
                when (error) {
                    is RepositoryError.NotFoundError -> LoanFinderError.LoanNotFoundError(loanId)
                    else -> LoanFinderError.Unknown(error)
                }
            }
}

sealed class LoanFinderError : RuntimeException {
    constructor(message: String) : super(message)
    constructor(throwable: Throwable) : super(throwable)

    class Unknown(error: Throwable) : LoanFinderError(error)
    class LoanNotFoundError(id: LoanId) : LoanFinderError("loan $id not found")
}
