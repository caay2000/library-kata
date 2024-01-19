package com.github.caay2000.librarykata.event.context.account.application

import arrow.core.Either
import com.github.caay2000.common.database.RepositoryError
import com.github.caay2000.librarykata.event.context.account.domain.Book
import com.github.caay2000.librarykata.event.context.account.domain.BookId

interface BookRepository {
    fun save(book: Book): Book

    fun find(bookId: BookId): Either<RepositoryError, Book>
}
