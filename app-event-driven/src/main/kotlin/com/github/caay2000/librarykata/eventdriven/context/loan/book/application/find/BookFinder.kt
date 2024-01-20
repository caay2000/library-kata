package com.github.caay2000.librarykata.eventdriven.context.loan.book.application.find

import com.github.caay2000.librarykata.eventdriven.context.loan.book.domain.Book
import com.github.caay2000.librarykata.eventdriven.context.loan.book.domain.BookId
import com.github.caay2000.librarykata.eventdriven.context.loan.book.domain.BookRepository

class BookFinder(private val bookRepository: BookRepository) {
    fun invoke(bookId: BookId): Book = bookRepository.find(bookId)
}
