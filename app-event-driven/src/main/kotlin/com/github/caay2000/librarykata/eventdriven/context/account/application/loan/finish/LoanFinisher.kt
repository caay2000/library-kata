package com.github.caay2000.librarykata.eventdriven.context.account.application.loan.finish

import arrow.core.Either
import arrow.core.flatMap
import com.github.caay2000.common.database.RepositoryError
import com.github.caay2000.librarykata.eventdriven.context.account.application.LoanRepository
import com.github.caay2000.librarykata.eventdriven.context.account.domain.FinishLoanDateTime
import com.github.caay2000.librarykata.eventdriven.context.account.domain.Loan
import com.github.caay2000.librarykata.eventdriven.context.account.domain.LoanId

class LoanFinisher(private val loanRepository: LoanRepository) {

    fun invoke(
        loanId: LoanId,
        finishedAt: FinishLoanDateTime,
    ): Either<LoanFinisherError, Unit> =
        findLoan(loanId)
            .map { loan -> loan.finish(finishedAt) }
            .flatMap { loan -> loan.save() }

    private fun findLoan(loanId: LoanId): Either<LoanFinisherError, Loan> =
        loanRepository.findById(loanId)
            .mapLeft {
                when (it) {
                    is RepositoryError.NotFoundError -> LoanFinisherError.LoanNotFoundError(loanId)
                    is RepositoryError.Unknown -> LoanFinisherError.UnknownError(it)
                }
            }

    private fun Loan.save(): Either<LoanFinisherError, Unit> =
        loanRepository.save(this)
            .mapLeft { com.github.caay2000.librarykata.eventdriven.context.account.application.loan.finish.LoanFinisherError.UnknownError(it) }
}

sealed class LoanFinisherError : RuntimeException {
    constructor(message: String) : super(message)
    constructor(throwable: Throwable) : super(throwable)

    class UnknownError(error: Throwable) : LoanFinisherError(error)
    class LoanNotFoundError(loanId: LoanId) : LoanFinisherError("loan ${loanId.value} not found")
}
