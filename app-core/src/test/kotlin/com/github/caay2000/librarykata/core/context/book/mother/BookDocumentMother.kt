package com.github.caay2000.librarykata.core.context.book.mother

import com.github.caay2000.librarykata.hexagonal.context.account.domain.Account
import com.github.caay2000.librarykata.hexagonal.context.book.domain.Book
import com.github.caay2000.librarykata.hexagonal.context.book.primaryadapter.http.transformer.toJsonApiBookDocument
import com.github.caay2000.librarykata.hexagonal.context.loan.domain.Loan

object BookDocumentMother {
    internal fun random(book: Book = BookMother.random()) = book.toJsonApiBookDocument()

    internal fun random(
        book: Book = BookMother.random(),
        accounts: List<Account> = emptyList(),
        loans: List<Loan> = emptyList(),
        include: List<String> = emptyList(),
    ) = book.toJsonApiBookDocument(accounts, loans, include)
}
