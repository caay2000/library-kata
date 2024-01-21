package com.github.caay2000.librarykata.eventdriven.context.account.application.loan.search

import com.github.caay2000.librarykata.eventdriven.context.account.domain.AccountId
import com.github.caay2000.librarykata.eventdriven.context.account.domain.Loan
import com.github.caay2000.librarykata.eventdriven.context.account.domain.LoanRepository

class LoanSearcher(private val loanRepository: LoanRepository) {
    fun invoke(accountId: AccountId): List<Loan> = loanRepository.search(accountId)
}
