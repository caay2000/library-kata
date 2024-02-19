package com.github.caay2000.librarykata.eventdriven.context.loan.secondaryadapter.database

import com.github.caay2000.librarykata.eventdriven.context.loan.domain.FindLoanCriteria
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.Loan
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.LoanRepository
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.SearchLoanCriteria
import com.github.caay2000.memorydb.InMemoryDatasource

class InMemoryLoanRepository(private val datasource: InMemoryDatasource) : LoanRepository {
    override fun save(loan: Loan) = datasource.save(TABLE_NAME, loan.id.value, loan)

    override fun find(criteria: FindLoanCriteria): Loan? =
        when (criteria) {
            is FindLoanCriteria.ById -> datasource.getById<Loan>(TABLE_NAME, criteria.id.value)
            is FindLoanCriteria.ByBookIdAndNotFinished -> datasource.getAll<Loan>(TABLE_NAME).firstOrNull { it.isNotFinished && it.bookId.value == criteria.bookId.value }
        }

    override fun search(criteria: SearchLoanCriteria): List<Loan> =
        when (criteria) {
            is SearchLoanCriteria.ByAccountId -> datasource.getAll<Loan>(TABLE_NAME).filter { it.accountId.value == criteria.accountId.value }
            is SearchLoanCriteria.ByBookId -> datasource.getAll<Loan>(TABLE_NAME).filter { it.bookId.value == criteria.bookId.value }
            is SearchLoanCriteria.ByBookIsbn -> TODO()
            is SearchLoanCriteria.AllLoan -> datasource.getAll(TABLE_NAME)
        }

    companion object {
        private const val TABLE_NAME = "loan.loan"
    }
}
