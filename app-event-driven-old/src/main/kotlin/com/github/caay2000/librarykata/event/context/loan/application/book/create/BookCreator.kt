package com.github.caay2000.librarykata.event.context.loan.application.book.create

import com.github.caay2000.librarykata.event.context.loan.application.BookRepository
import com.github.caay2000.librarykata.event.context.loan.domain.Book
import com.github.caay2000.librarykata.event.context.loan.domain.BookId
import com.github.caay2000.librarykata.event.context.loan.domain.BookIsbn

class BookCreator(private val bookRepository: BookRepository) {
    fun invoke(
        bookId: BookId,
        isbn: BookIsbn,
    ) {
        createBook(bookId, isbn).save()
    }

    private fun createBook(
        bookId: BookId,
        isbn: BookIsbn,
    ): Book = Book.create(bookId, isbn)

    private fun Book.save(): Book = bookRepository.save(this)
}
