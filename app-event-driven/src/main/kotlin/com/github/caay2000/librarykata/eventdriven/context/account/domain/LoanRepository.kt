package com.github.caay2000.librarykata.eventdriven.context.account.domain

import com.github.caay2000.common.database.Repository

interface LoanRepository : Repository {
    fun save(loan: Loan): Loan

    fun search(accountId: AccountId): List<Loan>
}
