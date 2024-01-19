package com.github.caay2000.librarykata.event.context.account.application.loan.finish

import arrow.core.Either
import com.github.caay2000.common.database.RepositoryError
import com.github.caay2000.librarykata.event.context.account.application.LoanRepository
import com.github.caay2000.librarykata.event.context.account.domain.FinishLoanDateTime
import com.github.caay2000.librarykata.event.context.account.domain.Loan
import com.github.caay2000.librarykata.event.context.account.domain.LoanId

class LoanFinisher(private val loanRepository: LoanRepository) {
    fun invoke(
        loanId: LoanId,
        finishedAt: FinishLoanDateTime,
    ): Either<LoanFinisherError, Unit> =
        findLoan(loanId)
            .map { loan -> loan.finish(finishedAt) }
            .map { loan -> loan.save() }

    private fun findLoan(loanId: LoanId): Either<LoanFinisherError, Loan> =
        loanRepository.find(loanId)
            .mapLeft {
                when (it) {
                    is RepositoryError.NotFoundError -> LoanFinisherError.LoanNotFoundError(loanId)
                }
            }

    private fun Loan.save() {
        loanRepository.save(this)
    }
}

sealed class LoanFinisherError(message: String) : RuntimeException(message) {
    class LoanNotFoundError(loanId: LoanId) : LoanFinisherError("loan ${loanId.value} not found")
}
