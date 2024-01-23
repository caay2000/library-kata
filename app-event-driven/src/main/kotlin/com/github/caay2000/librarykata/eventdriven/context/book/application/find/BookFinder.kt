package com.github.caay2000.librarykata.eventdriven.context.book.application.find

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.github.caay2000.librarykata.eventdriven.context.book.domain.Book
import com.github.caay2000.librarykata.eventdriven.context.book.domain.BookId
import com.github.caay2000.librarykata.eventdriven.context.book.domain.BookRepository
import com.github.caay2000.librarykata.eventdriven.context.book.domain.FindBookCriteria

class BookFinder(private val bookRepository: BookRepository) {
    fun invoke(id: BookId): Either<BookFinderError, Book> =
        bookRepository.find(FindBookCriteria.ById(id))
            ?.right()
            ?: BookFinderError.BookNotFoundError(id).left()
}

sealed class BookFinderError(message: String) : RuntimeException(message) {
    class BookNotFoundError(bookId: BookId) : BookFinderError("Book ${bookId.value} not found")
}
