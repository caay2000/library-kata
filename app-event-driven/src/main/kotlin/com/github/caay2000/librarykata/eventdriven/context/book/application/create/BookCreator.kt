package com.github.caay2000.librarykata.eventdriven.context.book.application.create

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.github.caay2000.common.event.DomainEventPublisher
import com.github.caay2000.librarykata.eventdriven.context.book.domain.Book
import com.github.caay2000.librarykata.eventdriven.context.book.domain.BookId
import com.github.caay2000.librarykata.eventdriven.context.book.domain.BookRepository
import com.github.caay2000.librarykata.eventdriven.context.book.domain.CreateBookRequest
import com.github.caay2000.librarykata.eventdriven.context.book.domain.FindBookCriteria

class BookCreator(
    private val bookRepository: BookRepository,
    private val eventPublisher: DomainEventPublisher,
) {
    fun invoke(request: CreateBookRequest): Either<BookCreatorError, Unit> =
        guardBookIsNotAlreadyCreated(request.id)
            .map { Book.create(request) }
            .map { book -> bookRepository.save(book) }
            .map { book -> eventPublisher.publish(book.pullEvents()) }

    private fun guardBookIsNotAlreadyCreated(bookId: BookId): Either<BookCreatorError, Unit> =
        bookRepository.find(FindBookCriteria.ById(bookId))
            ?.let { BookCreatorError.BookAlreadyExists(bookId).left() }
            ?: Unit.right()
}

sealed class BookCreatorError(message: String) : RuntimeException(message) {
    class BookAlreadyExists(bookId: BookId) : BookCreatorError("Book with id ${bookId.value} already exists")
}
