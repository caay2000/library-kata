package com.github.caay2000.librarykata.context.account.secondaryadapter.database

import arrow.core.Either
import com.github.caay2000.common.database.RepositoryError
import com.github.caay2000.librarykata.context.account.application.BookRepository
import com.github.caay2000.librarykata.context.account.domain.Book
import com.github.caay2000.librarykata.context.account.domain.BookId
import com.github.caay2000.memorydb.InMemoryDatasource

class InMemoryBookRepository(private val datasource: InMemoryDatasource) : BookRepository {

    override fun save(book: Book): Either<RepositoryError, Unit> =
        Either.catch { datasource.save(TABLE_NAME, book.id.toString(), book) }
            .mapLeft { RepositoryError.Unknown(it) }
            .map { }

    override fun findById(bookId: BookId): Either<RepositoryError, Book> =
        Either.catch { datasource.getById<Book>(TABLE_NAME, bookId.toString())!! }
            .mapLeft { error ->
                when (error) {
                    is NullPointerException -> RepositoryError.NotFoundError()
                    is NoSuchElementException -> RepositoryError.NotFoundError()
                    else -> RepositoryError.Unknown(error)
                }
            }

    companion object {
        private const val TABLE_NAME = "account.book"
    }
}
