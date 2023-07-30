package com.github.caay2000.librarykata.context.book.application.search

import arrow.core.Either
import com.github.caay2000.librarykata.context.book.application.BookRepository
import com.github.caay2000.librarykata.context.book.application.SearchBookCriteria
import com.github.caay2000.librarykata.context.book.domain.Book

class BookSearcher(private val bookRepository: BookRepository) {

    fun invoke(criteria: SearchBookCriteria): Either<BookSearcherError, List<Book>> =
        bookRepository.search(criteria)
            .mapLeft { BookSearcherError.Unknown(it) }
}

sealed class BookSearcherError : RuntimeException {
    constructor(throwable: Throwable) : super(throwable)

    class Unknown(error: Throwable) : BookSearcherError(error)
}
