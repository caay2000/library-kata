package com.github.caay2000.librarykata.core.context.loan.mother

import com.github.caay2000.librarykata.hexagonal.context.account.domain.Account
import com.github.caay2000.librarykata.hexagonal.context.book.domain.Book
import com.github.caay2000.librarykata.hexagonal.context.loan.domain.Loan
import com.github.caay2000.librarykata.hexagonal.context.loan.primaryadapter.http.transformer.toJsonApiDocument
import com.github.caay2000.librarykata.hexagonal.context.loan.primaryadapter.http.transformer.toJsonApiLoanDocumentList

object LoanDocumentListMother {
    fun random(loan: Loan = LoanMother.random()) = loan.toJsonApiDocument()

    fun random(
        loans: List<Loan> = listOf(LoanMother.random()),
        accounts: List<Account> = emptyList(),
        books: List<Book> = emptyList(),
        include: List<String> = emptyList(),
    ) = loans.toJsonApiLoanDocumentList(accounts, books, include)
}
