package com.github.caay2000.projectskeleton.context.loan.application.book.create

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.right
import com.github.caay2000.projectskeleton.context.loan.application.BookRepository
import com.github.caay2000.projectskeleton.context.loan.domain.Book
import com.github.caay2000.projectskeleton.context.loan.domain.BookId
import com.github.caay2000.projectskeleton.context.loan.domain.BookIsbn

class BookCreator(private val bookRepository: BookRepository) {

    fun invoke(bookId: BookId, isbn: BookIsbn): Either<BookCreatorError, Unit> =
        createBook(bookId, isbn)
            .flatMap { book -> book.save() }

    private fun createBook(bookId: BookId, isbn: BookIsbn): Either<BookCreatorError, Book> = Book.create(bookId, isbn).right()

    private fun Book.save(): Either<BookCreatorError, Unit> =
        bookRepository.save(this)
            .mapLeft { BookCreatorError.UnknownError(it) }
}

sealed class BookCreatorError : RuntimeException {
    constructor(throwable: Throwable) : super(throwable)

    class UnknownError(error: Throwable) : BookCreatorError(error)
}
