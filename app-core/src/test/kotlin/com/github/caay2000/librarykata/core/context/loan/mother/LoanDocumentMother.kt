package com.github.caay2000.librarykata.core.context.loan.mother

import com.github.caay2000.librarykata.hexagonal.context.account.domain.Account
import com.github.caay2000.librarykata.hexagonal.context.book.domain.Book
import com.github.caay2000.librarykata.hexagonal.context.loan.domain.Loan
import com.github.caay2000.librarykata.hexagonal.context.loan.primaryadapter.http.serialization.toJsonApiDocument

object LoanDocumentMother {
    fun random(loan: Loan = LoanMother.random()) = loan.toJsonApiDocument()

    fun random(
        loan: Loan = LoanMother.random(),
        account: Account? = null,
        book: Book? = null,
        include: List<String> = emptyList(),
    ) = loan.toJsonApiDocument(account, book, include)
}
