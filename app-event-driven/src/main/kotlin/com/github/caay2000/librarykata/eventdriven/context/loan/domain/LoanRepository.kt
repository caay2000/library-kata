package com.github.caay2000.librarykata.eventdriven.context.loan.domain

import com.github.caay2000.librarykata.eventdriven.context.account.domain.AccountId
import com.github.caay2000.librarykata.eventdriven.context.book.domain.BookId
import com.github.caay2000.librarykata.eventdriven.context.book.domain.BookIsbn

interface LoanRepository {
    fun save(loan: Loan): Loan

    fun find(criteria: FindLoanCriteria): Loan?

    fun search(criteria: SearchLoanCriteria): List<Loan>
}

sealed class FindLoanCriteria {
    class ById(val id: LoanId) : FindLoanCriteria()

    class ByBookIdAndNotFinished(val bookId: BookId) : FindLoanCriteria()
}

sealed class SearchLoanCriteria {
    class ByAccountId(val accountId: AccountId) : SearchLoanCriteria()

    class ByBookId(val bookId: BookId) : SearchLoanCriteria()

    class ByBookIsbn(val bookIsbn: BookIsbn) : SearchLoanCriteria()
}
