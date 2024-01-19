package com.github.caay2000.librarykata.hexagonal.context.domain.book

import arrow.core.Either
import com.github.caay2000.common.database.RepositoryError

interface BookRepository {
    fun save(book: Book)

    fun find(criteria: FindBookCriteria): Either<RepositoryError, Book>

    fun search(criteria: SearchBookCriteria): List<Book>
}

sealed class FindBookCriteria {
    class ById(val id: BookId) : FindBookCriteria()
}

sealed class SearchBookCriteria {
    data object All : SearchBookCriteria()

    data class ByIsbn(val isbn: BookIsbn) : SearchBookCriteria()
}

fun <E : Throwable> BookRepository.saveOrElse(
    book: Book,
    onError: (Throwable) -> E = { throw it },
): Either<E, Book> =
    Either.catch { save(book) }
        .mapLeft { onError(it) }.map { book }

fun <E : Throwable> BookRepository.findOrElse(
    criteria: FindBookCriteria,
    onResourceDoesNotExist: (Throwable) -> E = { throw it },
    onUnexpectedError: (Throwable) -> E = { throw it },
): Either<E, Book> =
    find(criteria).mapLeft { error ->
        if (error is RepositoryError.NotFoundError) {
            onResourceDoesNotExist(error)
        } else {
            onUnexpectedError(error)
        }
    }
