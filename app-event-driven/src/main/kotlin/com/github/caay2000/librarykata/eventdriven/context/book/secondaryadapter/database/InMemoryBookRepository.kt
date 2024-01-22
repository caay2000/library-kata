package com.github.caay2000.librarykata.eventdriven.context.book.secondaryadapter.database

import arrow.core.Either
import com.github.caay2000.common.database.RepositoryError
import com.github.caay2000.common.database.mapRepositoryErrors
import com.github.caay2000.librarykata.eventdriven.context.book.domain.Book
import com.github.caay2000.librarykata.eventdriven.context.book.domain.BookRepository
import com.github.caay2000.librarykata.eventdriven.context.book.domain.FindBookCriteria
import com.github.caay2000.librarykata.eventdriven.context.book.domain.SearchBookCriteria
import com.github.caay2000.memorydb.InMemoryDatasource

class InMemoryBookRepository(private val datasource: InMemoryDatasource) : BookRepository {
    override fun save(book: Book) = datasource.save(TABLE_NAME, book.id.value, book)

    override fun find(criteria: FindBookCriteria): Either<RepositoryError, Book> =
        when (criteria) {
            is FindBookCriteria.ById -> Either.catch { datasource.getById<Book>(TABLE_NAME, criteria.id.value)!! }
        }.mapRepositoryErrors()

    override fun search(criteria: SearchBookCriteria): List<Book> =
        when (criteria) {
            SearchBookCriteria.All -> datasource.getAll(TABLE_NAME)
            is SearchBookCriteria.ByIsbn -> datasource.getAll<Book>(TABLE_NAME).filter { it.isbn == criteria.isbn }
        }

    companion object {
        private const val TABLE_NAME = "book"
    }
}
