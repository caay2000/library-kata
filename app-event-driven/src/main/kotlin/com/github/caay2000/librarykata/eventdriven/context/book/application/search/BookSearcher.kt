package com.github.caay2000.librarykata.eventdriven.context.book.application.search

import com.github.caay2000.librarykata.eventdriven.context.book.domain.Book
import com.github.caay2000.librarykata.eventdriven.context.book.domain.BookRepository
import com.github.caay2000.librarykata.eventdriven.context.book.domain.SearchBookCriteria

class BookSearcher(private val bookRepository: BookRepository) {
    fun invoke(criteria: SearchBookCriteria): List<Book> = bookRepository.search(criteria)
}
