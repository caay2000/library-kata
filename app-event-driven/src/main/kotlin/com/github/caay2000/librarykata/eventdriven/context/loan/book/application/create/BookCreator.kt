package com.github.caay2000.librarykata.eventdriven.context.loan.book.application.create

import com.github.caay2000.librarykata.eventdriven.context.loan.book.domain.Book
import com.github.caay2000.librarykata.eventdriven.context.loan.book.domain.BookId
import com.github.caay2000.librarykata.eventdriven.context.loan.book.domain.BookIsbn
import com.github.caay2000.librarykata.eventdriven.context.loan.book.domain.BookRepository

class BookCreator(private val bookRepository: BookRepository) {
    fun invoke(
        bookId: BookId,
        bookIsbn: BookIsbn,
    ) {
        val book = Book.create(bookId, bookIsbn)
        bookRepository.save(book)
    }
}
