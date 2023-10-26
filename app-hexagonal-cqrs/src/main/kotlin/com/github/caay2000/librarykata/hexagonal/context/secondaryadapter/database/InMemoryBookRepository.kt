package com.github.caay2000.librarykata.hexagonal.context.secondaryadapter.database

import arrow.core.Either
import com.github.caay2000.common.database.RepositoryError
import com.github.caay2000.librarykata.hexagonal.context.domain.book.Book
import com.github.caay2000.librarykata.hexagonal.context.domain.book.BookRepository
import com.github.caay2000.librarykata.hexagonal.context.domain.book.FindBookCriteria
import com.github.caay2000.librarykata.hexagonal.context.domain.book.SearchBookCriteria
import com.github.caay2000.memorydb.InMemoryDatasource

class InMemoryBookRepository(private val datasource: InMemoryDatasource) : BookRepository {

    override fun save(book: Book): Either<RepositoryError, Unit> =
        Either.catch { datasource.save(TABLE_NAME, book.id.toString(), book) }
            .mapLeft { RepositoryError.Unknown(it) }
            .map { }

    override fun find(criteria: FindBookCriteria): Either<RepositoryError, Book> =
        when (criteria) {
            is FindBookCriteria.ById -> Either.catch { datasource.getById<Book>(TABLE_NAME, criteria.id.toString())!! }
        }.mapLeft { error ->
            when (error) {
                is NullPointerException -> RepositoryError.NotFoundError()
                is NoSuchElementException -> RepositoryError.NotFoundError()
                else -> RepositoryError.Unknown(error)
            }
        }

    override fun search(criteria: SearchBookCriteria): Either<RepositoryError, List<Book>> =
        when (criteria) {
            SearchBookCriteria.All -> Either.catch { datasource.getAll(TABLE_NAME) }
            is SearchBookCriteria.ByIsbn -> Either.catch { datasource.getAll<Book>(TABLE_NAME).filter { it.isbn == criteria.isbn } }
        }.mapLeft { RepositoryError.Unknown(it) }

    companion object {
        private const val TABLE_NAME = "book"
    }
}
