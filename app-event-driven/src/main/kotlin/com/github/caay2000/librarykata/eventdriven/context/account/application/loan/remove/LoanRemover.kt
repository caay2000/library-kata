package com.github.caay2000.librarykata.eventdriven.context.account.application.loan.remove

import com.github.caay2000.librarykata.eventdriven.context.account.domain.LoanId
import com.github.caay2000.librarykata.eventdriven.context.account.domain.LoanRepository

class LoanRemover(private val loanRepository: LoanRepository) {
    fun invoke(loanId: LoanId) = loanRepository.delete(loanId)
}
