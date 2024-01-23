package com.github.caay2000.librarykata.core.context.book.mother

import com.github.caay2000.librarykata.core.context.loan.mother.LoanMother
import com.github.caay2000.librarykata.hexagonal.context.book.domain.Book
import com.github.caay2000.librarykata.hexagonal.context.book.primaryadapter.http.transformer.toJsonApiBookDocument
import com.github.caay2000.librarykata.hexagonal.context.loan.domain.Loan

object BookDocumentMother {
    internal fun random(book: Book = BookMother.random()) = book.toJsonApiBookDocument()

    internal fun random(
        book: Book = BookMother.random(),
        loans: List<Loan> = List(3) { LoanMother.random(bookId = book.id) },
        include: List<String> = emptyList(),
    ) = book.toJsonApiBookDocument(loans, include)
}
