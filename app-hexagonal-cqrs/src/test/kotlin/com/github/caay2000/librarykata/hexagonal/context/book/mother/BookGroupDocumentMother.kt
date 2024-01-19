package com.github.caay2000.librarykata.hexagonal.context.book.mother

import com.github.caay2000.common.jsonapi.JsonApiDocumentList
import com.github.caay2000.common.jsonapi.JsonApiMeta
import com.github.caay2000.librarykata.hexagonal.context.domain.book.Book
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.Loan
import com.github.caay2000.librarykata.hexagonal.context.primaryadapter.http.book.transformer.toJsonApiBookGroupDocumentList
import com.github.caay2000.librarykata.jsonapi.context.book.BookGroupResource

object BookGroupDocumentMother {
    fun random(
        book: Book = BookMother.random(),
        copies: Int = 1,
        available: Int = 1,
        loans: List<Loan> = emptyList(),
    ): JsonApiDocumentList<BookGroupResource> =
        List(copies) { book }
            .mapIndexed { index, it -> if (index < available) it else it.unavailable() }
            .toJsonApiBookGroupDocumentList(loans)

    fun random(
        books: List<BookCopies>,
        loans: List<Loan> = emptyList(),
    ): JsonApiDocumentList<BookGroupResource> =
        books.flatMap { bookCopies ->
            List(bookCopies.copies) { bookCopies.book }
                .mapIndexed { index, book -> if (index < bookCopies.available) book else book.unavailable() }
        }.toJsonApiBookGroupDocumentList(loans)

    fun empty() = JsonApiDocumentList<BookGroupResource>(data = emptyList(), meta = JsonApiMeta(0))

    data class BookCopies(
        val book: Book,
        val copies: Int = 1,
        val available: Int = copies,
    )
}
