package com.github.caay2000.librarykata.eventdriven.context.account.application.loan.search

import com.github.caay2000.librarykata.eventdriven.context.account.application.LoanRepository
import com.github.caay2000.librarykata.eventdriven.context.account.domain.AccountId
import com.github.caay2000.librarykata.eventdriven.context.account.domain.Loan

class LoanSearcher(private val loanRepository: LoanRepository) {
    fun invoke(accountId: AccountId): List<Loan> = loanRepository.searchByAccountId(accountId)
}
