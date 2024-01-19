package com.github.caay2000.librarykata.eventdriven.context.book.secondaryadapter.database

import arrow.core.Either
import com.github.caay2000.common.database.RepositoryError
import com.github.caay2000.librarykata.eventdriven.context.book.application.BookRepository
import com.github.caay2000.librarykata.eventdriven.context.book.application.SearchBookCriteria
import com.github.caay2000.librarykata.eventdriven.context.book.domain.Book
import com.github.caay2000.librarykata.eventdriven.context.book.domain.BookId
import com.github.caay2000.memorydb.InMemoryDatasource

class InMemoryBookRepository(private val datasource: InMemoryDatasource) : BookRepository {
    override fun save(book: Book) {
        datasource.save(TABLE_NAME, book.id.toString(), book)
    }

    override fun search(criteria: SearchBookCriteria): List<Book> =
        when (criteria) {
            SearchBookCriteria.All -> datasource.getAll(TABLE_NAME)
            is SearchBookCriteria.ByIsbn -> datasource.getAll<Book>(TABLE_NAME).filter { it.isbn == criteria.isbn }
        }

    override fun find(id: BookId): Either<RepositoryError, Book> =
        Either.catch { datasource.getById<Book>(TABLE_NAME, id.toString())!! }
            .mapLeft { error ->
                when (error) {
                    is NullPointerException -> RepositoryError.NotFoundError()
                    is NoSuchElementException -> RepositoryError.NotFoundError()
                    else -> throw error
                }
            }

    companion object {
        private const val TABLE_NAME = "book.book"
    }
}
