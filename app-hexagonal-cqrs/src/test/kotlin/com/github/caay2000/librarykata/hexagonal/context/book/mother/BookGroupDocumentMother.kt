package com.github.caay2000.librarykata.hexagonal.context.book.mother

import com.github.caay2000.common.jsonapi.JsonApiListDocument
import com.github.caay2000.librarykata.hexagonal.configuration.jsonMapper
import com.github.caay2000.librarykata.hexagonal.context.domain.book.Book
import com.github.caay2000.librarykata.hexagonal.context.domain.loan.Loan
import com.github.caay2000.librarykata.hexagonal.context.loan.mother.LoanMother
import com.github.caay2000.librarykata.jsonapi.context.book.BookGroupResource
import kotlinx.serialization.encodeToString

object BookGroupDocumentMother {
    fun random(
        book: Book = BookMother.random(),
        copies: Int = 1,
        available: Int = 1,
        loans: List<Loan> = List(3) { LoanMother.random(bookId = book.id) },
    ): JsonApiListDocument<BookGroupResource> =
        List(copies) { book }
            .mapIndexed { index, book -> if (index < available) book else book.unavailable() }
            .toJsonApiBookGroupDocument()
//            .toBookResource(loans)

//    fun random(books: List<Book> = List(Random.nextInt(3)) { BookMother.random() }): JsonApiListDocument<BookGroupResource> =
//        books.toJsonApiListDocument()

    fun json(
        book: Book = BookMother.random(),
        copies: Int = 1,
        available: Int = 1,
    ) = jsonMapper.encodeToString(random(book, copies, available))

    fun json(
        book: Book = BookMother.random(),
        copies: Int = 1,
        available: Int = 1,
        loans: List<Loan> = List(3) { LoanMother.random(bookId = book.id) },
    ) = jsonMapper.encodeToString(random(book, copies, available))
//
//    fun json(books: List<Book> = List(Random.nextInt(3)) { BookMother.random() }) = jsonMapper.encodeToString(random(books))
}
