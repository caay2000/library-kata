package com.github.caay2000.librarykata.hexagonal.context.application.loan.finish

import arrow.core.Either
import arrow.core.flatMap
import com.github.caay2000.common.database.RepositoryError
import com.github.caay2000.librarykata.hexagonal.context.application.loan.FindLoanCriteria
import com.github.caay2000.librarykata.hexagonal.context.application.loan.LoanRepository
import com.github.caay2000.librarykata.hexagonal.context.domain.BookId
import com.github.caay2000.librarykata.hexagonal.context.domain.FinishedAt
import com.github.caay2000.librarykata.hexagonal.context.domain.Loan

class LoanFinisher(
    private val loanRepository: LoanRepository,
) {

    fun invoke(
        bookId: BookId,
        finishedAt: FinishedAt,
    ): Either<LoanFinisherError, Unit> =
        findLoan(bookId)
            .map { loan -> loan.finishLoan(finishedAt) }
            .flatMap { loan -> loan.save() }

    private fun findLoan(bookId: BookId): Either<LoanFinisherError, Loan> =
        loanRepository.find(FindLoanCriteria.ByBookIdAndNotFinished(bookId))
            .mapLeft { error ->
                when (error) {
                    is RepositoryError.NotFoundError -> LoanFinisherError.LoanNotFound(bookId)
                    else -> LoanFinisherError.UnknownError(error)
                }
            }

    private fun Loan.save(): Either<LoanFinisherError, Unit> =
        loanRepository.save(this)
            .mapLeft { LoanFinisherError.UnknownError(it) }
}

sealed class LoanFinisherError : RuntimeException {
    constructor(message: String) : super(message)
    constructor(throwable: Throwable) : super(throwable)

    class LoanNotFound(bookId: BookId) : LoanFinisherError("loan for book ${bookId.value} not found")
    class UnknownError(error: Throwable) : LoanFinisherError(error)
}
