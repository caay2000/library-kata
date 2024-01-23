package com.github.caay2000.librarykata.hexagonal.context.book.application.create

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.github.caay2000.librarykata.hexagonal.context.book.domain.Book
import com.github.caay2000.librarykata.hexagonal.context.book.domain.BookId
import com.github.caay2000.librarykata.hexagonal.context.book.domain.BookRepository
import com.github.caay2000.librarykata.hexagonal.context.book.domain.CreateBookRequest
import com.github.caay2000.librarykata.hexagonal.context.book.domain.FindBookCriteria

class BookCreator(
    private val bookRepository: BookRepository,
) {
    fun invoke(request: CreateBookRequest): Either<BookCreatorError, Unit> =
        guardBookIsNotAlreadyCreated(request.id)
            .map { createBook(request) }
            .map { book -> book.save() }

    private fun guardBookIsNotAlreadyCreated(bookId: BookId): Either<BookCreatorError, Unit> =
        bookRepository.find(FindBookCriteria.ById(bookId))
            ?.let { BookCreatorError.BookAlreadyExists(bookId).left() }
            ?: Unit.right()

    private fun createBook(request: CreateBookRequest): Book = Book.create(request)

    private fun Book.save() = bookRepository.save(this)
}

sealed class BookCreatorError(message: String) : RuntimeException(message) {
    class BookAlreadyExists(bookId: BookId) : BookCreatorError("Book with id ${bookId.value} already exists")
}
