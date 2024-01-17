package com.github.caay2000.librarykata.hexagonal.context.loan.mother

import com.github.caay2000.librarykata.hexagonal.context.domain.account.Account
import com.github.caay2000.librarykata.hexagonal.context.domain.book.Book
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.Loan
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.loan.serialization.toJsonApiDocument

object LoanDocumentMother {
    fun random(loan: Loan = LoanMother.random()) = loan.toJsonApiDocument()

    fun random(
        loan: Loan = LoanMother.random(),
        account: Account? = null,
        book: Book? = null,
        include: List<String> = emptyList(),
    ) = loan.toJsonApiDocument(account, book, include)
}
