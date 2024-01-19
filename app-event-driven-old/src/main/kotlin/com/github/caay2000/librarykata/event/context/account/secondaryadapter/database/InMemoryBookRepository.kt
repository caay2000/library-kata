package com.github.caay2000.librarykata.event.context.account.secondaryadapter.database

import arrow.core.Either
import com.github.caay2000.common.database.RepositoryError
import com.github.caay2000.librarykata.event.context.account.application.BookRepository
import com.github.caay2000.librarykata.event.context.account.domain.Book
import com.github.caay2000.librarykata.event.context.account.domain.BookId
import com.github.caay2000.memorydb.InMemoryDatasource

class InMemoryBookRepository(private val datasource: InMemoryDatasource) : BookRepository {
    override fun save(book: Book): Book = datasource.save(TABLE_NAME, book.id.toString(), book)

    override fun find(bookId: BookId): Either<RepositoryError, Book> =
        Either.catch { datasource.getById<Book>(TABLE_NAME, bookId.toString())!! }
            .mapLeft { error ->
                when (error) {
                    is NullPointerException -> RepositoryError.NotFoundError()
                    is NoSuchElementException -> RepositoryError.NotFoundError()
                    else -> throw error
                }
            }

    companion object {
        private const val TABLE_NAME = "account.book"
    }
}
