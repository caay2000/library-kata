package com.github.caay2000.librarykata.event.context.book.application.search

import com.github.caay2000.librarykata.event.context.book.application.BookRepository
import com.github.caay2000.librarykata.event.context.book.application.SearchBookCriteria
import com.github.caay2000.librarykata.event.context.book.domain.Book

class BookSearcher(private val bookRepository: BookRepository) {
    fun invoke(criteria: SearchBookCriteria): List<Book> = bookRepository.search(criteria)
}
