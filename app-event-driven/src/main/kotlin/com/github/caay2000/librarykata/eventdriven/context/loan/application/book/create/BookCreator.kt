package com.github.caay2000.librarykata.eventdriven.context.loan.application.book.create

import com.github.caay2000.librarykata.eventdriven.context.loan.domain.Book
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.BookId
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.BookIsbn
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.BookRepository

class BookCreator(private val bookRepository: BookRepository) {
    fun invoke(
        bookId: BookId,
        bookIsbn: BookIsbn,
    ) {
        val book = Book.create(bookId, bookIsbn)
        bookRepository.save(book)
    }
}
