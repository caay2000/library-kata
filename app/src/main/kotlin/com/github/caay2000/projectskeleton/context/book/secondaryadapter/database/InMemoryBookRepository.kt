package com.github.caay2000.projectskeleton.context.book.secondaryadapter.database

import arrow.core.Either
import com.github.caay2000.common.database.RepositoryError
import com.github.caay2000.memorydb.InMemoryDatasource
import com.github.caay2000.projectskeleton.context.book.application.BookRepository
import com.github.caay2000.projectskeleton.context.book.application.SearchBookCriteria
import com.github.caay2000.projectskeleton.context.book.domain.Book
import com.github.caay2000.projectskeleton.context.book.domain.BookId

class InMemoryBookRepository(private val datasource: InMemoryDatasource) : BookRepository {

    override fun save(book: Book): Either<RepositoryError, Unit> =
        Either.catch { datasource.save(TABLE_NAME, book.id.toString(), book) }
            .mapLeft { RepositoryError.Unknown(it) }
            .map { }

    override fun search(criteria: SearchBookCriteria): Either<RepositoryError, List<Book>> =
        Either.catch {
            when (criteria) {
                SearchBookCriteria.All -> datasource.getAll(TABLE_NAME)
                is SearchBookCriteria.ByIsbn -> datasource.getAll<Book>(TABLE_NAME).filter { it.isbn == criteria.isbn }
            }
        }.mapLeft { RepositoryError.Unknown(it) }

    override fun findById(id: BookId): Either<RepositoryError, Book> =
        Either.catch { datasource.getById<Book>(TABLE_NAME, id.toString())!! }
            .mapLeft { error ->
                when (error) {
                    is NullPointerException -> RepositoryError.NotFoundError()
                    is NoSuchElementException -> RepositoryError.NotFoundError()
                    else -> RepositoryError.Unknown(error)
                }
            }

    companion object {
        private const val TABLE_NAME = "book.book"
    }
}
