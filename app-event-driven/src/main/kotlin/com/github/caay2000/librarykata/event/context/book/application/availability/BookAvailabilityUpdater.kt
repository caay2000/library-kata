package com.github.caay2000.librarykata.event.context.book.application.availability

import arrow.core.Either
import com.github.caay2000.common.database.RepositoryError
import com.github.caay2000.librarykata.event.context.book.application.BookRepository
import com.github.caay2000.librarykata.event.context.book.domain.Book
import com.github.caay2000.librarykata.event.context.book.domain.BookAvailable
import com.github.caay2000.librarykata.event.context.book.domain.BookId

class BookAvailabilityUpdater(private val bookRepository: BookRepository) {
    fun invoke(
        bookId: BookId,
        available: BookAvailable,
    ): Either<BookAvailabilityUpdaterError, Unit> =
        findBook(bookId)
            .map { book -> book.updateAvailability(available) }
            .map { book -> book.save() }

    private fun findBook(bookId: BookId): Either<BookAvailabilityUpdaterError, Book> =
        bookRepository.find(bookId)
            .mapLeft { error ->
                when (error) {
                    is RepositoryError.NotFoundError -> BookAvailabilityUpdaterError.BookNotFound(bookId)
                    else -> throw error
                }
            }

    private fun Book.save() {
        bookRepository.save(this)
    }
}

sealed class BookAvailabilityUpdaterError(message: String) : RuntimeException(message) {
    class BookNotFound(bookId: BookId) : BookAvailabilityUpdaterError("book $bookId not found")
}
