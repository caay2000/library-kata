package com.github.caay2000.librarykata.hexagonal.context.loan.application.find

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.github.caay2000.librarykata.hexagonal.context.loan.domain.FindLoanCriteria
import com.github.caay2000.librarykata.hexagonal.context.loan.domain.Loan
import com.github.caay2000.librarykata.hexagonal.context.loan.domain.LoanId
import com.github.caay2000.librarykata.hexagonal.context.loan.domain.LoanRepository

class LoanFinder(private val loanRepository: LoanRepository) {
    fun invoke(loanId: LoanId): Either<LoanFinderError, Loan> =
        loanRepository.find(FindLoanCriteria.ById(loanId))?.right()
            ?: LoanFinderError.LoanNotFoundError(loanId).left()
}

sealed class LoanFinderError(message: String) : RuntimeException(message) {
    class LoanNotFoundError(id: LoanId) : LoanFinderError("Loan ${id.value} not found")
}
