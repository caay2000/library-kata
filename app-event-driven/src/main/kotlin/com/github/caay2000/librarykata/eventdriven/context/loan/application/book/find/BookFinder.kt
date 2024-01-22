package com.github.caay2000.librarykata.eventdriven.context.loan.application.book.find

import com.github.caay2000.librarykata.eventdriven.context.loan.domain.Book
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.BookId
import com.github.caay2000.librarykata.eventdriven.context.loan.domain.BookRepository

class BookFinder(private val bookRepository: BookRepository) {
    fun invoke(bookId: BookId): Book = bookRepository.find(bookId)
}
