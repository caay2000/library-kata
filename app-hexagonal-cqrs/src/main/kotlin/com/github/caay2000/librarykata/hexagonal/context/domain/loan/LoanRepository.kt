package com.github.caay2000.librarykata.hexagonal.context.domain.loan

import com.github.caay2000.librarykata.hexagonal.context.domain.account.AccountId
import com.github.caay2000.librarykata.hexagonal.context.domain.book.BookId
import com.github.caay2000.librarykata.hexagonal.context.domain.book.BookIsbn

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
    class ByAccountIdAndNotFinished(val accountId: AccountId) : SearchLoanCriteria()

    class ByBookId(val bookId: BookId) : SearchLoanCriteria()

    class ByBookIsbn(val bookIsbn: BookIsbn) : SearchLoanCriteria()
}
