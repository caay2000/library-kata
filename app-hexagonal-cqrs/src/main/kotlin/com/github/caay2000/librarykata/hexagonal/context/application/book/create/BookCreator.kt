package com.github.caay2000.librarykata.hexagonal.context.application.book.create

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.recover
import arrow.core.right
import com.github.caay2000.common.database.RepositoryError
import com.github.caay2000.librarykata.hexagonal.context.domain.book.Book
import com.github.caay2000.librarykata.hexagonal.context.domain.book.BookId
import com.github.caay2000.librarykata.hexagonal.context.domain.book.BookRepository
import com.github.caay2000.librarykata.hexagonal.context.domain.book.CreateBookRequest
import com.github.caay2000.librarykata.hexagonal.context.domain.book.FindBookCriteria
import com.github.caay2000.librarykata.hexagonal.context.domain.book.saveOrElse

class BookCreator(
    private val bookRepository: BookRepository,
) {
    fun invoke(request: CreateBookRequest): Either<BookCreatorError, Unit> =
        guardBookIsNotAlreadyCreated(request.id)
            .map { createBook(request) }
            .map { book -> book.save() }

    private fun guardBookIsNotAlreadyCreated(bookId: BookId): Either<BookCreatorError, Unit> =
        bookRepository.find(FindBookCriteria.ById(bookId))
            .flatMap { BookCreatorError.BookAlreadyExists(bookId).left() }
            .recover { error ->
                when (error) {
                    is RepositoryError.NotFoundError -> Unit.right()
                    is BookCreatorError -> raise(error)
                }
            }

    private fun createBook(request: CreateBookRequest): Book = Book.create(request)

    private fun Book.save() {
        bookRepository.saveOrElse<BookCreatorError>(this)
    }
}

sealed class BookCreatorError(message: String) : RuntimeException(message) {
    class BookAlreadyExists(bookId: BookId) : BookCreatorError("Book with id ${bookId.value} already exists")
}
