package com.github.caay2000.librarykata.eventdriven.context.book.application.find

import arrow.core.Either
import com.github.caay2000.librarykata.eventdriven.context.book.domain.Book
import com.github.caay2000.librarykata.eventdriven.context.book.domain.BookId
import com.github.caay2000.librarykata.eventdriven.context.book.domain.BookRepository
import com.github.caay2000.librarykata.eventdriven.context.book.domain.FindBookCriteria
import com.github.caay2000.librarykata.eventdriven.context.book.domain.findOrElse

class BookFinder(private val bookRepository: BookRepository) {
    fun invoke(id: BookId): Either<BookFinderError, Book> =

        bookRepository.findOrElse(
            criteria = FindBookCriteria.ById(id),
            onResourceDoesNotExist = { BookFinderError.BookNotFoundError(id) },
        )
}

sealed class BookFinderError(message: String) : RuntimeException(message) {
    class BookNotFoundError(bookId: BookId) : BookFinderError("Book ${bookId.value} not found")
}
