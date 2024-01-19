package com.github.caay2000.librarykata.eventdriven.context.loan.loan.application.search

import com.github.caay2000.librarykata.eventdriven.context.loan.loan.domain.Loan
import com.github.caay2000.librarykata.eventdriven.context.loan.loan.domain.LoanRepository
import com.github.caay2000.librarykata.eventdriven.context.loan.loan.domain.SearchLoanCriteria

class LoanSearcher(private val loanRepository: LoanRepository) {
    fun invoke(criteria: SearchLoanCriteria): List<Loan> = loanRepository.search(criteria)
}
