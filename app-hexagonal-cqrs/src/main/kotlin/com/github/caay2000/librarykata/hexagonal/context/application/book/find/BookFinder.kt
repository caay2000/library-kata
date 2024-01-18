package com.github.caay2000.librarykata.hexagonal.context.application.book.find

import arrow.core.Either
import com.github.caay2000.common.database.RepositoryError
import com.github.caay2000.librarykata.hexagonal.context.domain.book.Book
import com.github.caay2000.librarykata.hexagonal.context.domain.book.BookId
import com.github.caay2000.librarykata.hexagonal.context.domain.book.BookRepository
import com.github.caay2000.librarykata.hexagonal.context.domain.book.FindBookCriteria

class BookFinder(private val bookRepository: BookRepository) {
    fun invoke(criteria: FindBookCriteria): Either<BookFinderError, Book> =
        bookRepository.find(criteria)
            .mapLeft {
                when (it) {
                    is RepositoryError.NotFoundError -> {
                        when (criteria) {
                            is FindBookCriteria.ById -> BookFinderError.BookNotFoundError(criteria.id)
                        }
                    }
                }
            }
}

sealed class BookFinderError(message: String) : RuntimeException(message) {
    class BookNotFoundError(bookId: BookId) : BookFinderError("Book ${bookId.value} not found")
}
