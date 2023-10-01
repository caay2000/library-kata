package com.github.caay2000.librarykata.hexagonal.context.application.book.create

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.recover
import arrow.core.right
import com.github.caay2000.common.database.RepositoryError
import com.github.caay2000.librarykata.hexagonal.context.application.book.BookRepository
import com.github.caay2000.librarykata.hexagonal.context.application.book.FindBookCriteria
import com.github.caay2000.librarykata.hexagonal.context.application.book.saveOrElse
import com.github.caay2000.librarykata.hexagonal.context.domain.Book
import com.github.caay2000.librarykata.hexagonal.context.domain.BookId
import com.github.caay2000.librarykata.hexagonal.context.domain.CreateBookRequest

class BookCreator(
    private val bookRepository: BookRepository,
) {

    fun invoke(request: CreateBookRequest): Either<BookCreatorError, Unit> =
        guardBookIsNotAlreadyCreated(request.id)
            .flatMap { createBook(request) }
            .flatMap { book -> book.save() }

    private fun guardBookIsNotAlreadyCreated(bookId: BookId): Either<BookCreatorError, Unit> =
        bookRepository.find(FindBookCriteria.ById(bookId))
            .flatMap { BookCreatorError.BookAlreadyExists(bookId).left() }
            .recover { error ->
                when (error) {
                    is RepositoryError.NotFoundError -> Unit.right()
                    is BookCreatorError -> raise(error)
                    else -> raise(BookCreatorError.Unknown(error))
                }
            }

    private fun createBook(request: CreateBookRequest): Either<BookCreatorError, Book> =
        Either.catch { Book.create(request) }
            .mapLeft { BookCreatorError.Unknown(it) }

    private fun Book.save(): Either<BookCreatorError, Unit> =
        bookRepository.saveOrElse<BookCreatorError>(this) { BookCreatorError.Unknown(it) }.map { }
}

sealed class BookCreatorError : RuntimeException {
    constructor(message: String) : super(message)
    constructor(throwable: Throwable) : super(throwable)

    class Unknown(error: Throwable) : BookCreatorError(error)
    class BookAlreadyExists(bookId: BookId) : BookCreatorError("book ${bookId.value} already exists")
}
