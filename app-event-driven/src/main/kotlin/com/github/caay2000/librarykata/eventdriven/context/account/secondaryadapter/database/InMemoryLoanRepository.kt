package com.github.caay2000.librarykata.eventdriven.context.account.secondaryadapter.database

import com.github.caay2000.librarykata.eventdriven.context.account.domain.AccountId
import com.github.caay2000.librarykata.eventdriven.context.account.domain.Loan
import com.github.caay2000.librarykata.eventdriven.context.account.domain.LoanId
import com.github.caay2000.librarykata.eventdriven.context.account.domain.LoanRepository
import com.github.caay2000.memorydb.InMemoryDatasource

class InMemoryLoanRepository(private val datasource: InMemoryDatasource) : LoanRepository {
    override fun save(loan: Loan): Loan = datasource.save(TABLE_NAME, loan.id.value, loan)

    override fun delete(loanId: LoanId) = datasource.delete(TABLE_NAME, loanId.value)

    override fun search(accountId: AccountId): List<Loan> = datasource.getAll<Loan>(TABLE_NAME).filter { it.accountId == accountId }

    companion object {
        private const val TABLE_NAME = "account.loan"
    }
}
