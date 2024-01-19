package com.github.caay2000.librarykata.eventdriven.context.account.application.book.find

import arrow.core.Either
import com.github.caay2000.common.database.RepositoryError
import com.github.caay2000.librarykata.eventdriven.context.account.application.BookRepository
import com.github.caay2000.librarykata.eventdriven.context.account.domain.Book
import com.github.caay2000.librarykata.eventdriven.context.account.domain.BookId

class BookFinder(private val bookRepository: BookRepository) {
    fun invoke(id: BookId): Either<BookFinderError, Book> =
        bookRepository.find(id)
            .mapLeft {
                when (it) {
                    is RepositoryError.NotFoundError -> BookFinderError.BookNotFound(id)
                }
            }
}

sealed class BookFinderError(message: String) : RuntimeException(message) {
    class BookNotFound(bookId: BookId) : BookFinderError("Book ${bookId.value} not found")
}
