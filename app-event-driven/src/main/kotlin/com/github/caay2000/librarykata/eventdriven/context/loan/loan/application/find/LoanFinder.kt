package com.github.caay2000.librarykata.eventdriven.context.loan.loan.application.find

import arrow.core.Either
import com.github.caay2000.librarykata.eventdriven.context.loan.loan.domain.FindLoanCriteria
import com.github.caay2000.librarykata.eventdriven.context.loan.loan.domain.Loan
import com.github.caay2000.librarykata.eventdriven.context.loan.loan.domain.LoanId
import com.github.caay2000.librarykata.eventdriven.context.loan.loan.domain.LoanRepository
import com.github.caay2000.librarykata.eventdriven.context.loan.loan.domain.findOrElse

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
