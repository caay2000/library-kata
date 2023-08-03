package com.github.caay2000.librarykata.context.account.application.book.create

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.recover
import arrow.core.right
import com.github.caay2000.common.database.RepositoryError
import com.github.caay2000.librarykata.context.account.application.BookRepository
import com.github.caay2000.librarykata.context.account.domain.Book
import com.github.caay2000.librarykata.context.account.domain.BookAuthor
import com.github.caay2000.librarykata.context.account.domain.BookId
import com.github.caay2000.librarykata.context.account.domain.BookIsbn
import com.github.caay2000.librarykata.context.account.domain.BookPages
import com.github.caay2000.librarykata.context.account.domain.BookPublisher
import com.github.caay2000.librarykata.context.account.domain.BookTitle

class BookCreator(private val bookRepository: BookRepository) {

    fun invoke(
        id: BookId,
        isbn: BookIsbn,
        title: BookTitle,
        author: BookAuthor,
        pages: BookPages,
        publisher: BookPublisher,
    ): Either<BookCreatorError, Unit> =
        guardBookIsNotAlreadyCreated(id)
            .flatMap { createBook(id = id, isbn = isbn, title = title, author = author, pages = pages, publisher = publisher) }
            .flatMap { book -> book.save() }

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

    private fun createBook(id: BookId, isbn: BookIsbn, title: BookTitle, author: BookAuthor, pages: BookPages, publisher: BookPublisher): Either<BookCreatorError, Book> =
        Either.catch { Book(id, isbn, title, author, pages, publisher) }
            .mapLeft { BookCreatorError.Unknown(it) }

    private fun Book.save(): Either<BookCreatorError, Unit> =
        bookRepository.save(this)
            .mapLeft { BookCreatorError.Unknown(it) }
}

sealed class BookCreatorError : RuntimeException {
    constructor(message: String) : super(message)
    constructor(throwable: Throwable) : super(throwable)

    class Unknown(error: Throwable) : BookCreatorError(error)
    class BookAlreadyExists(bookId: BookId) : BookCreatorError("book ${bookId.value} already exists")
}
