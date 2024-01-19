package com.github.caay2000.librarykata.hexagonal.context.application.loan.find

import arrow.core.Either
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.FindLoanCriteria
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.Loan
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.LoanId
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.LoanRepository
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.findOrElse

class LoanFinder(private val loanRepository: LoanRepository) {
    fun invoke(loanId: LoanId): Either<LoanFinderError, Loan> =
        loanRepository.findOrElse(
            criteria = FindLoanCriteria.ById(loanId),
            onResourceDoesNotExist = { LoanFinderError.LoanNotFoundError(loanId) },
        )
}

sealed class LoanFinderError(message: String) : RuntimeException(message) {
    class LoanNotFoundError(id: LoanId) : LoanFinderError("Loan ${id.value} not found")
}
