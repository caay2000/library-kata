package com.github.caay2000.librarykata.hexagonal.context.book.mother

import com.github.caay2000.librarykata.hexagonal.context.domain.book.Book
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.Loan
import com.github.caay2000.librarykata.hexagonal.context.loan.mother.LoanMother
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.book.transformer.toJsonApiBookDocument

object BookDocumentMother {
    internal fun random(book: Book = BookMother.random()) = book.toJsonApiBookDocument()

    internal fun random(
        book: Book = BookMother.random(),
        loans: List<Loan> = List(3) { LoanMother.random(bookId = book.id) },
        include: List<String> = emptyList(),
    ) = book.toJsonApiBookDocument(loans, include)
}
