package com.github.caay2000.librarykata.event.context.loan.secondaryadapter.database

import arrow.core.Either
import com.github.caay2000.common.database.RepositoryError
import com.github.caay2000.librarykata.event.context.loan.application.BookRepository
import com.github.caay2000.librarykata.event.context.loan.domain.Book
import com.github.caay2000.librarykata.event.context.loan.domain.BookId
import com.github.caay2000.librarykata.event.context.loan.domain.BookIsbn
import com.github.caay2000.memorydb.InMemoryDatasource

class InMemoryBookRepository(private val datasource: InMemoryDatasource) : BookRepository {
    companion object {
        private const val TABLE_NAME = "loan.book"
    }

    override fun save(book: Book): Book = datasource.save(TABLE_NAME, book.id.value, book)

    override fun find(id: BookId): Either<RepositoryError, Book> =
        Either.catch { datasource.getById<Book>(TABLE_NAME, id.value)!! }
            .mapLeft { error ->
                when (error) {
                    is NullPointerException -> RepositoryError.NotFoundError()
                    is NoSuchElementException -> RepositoryError.NotFoundError()
                    else -> throw error
                }
            }

    override fun searchByIsbn(isbn: BookIsbn): List<Book> = datasource.getAll<Book>(TABLE_NAME).filter { it.isbn == isbn }
}
