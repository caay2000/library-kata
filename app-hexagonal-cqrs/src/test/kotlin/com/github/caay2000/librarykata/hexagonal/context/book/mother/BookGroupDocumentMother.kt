package com.github.caay2000.librarykata.hexagonal.context.book.mother

import com.github.caay2000.common.jsonapi.JsonApiListDocument
import com.github.caay2000.librarykata.hexagonal.configuration.jsonMapper
import com.github.caay2000.librarykata.hexagonal.context.domain.book.Book
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.Loan
import com.github.caay2000.librarykata.jsonapi.context.book.BookGroupResource
import kotlinx.serialization.encodeToString

object BookGroupDocumentMother {
    fun random(
        book: Book = BookMother.random(),
        copies: Int = 1,
        available: Int = 1,
        loans: List<Loan> = emptyList(),
    ): JsonApiListDocument<BookGroupResource> =
        List(copies) { book }
            .mapIndexed { index, it -> if (index < available) it else it.unavailable() }
            .toJsonApiBookGroupDocument(loans)

    fun random(
        books: List<BookCopies>,
        loans: List<Loan> = emptyList(),
    ): JsonApiListDocument<BookGroupResource> =
        books.flatMap { bookCopies ->
            List(bookCopies.copies) { bookCopies.book }
                .mapIndexed { index, book -> if (index < bookCopies.available) book else book.unavailable() }
        }
            .toJsonApiBookGroupDocument(loans)

    fun json(
        book: Book = BookMother.random(),
        copies: Int = 1,
        available: Int = 1,
    ) = jsonMapper.encodeToString(random(book, copies, available))

    fun json(
        book: Book = BookMother.random(),
        copies: Int = 1,
        available: Int = 1,
        loans: List<Loan> = emptyList(),
    ) = jsonMapper.encodeToString(random(book, copies, available, loans))

    fun json(
        books: List<BookCopies>,
        loans: List<Loan> = emptyList(),
    ) = jsonMapper.encodeToString(random(books, loans))

    data class BookCopies(
        val book: Book,
        val copies: Int = 1,
        val available: Int = copies,
    )
}
