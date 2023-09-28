package com.github.caay2000.librarykata.eventdriven.context.account.application

import arrow.core.Either
import com.github.caay2000.common.database.RepositoryError
import com.github.caay2000.librarykata.eventdriven.context.account.domain.Book
import com.github.caay2000.librarykata.eventdriven.context.account.domain.BookId

interface BookRepository {

    fun save(book: Book): Either<RepositoryError, Unit>

    fun findById(bookId: BookId): Either<RepositoryError, Book>
}
