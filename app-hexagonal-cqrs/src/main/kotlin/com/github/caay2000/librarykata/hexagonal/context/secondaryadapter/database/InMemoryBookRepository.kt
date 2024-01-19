package com.github.caay2000.librarykata.hexagonal.context.secondaryadapter.database

import arrow.core.Either
import com.github.caay2000.common.database.RepositoryError
import com.github.caay2000.librarykata.hexagonal.context.domain.book.Book
import com.github.caay2000.librarykata.hexagonal.context.domain.book.BookRepository
import com.github.caay2000.librarykata.hexagonal.context.domain.book.FindBookCriteria
import com.github.caay2000.librarykata.hexagonal.context.domain.book.SearchBookCriteria
import com.github.caay2000.memorydb.InMemoryDatasource

class InMemoryBookRepository(private val datasource: InMemoryDatasource) : BookRepository {
    override fun save(book: Book) {
        datasource.save(TABLE_NAME, book.id.toString(), book)
    }

    override fun find(criteria: FindBookCriteria): Either<RepositoryError, Book> =
        when (criteria) {
            is FindBookCriteria.ById -> Either.catch { datasource.getById<Book>(TABLE_NAME, criteria.id.toString())!! }
        }.mapLeft { error ->
            when (error) {
                is NullPointerException -> RepositoryError.NotFoundError()
                is NoSuchElementException -> RepositoryError.NotFoundError()
                else -> throw error
            }
        }

    override fun search(criteria: SearchBookCriteria): List<Book> =
        when (criteria) {
            SearchBookCriteria.All -> datasource.getAll(TABLE_NAME)
            is SearchBookCriteria.ByIsbn -> datasource.getAll<Book>(TABLE_NAME).filter { it.isbn == criteria.isbn }
        }

    companion object {
        private const val TABLE_NAME = "book"
    }
}
