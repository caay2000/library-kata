package com.github.caay2000.librarykata.eventdriven.context.book.application.create

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.recover
import arrow.core.right
import com.github.caay2000.common.database.RepositoryError
import com.github.caay2000.common.event.DomainEventPublisher
import com.github.caay2000.librarykata.eventdriven.context.book.application.BookRepository
import com.github.caay2000.librarykata.eventdriven.context.book.domain.Book
import com.github.caay2000.librarykata.eventdriven.context.book.domain.BookId
import com.github.caay2000.librarykata.eventdriven.context.book.domain.CreateBookRequest

class BookCreator(
    private val bookRepository: BookRepository,
    private val eventPublisher: DomainEventPublisher,
) {

    fun invoke(request: CreateBookRequest): Either<BookCreatorError, Unit> =
        guardBookIsNotAlreadyCreated(request.id)
            .flatMap { createBook(request) }
            .flatMap { book -> book.save() }
            .flatMap { book -> book.publishEvents() }

    private fun guardBookIsNotAlreadyCreated(bookId: BookId): Either<BookCreatorError, Unit> =
        bookRepository.findById(bookId)
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

    private fun Book.save(): Either<BookCreatorError, Book> =
        bookRepository.save(this).map { this }
            .mapLeft { com.github.caay2000.librarykata.eventdriven.context.book.application.create.BookCreatorError.Unknown(it) }

    private fun Book.publishEvents(): Either<BookCreatorError, Unit> =
        eventPublisher.publish(this.pullEvents())
            .mapLeft { com.github.caay2000.librarykata.eventdriven.context.book.application.create.BookCreatorError.Unknown(it) }
}

sealed class BookCreatorError : RuntimeException {
    constructor(message: String) : super(message)
    constructor(throwable: Throwable) : super(throwable)

    class Unknown(error: Throwable) : BookCreatorError(error)
    class BookAlreadyExists(bookId: BookId) : BookCreatorError("book ${bookId.value} already exists")
}
