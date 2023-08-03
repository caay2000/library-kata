package com.github.caay2000.librarykata.context.account.application.book.find

import arrow.core.Either
import com.github.caay2000.librarykata.context.account.application.BookRepository
import com.github.caay2000.librarykata.context.account.domain.Book
import com.github.caay2000.librarykata.context.account.domain.BookId

class BookFinder(private val bookRepository: BookRepository) {

    fun invoke(id: BookId): Either<BookFinderError, Book> =
        bookRepository.findById(id)
            .mapLeft { BookFinderError.Unknown(it) }
}

sealed class BookFinderError : RuntimeException {
    constructor(throwable: Throwable) : super(throwable)

    class Unknown(error: Throwable) : BookFinderError(error)
}
