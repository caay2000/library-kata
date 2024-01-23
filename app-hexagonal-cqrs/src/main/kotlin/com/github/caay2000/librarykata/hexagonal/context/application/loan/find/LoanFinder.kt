package com.github.caay2000.librarykata.hexagonal.context.application.loan.find

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.FindLoanCriteria
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.Loan
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.LoanId
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.LoanRepository

class LoanFinder(private val loanRepository: LoanRepository) {
    fun invoke(loanId: LoanId): Either<LoanFinderError, Loan> =
        loanRepository.find(FindLoanCriteria.ById(loanId))?.right()
            ?: LoanFinderError.LoanNotFoundError(loanId).left()
}

sealed class LoanFinderError(message: String) : RuntimeException(message) {
    class LoanNotFoundError(id: LoanId) : LoanFinderError("Loan ${id.value} not found")
}
